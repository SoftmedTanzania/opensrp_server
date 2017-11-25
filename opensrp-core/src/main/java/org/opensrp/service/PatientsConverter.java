package org.opensrp.service;

import org.opensrp.domain.PatientReferral;
import org.opensrp.domain.Patients;
import org.opensrp.dto.PatientsDTO;
import org.opensrp.dto.ReferralsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;

public class PatientsConverter {
    private static Logger logger = LoggerFactory.getLogger(PatientsConverter.class.toString());


    public static Patients toPatients(PatientsDTO patientsDTO) {
        try {
            Patients patients = new Patients();


            patients.setPatientId(patientsDTO.getPatientId());
            patients.setPatientFirstName(patientsDTO.getPatientFirstName());
            patients.setPatientSurname(patientsDTO.getPatientSurname());
            patients.setPhone_number(patientsDTO.getPhoneNumber());
            patients.setDateOfBirth(patientsDTO.getDateOfBirth());
            patients.setGender(patientsDTO.getGender());
            patients.setWard(patientsDTO.getWard());
            patients.setVillage(patientsDTO.getVillage());
            patients.setHamlet(patientsDTO.getHamlet());
            patients.setDateOfDeath(patientsDTO.getDateOfDeath());
            patients.setCreatedAt(Calendar.getInstance().getTime());
            patients.setUpdatedAt(Calendar.getInstance().getTime());

            return patients;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }


    public static PatientsDTO toPatientsDTO(Patients patients) {
        try {
            PatientsDTO patientsDTO = new PatientsDTO();

            patientsDTO.setPatientId(patients.getPatientId());
            patientsDTO.setPatientFirstName(patients.getPatientFirstName());
            patientsDTO.setPatientSurname(patients.getPatientSurname());
            patientsDTO.setPhoneNumber(patients.getPhone_number());
            patientsDTO.setDateOfBirth(patients.getDateOfBirth());
            patientsDTO.setGender(patients.getGender());
            patientsDTO.setWard(patients.getWard());
            patientsDTO.setVillage(patients.getVillage());
            patientsDTO.setHamlet(patients.getHamlet());
            patientsDTO.setDateOfDeath(patients.getDateOfDeath());


            return patientsDTO;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting Patient :{0}, failed with error: {1}.", patients, e));
            throw e;
        }
    }


    public static List<PatientReferral> toPatientReferralsList(List<ReferralsDTO> referralsDTOs) {
        try {

            List<PatientReferral> patientReferrals = new ArrayList<>();
            for(ReferralsDTO referralsDTO:referralsDTOs){
                patientReferrals.add(toPatientReferral(referralsDTO));
            }

            return patientReferrals;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting List<ReferralsDTO> :{0}, failed with error: {1}.", referralsDTOs, e));
            throw e;
        }
    }



    public static List<ReferralsDTO> toPatientReferralDTOsList(List<PatientReferral> referrals) {
        try {

            List<ReferralsDTO> referralsDTOS = new ArrayList<>();
            for(PatientReferral referral:referrals){
                referralsDTOS.add(toPatientDTO(referral));
            }

            return referralsDTOS;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting List<PatientReferral> :{0}, failed with error: {1}.", referrals, e));
            throw e;
        }
    }


    public static PatientReferral toPatientReferral(ReferralsDTO referralsDTO) {
        try {
            PatientReferral referral = new PatientReferral();


            referral.setId(referralsDTO.getId());
            referral.setPatient_id(referralsDTO.getPatientId());
            referral.setReferral_id(referralsDTO.getReferralId());
            referral.setCommunityBasedHivService(referralsDTO.getCommunityBasedHivService());
            referral.setReferralReason(referralsDTO.getReferralReason());
            referral.setServiceId(referralsDTO.getServiceId());
            referral.setCtcNumber(referralsDTO.getServiceProviderGroup());
            referral.setHas2WeeksCough(referralsDTO.getHas2WeeksCough());
            referral.setHasBloodCough(referralsDTO.getHasBloodCough());
            referral.setHasFever(referralsDTO.getHasFever());

            referral.setHasSevereSweating(referralsDTO.getHasSevereSweating());
            referral.setHadWeightLoss(referralsDTO.getHadWeightLoss());
            referral.setServiceProviderUIID(referralsDTO.getServiceProviderUIID());
            referral.setServiceProviderGroup(referralsDTO.getServiceProviderGroup());
            referral.setVillageLeader(referralsDTO.getVillageLeader());
            referral.setReferralDate(referralsDTO.getReferralDate());
            referral.setFacilityId(referralsDTO.getFacilityId());

            referral.setCreatedAt(Calendar.getInstance().getTime());
            referral.setUpdatedAt(Calendar.getInstance().getTime());

            return referral;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", referralsDTO, e));
            throw e;
        }
    }


    public static ReferralsDTO toPatientDTO(PatientReferral referral) {
        try {
            ReferralsDTO referralsDTO = new ReferralsDTO();


            referralsDTO.setId(referral.getId());
            referralsDTO.setPatientId(referral.getPatient_id());
            referralsDTO.setReferralId(referral.getReferral_id());
            referralsDTO.setCommunityBasedHivService(referral.getCommunityBasedHivService());
            referralsDTO.setReferralReason(referral.getReferralReason());
            referralsDTO.setServiceId(referral.getServiceId());
            referralsDTO.setCtcNumber(referral.getServiceProviderGroup());
            referralsDTO.setHas2WeeksCough(referral.getHas2WeeksCough());
            referralsDTO.setHasBloodCough(referral.getHasBloodCough());
            referralsDTO.setHasFever(referral.getHasFever());

            referralsDTO.setHasSevereSweating(referral.getHasSevereSweating());
            referralsDTO.setHadWeightLoss(referral.getHadWeightLoss());
            referralsDTO.setServiceProviderUIID(referral.getServiceProviderUIID());
            referralsDTO.setServiceProviderGroup(referral.getServiceProviderGroup());
            referralsDTO.setVillageLeader(referral.getVillageLeader());
            referralsDTO.setReferralDate(referral.getReferralDate());
            referralsDTO.setFacilityId(referral.getFacilityId());

            referralsDTO.setCreatedAt(Calendar.getInstance().getTime());
            referralsDTO.setUpdatedAt(Calendar.getInstance().getTime());

            return referralsDTO;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting ReferralsDTO :{0}, failed with error: {1}.", referral, e));
            throw e;
        }
    }



}
