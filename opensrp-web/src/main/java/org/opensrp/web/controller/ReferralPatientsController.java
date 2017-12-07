package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.*;
import org.opensrp.dto.CTCPatientsDTO;
import org.opensrp.dto.PatientReferralsDTO;
import org.opensrp.dto.PatientsDTO;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionConverter;
import org.opensrp.repository.HealthFacilitiesPatientsRepository;
import org.opensrp.repository.HealthFacilityRepository;
import org.opensrp.repository.PatientsAppointmentsRepository;
import org.opensrp.repository.PatientsRepository;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.opensrp.service.PatientsConverter;
import org.opensrp.service.ReferralPatientsService;
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

import java.text.ParseException;
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
    private PatientsAppointmentsRepository patientsAppointmentsRepository;
	private TaskSchedulerService scheduler;

    @Autowired
    public ReferralPatientsController(ReferralPatientsService patientsService, PatientsRepository patientsRepository, TaskSchedulerService scheduler,
                                      HealthFacilityRepository healthFacilityRepository,HealthFacilitiesPatientsRepository healthFacilitiesPatientsRepository,PatientsAppointmentsRepository patientsAppointmentsRepository) {
        this.patientsService = patientsService;
        this.patientsRepository = patientsRepository;
		this.scheduler = scheduler;
		this.healthFacilityRepository = healthFacilityRepository;
		this.healthFacilitiesPatientsRepository = healthFacilitiesPatientsRepository;
		this.patientsAppointmentsRepository = patientsAppointmentsRepository;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save_patients")
    public ResponseEntity<HttpStatus> savePatient(@RequestBody List<PatientsDTO> patientsDTOS) {
        try {
            if (patientsDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }
            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, patientsDTOS));
            String json = new Gson().toJson(patientsDTOS);
            List<PatientsDTO> healthFacilitiesDTOs = new Gson().fromJson(json, new TypeToken<List<PatientsDTO>>() {}.getType());

            try{
				List<Patients>patients = with(healthFacilitiesDTOs).convert(new Converter<PatientsDTO, Patients>() {
					@Override
					public Patients convert(PatientsDTO submission) {
						return PatientsConverter.toPatients(submission);
					}
				});

				for(Patients ctcPatients:patients){
					patientsService.storeCTCPatients(ctcPatients);
				}
            }
            catch(Exception e){
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
            List<CTCPatientsDTO> patientsDTOS = new Gson().fromJson(json, new TypeToken<List<CTCPatientsDTO>>() {}.getType());

            for(CTCPatientsDTO dto: patientsDTOS){
	            Patients patient = PatientsConverter.toPatients(dto);

	            String query = "SELECT * FROM " + Patients.tbName + " WHERE " +
			            Patients.COL_PATIENT_FIRST_NAME + " = ?     AND " +
			            Patients.COL_PATIENT_MIDDLE_NAME + " = ?    AND " +
			            Patients.COL_PATIENT_SURNAME + " = ?        AND " +
			            Patients.COL_PHONE_NUMBER + " = ?" ;

	            Object[] params = new Object[] {
			            patient.getFirstName(),
			            patient.getMiddleName(),
			            patient.getSurname(),
			            patient.getPhoneNumber()};
	            List<Patients> patientsResults = patientsRepository.getPatientReferrals(query,params);
	            System.out.println("Coze = number of patients found = "+patientsResults.size());
	            Long id;


	            if(patientsResults.size()>0){
		            System.out.println("Coze = using the received patients ");
		            id = patientsResults.get(0).getPatientId();
	            }else{
		            System.out.println("Coze = saving patient Data ");
		            id = patientsRepository.save(patient);
	            }

	            //Obtaining health facilityId from tbl_facilities
	            String healthFacilitySql = "SELECT * FROM "+ HealthFacilities.tbName+" WHERE "+
			            HealthFacilities.COL_FACILITY_CTC_CODE+" = ?";
	            Object[] healthFacilityParams = new Object[] {
			            dto.getHealthFacilityCode()};

	            Long healthFacilityId=(long)0;
	            List<HealthFacilities> healthFacilities = healthFacilityRepository.getHealthFacility(healthFacilitySql,healthFacilityParams);
	            if(healthFacilities.size()==0){
		            healthFacilityId = healthFacilities.get(0).getId();
	            }

	            HealthFacilitiesPatients healthFacilitiesPatients = new HealthFacilitiesPatients();
	            healthFacilitiesPatients.setPatient_id(id);
	            healthFacilitiesPatients.setCtcNumber(dto.getCtc_number());
	            healthFacilitiesPatients.setFacilityId(healthFacilityId);


	            String healthFacilityPatientsquery = "SELECT * FROM " + HealthFacilitiesPatients.tbName + " WHERE " +
			            HealthFacilitiesPatients.COL_CTC_NUMBER +  " = ?    AND " +
			            HealthFacilitiesPatients.COL_PATIENT_ID +  " = ?    AND " +
			            HealthFacilitiesPatients.COL_FACILITY_ID + " = ?" ;

	            Object[] healthFacilityPatientsparams = new Object[] {
			            healthFacilitiesPatients.getCtcNumber(),
			            healthFacilitiesPatients.getPatient_id(),
			            healthFacilitiesPatients.getFacilityId()};

	            List<HealthFacilitiesPatients> healthFacilitiesPatientsResults = healthFacilitiesPatientsRepository.getHealthFacilityPatients(healthFacilityPatientsquery,healthFacilityPatientsparams);


	            Long healthfacilityPatientId;
	            if(healthFacilitiesPatientsResults.size()>0){
		            healthfacilityPatientId = healthFacilitiesPatientsResults.get(0).getId();
	            }else{
		            healthfacilityPatientId = healthFacilitiesPatientsRepository.save(healthFacilitiesPatients);
	            }

	            List<PatientAppointments> appointments = PatientsConverter.toPatientsAppointments(dto);

	            for(PatientAppointments patientAppointment:appointments){
	            	patientAppointment.setStatus("0");
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


    @RequestMapping(method = GET, value="/all-patients-referrals")
	@ResponseBody
	private List<PatientReferralsDTO> getAllPatientsReferrals() {
		List<PatientReferralsDTO> patientReferralsDTOS = patientsService.getAllPatientReferrals();
		return patientReferralsDTOS;
	}
}
