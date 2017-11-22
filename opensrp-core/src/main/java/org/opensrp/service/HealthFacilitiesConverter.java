package org.opensrp.service;

import com.google.gson.Gson;
import org.opensrp.domain.HealthFacilities;
import org.opensrp.domain.ReferralPatients;
import org.opensrp.dto.HealthFacilitiesDTO;
import org.opensrp.dto.ReferralPatientsDTO;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormInstance;
import org.opensrp.form.domain.FormSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class HealthFacilitiesConverter {
    private static Logger logger = LoggerFactory.getLogger(HealthFacilitiesConverter.class.toString());


    public static HealthFacilities toHealthFacilities(HealthFacilitiesDTO healthFacilitiesDTO) {
        try {
            HealthFacilities healthFacilities = new HealthFacilities();

            healthFacilities.setFacilityId(healthFacilitiesDTO.getFacilityId());
            healthFacilities.setParentId(healthFacilitiesDTO.getParentId());
            healthFacilities.setFacilityName(healthFacilitiesDTO.getFacilityName());
            healthFacilities.setFacilityCode(healthFacilitiesDTO.getFacilityCode());

            return healthFacilities;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting HealthFacilitiesDTO :{0}, failed with error: {1}.", healthFacilitiesDTO, e));
            throw e;
        }
    }

    public static List<HealthFacilities> toHealthFacilities(List<HealthFacilitiesDTO> healthFacilitiesDTO) {

        try {
            List<HealthFacilities> healthFacilities = new ArrayList<>();
            for (HealthFacilitiesDTO healthFacilitiesDTO1 : healthFacilitiesDTO) {
                healthFacilities.add(toHealthFacilities(healthFacilitiesDTO1));
            }

            return healthFacilities;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting HealthFacilitiesDTO :{0}, failed with error: {1}.", healthFacilitiesDTO, e));
            throw e;
        }
    }

}
