package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.CTC_patients;
import org.opensrp.dto.CTCPatientsDTO;
import org.opensrp.repository.PatientsRepository;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.opensrp.service.CTC2PatientsConverter;
import org.opensrp.service.CTC2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class CTCPatientsSubmissionController {
    private static Logger logger = LoggerFactory.getLogger(CTCPatientsSubmissionController.class.toString());
    private CTC2Service ctc2Service;
    private PatientsRepository patientsRepository;
	private TaskSchedulerService scheduler;

    @Autowired
    public CTCPatientsSubmissionController(CTC2Service ctc2Service,PatientsRepository patientsRepository,TaskSchedulerService scheduler) {
        this.ctc2Service = ctc2Service;
        this.patientsRepository = patientsRepository;
		this.scheduler = scheduler;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/ctc_patients")
    public ResponseEntity<HttpStatus> savePatient(@RequestBody List<CTCPatientsDTO> ctcPatientsDTOS) {
        try {
            if (ctcPatientsDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }
            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.CTC2_PATIENTS_SUBMISSION, ctcPatientsDTOS));
            
            try{

				List<CTC_patients>patients = with(ctcPatientsDTOS).convert(new Converter<CTCPatientsDTO, CTC_patients>() {
					@Override
					public CTC_patients convert(CTCPatientsDTO submission) {
						return CTC2PatientsConverter.toCTCPatients(submission);
					}
				});

				for(CTC_patients ctcPatients:patients){
					ctc2Service.storeCTCPatients(ctcPatients);
				}
            }
            catch(Exception e){
            	e.printStackTrace();
            }
            logger.debug(format("Added CTC2 Patient to queue.\nSubmissions: {0}", ctcPatientsDTOS));
        } catch (Exception e) {
            logger.error(format("CTC2 Patients processing failed with exception {0}.\nSubmissions: {1}", e, ctcPatientsDTOS));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }
}
