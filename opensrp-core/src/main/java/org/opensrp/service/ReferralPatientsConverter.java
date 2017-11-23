package org.opensrp.service;

import com.google.gson.Gson;
import org.opensrp.domain.ReferralPatients;
import org.opensrp.dto.ReferralPatientsDTO;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormInstance;
import org.opensrp.form.domain.FormSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Calendar;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;

public class ReferralPatientsConverter {
    private static Logger logger = LoggerFactory.getLogger(ReferralPatientsConverter.class.toString());


    public static ReferralPatients toCTCPatients(ReferralPatientsDTO referralPatientsDTO) {
        try {
            ReferralPatients patients = new ReferralPatients();

            patients.setPatientId(referralPatientsDTO.getPatientId());
            patients.setPatientFirstName(referralPatientsDTO.getPatientFirstName());
            patients.setPatientSurname(referralPatientsDTO.getPatientSurname());


            patients.setCreatedAt(Calendar.getInstance().getTime());
            //TODO COZE: FINALIZE CTC2PATIENTS CONVERTER

            return patients;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", referralPatientsDTO, e));
            throw e;
        }
    }

    public static FormSubmission toFormSubmissionWithVersion(FormSubmissionDTO formSubmissionDTO) {
        return new FormSubmission(formSubmissionDTO.getAnmId(), formSubmissionDTO.getInstanceId(), formSubmissionDTO.getFormName(),
                formSubmissionDTO.getEntityId(), parseLong(formSubmissionDTO.getClientVersion()), formSubmissionDTO.getFormDataDefinitionVersion(), new Gson().fromJson(formSubmissionDTO.getFormInstance(), FormInstance.class),
                parseLong(formSubmissionDTO.getServerVersion()));
    }
}
