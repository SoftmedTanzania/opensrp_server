package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.HealthFacilities;
import org.opensrp.domain.ReferralPatients;
import org.opensrp.dto.HealthFacilitiesDTO;
import org.opensrp.dto.ReferralPatientsDTO;
import org.opensrp.repository.HealthFacilityRepository;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.opensrp.service.HealthFacilitiesConverter;
import org.opensrp.service.ReferralPatientsConverter;
import org.opensrp.service.HealthFacilitiesService;
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
public class HealthFacilitiesController {
    private static Logger logger = LoggerFactory.getLogger(HealthFacilitiesController.class.toString());
    private HealthFacilitiesService healthFacilitiesService;
    private HealthFacilityRepository facilityRepository;
    private TaskSchedulerService scheduler;

    @Autowired
    public HealthFacilitiesController(HealthFacilitiesService healthFacilitiesService, HealthFacilityRepository facilityRepository, TaskSchedulerService scheduler) {
        this.healthFacilitiesService = healthFacilitiesService;
        this.facilityRepository = facilityRepository;
        this.scheduler = scheduler;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/health_facilities")
    public ResponseEntity<HttpStatus> savePatient(@RequestBody List<HealthFacilitiesDTO> healthFacilitiesDTOS) {
        try {
            if (healthFacilitiesDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }
            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.HEALTH_FACILITY_SUBMISSION, healthFacilitiesDTOS));
            List<HealthFacilities> healthFacilities = with(healthFacilitiesDTOS).convert(new Converter<HealthFacilitiesDTO, HealthFacilities>() {
                @Override
                public HealthFacilities convert(HealthFacilitiesDTO submission) {
                    return HealthFacilitiesConverter.toHealthFacilities(submission);
                }
            });

            for (HealthFacilities healthFacility : healthFacilities) {
                healthFacilitiesService.storeHealthFacilities(healthFacility);
            }

            logger.debug(format("Saved Health Facility to queue.\nSubmissions: {0}", healthFacilitiesDTOS));
        } catch (Exception e) {
            logger.error(format("Health Facility processing failed with exception {0}.\nSubmissions: {1}", e, healthFacilitiesDTOS));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }
}
