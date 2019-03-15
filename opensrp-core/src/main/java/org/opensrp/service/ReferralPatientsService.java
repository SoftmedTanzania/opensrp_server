package org.opensrp.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.opensrp.domain.*;
import org.opensrp.dto.PatientReferralsDTO;
import org.opensrp.dto.PatientsDTO;
import org.opensrp.dto.ReferralsDTO;
import org.opensrp.repository.*;
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
    private ClientsRepository clientsRepository;

    @Autowired
    private ClientReferralRepository clientReferralRepository;

    @Autowired
    private ClientsAppointmentsRepository clientsAppointmentsRepository;

	@Autowired
	private ClientReferralIndicatorRepository clientReferralIndicatorRepository;

    @Autowired
    private HealthFacilitiesClientsRepository healthFacilitiesClientsRepository;

    @Autowired
    private HealthFacilityRepository healthFacilityRepository;


    public ReferralPatientsService() {
        this.client = HttpClientBuilder.create().build();
    }

    public void storeCTCPatients(ReferralClient patient) throws SQLException {
        // create jdbc template to persist the ids
        try {
            if (!this.checkIfClientExists(patient)) {
                clientsRepository.save(patient);
                logger.info("Successfully saved client " + patient.getFirstName());
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void storeHealthFacilityRefferal(ClientReferrals clientReferrals) throws SQLException {
        if (!this.checkIfClientExists(clientReferrals)) {
            try {
                clientReferralRepository.save(clientReferrals);
            } catch (Exception e) {
                logger.error("", e);
            }

        }
    }

    public List<PatientReferralsDTO> getAllPatientReferrals(){
        return getPatientsReferralsDTO("SELECT * FROM "+ HealthFacilitiesReferralClients.tbName,null);
    }

    public List<PatientReferralsDTO> getHealthFacilityReferrals(String facilityUUID){

        String[] healthFacilityPatientArg = new String[1];
        healthFacilityPatientArg[0] =  facilityUUID;


        return getPatientsReferralsDTO("SELECT * FROM " + HealthFacilitiesReferralClients.tbName +
                " INNER JOIN "+HealthFacilities.tbName+" ON "+ HealthFacilitiesReferralClients.tbName+"."+ HealthFacilitiesReferralClients.COL_FACILITY_ID +" = "+HealthFacilities.tbName+"._id " +
                " WHERE " + HealthFacilities.COL_OPENMRS_UUID + "=?",healthFacilityPatientArg);
    }

    public  List<PatientReferralsDTO> getPatientsReferralsDTO(String sql, Object[] arg){

        List<HealthFacilitiesReferralClients> healthFacilitiesPatients = null;
        try {
            healthFacilitiesPatients = healthFacilitiesClientsRepository.getHealthFacilityPatients(sql,arg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<PatientReferralsDTO> patientReferralsDTOS = new ArrayList<>();
        for(HealthFacilitiesReferralClients facilitiesPatients:healthFacilitiesPatients){
            String getPatientsSQL = "SELECT * from " + ReferralClient.tbName+" WHERE "+ ReferralClient.COL_CLIENT_ID + " = "+facilitiesPatients.getClient().getClientId();
            try {
                ReferralClient patient = clientsRepository.getPatients(getPatientsSQL,null).get(0);

                PatientReferralsDTO patientReferralsDTO = new PatientReferralsDTO();
                PatientsDTO patientsDTO = PatientsConverter.toPatientsDTO(patient);
                patientsDTO.setCtcNumber(facilitiesPatients.getCtcNumber());
                patientReferralsDTO.setPatientsDTO(patientsDTO);


                String getReferralPatientsSQL = "SELECT * from " + ClientReferrals.tbName+" WHERE "+ ClientReferrals.COL_CLIENT_ID +" =?";
                Object[] args = new Object[]{patient.getClientId()};

                List<ReferralsDTO> referralsDTOS = PatientsConverter.toPatientReferralDTOsList(clientReferralRepository.getReferrals(getReferralPatientsSQL,args));
                for(ReferralsDTO referralsDTO:referralsDTOS) {

                    Object[] args2 = new Object[]{referralsDTO.getReferralId()};

                    List<ClientReferralIndicators> clientReferralIndicators = clientReferralIndicatorRepository.getPatientReferralIndicators("SELECT * FROM " + ClientReferralIndicators.tbName + " WHERE " + ClientReferralIndicators.COL_REFERRAL_ID + " =?", args2);

                    List<Long> patientReferralIndicatorsIds = new ArrayList<>();
                    for(ClientReferralIndicators referralIndicator: clientReferralIndicators){
                        patientReferralIndicatorsIds.add(referralIndicator.getServiceIndicator().getServiceIndicatorId());
                    }

                    referralsDTO.setServiceIndicatorIds(patientReferralIndicatorsIds);
                }

                patientReferralsDTO.setPatientReferralsList(referralsDTOS);


                Object[] args2 = new Object[]{facilitiesPatients.getHealthFacilityClientId()};
                String getPatientsAppointmentsSQL = "SELECT * from " + ClientAppointments.tbName+" WHERE "+ ClientAppointments.COL_HEALTH_FACILITY_CLIENT_ID +" =?";
                List<ClientAppointments> clientAppointments = clientsAppointmentsRepository.getAppointments(getPatientsAppointmentsSQL,args2);

                patientReferralsDTO.setPatientsAppointmentsDTOS(PatientsConverter.toPatientAppointmentDTOsList(clientAppointments,patient.getClientId()));

                patientReferralsDTOS.add(patientReferralsDTO);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return patientReferralsDTOS;
    }

    public List<ReferralClient> getPatients(String sql, Object[] args){
        try {
            return clientsRepository.getPatients(sql,args);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Boolean checkIfClientExists(ReferralClient patient) throws SQLException {
        try {
            String checkIfExistQuery = "SELECT count(*) from " + ReferralClient.tbName + " WHERE " + ReferralClient.COL_CLIENT_ID + " = ?";
            String[] args = new String[1];
            args[0] = patient.getClientId()+"";

            int rowCount = clientsRepository.checkIfExists(checkIfExistQuery, args);

            logger.info(
                    "[checkIfClientExists] - Card Number:" + args[0] + " - [Exists] " + (rowCount == 0 ? "false" : "true"));

            return rowCount >= 1;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public Boolean checkIfClientExists(ClientReferrals clientReferrals) throws SQLException {
        try {
            String checkIfExistQuery = "SELECT count(*) from " + ClientReferrals.tbName + " WHERE  "+ ClientReferrals.COL_REFERRAL_ID+" = ?";
            Object[] args = new Object[1];
            args[0] = clientReferrals.getId();
            int rowCount = clientsRepository.checkIfExists(checkIfExistQuery, args);

            logger.info(
                    "[checkIfClientExists] - Referral ID:" + args[0] + " - [Exists] " + (rowCount == 0 ? "false" : "true"));

            return rowCount >= 1;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public String delete_last_char_java(String string) {

        String phrase = "level up lunch";

        String rephrase = null;
        if (phrase != null && phrase.length() > 1) {
            rephrase = phrase.substring(0, phrase.length() - 1);
        }

        return rephrase;
    }

    public long savePatient(ReferralClient patient, String healthFacilityCode, String ctcNumber) {
        String query = "SELECT * FROM " + ReferralClient.tbName + " WHERE " +
                ReferralClient.COL_PATIENT_FIRST_NAME + " = ?     AND " +
                ReferralClient.COL_DATE_OF_BIRTH + " = ?    AND " +
                ReferralClient.COL_PATIENT_SURNAME + " = ?        AND " +
                ReferralClient.COL_PHONE_NUMBER + " = ?";
        Object[] params = new Object[]{
                patient.getFirstName(),
                patient.getDateOfBirth(),
                patient.getSurname(),
                patient.getPhoneNumber()};
        List<ReferralClient> referralClientResults = null;
        try {
            referralClientResults = clientsRepository.getPatients(query, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long id;
        if (referralClientResults.size() > 0) {
            id = referralClientResults.get(0).getClientId();
        } else {
            try {
                id = clientsRepository.save(patient);
            } catch (Exception e) {
                e.printStackTrace();
                id = -1;
            }
        }

        //Obtaining facilityId from facilities
        String healthFacilitySql = "SELECT * FROM " + HealthFacilities.tbName + " WHERE " +
                HealthFacilities.COL_FACILITY_CTC_CODE + " = ? OR " + HealthFacilities.COL_OPENMRS_UUID + " = ?";
        Object[] healthFacilityParams = new Object[]{
                healthFacilityCode,healthFacilityCode};

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

        HealthFacilitiesReferralClients healthFacilitiesReferralClients = new HealthFacilitiesReferralClients();

        ReferralClient referralClient = new ReferralClient();
        referralClient.setClientId(id);

        healthFacilitiesReferralClients.setClient(referralClient);
        healthFacilitiesReferralClients.setCtcNumber(ctcNumber);
        healthFacilitiesReferralClients.setFacilityId(healthFacilityId);


        String healthFacilityPatientsquery = "SELECT * FROM " + HealthFacilitiesReferralClients.tbName + " WHERE " +
                HealthFacilitiesReferralClients.COL_CLIENT_ID + " = ?    AND " +
                HealthFacilitiesReferralClients.COL_FACILITY_ID + " = ?";

        Object[] healthFacilityPatientsparams = new Object[]{
                healthFacilitiesReferralClients.getClient().getClientId(),
                healthFacilitiesReferralClients.getFacilityId()};

        List<HealthFacilitiesReferralClients> healthFacilitiesReferralClientsResults = null;
        try {
            healthFacilitiesReferralClientsResults = healthFacilitiesClientsRepository.getHealthFacilityPatients(healthFacilityPatientsquery, healthFacilityPatientsparams);
        } catch (Exception e) {
            e.printStackTrace();
        }


        long healthfacilityPatientId = -1;
        if (healthFacilitiesReferralClientsResults.size() > 0) {
            healthfacilityPatientId = healthFacilitiesReferralClientsResults.get(0).getHealthFacilityClientId();
        } else {
            try {
                healthfacilityPatientId = healthFacilitiesClientsRepository.save(healthFacilitiesReferralClients);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return healthfacilityPatientId;
    }
}
