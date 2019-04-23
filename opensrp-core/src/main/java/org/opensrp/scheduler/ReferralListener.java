package org.opensrp.scheduler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.*;
import org.opensrp.dto.ReferralsDTO;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.repository.ClientReferralRepository;
import org.opensrp.repository.GooglePushNotificationsUsersRepository;
import org.opensrp.repository.ReferralServiceRepository;
import org.opensrp.service.*;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.opensrp.service.formSubmission.FormSubmissionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.text.MessageFormat.format;
import static java.util.Collections.sort;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
public class ReferralListener {
    private static Logger logger = LoggerFactory.getLogger(ReferralListener.class.toString());
    private ReferralServiceRepository referralServiceRepository;
    private ClientReferralRepository clientReferralRepository;
    private FormSubmissionService formSubmissionService;
    private ReferralPatientsService referralPatientService;
    private FormEntityConverter formEntityConverter;
    private GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository;
    private GoogleFCMService googleFCMService;

    @Autowired
    public ReferralListener(ReferralServiceRepository referralServiceRepository, ClientReferralRepository clientReferralRepository,
                            FormSubmissionService formSubmissionService, ReferralPatientsService referralPatientService,FormEntityConverter formEntityConverter,GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository,GoogleFCMService googleFCMService) {
        this.referralServiceRepository=referralServiceRepository;
        this.clientReferralRepository=clientReferralRepository;
        this.formSubmissionService=formSubmissionService;
        this.referralPatientService=referralPatientService;
        this.formEntityConverter=formEntityConverter;
        this.googlePushNotificationsUsersRepository=googlePushNotificationsUsersRepository;
        this.googleFCMService=googleFCMService;
    }

    @MotechListener(subjects = AllConstants.OpenSRPEvent.CHECK_REFERRAL_STATUS)
    public void updateReferralStatus(MotechEvent event) {
        logger.info("Coze : Checking referral status");
        try {
            long malariaServiceId = referralServiceRepository.getReferralServices("SELECT * FROM " + ReferralService.tbName + " WHERE " + ReferralService.COL_CATEGORY_NAME + " = 'malaria' ", null).get(0).getServiceId();
            long tbServiceId = referralServiceRepository.getReferralServices("SELECT * FROM " + ReferralService.tbName + " WHERE " + ReferralService.COL_CATEGORY_NAME + " = 'tb' ", null).get(0).getServiceId();

            List<ClientReferrals> clientReferrals = clientReferralRepository.getReferrals("SELECT * FROM " + ClientReferrals.tbName + " WHERE " + ClientReferrals.COL_REFERRAL_STATUS + " = 0 ", null);

            Date now = Calendar.getInstance().getTime();

            for (ClientReferrals clientReferral : clientReferrals) {
                long diff = now.getTime() - clientReferral.getReferralDate().getTime();
                logger.info("Days since referrals Issued: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                logger.info("Referral Issued: " + clientReferral.getId());
                logger.info("Referral Service Id: " + clientReferral.getServiceId());
                logger.info("Malaria Service Id: " + malariaServiceId);
                logger.info("TB Service Id: " + tbServiceId);
                logger.info("Referral Service Id: " + clientReferral.getServiceId());

                //Failed referrals
                if ((clientReferral.getServiceId() == malariaServiceId && TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) > 24) ||
                        (clientReferral.getServiceId() == tbServiceId && TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 3)||
                        (clientReferral.getServiceId() != malariaServiceId && clientReferral.getServiceId() != tbServiceId && TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 7)) {

                    logger.info("failed referral " + clientReferral.getId());
                    clientReferral.setReferralStatus(-1);
                    String sql = "UPDATE " + ClientReferrals.tbName + " SET " +
                            ClientReferrals.COL_REFERRAL_STATUS + " = -1 WHERE  " + ClientReferrals.COL_REFERRAL_ID + " = " + clientReferral.getId();
                    clientReferralRepository.executeQuery(sql);


                    if (clientReferral.getReferralType().getReferralTypeId() == 1) {
                        try {
                            FormSubmission formSubmission = formSubmissionService.findByInstanceId(clientReferral.getInstanceId());
                            formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission, ClientReferrals.COL_REFERRAL_STATUS, clientReferral.getReferralStatus() + "");
                            logger.info("Coze: updated formsubmission = " + new Gson().toJson(formSubmission));
                            formSubmissionService.update(formSubmission);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    List<ReferralClient> patients = referralPatientService.getPatients("SELECT * FROM " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + " = " + clientReferral.getPatient().getClientId(), null);
                    logger.info("Coze: Send notification sms to user " + patients.get(0).getPhoneNumber());

                    //TODO send sms notification to the user

                    ReferralsDTO referralsDTO = PatientsConverter.toPatientDTO(clientReferral);
                    JSONObject body = new JSONObject();
                    body.put("type", "FailedReferrals");

                    //sending push notification to facility users
                    Object[] facilityParams = new Object[]{clientReferral.getFacilityId(), clientReferral.getFromFacilityId()};
                    List<GooglePushNotificationsUsers> facilityPushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName +
                            " WHERE " +GooglePushNotificationsUsers.COL_USER_TYPE+" = 1 AND ("+
                            GooglePushNotificationsUsers.COL_FACILITY_UUID + " = ? OR " +
                            GooglePushNotificationsUsers.COL_FACILITY_UUID + " = ? )", facilityParams);

                    JSONArray tokens = new JSONArray();
                    for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : facilityPushNotificationsUsers) {
                        tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
                    }

                    if (tokens.length() > 0) {
                        String jsonData = new Gson().toJson(referralsDTO);
                        JSONObject msg = new JSONObject(jsonData);
                        googleFCMService.SendPushNotification(msg, tokens, true);
                    }

                    //sending push notifications to CHW apps
                    Object[] chwParams = new Object[]{clientReferral.getServiceProviderUIID()};
                    List<GooglePushNotificationsUsers> chwPushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName +
                            " WHERE " +GooglePushNotificationsUsers.COL_USER_TYPE+" = 0 AND "+
                            GooglePushNotificationsUsers.COL_USER_UUID + " = ? )", chwParams);

                    JSONArray chwTokens = new JSONArray();
                    for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : chwPushNotificationsUsers) {
                        chwTokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
                    }

                    if (chwTokens.length() > 0) {
                        String jsonData = new Gson().toJson(referralsDTO);
                        JSONObject msg = new JSONObject(jsonData);
                        googleFCMService.SendPushNotification(msg, chwTokens, false);
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
