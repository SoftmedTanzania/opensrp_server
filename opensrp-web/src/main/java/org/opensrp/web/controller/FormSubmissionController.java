package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.AllConstants;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.connector.openmrs.service.HouseholdService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.domain.*;
import org.opensrp.dto.PatientReferralsDTO;
import org.opensrp.dto.PatientsDTO;
import org.opensrp.dto.ReferralsDTO;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.dto.form.MultimediaDTO;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionConverter;
import org.opensrp.form.service.FormSubmissionMap;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.repository.*;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.opensrp.service.*;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class FormSubmissionController {
    private static Logger logger = LoggerFactory.getLogger(FormSubmissionController.class.toString());
    private FormSubmissionService formSubmissionService;
    private GoogleFCMService googleFCMService;
    private TaskSchedulerService scheduler;
    private EncounterService encounterService;
    private FormEntityConverter formEntityConverter;
    private PatientService patientService;
    private ReferralPatientsService referralPatientService;
    private HouseholdService householdService;
    private ErrorTraceService errorTraceService;
    private MultimediaService multimediaService;
    private MultimediaRepository multimediaRepository;
    private HealthFacilitiesClientsRepository healthFacilitiesClientsRepository;
    private ClientReferralRepository clientReferralRepository;
    private ClientReferralIndicatorRepository clientReferralIndicatorRepository;
    private GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository;
    private RapidProServiceImpl rapidProService;

    @Autowired
    public FormSubmissionController(FormSubmissionService formSubmissionService, TaskSchedulerService scheduler,
                                    EncounterService encounterService, FormEntityConverter formEntityConverter, PatientService patientService,
                                    HouseholdService householdService, MultimediaService multimediaService, MultimediaRepository multimediaRepository,
                                    ErrorTraceService errorTraceService, HealthFacilitiesClientsRepository healthFacilitiesClientsRepository, ClientReferralRepository clientReferralRepository,
                                    GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository, GoogleFCMService googleFCMService,
                                    ClientReferralIndicatorRepository clientReferralIndicatorRepository,
                                    ReferralPatientsService referralPatientService, RapidProServiceImpl rapidProService) {
        this.formSubmissionService = formSubmissionService;
        this.scheduler = scheduler;
        this.errorTraceService = errorTraceService;
        this.encounterService = encounterService;
        this.formEntityConverter = formEntityConverter;
        this.patientService = patientService;
        this.householdService = householdService;
        this.multimediaService = multimediaService;
        this.multimediaRepository = multimediaRepository;
        this.healthFacilitiesClientsRepository = healthFacilitiesClientsRepository;
        this.clientReferralRepository = clientReferralRepository;
        this.googlePushNotificationsUsersRepository = googlePushNotificationsUsersRepository;
        this.googleFCMService = googleFCMService;
        this.clientReferralIndicatorRepository = clientReferralIndicatorRepository;
        this.referralPatientService = referralPatientService;
        this.rapidProService = rapidProService;
    }

    @RequestMapping(method = GET, value = "/form-submissions")
    @ResponseBody
    private List<FormSubmissionDTO> getNewSubmissionsForANM(@RequestParam("anm-id") String anmIdentifier,
                                                            @RequestParam("timestamp") Long timeStamp,
                                                            @RequestParam(value = "batch-size", required = false)
                                                                    Integer batchSize) {
        List<FormSubmission> newSubmissionsForANM = formSubmissionService
                .getNewSubmissionsForANM(anmIdentifier, timeStamp, batchSize);
        return with(newSubmissionsForANM).convert(new Converter<FormSubmission, FormSubmissionDTO>() {
            @Override
            public FormSubmissionDTO convert(FormSubmission submission) {
                return FormSubmissionConverter.from(submission);
            }
        });
    }

    @RequestMapping(method = GET, value = "/all-form-submissions")
    @ResponseBody
    private List<FormSubmissionDTO> getAllFormSubmissions(@RequestParam("timestamp") Long timeStamp,
                                                          @RequestParam(value = "batch-size", required = false)
                                                                  Integer batchSize) {
        List<FormSubmission> allSubmissions = formSubmissionService
                .getAllSubmissions(timeStamp, batchSize);
        return with(allSubmissions).convert(new Converter<FormSubmission, FormSubmissionDTO>() {
            @Override
            public FormSubmissionDTO convert(FormSubmission submission) {
                return FormSubmissionConverter.from(submission);
            }
        });
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/form-submissions")
    public ResponseEntity<HttpStatus> submitForms(@RequestBody List<FormSubmissionDTO> formSubmissionsDTO) {
        try {
            if (formSubmissionsDTO.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            System.out.println("\n");
            System.out.println("DrTest received formsubmission: " + formSubmissionsDTO.toString());

            try {
                System.out.println("DrTest saving to couchdb formsubmission: " + formSubmissionsDTO.toString());
                scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.FORM_SUBMISSION, formSubmissionsDTO));
            } catch (Exception e) {
                System.out.println("Error saving to couchdb : " + formSubmissionsDTO.toString());
                e.printStackTrace();
            }

            System.out.println("DrTest received saving data ");

            try {

                String json = new Gson().toJson(formSubmissionsDTO);
                List<FormSubmissionDTO> formSubmissions = new Gson().fromJson(json, new TypeToken<List<FormSubmissionDTO>>() {
                }.getType());
                List<FormSubmission> fsl = with(formSubmissions).convert(new Converter<FormSubmissionDTO, FormSubmission>() {
                    @Override
                    public FormSubmission convert(FormSubmissionDTO submission) {
                        return FormSubmissionConverter.toFormSubmission(submission);
                    }
                });
                for (FormSubmission formSubmission : fsl) {
                    try {
                        System.out.println("DrTest received saving data to openSRP ");
                        saveFormToOpenSRP(formSubmission);

                        System.out.println("DrTest received saving data to openMRS ");
//	            		addFormToOpenMRS(formSubmission);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorTrace errorTrace = new ErrorTrace(new DateTime(), "Parse Exception", "", e.getStackTrace().toString(), "Unsolved", formSubmission.formName());
                        errorTrace.setRecordId(formSubmission.instanceId());
                        errorTraceService.addError(errorTrace);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.debug(format("Added Form submissions to queue.\nSubmissions: {0}", formSubmissionsDTO));
        } catch (Exception e) {
            logger.error(format("Form submissions processing failed with exception {0}.\nSubmissions: {1}", e, formSubmissionsDTO));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }

    private void addFormToOpenMRS(FormSubmission formSubmission) throws ParseException, IllegalStateException, JSONException {

        FormSubmissionMap formSubmissionMap = null;
        try {
            formSubmissionMap = formSubmissionService.formAttributeParser.createFormSubmissionMap(formSubmission);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }


        if (formEntityConverter.isOpenmrsForm(formSubmissionMap)) {
            Client c = formEntityConverter.getClientFromFormSubmission(formSubmission);
            Event e = formEntityConverter.getEventFromFormSubmission(formSubmission);
            Map<String, Map<String, Object>> dep = formEntityConverter.getDependentClientsFromFormSubmission(formSubmission);

            // TODO temporary because not necessarily we register inner entity for Household only
            if (formSubmission.formName().toLowerCase().contains("household")
                    || formSubmission.formName().toLowerCase().contains("census")) {
                OpenmrsHouseHold hh = new OpenmrsHouseHold(c, e);
                for (Map<String, Object> cm : dep.values()) {
                    hh.addHHMember((Client) cm.get("client"), (Event) cm.get("event"));
                }

                householdService.saveHH(hh, true);
            } else {
                JSONObject p = patientService.getPatientByIdentifier(c.getBaseEntityId());
                if (p == null) {
                    System.out.println(patientService.createPatient(c));
                }

                System.out.println(encounterService.createEncounter(e));

                for (Map<String, Object> cm : dep.values()) {
                    Client cin = (Client) cm.get("client");
                    Event evin = (Event) cm.get("event");
                    JSONObject pin = patientService.getPatientByIdentifier(cin.getBaseEntityId());
                    if (pin == null) {
                        System.out.println(patientService.createPatient(cin));
                    }

                    System.out.println(encounterService.createEncounter(evin));
                }
            }
        }
    }

    private void saveFormToOpenSRP(FormSubmission formSubmission) throws ParseException, IllegalStateException, JSONException {
        logger.info("saveFormToOpenSRP : saving patient into OpenSRP");

        try {
            if (formSubmission.formName().equalsIgnoreCase("client_registration_form")) {
                ReferralClient client = formEntityConverter.getReferralClientFromFormSubmission(formSubmission);
                String temporallyClientId = formEntityConverter.getFieldValueFromFormSubmission(formSubmission, "client_id");
                String ctcNumber = formEntityConverter.getFieldValueFromFormSubmission(formSubmission, HealthFacilitiesReferralClients.COL_CTC_NUMBER);

                //TODO reimplement this to only obtain CTC_NUMBER,CBHS_NUMBER and FACILITY_ID
                ClientReferrals clientReferrals = formEntityConverter.getPatientReferralFromFormSubmission(formSubmission);
                long healthfacilityPatientId = referralPatientService.savePatient(client, clientReferrals.getFacilityId(), ctcNumber);


                List<HealthFacilitiesReferralClients> healthFacilitiesPatients = null;
                try {
                    healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesReferralClients.tbName + " WHERE " + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + " = " + healthfacilityPatientId, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                client.setClientId(healthFacilitiesPatients.get(0).getClient().getClientId());

                logger.info("saveFormToOpenSRP : saving patient. Generated Client Id = " + client.getClientId());
                logger.info("saveFormToOpenSRP : saving patient. Updating form submissions with client Id " + temporallyClientId + " To Client Id = " + client.getClientId());


                //Retrieve the saved form in couchdb to be updated
                FormSubmission savedSubmission = formSubmissionService.findByInstanceId(formSubmission.getInstanceId());

                //Updating the formsubmission with the correct clientId;
                formEntityConverter.updateClientIdInFormSubmission(savedSubmission, temporallyClientId, client.getClientId());
                formSubmissionService.update(savedSubmission);

                //updating any existing referrals with the correct clientId
                List<FormSubmission> referralSubmissions = formSubmissionService.findByFormName("referral_form", 4);
                for (FormSubmission submission : referralSubmissions) {
                    formEntityConverter.updateClientIdInFormSubmission(submission, temporallyClientId, client.getClientId());
                    formSubmissionService.update(submission);
                }


                try {
                    String userUUIDString = formEntityConverter.getFieldValueFromFormSubmission(formSubmission, "service_provider_uuid");


                    logger.info("saveFormToOpenSRP : sending notification. User UUID  = " + userUUIDString);
                    Object[] userUUID = new Object[]{};
                    List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName + " WHERE " + GooglePushNotificationsUsers.COL_USER_UUID + " = ? ", userUUID);
                    JSONArray fcmTokens = new JSONArray();

                    for (GooglePushNotificationsUsers users : googlePushNotificationsUsers) {
                        fcmTokens.put(users.getGooglePushNotificationToken());
                    }


                    //Sending a push notification to the tablet to force updating of the client IDs used for future referrals
                    JSONObject body = new JSONObject();
                    body.put("GENERATED_CLIENT_ID", client.getClientId());
                    body.put("TEMP_CLIENT_ID", temporallyClientId);

                    String json = new Gson().toJson(body);

                    logger.info("saveFormToOpenSRP: FCM msg = " + json);

                    JSONObject msg = new JSONObject(json);
                    msg.put("type", "UpdateClientId");

                    googleFCMService.SendPushNotification(msg, fcmTokens, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (formSubmission.formName().equalsIgnoreCase("referral_form")) {
                try {

                    FormSubmission updatedFormSubmission = formSubmissionService.findByInstanceId(formSubmission.getInstanceId());
                    if (updatedFormSubmission == null) {
                        updatedFormSubmission = formSubmission;
                    }
                    ClientReferrals clientReferrals = formEntityConverter.getPatientReferralFromFormSubmission(updatedFormSubmission);


                    String clientId = formEntityConverter.getFieldValueFromFormSubmission(updatedFormSubmission, "client_id");

                    logger.info("saveFormToOpenSRP : saving referral.  Client Id = " + clientId);

                    Object[] args = new Object[1];
                    args[0] = clientId;

                    ReferralClient patient = referralPatientService.getPatients("SELECT * FROM " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + " = ?", args).get(0);
                    saveReferralData(patient, clientReferrals, updatedFormSubmission);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(format("Patient Form submissions processing failed with exception {0}.\nSubmissions: {1}", e, formSubmission));
                }


            } else if (formSubmission.formName().equalsIgnoreCase("followup_form")) {
                try {

                    FormSubmission updatedFormSubmission = formSubmissionService.findByInstanceId(formSubmission.getInstanceId());
                    if (updatedFormSubmission == null) {
                        updatedFormSubmission = formSubmission;
                    }
                    ClientReferrals clientReferrals = formEntityConverter.getPatientReferralFromFormSubmission(updatedFormSubmission);


                    String sql = "UPDATE " + ClientReferrals.tbName + " SET " +
                            ClientReferrals.COL_REFERRAL_STATUS + " = '" + clientReferrals.getReferralStatus() + "' , " +
                            ClientReferrals.COL_REFERRAL_FEEDBACK_ID + " = '" + clientReferrals.getReferralFeedback().getId() + "' , " +
                            ClientReferrals.COL_OTHER_NOTES + " = '" + clientReferrals.getOtherNotes() + "' WHERE  " + ClientReferrals.COL_REFERRAL_ID + " = " + clientReferrals.getId();
                    clientReferralRepository.executeQuery(sql);
                    logger.info("Coze: updated referral feedback : " + sql);

                    List<ClientReferrals> updatedReferralList = clientReferralRepository.getReferrals("SELECT * FROM " + ClientReferrals.tbName + " WHERE " + ClientReferrals.COL_REFERRAL_ID + "=?",
                            new Object[]{clientReferrals.getId()});


                    ClientReferrals updatedReferral = updatedReferralList.get(0);
                    ReferralsDTO referralsDTO = PatientsConverter.toPatientDTO(updatedReferral);
                    referralPatientService.sendReferralFeedbackFCMNotification(referralsDTO.getServiceProviderUIID(), referralsDTO, updatedReferral.getReferralType().getReferralTypeId());

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(format("Patient Form submissions processing failed with exception {0}.\nSubmissions: {1}", e, formSubmission));
                }
            } else {
                //TODO This block of code only used for providing backward compatibility with old versions of community app

                logger.info("saveFormToOpenSRP : saving patient into OpenSRP");
                ReferralClient patient = formEntityConverter.getReferralClientFromFormSubmission(formSubmission);
                ClientReferrals clientReferrals = formEntityConverter.getPatientReferralFromFormSubmission(formSubmission);
                saveReferralData(patient, clientReferrals, formSubmission);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/multimedia-file")
    @ResponseBody
    public List<MultimediaDTO> getFiles(@RequestParam("anm-id") String providerId) {

        List<Multimedia> allMultimedias = multimediaService.getMultimediaFiles(providerId);

        return with(allMultimedias).convert(new Converter<Multimedia, MultimediaDTO>() {
            @Override
            public MultimediaDTO convert(Multimedia md) {
                return new MultimediaDTO(md.getCaseId(), md.getProviderId(), md.getContentType(), md.getFilePath(), md.getFileCategory());
            }
        });
    }

    public String reformatPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            logger.info("ReformatPhoneNumber: registered phone number = " + phoneNumber);
            Phonenumber.PhoneNumber tzPhoneNumber = phoneUtil.parse(phoneNumber, "TZ");
            phoneNumber = phoneUtil.format(tzPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

            return phoneNumber;
        } catch (NumberParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public void saveReferralData(ReferralClient patient, ClientReferrals clientReferrals, FormSubmission formSubmission) {
        try {


            String ctcNumber = formEntityConverter.getFieldValueFromFormSubmission(formSubmission, HealthFacilitiesReferralClients.COL_CTC_NUMBER);
            long healthfacilityPatientId = referralPatientService.savePatient(patient, clientReferrals.getFacilityId(), ctcNumber);

            List<HealthFacilitiesReferralClients> healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesReferralClients.tbName + " WHERE " + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + " = " + healthfacilityPatientId, null);

            patient.setClientId(healthFacilitiesPatients.get(0).getClient().getClientId());
            clientReferrals.setPatient(patient);

            //TODO Coze remove hardcoding of these values. This is a temporally patch to be removed later on
            clientReferrals.setReferralStatus(0);
            clientReferrals.setReferralSource(0);


            ReferralType referralType = new ReferralType();
            referralType.setReferralTypeId((long) 1);

            clientReferrals.setReferralType(referralType);

            logger.info("saveFormToOpenSRP : saving referral Data");
            long id = clientReferralRepository.save(clientReferrals);
            clientReferrals.setId(id);

            JSONArray indicatorIds = formEntityConverter.getReferralIndicatorsFromFormSubmission(formSubmission);
            int size = indicatorIds.length();

            List<Long> referralIndicatorIds = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ClientReferralIndicators referralIndicators = new ClientReferralIndicators();
                referralIndicators.setActive(true);

                ClientReferrals referral = new ClientReferrals();
                referral.setId(id);

                referralIndicators.setClientReferrals(referral);

                ServiceIndicator serviceIndicator = new ServiceIndicator();
                serviceIndicator.setServiceIndicatorId(indicatorIds.getLong(i));

                referralIndicators.setServiceIndicator(serviceIndicator);

                long patientReferralIndicatorId = clientReferralIndicatorRepository.save(referralIndicators);
                referralIndicatorIds.add(indicatorIds.getLong(i));
            }


            String phoneNumber = patient.getPhoneNumber();
            phoneNumber = reformatPhoneNumber(phoneNumber);


            List<String> urns;
            urns = new ArrayList<String>();
            urns.add("tel:" + phoneNumber);

            try {

                //TODO RAPIDPRO, fix the message sent
                String response = rapidProService.sendMessage(urns, null, null, "Successful registration", null);
                logger.info("Received rapidpro response : " + response);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Object[] facilityParams = new Object[]{clientReferrals.getFacilityId(), 1};
            List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName + " WHERE " + GooglePushNotificationsUsers.COL_FACILITY_UUID + " = ? AND " + GooglePushNotificationsUsers.COL_USER_TYPE + " = ?", facilityParams);
            JSONArray tokens = new JSONArray();
            for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : googlePushNotificationsUsers) {
                tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
            }


            PatientReferralsDTO patientReferralsDTO = new PatientReferralsDTO();

            PatientsDTO patientsDTO = PatientsConverter.toPatientsDTO(patient);
            patientsDTO.setCtcNumber(ctcNumber);
            patientReferralsDTO.setPatientsDTO(patientsDTO);


            List<ReferralsDTO> referralsDTOS = new ArrayList<>();
            ReferralsDTO referralsDTO = PatientsConverter.toPatientDTO(clientReferrals);
            referralsDTO.setServiceIndicatorIds(referralIndicatorIds);
            referralsDTOS.add(referralsDTO);
            patientReferralsDTO.setPatientReferralsList(referralsDTOS);

            JSONObject body = new JSONObject();

            String json = new Gson().toJson(patientReferralsDTO);

            logger.info("saveFormToOpenSRP: FCM msg = " + json);

            JSONObject msg = new JSONObject(json);
            msg.put("type", "PatientReferral");

            googleFCMService.SendPushNotification(msg, tokens, true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(format("Patient Form submissions processing failed with exception {0}.\nSubmissions: {1}", e, formSubmission));

        }
    }
}
