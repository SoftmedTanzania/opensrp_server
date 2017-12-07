package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensrp.common.AllConstants;
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
//        try {
//            if (patientsDTOS.isEmpty()) {
//                return new ResponseEntity<>(BAD_REQUEST);
//            }
//            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.REFERRED_PATIENTS_SUBMISSION, patientsDTOS));
//            String json = new Gson().toJson(patientsDTOS);
//            List<PatientsDTO> healthFacilitiesDTOs = new Gson().fromJson(json, new TypeToken<List<PatientsDTO>>() {}.getType());
//
//            try{
//
//                List<Patients>patients = with(healthFacilitiesDTOs).convert(new Converter<PatientsDTO, Patients>() {
//                    @Override
//                    public Patients convert(PatientsDTO submission) {
//                        return PatientsConverter.toPatients(submission);
//                    }
//                });
//
//                for(Patients ctcPatients:patients){
//                    patientsService.storeCTCPatients(ctcPatients);
//                }
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }
//            logger.debug(format("Added  Patient to queue.\nSubmissions: {0}", patientsDTOS));
//        } catch (Exception e) {
//            logger.error(format("Patients processing failed with exception {0}.\nSubmissions: {1}", e, patientsDTOS));
//            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<>(CREATED);
    }


    @RequestMapping(method = GET, value="/all-patients-referrals")
	@ResponseBody
	private List<PatientReferralsDTO> getAllPatientsReferrals() {
		List<PatientReferralsDTO> patientReferralsDTOS = patientsService.getAllPatientReferrals();
		return patientReferralsDTOS;
	}
}
