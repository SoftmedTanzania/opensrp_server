package org.opensrp.web.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.*;
import org.opensrp.dto.*;
import org.opensrp.form.domain.FormData;
import org.opensrp.form.domain.FormField;
import org.opensrp.form.domain.FormInstance;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.repository.*;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.opensrp.service.GoogleFCMService;
import org.opensrp.service.PatientsConverter;
import org.opensrp.service.ReferralPatientsService;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ReferralPatientsController {
	private static Logger logger = LoggerFactory.getLogger(ReferralPatientsController.class.toString());
	private ReferralPatientsService patientsService;
	private PatientsRepository patientsRepository;
	private HealthFacilityRepository healthFacilityRepository;
	private HealthFacilitiesPatientsRepository healthFacilitiesPatientsRepository;
	private TBEncounterRepository tbEncounterRepository;
	private PatientsAppointmentsRepository patientsAppointmentsRepository;
	private PatientReferralRepository patientReferralRepository;
	private PatientReferralIndicatorRepository patientReferralIndicatorRepository;
	private GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository;
	private TBPatientsRepository tbPatientsRepository;
	private FormSubmissionService formSubmissionService;
	private FormEntityConverter formEntityConverter;
	private TaskSchedulerService scheduler;
	private GoogleFCMService googleFCMService;
	private ReferralPatientsService referralPatientService;

	@Autowired
	public ReferralPatientsController(ReferralPatientsService patientsService, PatientsRepository patientsRepository, TaskSchedulerService scheduler,
	                                  HealthFacilityRepository healthFacilityRepository, HealthFacilitiesPatientsRepository healthFacilitiesPatientsRepository, PatientsAppointmentsRepository patientsAppointmentsRepository,
	                                  TBEncounterRepository tbEncounterRepository, PatientReferralRepository patientReferralRepository, TBPatientsRepository tbPatientsRepository, FormSubmissionService formSubmissionService,
	                                  FormEntityConverter formEntityConverter, GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository, GoogleFCMService googleFCMService,
	                                  PatientReferralIndicatorRepository patientReferralIndicatorRepository,ReferralPatientsService referralPatientService) {
		this.patientsService = patientsService;
		this.patientsRepository = patientsRepository;
		this.scheduler = scheduler;
		this.healthFacilityRepository = healthFacilityRepository;
		this.healthFacilitiesPatientsRepository = healthFacilitiesPatientsRepository;
		this.patientsAppointmentsRepository = patientsAppointmentsRepository;
		this.tbEncounterRepository = tbEncounterRepository;
		this.patientReferralRepository = patientReferralRepository;
		this.tbPatientsRepository = tbPatientsRepository;
		this.formSubmissionService = formSubmissionService;
		this.formEntityConverter = formEntityConverter;
		this.googlePushNotificationsUsersRepository = googlePushNotificationsUsersRepository;
		this.googleFCMService = googleFCMService;
		this.patientReferralIndicatorRepository = patientReferralIndicatorRepository;
		this.referralPatientService = referralPatientService;
	}

	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-patients")
	public ResponseEntity<PatientsDTO> savePatient(@RequestBody String json) {
		PatientsDTO patientsDTO = new Gson().fromJson(json, PatientsDTO.class);
		try {
			if (patientsDTO==null) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, patientsDTO));

			Patients patient = PatientsConverter.toPatients(patientsDTO);
			long healthfacilityPatientId = referralPatientService.savePatient(patient, patientsDTO.getHealthFacilityCode(), patientsDTO.getCtcNumber());

			patientsDTO.setPatientId(healthfacilityPatientId);

			JSONObject body = new JSONObject();
			body.put("type", "PatientReferral");

			JSONObject notificationObject = new JSONObject();
			notificationObject.put("body", body);

			Object[] facilityParams = new Object[]{patientsDTO.getHealthFacilityCode(), 1};
			List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName + " WHERE " + GooglePushNotificationsUsers.COL_FACILITY_UIID + " = ? AND " + GooglePushNotificationsUsers.COL_USER_TYPE + " = ?", facilityParams);
			JSONArray tokens = new JSONArray();
			for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : googlePushNotificationsUsers) {
				tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
			}

			if(tokens.length()>0) {
				String jsonData = new Gson().toJson(patientsDTO);
				JSONObject msg = new JSONObject(jsonData);
				googleFCMService.SendPushNotification(msg, notificationObject, tokens, false);
			}

			logger.debug(format("Added  Patient to queue.\nSubmissions: {0}", patientsDTO));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(format("Patients processing failed with exception {0}.\nSubmissions: {1}", e, json));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<PatientsDTO>(patientsDTO,OK);
	}

	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-ctc-patients")
	public ResponseEntity<HttpStatus> saveCtcPatients(@RequestBody String json) {
		try {

			List<CTCPatientsDTO> patientsDTOS = new Gson().fromJson(json, new TypeToken<List<CTCPatientsDTO>>() {
			}.getType());

			if (patientsDTOS.isEmpty()) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, patientsDTOS));


			for (CTCPatientsDTO dto : patientsDTOS) {
				try {
					System.out.println("saving patient");
					Patients patient = PatientsConverter.toPatients(dto);

					long healthfacilityPatientId = referralPatientService.savePatient(patient, dto.getHealthFacilityCode(), dto.getCtcNumber());

					List<PatientAppointments> appointments = PatientsConverter.toPatientsAppointments(dto);

					long id = 1;
					List<PatientAppointments> patientAppointments =  patientsAppointmentsRepository.getAppointments("SELECT * FROM "+PatientAppointments.tbName+" ORDER BY "+PatientAppointments.COL_APPOINTMENT_ID+" LIMIT 1",null);
					if(patientAppointments.size()>0){
						id = patientAppointments.get(0).getAppointment_id()+1;
					}

					for (PatientAppointments patientAppointment : appointments) {
						System.out.println("saving appointment");
						patientAppointment.setAppointment_id(id);
						patientAppointment.setAppointmentType(1);
						patientAppointment.setHealthFacilityPatientId(healthfacilityPatientId);
						patientsAppointmentsRepository.save(patientAppointment);
						id++;
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}


			logger.debug(format("Added  Patients and their appointments from CTC to queue.\nSubmissions: {0}", patientsDTOS));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(format("CTC Patients processing failed with exception {0}.\nSubmissions: {1}", e, json));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(CREATED);
	}


	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-tb-patient")
	@ResponseBody
	public ResponseEntity<TBCompletePatientDataDTO> saveTBPatients(@RequestBody String json) {
		TBPatientMobileClientDTO tbPatientMobileClientDTO = new Gson().fromJson(json,TBPatientMobileClientDTO.class);
		try {
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, tbPatientMobileClientDTO));

			Patients convertedPatient = PatientsConverter.toPatients(tbPatientMobileClientDTO);

			System.out.println("Coze:Patient data = "+new Gson().toJson(convertedPatient));

			TBPatient tbPatient = PatientsConverter.toTBPatients(tbPatientMobileClientDTO);


			System.out.println("Coze:TB patient data = "+new Gson().toJson(tbPatient));

			long healthfacilityPatientId = referralPatientService.savePatient(convertedPatient, tbPatientMobileClientDTO.getHealthFacilityCode(), null);

			HealthFacilitiesPatients hPatient = new HealthFacilitiesPatients();
			hPatient.setHealthFacilityPatientId(healthfacilityPatientId);

			tbPatient.setHealthFacilitiesPatients(hPatient);
			tbPatientsRepository.save(tbPatient);
			createAppointments(healthfacilityPatientId);


			TBCompletePatientDataDTO tbCompletePatientDataDTO = new TBCompletePatientDataDTO();
			List<HealthFacilitiesPatients> healthFacilitiesPatients = healthFacilitiesPatientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesPatients.tbName + " WHERE " + HealthFacilitiesPatients.COL_HEALTH_FACILITY_PATIENT_ID + "=?",
					new Object[]{healthfacilityPatientId});

			HealthFacilitiesPatients healthFacilitiesPatient = healthFacilitiesPatients.get(0);
			List<Patients> patients = patientsRepository.getPatients("SELECT * FROM " + org.opensrp.domain.Patients.tbName + " WHERE " + org.opensrp.domain.Patients.COL_PATIENT_ID + "=?",
					new Object[]{healthFacilitiesPatient.getPatient().getPatientId()});

			tbCompletePatientDataDTO.setPatientsDTO(PatientsConverter.toPatientsDTO(patients.get(0)));

			List<TBPatient> tbPatients = tbPatientsRepository.getTBPatients("SELECT * FROM " + org.opensrp.domain.TBPatient.tbName + " WHERE " + TBPatient.COL_HEALTH_FACILITY_PATIENT_ID + "=?",
					new Object[]{healthFacilitiesPatient.getPatient().getPatientId()});
			tbCompletePatientDataDTO.setTbPatientDTO(PatientsConverter.toTbPatientDTO(tbPatients.get(0)));

			List<PatientAppointments> patientAppointments = patientsAppointmentsRepository.getAppointments("SELECT * FROM " + PatientAppointments.tbName + " WHERE " + PatientAppointments.COL_HEALTH_FACILITY_PATIENT_ID + "=?",
					new Object[]{healthfacilityPatientId});
			tbCompletePatientDataDTO.setPatientsAppointmentsDTOS(PatientsConverter.toPatientAppointmentDTOsList(patientAppointments));

			return new ResponseEntity<TBCompletePatientDataDTO>(tbCompletePatientDataDTO,HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(format("TB Patients processing failed with exception {0}.\nSubmissions: {1}", e, tbPatientMobileClientDTO));

		}
		return null;
	}


	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-tb-encounters")
	public ResponseEntity<TBEncounter> saveTBEncounter(@RequestBody String json) {
		TBEncounterDTO tbEncounterDTOS = new Gson().fromJson(json,TBEncounterDTO.class);
		try {
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, tbEncounterDTOS));
			TBEncounter encounter = PatientsConverter.toTBEncounter(tbEncounterDTOS);


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
				tbEncounter.setUpdatedAt(Calendar.getInstance().getTime());
				tbEncounter.setMakohozi(encounter.getMakohozi());
				tbEncounter.setEncounterMonth(encounter.getEncounterMonth());
				tbEncounter.setHasFinishedPreviousMonthMedication(encounter.isHasFinishedPreviousMonthMedication());
				tbEncounter.setMedicationStatus(encounter.isMedicationStatus());
				tbEncounterRepository.update(tbEncounter);
				return new ResponseEntity<TBEncounter>(tbEncounter,HttpStatus.CREATED);

			} catch (Exception e) {
				e.printStackTrace();
			}


			logger.debug(format("Added  TB Encounters Submissions: {0}", tbEncounterDTOS));
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
		ReferralsDTO referralsDTO = new Gson().fromJson(jsonData,ReferralsDTO.class);
		try {
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, referralsDTO));

			PatientReferral patientReferral = PatientsConverter.toPatientReferral(referralsDTO);
			Long referralId = patientReferralRepository.save(patientReferral);

			for(Long indicatorId:referralsDTO.getServiceIndicatorIds()){
				PatientReferralIndicators referralIndicators = new PatientReferralIndicators();
				referralIndicators.setReferralId(referralId);
				referralIndicators.setReferralServiceIndicatorId(indicatorId);
				referralIndicators.setActive(true);

				patientReferralIndicatorRepository.save(referralIndicators);
			}

			List<PatientReferral> savedPatientReferrals = patientReferralRepository.getReferrals("SELECT * FROM "+PatientReferral.tbName+" ORDER BY "+PatientReferral.COL_REFERRAL_ID+" DESC LIMIT 1 ",null);
			logger.debug(format("Added  ReferralsDTO Submissions: {0}", referralsDTO));

			referralsDTO.setReferralId(savedPatientReferrals.get(0).getId());


			Object[] patientParams = new Object[]{
					savedPatientReferrals.get(0).getPatient().getPatientId()};
			List<Patients> patients = patientsRepository.getPatients("SELECT * FROM "+Patients.tbName+" WHERE "+Patients.COL_PATIENT_ID+" =?",patientParams);

			PatientReferralsDTO patientReferralsDTO = new PatientReferralsDTO();
			patientReferralsDTO.setPatientsDTO(PatientsConverter.toPatientsDTO(patients.get(0)));

			List<ReferralsDTO> patientReferrals = new ArrayList<>();
			patientReferrals.add(PatientsConverter.toPatientDTO(savedPatientReferrals.get(0)));

			for(ReferralsDTO refDTO:patientReferrals) {
				Object[] args2 = new Object[1];
				args2[0] = refDTO.getReferralId();
				List<PatientReferralIndicators> patientReferralIndicators = patientReferralIndicatorRepository.getPatientReferralIndicators("SELECT * FROM " + PatientReferralIndicators.tbName + " WHERE " + PatientReferralIndicators.COL_REFERRAL_ID + " =?", args2);
				List<Long> patientReferralIndicatorsIds = new ArrayList<>();
				for(PatientReferralIndicators referralIndicator:patientReferralIndicators){
					patientReferralIndicatorsIds.add(referralIndicator.getReferralServiceIndicatorId());
				}
				refDTO.setServiceIndicatorIds(patientReferralIndicatorsIds);
			}

			patientReferralsDTO.setPatientReferralsList(patientReferrals);

			if(referralsDTO.getReferralType()!=4) {

				JSONObject body = new JSONObject();
				body.put("type", "PatientReferral");

				JSONObject notificationObject = new JSONObject();
				notificationObject.put("body", body);

				Object[] facilityParams = new Object[]{savedPatientReferrals.get(0).getFacilityId(), 1};
				List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM " + GooglePushNotificationsUsers.tbName + " WHERE " + GooglePushNotificationsUsers.COL_FACILITY_UIID + " = ? AND " + GooglePushNotificationsUsers.COL_USER_TYPE + " = ?", facilityParams);
				JSONArray tokens = new JSONArray();
				for (GooglePushNotificationsUsers googlePushNotificationsUsers1 : googlePushNotificationsUsers) {
					tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
				}


				String json = new Gson().toJson(patientReferralsDTO);

				JSONObject msg = new JSONObject(json);

				googleFCMService.SendPushNotification(msg, notificationObject, tokens, false);
			}else{
				String healthFacilitySql = "SELECT * FROM " + HealthFacilities.tbName + " WHERE " +
						HealthFacilities.COL_FACILITY_CTC_CODE + " = ? OR " + HealthFacilities.COL_OPENMRS_UIID + " = ?";
				Object[] healthFacilityParams = new Object[]{
						patientReferral.getFacilityId(),patientReferral.getFacilityId()};

				List<HealthFacilities> healthFacilities = null;
				try {
					healthFacilities = healthFacilityRepository.getHealthFacility(healthFacilitySql, healthFacilityParams);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if(healthFacilities!=null)
					saveReferralFollowup(patientReferral,healthFacilities.get(0).getId()+"");
			}


			return new ResponseEntity<ReferralsDTO>(referralsDTO,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(format("ReferralsDTO processing failed with exception {0}.\nSubmissions: {1}", e, jsonData));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
	}


	public void saveReferralFollowup(PatientReferral patientReferral,String facilityId){
		List<HealthFacilitiesPatients> healthFacilitiesPatients = null;
		try {
			healthFacilitiesPatients = healthFacilitiesPatientsRepository.getHealthFacilityPatients("SELECT * FROM "+ HealthFacilitiesPatients.tbName+" WHERE "+HealthFacilitiesPatients.COL_PATIENT_ID+ " = "+patientReferral.getPatient().getPatientId()+" AND "+HealthFacilitiesPatients.COL_FACILITY_ID+ " = '"+facilityId+"'",null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Patients> patients = null;
		try {
			patients = patientsRepository.getPatients("SELECT * FROM "+ Patients.tbName+" WHERE "+Patients.COL_PATIENT_ID+" = "+patientReferral.getPatient().getPatientId(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Patients patient = patients.get(0);

			HealthFacilitiesPatients healthFacilitiesPatient = healthFacilitiesPatients.get(0);

			List<org.opensrp.form.domain.FormField> formFields = new ArrayList<>();
			formFields.add(new org.opensrp.form.domain.FormField("first_name", patient.getFirstName(), null));
			formFields.add(new org.opensrp.form.domain.FormField("middle_name", patient.getMiddleName(), null));
			formFields.add(new org.opensrp.form.domain.FormField("surname", patient.getSurname(), null));
			formFields.add(new org.opensrp.form.domain.FormField("community_based_hiv_service", patient.getCommunityBasedHivService(), null));
			formFields.add(new org.opensrp.form.domain.FormField("ctc_number", healthFacilitiesPatient.getCtcNumber(), null));
			formFields.add(new org.opensrp.form.domain.FormField("care_taker_name", healthFacilitiesPatient.getCtcNumber(), null));
			formFields.add(new org.opensrp.form.domain.FormField("care_taker_name_phone_number", healthFacilitiesPatient.getCtcNumber(), null));
			formFields.add(new org.opensrp.form.domain.FormField("care_taker_relationship", healthFacilitiesPatient.getCtcNumber(), null));
			formFields.add(new org.opensrp.form.domain.FormField("facility_id", patientReferral.getFromFacilityId() + "", null));
			formFields.add(new FormField("referral_reason", patientReferral.getReferralReason(), null));

			FormData formData = new FormData("client_referral", "/model/instance/follow_up_form/", formFields, null);
			FormInstance formInstance = new FormInstance(formData);
			FormSubmission formSubmission = new FormSubmission(patientReferral.getFromFacilityId(), UUID.randomUUID().toString(), "follow_up", patientReferral.getId() + "", "1", 1, formInstance);

			formSubmissionService.submit(formSubmission);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/receive-feedback")
	public ResponseEntity<String> saveReferralFeedback(@RequestBody String json) {
		try {
			System.out.println("Coze: receive feedback");
			ReferralsDTO referralsDTO = new Gson().fromJson(json,ReferralsDTO.class);
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, referralsDTO));

			List<PatientReferral> referrals = patientReferralRepository.getReferrals("SELECT * FROM " + org.opensrp.domain.PatientReferral.tbName + " WHERE " + PatientReferral.COL_REFERRAL_ID + "=?",
					new Object[]{referralsDTO.getReferralId()});

			PatientReferral referral=null;
			try {
				referral = referrals.get(0);
				referral.setReferralStatus(referralsDTO.getReferralStatus());
				referral.setServiceGivenToPatient(referralsDTO.getServiceGivenToPatient());
				referral.setOtherNotes(referralsDTO.getOtherNotes());
				referral.setReferralStatus(referralsDTO.getReferralStatus());
			}catch (Exception e){
				e.printStackTrace();
				System.out.println("Coze: referral not found");
				return new ResponseEntity<String>("referral not found",PRECONDITION_FAILED);
			}


			if(referral!=null) {
				String sql ="UPDATE " + PatientReferral.tbName + " SET " +
						PatientReferral.COL_REFERRAL_STATUS + " = '" + referral.getReferralStatus() + "' , " +
						PatientReferral.COL_SERVICES_GIVEN_TO_PATIENT + " = '" + referral.getServiceGivenToPatient() + "' , " +
						PatientReferral.COL_OTHER_NOTES + " = '" + referral.getOtherNotes() + "' WHERE  " + PatientReferral.COL_REFERRAL_ID + " = " + referral.getId();
				patientReferralRepository.executeQuery(sql);
				System.out.println("Coze: updated referral feedback : "+sql);

				if (referral.getReferralType() == 1) {
					try {
						FormSubmission formSubmission = formSubmissionService.findByInstanceId(referral.getInstanceId());
						formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission, PatientReferral.COL_SERVICES_GIVEN_TO_PATIENT, referral.getServiceGivenToPatient());
						formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission, PatientReferral.COL_OTHER_NOTES, referral.getOtherNotes());
						formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission, PatientReferral.COL_REFERRAL_STATUS, referral.getReferralStatus() + "");
						formSubmissionService.update(formSubmission);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				JSONObject body = new JSONObject();
				body.put("type","PatientReferral");

				JSONObject notificationObject = new JSONObject();
				notificationObject.put("body",body);

				Object[] facilityParams = new Object[]{referralsDTO.getServiceProviderUIID(),0};
				List<GooglePushNotificationsUsers> googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("SELECT * FROM "+GooglePushNotificationsUsers.tbName+" WHERE "+GooglePushNotificationsUsers.COL_USER_UIID+" = ? AND "+GooglePushNotificationsUsers.COL_USER_TYPE+" = ?",facilityParams);
				JSONArray tokens = new JSONArray();
				for(GooglePushNotificationsUsers googlePushNotificationsUsers1:googlePushNotificationsUsers){
					tokens.put(googlePushNotificationsUsers1.getGooglePushNotificationToken());
				}

				String referralDTOJson = new Gson().toJson(referralsDTO);

				JSONObject msg = new JSONObject(referralDTOJson);

				try {
					if(referral.getReferralType()==1)
						googleFCMService.SendPushNotification(msg, notificationObject, tokens, false);
					else{
						googleFCMService.SendPushNotification(msg, notificationObject, tokens, true);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}else{
				return new ResponseEntity<String>("Referral Not found",BAD_REQUEST);
			}


			logger.debug(format("updated  ReferralsFeedbackDTO Submissions: {0}", referralsDTO));
		} catch (Exception e) {
			logger.error(format("ReferralsFeedbackDTO processing failed with exception {0}.\nSubmissions: {1}", e, json));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>("success",OK);
	}





	//TODO implement regeration of appointments
	private void createAppointments(long healthfacilityPatientId) {
		for (int i = 1; i <= 8; i++) {
			PatientAppointments appointments = new PatientAppointments();
			appointments.setHealthFacilityPatientId(healthfacilityPatientId);
			appointments.setAppointmentType(2);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, +i);
			c.add(Calendar.DAY_OF_MONTH, +checkIfWeekend(c.getTime()));
			appointments.setAppointmentDate(c.getTime());
			appointments.setIsCancelled(false);

			try {
				System.out.println("Coze:save appointment");
				patientsAppointmentsRepository.save(appointments);
			} catch (Exception e) {
				e.printStackTrace();
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
