package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.HealthFacilitiesPatients;
import org.opensrp.domain.PatientAppointments;
import org.opensrp.domain.PatientReferral;
import org.opensrp.domain.Patients;
import org.opensrp.dto.CTCPatientsDTO;
import org.opensrp.dto.PatientReferralsDTO;
import org.opensrp.dto.PatientsDTO;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionConverter;
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
	private TaskSchedulerService scheduler;

    @Autowired
    public ReferralPatientsController(ReferralPatientsService patientsService, PatientsRepository patientsRepository, TaskSchedulerService scheduler) {
        this.patientsService = patientsService;
        this.patientsRepository = patientsRepository;
		this.scheduler = scheduler;
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


	            //TODO finalize saving CTC patinents appointments
//	            HealthFacilitiesPatients healthFacilitiesPatients = new HealthFacilitiesPatients();
//	            if(patientsResults.size()>0){
//		            System.out.println("Coze = using the received patients");
//		            id = patientsResults.get(0).getPatientId();
//	            }else{
//		            System.out.println("Coze = saving patient Data");
//		            id = patientsRepository.save(patient);
//	            }
//	            healthFacilitiesPatients.setPatient_id(id);
//	            healthFacilitiesPatients.setCtcNumber(dto.getCtc_number());
//	            healthFacilitiesPatients.setFacilityId(dto.getCtc_number());







	            patientsService.storeCTCPatients(patient);

	            List<PatientAppointments> appointments = PatientsConverter.toPatientsAppointments(dto);

	            for(PatientAppointments patientAppointments:appointments){

	            }
            }







            logger.debug(format("Added  Patient to queue.\nSubmissions: {0}", patientsDTOS));
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
