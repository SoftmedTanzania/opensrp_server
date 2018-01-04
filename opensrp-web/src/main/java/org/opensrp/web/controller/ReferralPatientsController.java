package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.*;
import org.opensrp.dto.*;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.repository.*;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.opensrp.service.PatientsConverter;
import org.opensrp.service.ReferralPatientsService;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	private TBPatientsRepository tbPatientsRepository;
	private FormSubmissionService formSubmissionService;
	private FormEntityConverter formEntityConverter;
	private TaskSchedulerService scheduler;

	@Autowired
	public ReferralPatientsController(ReferralPatientsService patientsService, PatientsRepository patientsRepository, TaskSchedulerService scheduler,
	                                  HealthFacilityRepository healthFacilityRepository, HealthFacilitiesPatientsRepository healthFacilitiesPatientsRepository, PatientsAppointmentsRepository patientsAppointmentsRepository,
	                                  TBEncounterRepository tbEncounterRepository, PatientReferralRepository patientReferralRepository, TBPatientsRepository tbPatientsRepository,FormSubmissionService formSubmissionService,FormEntityConverter formEntityConverter) {
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
	}

	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save_patients")
	public ResponseEntity<HttpStatus> savePatient(@RequestBody List<PatientsDTO> patientsDTOS) {
		try {
			if (patientsDTOS.isEmpty()) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, patientsDTOS));
			String json = new Gson().toJson(patientsDTOS);
			List<PatientsDTO> healthFacilitiesDTOs = new Gson().fromJson(json, new TypeToken<List<PatientsDTO>>() {
			}.getType());

			try {
				List<Patients> patients = with(healthFacilitiesDTOs).convert(new Converter<PatientsDTO, Patients>() {
					@Override
					public Patients convert(PatientsDTO submission) {
						return PatientsConverter.toPatients(submission);
					}
				});

				for (Patients ctcPatients : patients) {
					patientsService.storeCTCPatients(ctcPatients);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.debug(format("Added  Patient to queue.\nSubmissions: {0}", patientsDTOS));
		} catch (Exception e) {
			logger.error(format("Patients processing failed with exception {0}.\nSubmissions: {1}", e, patientsDTOS));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(CREATED);
	}

	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save_ctc_patients")
	public ResponseEntity<HttpStatus> saveCtcPatients(@RequestBody List<CTCPatientsDTO> ctcPatientsDTOS) {
		try {
			if (ctcPatientsDTOS.isEmpty()) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, ctcPatientsDTOS));
			String json = new Gson().toJson(ctcPatientsDTOS);
			List<CTCPatientsDTO> patientsDTOS = new Gson().fromJson(json, new TypeToken<List<CTCPatientsDTO>>() {
			}.getType());

			for (CTCPatientsDTO dto : patientsDTOS) {
				Patients patient = PatientsConverter.toPatients(dto);

				long healthfacilityPatientId = savePatient(patient, dto.getHealthFacilityCode(), dto.getCtc_number());
				List<PatientAppointments> appointments = PatientsConverter.toPatientsAppointments(dto);

				for (PatientAppointments patientAppointment : appointments) {
					patientAppointment.setStatus("CTC");
					patientAppointment.setHealthFacilityPatientId(healthfacilityPatientId);
					patientsAppointmentsRepository.save(patientAppointment);
				}
			}


			logger.debug(format("Added  Patients and their appointments from CTC to queue.\nSubmissions: {0}", ctcPatientsDTOS));
		} catch (Exception e) {
			logger.error(format("CTC Patients processing failed with exception {0}.\nSubmissions: {1}", e, ctcPatientsDTOS));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(CREATED);
	}


	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save_tb_patients")
	@ResponseBody
	public TBCompletePatientDataDTO saveTBPatients(@RequestBody TBPatientMobileClientDTO tbPatientMobileClientDTO) {
		try {
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, tbPatientMobileClientDTO));

			Patients convertedPatient = PatientsConverter.toPatients(tbPatientMobileClientDTO);
			TBPatient tbPatient = PatientsConverter.toTBPatients(tbPatientMobileClientDTO);
			long healthfacilityPatientId = savePatient(convertedPatient, tbPatientMobileClientDTO.getHealthFacilityCode(), null);
			tbPatient.setHealthFacilityPatientId(healthfacilityPatientId);
			tbPatientsRepository.save(tbPatient);
			createAppointments(healthfacilityPatientId);


			TBCompletePatientDataDTO tbCompletePatientDataDTO = new TBCompletePatientDataDTO();
			List<HealthFacilitiesPatients> healthFacilitiesPatients = healthFacilitiesPatientsRepository.getHealthFacilityPatients("SELECT * FROM " + HealthFacilitiesPatients.tbName + " WHERE " + HealthFacilitiesPatients.COL_HEALTH_FACILITY_PATIENT_ID + "=?",
					new Object[]{healthfacilityPatientId});

			HealthFacilitiesPatients healthFacilitiesPatient = healthFacilitiesPatients.get(0);
			List<Patients> patients = patientsRepository.getPatients("SELECT * FROM " + org.opensrp.domain.Patients.tbName + " WHERE " + org.opensrp.domain.Patients.COL_PATIENT_ID + "=?",
					new Object[]{healthFacilitiesPatient.getPatient_id()});

			tbCompletePatientDataDTO.setPatientsDTO(PatientsConverter.toPatientsDTO(patients.get(0)));

			List<TBPatient> tbPatients = tbPatientsRepository.getTBPatients("SELECT * FROM " + org.opensrp.domain.TBPatient.tbName + " WHERE " + TBPatient.COL_HEALTH_FACILITY_PATIENT_ID + "=?",
					new Object[]{healthFacilitiesPatient.getPatient_id()});
			tbCompletePatientDataDTO.setTbPatientDTO(PatientsConverter.toTbPatientDTO(tbPatients.get(0)));

			List<PatientAppointments> patientAppointments = patientsAppointmentsRepository.getAppointments("SELECT * FROM " + PatientAppointments.tbName + " WHERE " + PatientAppointments.COL_HEALTH_FACILITY_PATIENT_ID + "=?",
					new Object[]{healthfacilityPatientId});
			tbCompletePatientDataDTO.setPatientsAppointmentsDTOS(PatientsConverter.toPatientAppointmentDTOsList(patientAppointments));


			return tbCompletePatientDataDTO;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(format("TB Patients processing failed with exception {0}.\nSubmissions: {1}", e, tbPatientMobileClientDTO));

		}
		return null;
	}


	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save_tb_encounters")
	public ResponseEntity<HttpStatus> saveTBEncounter(@RequestBody TBEncounterDTO tbEncounterDTOS) {
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

	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save_facility_referral")
	public ResponseEntity<HttpStatus> saveFacilityReferral(@RequestBody ReferralsDTO referralsDTO) {
		try {
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, referralsDTO));

			referralsDTO.setReferralSource(1);
			referralsDTO.setReferralStatus(0);
			PatientReferral patientReferral = PatientsConverter.toPatientReferral(referralsDTO);
			patientReferralRepository.save(patientReferral);

			logger.debug(format("Added  ReferralsDTO Submissions: {0}", referralsDTO));
		} catch (Exception e) {
			logger.error(format("ReferralsDTO processing failed with exception {0}.\nSubmissions: {1}", e, referralsDTO));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(CREATED);
	}



	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/receive_feedback")
	public ResponseEntity<HttpStatus> saveReferralFeedback(@RequestBody ReferralsFeedbackDTO referralsFeedbackDTO) {
		try {
			scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, referralsFeedbackDTO));

			List<PatientReferral> referrals = patientReferralRepository.getReferrals("SELECT * FROM " + org.opensrp.domain.PatientReferral.tbName + " WHERE " + PatientReferral.COL_REFERRAL_ID + "=?",
					new Object[]{referralsFeedbackDTO.getReferralId()});

			PatientReferral referral = referrals.get(0);
			referral.setReferralStatus(referralsFeedbackDTO.getReferralStatus());
			referral.setServiceGivenToPatient(referralsFeedbackDTO.getServiceGivenToPatient());
			referral.setOtherNotes(referralsFeedbackDTO.getOtherNotes());
			patientReferralRepository.save(referral);

			if(referral.getReferralSource()==0){
				FormSubmission formSubmission = formSubmissionService.findByInstanceId(referral.getInstanceId());
				formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission,PatientReferral.COL_SERVICES_GIVEN_TO_PATIENT,referral.getServiceGivenToPatient());
				formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission,PatientReferral.COL_OTHER_NOTES,referral.getOtherNotes());
				formSubmission = formEntityConverter.updateFormSUbmissionField(formSubmission,PatientReferral.COL_REFERRAL_STATUS,referral.getReferralStatus()+"");

				formSubmissionService.update(formSubmission);
			}


			logger.debug(format("updated  ReferralsFeedbackDTO Submissions: {0}", referralsFeedbackDTO));
		} catch (Exception e) {
			logger.error(format("ReferralsFeedbackDTO processing failed with exception {0}.\nSubmissions: {1}", e, referralsFeedbackDTO));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(CREATED);
	}


	private long savePatient(Patients patient, String healthFacilityCode, String ctcNumber) {
		String query = "SELECT * FROM " + Patients.tbName + " WHERE " +
				Patients.COL_PATIENT_FIRST_NAME + " = ?     AND " +
				Patients.COL_PATIENT_MIDDLE_NAME + " = ?    AND " +
				Patients.COL_PATIENT_SURNAME + " = ?        AND " +
				Patients.COL_PHONE_NUMBER + " = ?";
		Object[] params = new Object[]{
				patient.getFirstName(),
				patient.getMiddleName(),
				patient.getSurname(),
				patient.getPhoneNumber()};
		List<Patients> patientsResults = null;
		try {
			patientsResults = patientsRepository.getPatients(query, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Coze = number of patients found = " + patientsResults.size());
		long id;
		if (patientsResults.size() > 0) {
			System.out.println("Coze = using the received patients ");
			id = patientsResults.get(0).getPatientId();
		} else {
			System.out.println("Coze = saving patient Data ");
			try {
				id = patientsRepository.save(patient);
			} catch (Exception e) {
				e.printStackTrace();
				id = -1;
			}
		}

		//Obtaining health facilityId from tbl_facilities
		String healthFacilitySql = "SELECT * FROM " + HealthFacilities.tbName + " WHERE " +
				HealthFacilities.COL_FACILITY_CTC_CODE + " = ? OR " + HealthFacilities.COL_OPENMRS_UIID + " = ?";
		Object[] healthFacilityParams = new Object[]{
				healthFacilityCode,};

		System.out.println("Coze facility ctc code = " + healthFacilityCode);
		Long healthFacilityId = (long) 0;
		List<HealthFacilities> healthFacilities = null;
		try {
			healthFacilities = healthFacilityRepository.getHealthFacility(healthFacilitySql, healthFacilityParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (healthFacilities.size() > 0) {
			healthFacilityId = healthFacilities.get(0).getId();
		}

		HealthFacilitiesPatients healthFacilitiesPatients = new HealthFacilitiesPatients();
		healthFacilitiesPatients.setPatient_id(id);
		healthFacilitiesPatients.setCtcNumber(ctcNumber);
		healthFacilitiesPatients.setFacilityId(healthFacilityId);


		String healthFacilityPatientsquery = "SELECT * FROM " + HealthFacilitiesPatients.tbName + " WHERE " +
				HealthFacilitiesPatients.COL_CTC_NUMBER + " = ?    AND " +
				HealthFacilitiesPatients.COL_PATIENT_ID + " = ?    AND " +
				HealthFacilitiesPatients.COL_FACILITY_ID + " = ?";

		Object[] healthFacilityPatientsparams = new Object[]{
				healthFacilitiesPatients.getCtcNumber(),
				healthFacilitiesPatients.getPatient_id(),
				healthFacilitiesPatients.getFacilityId()};

		List<HealthFacilitiesPatients> healthFacilitiesPatientsResults = null;
		try {
			healthFacilitiesPatientsResults = healthFacilitiesPatientsRepository.getHealthFacilityPatients(healthFacilityPatientsquery, healthFacilityPatientsparams);
		} catch (Exception e) {
			e.printStackTrace();
		}


		long healthfacilityPatientId = -1;
		if (healthFacilitiesPatientsResults.size() > 0) {
			healthfacilityPatientId = healthFacilitiesPatientsResults.get(0).getHealthFacilityPatientId();
		} else {
			try {
				healthfacilityPatientId = healthFacilitiesPatientsRepository.save(healthFacilitiesPatients);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return healthfacilityPatientId;
	}

	private void createAppointments(long healthfacilityPatientId) {
		for (int i = 1; i <= 8; i++) {
			PatientAppointments appointments = new PatientAppointments();
			appointments.setHealthFacilityPatientId(healthfacilityPatientId);
			appointments.setStatus("TB");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, +i);
			c.add(Calendar.DAY_OF_MONTH, +checkIfWeekend(c.getTime()));
			appointments.setAppointmentDate(c.getTime());
			appointments.setIsCancelled(false);
		}

	}

	private int checkIfWeekend(Date d1) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		System.out.println(c1.get(Calendar.DAY_OF_WEEK));
		if ((c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {  //or sunday
			return 2;
		} else if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return 1;
		} else {
			return 0;
		}
	}
}
