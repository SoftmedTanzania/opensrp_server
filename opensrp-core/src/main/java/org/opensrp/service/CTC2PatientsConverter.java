package org.opensrp.service;

import com.google.gson.Gson;
import org.opensrp.domain.CTC_patients;
import org.opensrp.dto.CTCPatientsDTO;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormInstance;
import org.opensrp.form.domain.FormSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;

public class CTC2PatientsConverter {
    private static Logger logger = LoggerFactory.getLogger(CTC2PatientsConverter.class.toString());


    public static CTC_patients toCTCPatients(CTCPatientsDTO ctcPatientsDTO) {
        try {
            CTC_patients patients = new CTC_patients();

            patients.setPatientId(ctcPatientsDTO.getPatientId());
            patients.setPatientFirstName(ctcPatientsDTO.getPatientFirstName());
            patients.setPatientSurname(ctcPatientsDTO.getPatientSurname());
            //TODO COZE: FINALIZE CTC2PATIENTS CONVERTER

            return patients;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", ctcPatientsDTO, e));
            throw e;
        }
    }

    public static FormSubmission toFormSubmissionWithVersion(FormSubmissionDTO formSubmissionDTO) {
        return new FormSubmission(formSubmissionDTO.getAnmId(), formSubmissionDTO.getInstanceId(), formSubmissionDTO.getFormName(),
                formSubmissionDTO.getEntityId(), parseLong(formSubmissionDTO.getClientVersion()), formSubmissionDTO.getFormDataDefinitionVersion(), new Gson().fromJson(formSubmissionDTO.getFormInstance(), FormInstance.class),
                parseLong(formSubmissionDTO.getServerVersion()));
    }
}
