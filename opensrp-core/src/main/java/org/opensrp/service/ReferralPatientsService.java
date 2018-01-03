package org.opensrp.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.opensrp.domain.PatientReferral;
import org.opensrp.domain.Patients;
import org.opensrp.dto.PatientReferralsDTO;
import org.opensrp.dto.ReferralsDTO;
import org.opensrp.repository.PatientReferralRepository;
import org.opensrp.repository.PatientsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReferralPatientsService {

    private static Logger logger = LoggerFactory.getLogger(ReferralPatientsService.class.toString());

    private HttpClient client;

    @Autowired
    private PatientsRepository patientsRepository;

    @Autowired
    private PatientReferralRepository patientReferralRepository;


    public ReferralPatientsService() {
        this.client = HttpClientBuilder.create().build();
    }

    public void storeCTCPatients(Patients patient) throws SQLException {
        // create jdbc template to persist the ids
        try {
            if (!this.checkIfClientExists(patient)) {
                patientsRepository.save(patient);
                logger.info("Successfully saved client " + patient.getFirstName());
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void storeHealthFacilityRefferal(PatientReferral patientReferral) throws SQLException {
        if (!this.checkIfClientExists(patientReferral)) {
            try {
                patientReferralRepository.save(patientReferral);
                logger.info("Successfully saved referral " + patientReferral.getReferral_id());
            } catch (Exception e) {
                logger.error("", e);
            }

        }
    }

    public List<PatientReferralsDTO> getAllPatientReferrals(){
        List<PatientReferralsDTO> patientReferralsDTOS = new ArrayList<>();
        String getPatientsSQL = "SELECT * from " + Patients.tbName;
        try {
            List<Patients> patientsRepositoryList = patientsRepository.getPatients(getPatientsSQL,null);
            for(Patients patient : patientsRepositoryList){
                PatientReferralsDTO patientReferralsDTO = new PatientReferralsDTO();
                patientReferralsDTO.setPatientsDTO(PatientsConverter.toPatientsDTO(patient));

                String getReferralPatientsSQL = "SELECT * from " + PatientReferral.tbName+" WHERE "+PatientReferral.COL_PATIENT_ID +" =?";
                String[] args = new String[1];
                args[0] =  patient.getPatientId()+"";

                List<ReferralsDTO> referralsDTOS = PatientsConverter.toPatientReferralDTOsList(patientReferralRepository.getReferrals(getReferralPatientsSQL,args));
                patientReferralsDTO.setPatientReferralsList(referralsDTOS);
                patientReferralsDTOS.add(patientReferralsDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientReferralsDTOS;
    }


    public Boolean checkIfClientExists(Patients patient) throws SQLException {
        try {
            String checkIfExistQuery = "SELECT count(*) from " + Patients.tbName + " WHERE " + Patients.COL_PATIENT_ID + " = ?";
            String[] args = new String[1];
            args[0] = patient.getPatientId()+"";

            int rowCount = patientsRepository.checkIfExists(checkIfExistQuery, args);

            logger.info(
                    "[checkIfClientExists] - Card Number:" + args[0] + " - [Exists] " + (rowCount == 0 ? "false" : "true"));

            return rowCount >= 1;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public Boolean checkIfClientExists(PatientReferral patientReferral) throws SQLException {
        try {
            String checkIfExistQuery = "SELECT count(*) from " + PatientReferral.tbName + " WHERE " + PatientReferral.COL_REFERRAL_ID + " = ?";
            String[] args = new String[1];
            args[0] = (String) patientReferral.getReferral_id();

            int rowCount = patientsRepository.checkIfExists(checkIfExistQuery, args);

            logger.info(
                    "[checkIfClientExists] - Referral ID:" + args[0] + " - [Exists] " + (rowCount == 0 ? "false" : "true"));

            return rowCount >= 1;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

}
