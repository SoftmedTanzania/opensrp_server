package org.opensrp.web.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opensrp.common.AllConstants;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.*;
import org.opensrp.dto.*;
import org.opensrp.form.domain.FormData;
import org.opensrp.form.domain.FormInstance;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.repository.*;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.opensrp.service.GoogleFCMService;
import org.opensrp.service.PatientsConverter;
import org.opensrp.service.RapidProServiceImpl;
import org.opensrp.service.ReferralPatientsService;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ReferralPatientsController {
    private static Logger logger = LoggerFactory.getLogger(ReferralPatientsController.class.toString());
    private ReferralPatientsService patientsService;
    private HealthFacilityRepository healthFacilityRepository;
    private HealthFacilitiesClientsRepository healthFacilitiesClientsRepository;
    private TBEncounterRepository tbEncounterRepository;
    private ClientsAppointmentsRepository clientsAppointmentsRepository;
    private ClientReferralRepository clientReferralRepository;
    private ClientsRepository clientsRepository;
    private ClientReferralIndicatorRepository clientReferralIndicatorRepository;
    private GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository;
    private TBPatientsRepository tbPatientsRepository;
    private FormSubmissionService formSubmissionService;
    private FormEntityConverter formEntityConverter;
    private TaskSchedulerService scheduler;
    private GoogleFCMService googleFCMService;
    private ReferralPatientsService referralPatientService;
    private RapidProServiceImpl rapidProService;
    private ReferralServiceRepository referralServiceRepository;
    private TBTestTypeRepository tbTestTypeRepository;
    private TBMedicatinRegimesRepository tbSputumMedicationRegimesRepository;
    private HealthFacilityRepository facilityRepository;
    private ServiceIndicatorRepository serviceIndicatorRepository;
    private OpenmrsUserService openmrsUserService;

    @Autowired
    public ReferralPatientsController(ReferralPatientsService patientsService, TaskSchedulerService scheduler,
                                      HealthFacilityRepository healthFacilityRepository, HealthFacilitiesClientsRepository healthFacilitiesClientsRepository, ClientsAppointmentsRepository clientsAppointmentsRepository,
                                      TBEncounterRepository tbEncounterRepository, ClientReferralRepository clientReferralRepository, TBPatientsRepository tbPatientsRepository, FormSubmissionService formSubmissionService,
                                      FormEntityConverter formEntityConverter, GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository, GoogleFCMService googleFCMService,
                                      ClientReferralIndicatorRepository clientReferralIndicatorRepository, ReferralPatientsService referralPatientService, RapidProServiceImpl rapidProService, ReferralServiceRepository referralServiceRepository, HealthFacilityRepository facilityRepository,
                                      TBTestTypeRepository tbTestTypeRepository, TBMedicatinRegimesRepository tbSputumMedicationRegimesRepository, OpenmrsUserService openmrsUserService, ServiceIndicatorRepository serviceIndicatorRepository,ClientsRepository clientsRepository) {
        this.patientsService = patientsService;
        this.scheduler = scheduler;
        this.healthFacilityRepository = healthFacilityRepository;
        this.healthFacilitiesClientsRepository = healthFacilitiesClientsRepository;
        this.clientsAppointmentsRepository = clientsAppointmentsRepository;
        this.tbEncounterRepository = tbEncounterRepository;
        this.clientReferralRepository = clientReferralRepository;
        this.tbPatientsRepository = tbPatientsRepository;
        this.formSubmissionService = formSubmissionService;
        this.formEntityConverter = formEntityConverter;
        this.googlePushNotificationsUsersRepository = googlePushNotificationsUsersRepository;
        this.googleFCMService = googleFCMService;
        this.clientReferralIndicatorRepository = clientReferralIndicatorRepository;
        this.referralPatientService = referralPatientService;
        this.rapidProService = rapidProService;
        this.referralServiceRepository = referralServiceRepository;
        this.tbTestTypeRepository = tbTestTypeRepository;
        this.tbSputumMedicationRegimesRepository = tbSputumMedicationRegimesRepository;
        this.facilityRepository = facilityRepository;
        this.serviceIndicatorRepository = serviceIndicatorRepository;
        this.openmrsUserService = openmrsUserService;
        this.clientsRepository = clientsRepository;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-patients")
    public ResponseEntity<PatientsDTO> savePatient(@RequestBody String json) {
        PatientsDTO patientsDTO = new Gson().fromJson(json, PatientsDTO.class);
        try {
            if (patientsDTO == null) {
                return new ResponseEntity<>(BAD_REQUEST);
            }
            try {
                scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, patientsDTO));
            } catch (Exception e) {
                e.printStackTrace();
            }

            ReferralClient patient = PatientsConverter.toPatients(patientsDTO);
            long healthfacilityPatientId = referralPatientService.savePatient(patient, patientsDTO.getHealthFacilityCode(), patientsDTO.getCtcNumber());

            patientsDTO.setPatientId(healthfacilityPatientId);


            Object[] facilityParams = new Object[]{patientsDTO.getHealthFacilityCode(), 1};
            List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName + " WHERE " + GooglePushNotificationsUsers.COL_FACILITY_UUID + " = ? AND " + GooglePushNotificationsUsers.COL_USER_TYPE + " = ?", facilityParams);
            JSONArray tokens = new JSONArray();
            for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : googlePushNotificationsUsers) {
                tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
            }

            if (tokens.length() > 0) {
                String jsonData = new Gson().toJson(patientsDTO);
                JSONObject msg = new JSONObject(jsonData);
                msg.put("type", "PatientRegistration");

                googleFCMService.SendPushNotification(msg, tokens, true);
            }

            String phoneNumber = patientsDTO.getPhoneNumber();
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                System.out.println("Coze: registered phone number : " + phoneNumber);
                Phonenumber.PhoneNumber tzPhoneNumber = phoneUtil.parse(phoneNumber, "TZ");
                phoneNumber = phoneUtil.format(tzPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

                System.out.println("Coze:formatted phone number : " + phoneNumber);
            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }


            List<String> urns;
            urns = new ArrayList<String>();
            urns.add("tel:" + phoneNumber);

            try {
                System.out.println("Coze: sending phone number to rapidpro : " + phoneNumber);
                String response = rapidProService.startFlow(urns, "251c1c0c-a082-474b-826b-a0ab233013e3");

                System.out.println("Coze: received rapidpro response : " + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.debug(format("Added  Patient to queue.\nSubmissions: {0}", patientsDTO));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(format("ReferralClient processing failed with exception {0}.\nSubmissions: {1}", e, json));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<PatientsDTO>(patientsDTO, OK);
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-ctc-patients")
    public ResponseEntity<HttpStatus> saveCtcPatients(@RequestBody String json) {
        try {
            System.out.println("saving ctc patients");
            CTCPayloadDTO ctcPayloadDTO = new Gson().fromJson(json, CTCPayloadDTO.class);
            List<CTCPatientsDTO> patientsDTOS = ctcPayloadDTO.getCtcPatientsDTOS();

            if (patientsDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            //updating health facility with its correct facililty CTC2 code
            List<HealthFacilities> healthFacilities = facilityRepository.getHealthFacility("SELECT * FROM " + HealthFacilities.tbName + " WHERE " +
                    HealthFacilities.COL_FACILITY_CTC_CODE + " = '" + ctcPayloadDTO.getFacilityCTC2Code() + "'", null);
            if (healthFacilities.size() == 0) {
                facilityRepository.executeQuery("UPDATE " + HealthFacilities.tbName + "  SET " + HealthFacilities.COL_FACILITY_CTC_CODE + " = '" + ctcPayloadDTO.getFacilityCTC2Code() + "' WHERE " + HealthFacilities.COL_HFR_CODE + " = '" + ctcPayloadDTO.getHfrCode() + "'");
            }

            List<HealthFacilities> healthFacilitiesCheck = facilityRepository.getHealthFacility("SELECT * FROM " + HealthFacilities.tbName + " WHERE " +
                    HealthFacilities.COL_FACILITY_CTC_CODE + " = '" + ctcPayloadDTO.getFacilityCTC2Code() + "'", null);

            if (healthFacilitiesCheck.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }


//            try {
//                scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, patientsDTOS));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            List<PatientReferralsDTO> successfullySavedLTFs = new ArrayList<>();
            for (CTCPatientsDTO dto : patientsDTOS) {
                try {
                    System.out.println("saving patient");
                    ReferralClient p = PatientsConverter.toPatients(dto);

                    long healthFacilityPatientId = referralPatientService.savePatient(p, dto.getHealthFacilityCode(), dto.getCtcNumber());


                    ReferralClient patient = referralPatientService.getPatientsByHealthFacilityPatientId(healthFacilityPatientId);


                    List<ClientAppointments> appointments = PatientsConverter.toPatientsAppointments(dto);
                    int savedAppointmentsCount = 0;


                    List<ReferralsDTO> referralsDTOS = new ArrayList<>();
                    for (ClientAppointments patientAppointment : appointments) {
                        System.out.println("saving appointment");

                        HealthFacilitiesReferralClients healthFacilitiesReferralClients = new HealthFacilitiesReferralClients();
                        healthFacilitiesReferralClients.setHealthFacilityClientId(healthFacilityPatientId);

                        patientAppointment.setHealthFacilitiesReferralClients(healthFacilitiesReferralClients);

                        try {
                            //saving LTFs appointments
                            long appointmentId = clientsAppointmentsRepository.save(patientAppointment);

                            ClientReferrals referral = new ClientReferrals();
                            referral.setReferralStatus(0);
                            referral.setOtherNotes("");
                            referral.setAppointmentDate(Calendar.getInstance().getTime());
                            referral.setReferralDate(Calendar.getInstance().getTime());
                            referral.setReferralReason("Lost follow up");
                            referral.setEmergency(false);
                            referral.setInstanceId(UUID.randomUUID().toString());
                            referral.setPatient(patient);
                            ReferralType referralType = new ReferralType();
                            referralType.setReferralTypeId((long)4);
                            referral.setReferralType(referralType);

                            long referralId =  clientReferralRepository.save(referral);
                            referral.setId(referralId);

                            ReferralsDTO referralsDTO = PatientsConverter.toPatientDTO(referral);
                            referralsDTOS.add(referralsDTO);

                            clientsAppointmentsRepository.executeQuery("UPDATE " + ClientAppointments.tbName + " SET " + ClientAppointments.COL_FOLLOWUP_REFERRAL_ID + " = '"+referralId+"' WHERE " + ClientAppointments.COL_APPOINTMENT_ID + " = " + appointmentId);
                            savedAppointmentsCount++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    if(savedAppointmentsCount>0){

                        PatientReferralsDTO patientReferralsDTO = new PatientReferralsDTO();
                        PatientsDTO patientsDTO = PatientsConverter.toPatientsDTO(patient);
                        patientReferralsDTO.setPatientsDTO(patientsDTO);
                        patientReferralsDTO.setPatientReferralsList(referralsDTOS);
                        successfullySavedLTFs.add(patientReferralsDTO);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            Map<String,List<PatientReferralsDTO>> wardsCTCPatients =  new HashMap<>();
            //Sorting the ctc patients by their wards and villages
            for(PatientReferralsDTO dto:successfullySavedLTFs){
                if(wardsCTCPatients.get(dto.getPatientsDTO().getWard())!=null){
                    wardsCTCPatients.get(dto.getPatientsDTO().getWard()).add(dto);

                }else{
                    List<PatientReferralsDTO> ctcPatientsDTOs = new ArrayList<>();
                    ctcPatientsDTOs.add(dto);
                    wardsCTCPatients.put(dto.getPatientsDTO().getWard(),ctcPatientsDTOs);
                }
            }

            JSONArray teamMembersArray =  openmrsUserService.getTeamMembers();
            Map<String,List<String>> chwsInAWard =  new HashMap<>();

            //Looping through the LTFs wards and for each ward, identify the CHWs involved.
            for (Map.Entry<String,List<PatientReferralsDTO>> entry : wardsCTCPatients.entrySet()) {
                System.out.println("ward = "+entry.getKey());
                //for each ward loop through the CHWs obtain and categories them into their respective wards
                for(int i=0; i<teamMembersArray.length();i++){
                    try {
                        JSONObject object = teamMembersArray.getJSONObject(i);
                        JSONObject person = object.getJSONObject("person");
                        String teamRole = object.getJSONObject("teamRole").getString("display");
                        if (teamRole.equals("CHW")) { //Checking if the team member is a CHW
                            JSONArray location = object.getJSONArray("locations");
                            for(int j=0;j<location.length();j++){

                                try {
                                    //checking if the chw location or parent location is equal to the ctc client's ward
                                    String locationName = location.getJSONObject(j).getString("display");
                                    String parentLocationName = location.getJSONObject(j).getJSONObject("parentLocation").getString("display");

                                    if (locationName.toLowerCase().contains(entry.getKey().toLowerCase()) ||
                                            parentLocationName.toLowerCase().equals(entry.getKey().toLowerCase())) {

                                        Object[] facilityParams = new Object[]{person.getString("uuid")};
                                        List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName + " WHERE " + GooglePushNotificationsUsers.COL_USER_UUID + " = ?", facilityParams);

                                        try {
                                            if (chwsInAWard.get(entry.getKey()) != null) {
                                                chwsInAWard.get(entry.getKey()).add(googlePushNotificationsUsers.get(0).getGooglePushNotificationToken());
                                            } else {
                                                List<String> chwFCMTokensUUIDs = new ArrayList<>();
                                                chwFCMTokensUUIDs.add(googlePushNotificationsUsers.get(0).getGooglePushNotificationToken());
                                                chwsInAWard.put(entry.getKey(), chwFCMTokensUUIDs);
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        break;
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }


            System.out.println("Sorted CTCPatients by wards = "+new Gson().toJson(wardsCTCPatients));
            System.out.println("Sorted CHWs by wards = "+new Gson().toJson(chwsInAWard));


           for (Map.Entry<String,List<PatientReferralsDTO>> entry : wardsCTCPatients.entrySet()) {
               List<PatientReferralsDTO> ltfsReferralsDTOs = entry.getValue();
               List<String> chwsFCMIds = chwsInAWard.get(entry.getKey());
               int size = chwsFCMIds.size();

               for(int i=0;i<ltfsReferralsDTOs.size();i++){
                   PatientReferralsDTO patientReferralsDTO = ltfsReferralsDTOs.get(i);

                   String jsonString = new Gson().toJson(patientReferralsDTO);
                   JSONObject msg = new JSONObject(jsonString);
                   msg.put("type", "PatientReferral");
                   try {

                       //Issuing referrals to specific CHWs
                       JSONArray tokens = new JSONArray();
                       tokens.put(chwsFCMIds.get(i%size));
                       googleFCMService.SendPushNotification(msg, tokens, false);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           }

            //TODO send push notifications to facility apps
            logger.debug(format("Added  ReferralClient and their appointments from CTC to queue.\nSubmissions: {0}", patientsDTOS));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(format("CTC ReferralClient processing failed with exception {0}.\nSubmissions: {1}", e, json));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-tb-patient")
    @ResponseBody
    public ResponseEntity<TBCompletePatientDataDTO> saveTBPatients(@RequestBody String json) {
        TBPatientMobileClientDTO tbPatientMobileClientDTO = new Gson().fromJson(json, TBPatientMobileClientDTO.class);
        try {

            try {
                scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, tbPatientMobileClientDTO));
            } catch (Exception e) {
                e.printStackTrace();
            }

            ReferralClient convertedPatient = PatientsConverter.toPatients(tbPatientMobileClientDTO);
            logger.info("Patient data", new Gson().toJson(convertedPatient));

            TBPatient tbPatient = PatientsConverter.toTBPatients(tbPatientMobileClientDTO);
            logger.info("TB patient data", new Gson().toJson(tbPatient));

            long healthfacilityPatientId = referralPatientService.savePatient(convertedPatient, tbPatientMobileClientDTO.getHealthFacilityCode(), null);

            HealthFacilitiesReferralClients hPatient = new HealthFacilitiesReferralClients();
            hPatient.setHealthFacilityClientId(healthfacilityPatientId);

            tbPatient.setHealthFacilitiesReferralClients(hPatient);
            tbPatientsRepository.save(tbPatient);
            createAppointments(healthfacilityPatientId);


            TBCompletePatientDataDTO tbCompletePatientDataDTO = new TBCompletePatientDataDTO();
            List<HealthFacilitiesReferralClients> healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesReferralClients.tbName + " WHERE " + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + "=?",
                    new Object[]{healthfacilityPatientId});

            HealthFacilitiesReferralClients healthFacilitiesPatient = healthFacilitiesPatients.get(0);

            List<ReferralClient> patients = referralPatientService.getPatients("SELECT * FROM " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + "=?",
                    new Object[]{healthFacilitiesPatient.getClient().getClientId()});

            tbCompletePatientDataDTO.setPatientsDTO(PatientsConverter.toPatientsDTO(patients.get(0)));

            List<TBPatient> tbPatients = tbPatientsRepository.getTBPatients("SELECT * FROM " + org.opensrp.domain.TBPatient.tbName + " WHERE " + TBPatient.COL_HEALTH_FACILITY_CLIENT_ID + "=?",
                    new Object[]{healthFacilitiesPatient.getClient().getClientId()});
            tbCompletePatientDataDTO.setTbPatientDTO(PatientsConverter.toTbPatientDTO(tbPatients.get(0), patients.get(0).getClientId()));

            List<ClientAppointments> clientAppointments = clientsAppointmentsRepository.getAppointments("SELECT * FROM " + ClientAppointments.tbName + " WHERE " + ClientAppointments.COL_HEALTH_FACILITY_CLIENT_ID + "=?",
                    new Object[]{healthfacilityPatientId});
            tbCompletePatientDataDTO.setPatientsAppointmentsDTOS(PatientsConverter.toPatientAppointmentDTOsList(clientAppointments, patients.get(0).getClientId()));

            //TODO implement push notification to other tablets in the same facility.

            return new ResponseEntity<TBCompletePatientDataDTO>(tbCompletePatientDataDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(format("TB ReferralClient processing failed with exception {0}.\nSubmissions: {1}", e, tbPatientMobileClientDTO));

        }
        return null;
    }

    @RequestMapping("get-facility-tb-patients/{facilityUUID}")
    @ResponseBody
    public ResponseEntity<List<TBCompletePatientDataDTO>> getFacilityTBPatients(@PathVariable("facilityUUID") String facilityUUID) {
        try {
            List<TBCompletePatientDataDTO> tbCompletePatientDataDTOS = new ArrayList<>();
            List<HealthFacilitiesReferralClients> healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesReferralClients.tbName +
                            " INNER JOIN " + HealthFacilities.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_FACILITY_ID + " = " + HealthFacilities.tbName + "._id WHERE " + HealthFacilities.COL_OPENMRS_UUID + "=?",
                    new Object[]{facilityUUID});

            for (HealthFacilitiesReferralClients healthFacilitiesPatient : healthFacilitiesPatients) {
                try {
                    TBCompletePatientDataDTO tbCompletePatientDataDTO = new TBCompletePatientDataDTO();

                    List<ReferralClient> patients = referralPatientService.getPatients("SELECT * FROM " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + "=?",
                            new Object[]{healthFacilitiesPatient.getClient().getClientId()});

                    tbCompletePatientDataDTO.setPatientsDTO(PatientsConverter.toPatientsDTO(patients.get(0)));

                    List<TBPatient> tbPatients = tbPatientsRepository.getTBPatients("SELECT * FROM " + org.opensrp.domain.TBPatient.tbName + " WHERE " + TBPatient.COL_HEALTH_FACILITY_CLIENT_ID + "=?",
                            new Object[]{healthFacilitiesPatient.getClient().getClientId()});
                    tbCompletePatientDataDTO.setTbPatientDTO(PatientsConverter.toTbPatientDTO(tbPatients.get(0), patients.get(0).getClientId()));

                    List<ClientAppointments> clientAppointments = clientsAppointmentsRepository.getAppointments("SELECT * FROM " + ClientAppointments.tbName + " WHERE " + ClientAppointments.COL_HEALTH_FACILITY_CLIENT_ID + "=?",
                            new Object[]{healthFacilitiesPatient.getClient().getClientId()});
                    tbCompletePatientDataDTO.setPatientsAppointmentsDTOS(PatientsConverter.toPatientAppointmentDTOsList(clientAppointments, patients.get(0).getClientId()));

                    List<TBEncounter> tbEncounters = tbEncounterRepository.getTBEncounters("SELECT * FROM " + TBEncounter.tbName + " WHERE " + TBEncounter.COL_TB_PATIENT_ID + "=?",
                            new Object[]{tbPatients.get(0).getTbClientId()});
                    tbCompletePatientDataDTO.setTbEncounterDTOS(PatientsConverter.toTbPatientEncounterDTOsList(tbEncounters));

                    tbCompletePatientDataDTOS.add(tbCompletePatientDataDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return new ResponseEntity<List<TBCompletePatientDataDTO>>(tbCompletePatientDataDTOS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(format("Obtaining TB ReferralClient failed with exception {0}.\nfacility id: {1}", e, facilityUUID));

        }
        return null;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-tb-encounters")
    public ResponseEntity<TBEncounterFeedbackDTO> saveTBEncounter(@RequestBody String json) {
        System.out.println("saveTBEncounter : " + json);
        TBEncounterDTO tbEncounterDTOS = new Gson().fromJson(json, TBEncounterDTO.class);
        try {
            try {
                scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, tbEncounterDTOS));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TBEncounter encounter = PatientsConverter.toTBEncounter(tbEncounterDTOS);

            tbEncounterRepository.save(encounter);

            List<ClientAppointments> clientAppointments = clientsAppointmentsRepository.getAppointments("SELECT * FROM " + ClientAppointments.tbName + " WHERE " + ClientAppointments.COL_APPOINTMENT_ID + "=?",
                    new Object[]{encounter.getAppointmentId()});

            if (clientAppointments.size() == 0) {
                return new ResponseEntity<>(PRECONDITION_FAILED);
            }

            ClientAppointments clientAppointments1 = clientAppointments.get(0);
            clientsAppointmentsRepository.executeQuery("UPDATE " + ClientAppointments.tbName + " SET " + ClientAppointments.COL_STATUS + " = '1' WHERE " + ClientAppointments.COL_APPOINTMENT_ID + " = " + clientAppointments1.getAppointment_id());


            recalculateAppointments(clientAppointments.get(0).getHealthFacilitiesReferralClients().getHealthFacilityClientId(), encounter.getAppointmentId(), encounter.getMedicationDate().getTime());
            String encounterQuery = "SELECT * FROM " + TBEncounter.tbName + " WHERE " +
                    TBEncounter.COL_TB_PATIENT_ID + " = ?    AND " +
                    TBEncounter.COL_APPOINTMENT_ID + " = ?  ";

            Object[] tbEncountersParams = new Object[]{
                    encounter.getTbPatientId(),
                    encounter.getAppointmentId()};

            List<TBEncounter> tbEncounters = null;
            try {
                tbEncounters = tbEncounterRepository.getTBEncounters(encounterQuery, tbEncountersParams);
                TBEncounter tbEncounter = tbEncounters.get(0);

                TBEncounterDTO tbEncounterDTO = new TBEncounterDTO();
                tbEncounterDTO.setId(tbEncounter.getId());
                tbEncounterDTO.setTbPatientId(tbEncounter.getTbPatientId());
                tbEncounterDTO.setAppointmentId(tbEncounter.getAppointmentId());
                tbEncounterDTO.setLocalID(tbEncounter.getLocalID());
                tbEncounterDTO.setMakohozi(tbEncounter.getMakohozi());
                tbEncounterDTO.setWeight(tbEncounter.getWeight());
                tbEncounterDTO.setEncounterMonth(tbEncounter.getEncounterMonth());
                tbEncounterDTO.setEncounterYear(tbEncounter.getEncounterYear());
                tbEncounterDTO.setScheduledDate(tbEncounter.getScheduledDate().getTime());
                tbEncounterDTO.setMedicationDate(tbEncounter.getMedicationDate().getTime());
                tbEncounterDTO.setMedicationStatus(tbEncounter.isMedicationStatus());
                tbEncounterDTO.setHasFinishedPreviousMonthMedication(tbEncounter.isHasFinishedPreviousMonthMedication());

                TBEncounterFeedbackDTO tbEncounterFeedbackDTO = new TBEncounterFeedbackDTO();
                tbEncounterFeedbackDTO.setTbEncounterDTO(tbEncounterDTO);

                List<ClientAppointments> appointments = clientsAppointmentsRepository.getAppointments("SELECT * FROM " + ClientAppointments.tbName + " WHERE " + ClientAppointments.COL_HEALTH_FACILITY_CLIENT_ID + " =?",
                        new Object[]{clientAppointments.get(0).getHealthFacilitiesReferralClients().getHealthFacilityClientId()});


                List<HealthFacilitiesReferralClients> healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesReferralClients.tbName + " WHERE " + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + " =?",
                        new Object[]{clientAppointments.get(0).getHealthFacilitiesReferralClients().getHealthFacilityClientId()});

                tbEncounterFeedbackDTO.setPatientsAppointmentsDTOS(PatientsConverter.toPatientAppointmentDTOsList(appointments, healthFacilitiesPatients.get(0).getClient().getClientId()));


                //TODO push notifications to other tablets in the facility.
                return new ResponseEntity<TBEncounterFeedbackDTO>(tbEncounterFeedbackDTO, HttpStatus.OK);

            } catch (Exception e) {
                e.printStackTrace();
            }


            logger.debug(format("Added  TB Encounters Submissions: {0}", tbEncounterDTOS));
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(PRECONDITION_FAILED);
        } catch (Exception e) {
            logger.error(format("TB Encounters processing failed with exception {0}.\nSubmissions: {1}", e, tbEncounterDTOS));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }

    @RequestMapping(method = GET, value = "/all-patients-referrals")
    @ResponseBody
    private List<PatientReferralsDTO> getAllPatientsReferrals() {
        List<PatientReferralsDTO> patientReferralsDTOS = patientsService.getAllPatientReferrals();

        return patientReferralsDTOS;
    }

    @RequestMapping("get-facility-referrals/{facilityUUID}")
    @ResponseBody
    private List<PatientReferralsDTO> getHealthFacilityReferrals(@PathVariable("facilityUUID") String facilityUuid) {
        List<PatientReferralsDTO> patientReferralsDTOS = patientsService.getHealthFacilityReferrals(facilityUuid);
        return patientReferralsDTOS;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-facility-referral")
    public ResponseEntity<ReferralsDTO> saveFacilityReferral(@RequestBody String jsonData) {
        ReferralsDTO referralsDTO = new Gson().fromJson(jsonData, ReferralsDTO.class);
        try {
            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, referralsDTO));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ClientReferrals clientReferrals = PatientsConverter.toPatientReferral(referralsDTO);
            Long referralId = clientReferralRepository.save(clientReferrals);

            try {
                for (Long serviceIndicatorId : referralsDTO.getServiceIndicatorIds()) {
                    ClientReferralIndicators clientReferralIndicators = new ClientReferralIndicators();

                    ClientReferrals referral = new ClientReferrals();
                    referral.setId(referralId);
                    clientReferralIndicators.setClientReferrals(referral);


                    ServiceIndicator serviceIndicator = serviceIndicatorRepository.getReferralServicesIndicators("SELECT * FROM "+ServiceIndicator.tbName+" WHERE "+ServiceIndicator.COL_SERVICE_INDICATOR_ID+"=?",
                            new Object[]{serviceIndicatorId}).get(0);

                    clientReferralIndicators.setServiceIndicator(serviceIndicator);

                    clientReferralIndicators.setActive(true);

                    try {
                        clientReferralIndicatorRepository.save(clientReferralIndicators);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResponseEntity<>(CONFLICT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<ClientReferrals> savedClientReferrals = clientReferralRepository.getReferrals("SELECT * FROM " + ClientReferrals.tbName + " ORDER BY " + ClientReferrals.COL_REFERRAL_ID + " DESC LIMIT 1 ", null);
            logger.debug(format("Added  ReferralsDTO Submissions: {0}", referralsDTO));

            referralsDTO.setReferralId(savedClientReferrals.get(0).getId());


            Object[] patientParams = new Object[]{
                    savedClientReferrals.get(0).getPatient().getClientId()};
            List<ReferralClient> patients = referralPatientService.getPatients("SELECT * FROM " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + " =?", patientParams);

            PatientReferralsDTO patientReferralsDTO = new PatientReferralsDTO();
            patientReferralsDTO.setPatientsDTO(PatientsConverter.toPatientsDTO(patients.get(0)));

            List<ReferralsDTO> patientReferrals = new ArrayList<>();
            patientReferrals.add(PatientsConverter.toPatientDTO(savedClientReferrals.get(0)));

            for (ReferralsDTO refDTO : patientReferrals) {
                Object[] args2 = new Object[1];
                args2[0] = refDTO.getReferralId();
                List<ClientReferralIndicators> clientReferralIndicators = clientReferralIndicatorRepository.getPatientReferralIndicators("SELECT * FROM " + ClientReferralIndicators.tbName + " WHERE " + ClientReferralIndicators.COL_REFERRAL_ID + " =?", args2);
                List<Long> patientReferralIndicatorsIds = new ArrayList<>();
                for (ClientReferralIndicators referralIndicator : clientReferralIndicators) {
                    patientReferralIndicatorsIds.add(referralIndicator.getServiceIndicator().getServiceIndicatorId());
                }
                refDTO.setServiceIndicatorIds(patientReferralIndicatorsIds);
            }

            patientReferralsDTO.setPatientReferralsList(patientReferrals);

            if (referralsDTO.getReferralType() == 4) {
                System.out.println("chwreferral FROM FACILITY ID : " + savedClientReferrals.get(0).getFromFacilityId());
                System.out.println("chwreferral CHW UUID : " + referralsDTO.getFacilityId());


                Object[] facilityParams = new Object[]{savedClientReferrals.get(0).getFromFacilityId(), referralsDTO.getFacilityId()};
                List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName + " WHERE " + GooglePushNotificationsUsers.COL_FACILITY_UUID + " = ? AND " + GooglePushNotificationsUsers.COL_USER_UUID + " = ?", facilityParams);
                JSONArray tokens = new JSONArray();
                for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : googlePushNotificationsUsers) {
                    tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
                }

                logger.info("tokens : " + tokens.toString());
                String json = new Gson().toJson(patientReferralsDTO);

                logger.info("ChwReferralObject : " + json);

                JSONObject msg = new JSONObject(json);
                msg.put("type", "PatientReferral");

                try {
                    googleFCMService.SendPushNotification(msg, tokens, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String healthFacilitySql = "SELECT * FROM " + HealthFacilities.tbName + " WHERE " +
                        HealthFacilities.COL_FACILITY_CTC_CODE + " = ? OR " + HealthFacilities.COL_OPENMRS_UUID + " = ?";
                Object[] healthFacilityParams = new Object[]{
                        clientReferrals.getFromFacilityId(), clientReferrals.getFromFacilityId()};

                List<HealthFacilities> healthFacilities = null;
                try {
                    healthFacilities = healthFacilityRepository.getHealthFacility(healthFacilitySql, healthFacilityParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (healthFacilities != null)
                        saveReferralFollowup(clientReferrals, healthFacilities.get(0).getId() + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Object[] facilityParams;
                if (referralsDTO.getReferralType() == 3) {
                    logger.info("facility-facility referral : " + savedClientReferrals.get(0).getFacilityId());
                    facilityParams = new Object[]{savedClientReferrals.get(0).getFacilityId(), 1};
                } else {
                    logger.info("intra-facility referral : " + savedClientReferrals.get(0).getFromFacilityId());
                    facilityParams = new Object[]{savedClientReferrals.get(0).getFromFacilityId(), 1};
                }
                try {
                    List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName + " WHERE " + GooglePushNotificationsUsers.COL_FACILITY_UUID + " = ? AND " + GooglePushNotificationsUsers.COL_USER_TYPE + " = ?", facilityParams);
                    JSONArray tokens = new JSONArray();
                    for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : googlePushNotificationsUsers) {
                        tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
                    }
                    logger.info("tokens : " + tokens.toString());
                    String json = new Gson().toJson(patientReferralsDTO);
                    JSONObject msg = new JSONObject(json);
                    msg.put("type", "PatientReferral");

                    try {
                        googleFCMService.SendPushNotification(msg, tokens, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new ResponseEntity<ReferralsDTO>(referralsDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(format("ReferralsDTO processing failed with exception {0}.\nSubmissions: {1}", e, jsonData));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/receive-feedback")
    public ResponseEntity<String> saveReferralFeedback(@RequestBody String json) {
        try {
            logger.info("Coze: receive feedback = " + json);
            ReferralsDTO referralsDTO = new Gson().fromJson(json, ReferralsDTO.class);

            List<ClientReferrals> referrals = clientReferralRepository.getReferrals("SELECT * FROM " + ClientReferrals.tbName + " WHERE " + ClientReferrals.COL_REFERRAL_ID + "=?",
                    new Object[]{referralsDTO.getReferralId()});

            ClientReferrals referral = null;
            try {
                referral = referrals.get(0);
                referral.setReferralStatus(referralsDTO.getReferralStatus());

                ReferralFeedback referralFeedback = new ReferralFeedback();
                referralFeedback.setId(Long.parseLong(referralsDTO.getServiceGivenToPatient()));

                referral.setReferralFeedback(referralFeedback);
                referral.setOtherNotes(referralsDTO.getOtherNotes());
                referral.setTestResults(referralsDTO.getTestResults());
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("Coze: referral not found");
                return new ResponseEntity<String>("referral not found", PRECONDITION_FAILED);
            }


            if (referral != null) {
                String sql = "UPDATE " + ClientReferrals.tbName + " SET " +
                        ClientReferrals.COL_REFERRAL_STATUS + " = '" + referral.getReferralStatus() + "' , " +
                        ClientReferrals.COL_TEST_RESULTS + " = '" + referral.getTestResults() + "' , " +
                        ClientReferrals.COL_REFERRAL_FEEDBACK_ID + " = '" + referral.getReferralFeedback().getId() + "' , " +
                        ClientReferrals.COL_OTHER_NOTES + " = '" + referral.getOtherNotes() + "' WHERE  " + ClientReferrals.COL_REFERRAL_ID + " = " + referral.getId();
                clientReferralRepository.executeQuery(sql);
                logger.info("Coze: updated referral feedback : " + sql);

                if (referral.getReferralType().getReferralTypeId() == 1) {
                    try {
                        FormSubmission formSubmission = formSubmissionService.findByInstanceId(referral.getInstanceId());
                        logger.info("Coze: formsubmission to be updated = " + new Gson().toJson(formSubmission));

                        formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission, ClientReferrals.COL_REFERRAL_FEEDBACK_ID, referral.getReferralFeedback().getId().toString());
                        formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission, ClientReferrals.COL_OTHER_NOTES, referral.getOtherNotes());
                        formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission, ClientReferrals.COL_TEST_RESULTS, referral.isTestResults() + "");
                        formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission, ClientReferrals.COL_REFERRAL_STATUS, referral.getReferralStatus() + "");


                        logger.info("Updated formsubmission = " + new Gson().toJson(formSubmission));
                        formSubmissionService.update(formSubmission);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                referralPatientService.sendReferralFeedbackFCMNotification(referralsDTO.getServiceProviderUIID(),referralsDTO,referral.getReferralType().getReferralTypeId());

            } else {
                return new ResponseEntity<String>("Referral Not found", BAD_REQUEST);
            }


            logger.debug(format("updated  ReferralsFeedbackDTO Submissions: {0}", referralsDTO));
        } catch (Exception e) {
            logger.error(format("ReferralsFeedbackDTO processing failed with exception {0}.\nSubmissions: {1}", e, json));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("success", OK);
    }

    @RequestMapping(method = GET, value = "/check-status-of-referrals")
    @ResponseBody
    public ResponseEntity<HttpStatus> checkStatusOfReferrals() {
        try {

            List<ReferralService> referralServices = referralServiceRepository.getReferralServices("SELECT * FROM " + ReferralService.tbName + " WHERE " + ReferralService.COL_CATEGORY_NAME + " = 'malaria' ", null);
            long malariaServiceId = referralServices.get(0).getServiceId();

            List<ClientReferrals> clientReferrals = clientReferralRepository.getReferrals("SELECT * FROM " + ClientReferrals.tbName + " WHERE " + ClientReferrals.COL_REFERRAL_STATUS + " = 0 ", null);

            Date now = Calendar.getInstance().getTime();

            for (ClientReferrals clientReferral : clientReferrals) {
                long diff = now.getTime() - clientReferral.getReferralDate().getTime();
                logger.info("hours since referrals Isued: " + TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS));

                //Failed referrals
                if ((clientReferral.getServiceId() == malariaServiceId && TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) > 24) || (clientReferral.getServiceId() != malariaServiceId && TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 3)) {

                    logger.info("failed referral " + clientReferral.getId());
                    clientReferral.setReferralStatus(-1);
                    String sql = "UPDATE " + ClientReferrals.tbName + " SET " +
                            ClientReferrals.COL_REFERRAL_STATUS + " = '" + clientReferral.getReferralStatus() + "' WHERE  " + ClientReferrals.COL_REFERRAL_ID + " = " + clientReferral.getId();
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

                    //TODO send notification to the user

                    ReferralsDTO referralsDTO = PatientsConverter.toPatientDTO(clientReferral);
                    JSONObject body = new JSONObject();
                    body.put("type", "FailedReferrals");

                    Object[] facilityParams = new Object[]{clientReferral.getFacilityId(), clientReferral.getFromFacilityId()};
                    List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName +
                            " WHERE " +
                            GooglePushNotificationsUsers.COL_FACILITY_UUID + " = ? OR " +
                            GooglePushNotificationsUsers.COL_FACILITY_UUID + " = ? ", facilityParams);

                    JSONArray tokens = new JSONArray();
                    for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : googlePushNotificationsUsers) {
                        tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
                    }

                    if (tokens.length() > 0) {
                        String jsonData = new Gson().toJson(referralsDTO);
                        JSONObject msg = new JSONObject(jsonData);
                        googleFCMService.SendPushNotification(msg, tokens, false);
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }

    @RequestMapping(method = GET, value = "/check-appointments")
    @ResponseBody
    public ResponseEntity<String> checkUpcomingAppointments() {
        try {

            JSONArray phoneNumbers = new JSONArray();
            Date d = Calendar.getInstance().getTime();
            List<ClientAppointments> clientAppointments = clientsAppointmentsRepository.getAppointments("SELECT * FROM " + ClientAppointments.tbName + " WHERE " + ClientAppointments.COL_APPOINTMENT_DATE + " > '" + d.getTime() + "'", null);

            logger.info("Coze: checking appointment ");
            Date now = Calendar.getInstance().getTime();


            List<String> threeDaysToAppointmentUrns = new ArrayList<String>();
            List<String> aDayToAppointmentUrns = new ArrayList<String>();
            for (ClientAppointments appointments : clientAppointments) {
                logger.info("Coze: checking appointment " + appointments.getAppointmentDate());


                long diff = appointments.getAppointmentDate().getTime() - now.getTime();

                logger.info("Coze: Days to appointment : " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 2) {

                    List<HealthFacilitiesReferralClients> healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesReferralClients.tbName + " WHERE " + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + " = " + appointments.getHealthFacilitiesReferralClients().getHealthFacilityClientId(), null);
                    List<ReferralClient> patients = referralPatientService.getPatients("SELECT * FROM " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + " = " + healthFacilitiesPatients.get(0).getClient().getClientId(), null);
                    logger.info("Coze: Send 1 days to Appointment notification to user " + patients.get(0).getPhoneNumber());

                    if (!patients.get(0).getPhoneNumber().equals("")) {

                        phoneNumbers.put(patients.get(0).getPhoneNumber());
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                        try {
                            logger.info("Coze: registered phone number : " + patients.get(0).getPhoneNumber());
                            Phonenumber.PhoneNumber tzPhoneNumber = phoneUtil.parse(patients.get(0).getPhoneNumber(), "TZ");
                            String formatedPhoneNumber = phoneUtil.format(tzPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                            logger.info("Coze:formatted a day to appointment phone number : " + formatedPhoneNumber);
                            aDayToAppointmentUrns.add("tel:" + formatedPhoneNumber);
                        } catch (NumberParseException e) {
                            System.err.println("NumberParseException was thrown: " + e.toString());
                        }

                    }

                } else if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) < 5) {
                    List<HealthFacilitiesReferralClients> healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesReferralClients.tbName + " WHERE " + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + " = " + appointments.getHealthFacilitiesReferralClients().getHealthFacilityClientId(), null);
                    List<ReferralClient> patients = referralPatientService.getPatients("SELECT * FROM " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + " = " + healthFacilitiesPatients.get(0).getClient().getClientId(), null);
                    logger.info("Coze: Send 3 days to Appointment notification to user " + patients.get(0).getPhoneNumber());
                    if (!patients.get(0).getPhoneNumber().equals("")) {
                        phoneNumbers.put(patients.get(0).getPhoneNumber());
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                        try {
                            logger.info("Coze: registered phone number : " + patients.get(0).getPhoneNumber());
                            Phonenumber.PhoneNumber tzPhoneNumber = phoneUtil.parse(patients.get(0).getPhoneNumber(), "TZ");
                            String formatedPhoneNumber = phoneUtil.format(tzPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

                            logger.info("Coze:formatted phone number : " + formatedPhoneNumber);
                            threeDaysToAppointmentUrns.add("tel:" + formatedPhoneNumber);
                        } catch (NumberParseException e) {
                            System.err.println("NumberParseException was thrown: " + e.toString());
                        }

                    }

                }
            }

            try {
                //TODO RAPIDPRO, fix the message sent
                String response = rapidProService.sendMessage(threeDaysToAppointmentUrns, null, null, "test message 3 days to appointment", null);
                logger.info("Coze: received rapidpro response for 3 days to appointment notifications : " + response);

                String response2 = rapidProService.sendMessage(aDayToAppointmentUrns, null, null, "test message 3 days to appointment", null);
                logger.info("Coze: received rapidpro response for a day to appointment notifications : " + response2);


            } catch (Exception e) {
                e.printStackTrace();
            }


            return new ResponseEntity<String>(phoneNumbers.toString(), HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = GET, value = "/get-tb-test-type")
    @ResponseBody
    public ResponseEntity<List<TBTestType>> getTBTestTypes() {
        try {
            List<TBTestType> tbTestTypes = tbTestTypeRepository.getTBPatientTypes("SELECT * FROM " + TBTestType.tbName, null);
            return new ResponseEntity<List<TBTestType>>(tbTestTypes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<TBTestType>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = GET, value = "/get-tb-medication-regimes")
    @ResponseBody
    public ResponseEntity<List<TBMedicationRegime>> getTBSputumMedicationRegimes() {
        try {
            List<TBMedicationRegime> tbMedicationRegime = tbSputumMedicationRegimesRepository.getTBSputumMedicationRegime("SELECT * FROM " + TBMedicationRegime.tbName, null);
            return new ResponseEntity<List<TBMedicationRegime>>(tbMedicationRegime, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<TBMedicationRegime>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveReferralFollowup(ClientReferrals clientReferrals, String facilityId) {
        logger.info("saveReferralFollowup : saving referral Form data for followup = " + new Gson().toJson(clientReferrals));
        logger.info("saveReferralFollowup : saving referral Form data for facilityId = " + facilityId);


        List<HealthFacilitiesReferralClients> healthFacilitiesPatients = null;
        try {
            healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesReferralClients.tbName + " WHERE " + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + clientReferrals.getPatient().getClientId() + " AND " + HealthFacilitiesReferralClients.COL_FACILITY_ID + " = '" + facilityId + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<ReferralClient> patients = null;
        try {
            patients = referralPatientService.getPatients("SELECT * FROM " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + " = " + clientReferrals.getPatient().getClientId(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ReferralClient patient = patients.get(0);

            HealthFacilitiesReferralClients healthFacilitiesPatient = healthFacilitiesPatients.get(0);

            String uuid = UUID.randomUUID().toString();

            List<org.opensrp.form.domain.FormField> formFields = new ArrayList<>();
            formFields.add(new org.opensrp.form.domain.FormField("first_name", patient.getFirstName() == null ? "" : patient.getFirstName(), "followup_client.first_name"));
            formFields.add(new org.opensrp.form.domain.FormField("middle_name", patient.getMiddleName() == null ? "" : patient.getMiddleName(), "followup_client.middlename"));
            formFields.add(new org.opensrp.form.domain.FormField("surname", patient.getSurname() == null ? "" : patient.getSurname(), "followup_client.surname"));
            formFields.add(new org.opensrp.form.domain.FormField("date_of_birth", patient.getDateOfBirth().getTime() + "", "followup_client.date_of_birth"));
            formFields.add(new org.opensrp.form.domain.FormField("community_based_hiv_service", patient.getCommunityBasedHivService() == null ? "" : patient.getCommunityBasedHivService(), "followup_client.community_based_hiv_service"));
            formFields.add(new org.opensrp.form.domain.FormField("ctc_number", healthFacilitiesPatient.getCtcNumber() == null ? "" : healthFacilitiesPatient.getCtcNumber(), "followup_client.ctc_number"));
            formFields.add(new org.opensrp.form.domain.FormField("care_taker_name", patient.getCareTakerName() == null ? "" : patient.getCareTakerName(), "followup_client.care_taker_name"));
            formFields.add(new org.opensrp.form.domain.FormField("care_taker_phone_number", patient.getCareTakerPhoneNumber() == null ? "" : patient.getCareTakerPhoneNumber(), "followup_client.care_taker_relationship"));
            formFields.add(new org.opensrp.form.domain.FormField("care_taker_relationship", patient.getCareTakerRelationship() == null ? "" : patient.getCareTakerRelationship(), "followup_client.care_taker_relationship"));
            formFields.add(new org.opensrp.form.domain.FormField("facility_id", clientReferrals.getFromFacilityId() + "", "followup_client.facility_id"));
            formFields.add(new org.opensrp.form.domain.FormField("referral_reason", clientReferrals.getReferralReason() == null ? "" : clientReferrals.getReferralReason(), "followup_client.referral_reason"));
            formFields.add(new org.opensrp.form.domain.FormField("gender", patient.getGender() == null ? "" : patient.getGender(), "followup_client.gender"));
            formFields.add(new org.opensrp.form.domain.FormField("phone_number", patient.getPhoneNumber() == null ? "" : patient.getPhoneNumber(), "followup_client.phone_number"));
            formFields.add(new org.opensrp.form.domain.FormField("comment", "", "followup_client.comment"));
            formFields.add(new org.opensrp.form.domain.FormField("referral_status", "0", "followup_client.referral_status"));
            formFields.add(new org.opensrp.form.domain.FormField("service_provider_uiid", "", "followup_client.service_provider_uiid"));
            formFields.add(new org.opensrp.form.domain.FormField("visit_date", "", "followup_client.visit_date"));
            formFields.add(new org.opensrp.form.domain.FormField("referral_date", clientReferrals.getReferralDate().getTime() + "", "followup_client.referral_date"));
            formFields.add(new org.opensrp.form.domain.FormField("village", patient.getVillage() == null ? "" : patient.getVillage(), "followup_client.village"));
            formFields.add(new org.opensrp.form.domain.FormField("relationalid", uuid, "followup_client.relationalid"));
            formFields.add(new org.opensrp.form.domain.FormField("is_valid", "true", "followup_client.is_valid"));
            formFields.add(new org.opensrp.form.domain.FormField("id", uuid, "followup_client.id"));

            FormData formData = new FormData("followup_client", "/model/instance/follow_up_form/", formFields, null);
            FormInstance formInstance = new FormInstance(formData);
            FormSubmission formSubmission = new FormSubmission(clientReferrals.getFromFacilityId() + "", uuid + "", "client_follow_up_form", clientReferrals.getReferralUUID() + "", "1", 4, formInstance);


            logger.info("Coze : saving referral form submission");
            formSubmissionService.submit(formSubmission);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createAppointments(long healthFacilityPatientId) {
        for (int i = 1; i <= 8; i++) {
            ClientAppointments appointments = new ClientAppointments();

            HealthFacilitiesReferralClients healthFacilitiesReferralClients = new HealthFacilitiesReferralClients();
            healthFacilitiesReferralClients.setHealthFacilityClientId(healthFacilityPatientId);

            appointments.setHealthFacilitiesReferralClients(healthFacilitiesReferralClients);

            AppointmentType appointmentType = new AppointmentType();
            //TB Appointments
            appointmentType.setId(2);

            appointments.setAppointmentType(appointmentType);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, +i);
            c.add(Calendar.DAY_OF_MONTH, +checkIfWeekend(c.getTime()));
            appointments.setAppointmentDate(c.getTime());
            appointments.setIsCancelled(false);


            Status status = new Status();
            status.setStatusId(0);
            appointments.setStatus(status);

            try {
                logger.info("Coze:save appointment");
                clientsAppointmentsRepository.save(appointments);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void recalculateAppointments(long healthFacilityPatientId, long appointmentId, long appointmentDate) {
        List<ClientAppointments> clientAppointments = null;
        try {
            clientAppointments = clientsAppointmentsRepository.getAppointments("SELECT * FROM " + ClientAppointments.tbName + " WHERE " + ClientAppointments.COL_HEALTH_FACILITY_CLIENT_ID + " = " + healthFacilityPatientId, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        int i = 1;
        for (ClientAppointments patientAppointment : clientAppointments) {
            logger.info("Checking previous patient appointments");
            if (patientAppointment.getAppointment_id() > appointmentId) {

                logger.info("updating previous patient appointments date from " + patientAppointment.getAppointmentDate());
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(appointmentDate);
                c.add(Calendar.MONTH, +i);
                c.add(Calendar.DAY_OF_MONTH, +checkIfWeekend(c.getTime()));
                patientAppointment.setAppointmentDate(c.getTime());

                logger.info("updating to new  patient appointments date  " + c.getTime());

                try {
                    logger.info("Coze:update appointment");
                    clientsAppointmentsRepository.executeQuery("UPDATE " + ClientAppointments.tbName + " SET " + ClientAppointments.COL_APPOINTMENT_DATE + " = '" + c.getTime() + "' WHERE " + ClientAppointments.COL_APPOINTMENT_ID + " = " + patientAppointment.getAppointment_id());
                    logger.info("Coze:update appointment query : UPDATE " + ClientAppointments.tbName + " SET " + ClientAppointments.COL_APPOINTMENT_DATE + " = '" + c.getTime() + "' WHERE " + ClientAppointments.COL_APPOINTMENT_ID + " = " + patientAppointment.getAppointment_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private int checkIfWeekend(Date d1) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        System.out.println(c1.get(Calendar.DAY_OF_WEEK));
        if ((c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
            return 2;
        } else if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return 1;
        } else {
            return 0;
        }
    }
}
