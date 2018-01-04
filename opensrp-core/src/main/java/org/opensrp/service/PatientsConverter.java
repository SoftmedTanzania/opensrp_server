package org.opensrp.service;

import org.opensrp.domain.*;
import org.opensrp.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;

public class PatientsConverter {
    private static Logger logger = LoggerFactory.getLogger(PatientsConverter.class.toString());


    public static Patients toPatients(PatientsDTO patientsDTO) {
        try {
            Patients patients = new Patients();


            patients.setPatientId(patientsDTO.getPatientId());
            patients.setFirstName(patientsDTO.getFirstName());
            patients.setSurname(patientsDTO.getSurname());
            patients.setPhoneNumber(patientsDTO.getPhoneNumber());
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



    public static Patients toPatients(CTCPatientsDTO patientsDTO) {
        try {
            Patients patients = new Patients();
            patients.setFirstName(patientsDTO.getFirstName());
            patients.setSurname(patientsDTO.getSurname());
            patients.setPhoneNumber(patientsDTO.getContact());
            Date dob = new Date();
            dob.setTime(patientsDTO.getDateOfBirth());
            patients.setDateOfBirth(dob);
            patients.setGender(patientsDTO.getGender());

            Date deathDate = new Date();
            deathDate.setTime(patientsDTO.getDateOfDeath());

            patients.setDateOfDeath(deathDate);
            patients.setCreatedAt(Calendar.getInstance().getTime());
            patients.setUpdatedAt(Calendar.getInstance().getTime());

            return patients;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }


    public static Patients toPatients(TBPatientsDTO patientsDTO) {
        try {
            Patients patients = new Patients();


            patients.setFirstName(patientsDTO.getFirstName());
            patients.setSurname(patientsDTO.getSurname());
            patients.setPhoneNumber(patientsDTO.getPhoneNumber());
            Date dob = new Date();
            dob.setTime(patientsDTO.getDateOfBirth().getTime());
            patients.setDateOfBirth(dob);
            patients.setGender(patientsDTO.getGender());
            patients.setHivStatus(patientsDTO.isHivStatus());

            Date deathDate = new Date();
            deathDate.setTime(patientsDTO.getDateOfDeath().getTime());

            patients.setDateOfDeath(deathDate);
            patients.setCreatedAt(Calendar.getInstance().getTime());
            patients.setUpdatedAt(Calendar.getInstance().getTime());

            return patients;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting TBPatientsDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }

    public static TBPatient toTBPatients(TBPatientsDTO patientsDTO) {
        try {
            TBPatient tbPatient = new TBPatient();
            tbPatient.setOutcomeDetails(patientsDTO.getOutcomeDetails());
            tbPatient.setOutcome(patientsDTO.getOutcome());
            tbPatient.setTbPatientId(patientsDTO.getPatientId());
            tbPatient.setOutcomeDate(patientsDTO.getOutcomeDate());
            tbPatient.setOtherTests(patientsDTO.getOtherTests());
            tbPatient.setMakohozi(patientsDTO.getMakohozi());
            tbPatient.setWeight(patientsDTO.getWeight());
            tbPatient.setXray(patientsDTO.getXray());
            tbPatient.setVeo(patientsDTO.getVeo());
            tbPatient.setReferralType(patientsDTO.getReferralType());
            tbPatient.setTreatment_type(patientsDTO.getTreatment_type());
            tbPatient.setTransferType(patientsDTO.getTransferType());
            tbPatient.setPatientType(patientsDTO.getPatientType());
            tbPatient.setPregnant(patientsDTO.isPregnant());

            return tbPatient;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting TBPatientsDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }


    public static List<PatientAppointments> toPatientsAppointments(CTCPatientsDTO patientsDTO) {
        try {

            List<PatientAppointments> patientAppointments = new ArrayList<>();
            CTCPatientsAppointmesDTO[] appointments = patientsDTO.getAppointments();

            int count = appointments.length;
            for(int i=0;i<count;i++){
                PatientAppointments patientAppointment = new PatientAppointments();
                Date appointDate = new Date();
                appointDate.setTime(appointments[i].getDateOfAppointment());

                patientAppointment.setAppointmentDate(appointDate);
                patientAppointment.setIsCancelled(appointments[i].getCancelled());

                Date rowVersion = new Date();
                rowVersion.setTime(appointments[i].getRowVersion());

                patientAppointment.setRowVersion(rowVersion);
                patientAppointment.setStatus("0");
                patientAppointments.add(patientAppointment);
            }

            return patientAppointments;

        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }


    public static PatientsDTO toPatientsDTO(Patients patients) {
        try {
            PatientsDTO patientsDTO = new PatientsDTO();

            patientsDTO.setPatientId(patients.getPatientId());
            patientsDTO.setFirstName(patients.getFirstName());
            patientsDTO.setSurname(patients.getSurname());
            patientsDTO.setPhoneNumber(patients.getPhoneNumber());
            patientsDTO.setDateOfBirth(patients.getDateOfBirth());
            patientsDTO.setGender(patients.getGender());
            patientsDTO.setWard(patients.getWard());
            patientsDTO.setVillage(patients.getVillage());
            patientsDTO.setHamlet(patients.getHamlet());
            patientsDTO.setDateOfDeath(patients.getDateOfDeath());
            patientsDTO.setHivStatus(patients.isHivStatus());


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

            referral.setReferralSource(referralsDTO.getReferralSource());
            referral.setServiceGivenToPatient(referralsDTO.getServiceGivenToPatient());
            referral.setFromFacilityId(referralsDTO.getFromFacilityId());
            referral.setOtherClinicalInformation(referralsDTO.getOtherClinicalInformation());
            referral.setOtherNotes(referralsDTO.getOtherNotes());

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


    public static TBEncounter toTBEncounter(TBEncounterDTO tbEncounterDTO) {
        try {
           TBEncounter encounter = new TBEncounter();
           encounter.setTbPatientId(tbEncounterDTO.getTbPatientId());
           encounter.setMedicationDate(tbEncounterDTO.getMedicationDate());
           encounter.setMedicationStatus(tbEncounterDTO.isMedicationStatus());
           encounter.setScheduledDate(tbEncounterDTO.getScheduledDate());
           encounter.setHasFinishedPreviousMonthMedication(tbEncounterDTO.isHasFinishedPreviousMonthMedication());
           encounter.setEncounterMonth(tbEncounterDTO.getEncounterMonth());
           encounter.setAppointmentId(tbEncounterDTO.getAppointmentId());
           encounter.setMakohozi(tbEncounterDTO.getMakohozi());
           encounter.setUpdatedAt(Calendar.getInstance().getTime());

            return encounter;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting TBEncounterDTO :{0}, failed with error: {1}.", tbEncounterDTO, e));
            throw e;
        }
    }


	public static List<PatientsAppointmentsDTO> toPatientAppointmentDTOsList(List<PatientAppointments> patientAppointments) {
		try {

			List<PatientsAppointmentsDTO> patientsAppointmentsDTOS = new ArrayList<>();
			for(PatientAppointments appointments:patientAppointments){
				patientsAppointmentsDTOS.add(toPatientAppointmentsDTO(appointments));
			}

			return patientsAppointmentsDTOS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MessageFormat.format("Converting  List<PatientsAppointmentsDTO> :{0}, failed with error: {1}.", patientAppointments, e));
			throw e;
		}
	}

	public static PatientsAppointmentsDTO toPatientAppointmentsDTO(PatientAppointments patientAppointments) {
		try {
			PatientsAppointmentsDTO patientsAppointmentsDTO = new PatientsAppointmentsDTO();

			patientsAppointmentsDTO.setAppointment_id(patientAppointments.getAppointment_id());
			patientsAppointmentsDTO.setAppointmentDate(patientAppointments.getAppointmentDate());
			patientsAppointmentsDTO.setCancelled(patientAppointments.getIsCancelled());
			patientsAppointmentsDTO.setCreatedAt(patientAppointments.getCreatedAt());
			patientsAppointmentsDTO.setRowVersion(patientAppointments.getRowVersion());
			patientsAppointmentsDTO.setHealthFacilityPatientId(patientAppointments.getHealthFacilityPatientId());
			patientsAppointmentsDTO.setStatus(patientAppointments.getStatus());
			patientsAppointmentsDTO.setUpdatedAt(patientAppointments.getUpdatedAt());

			return patientsAppointmentsDTO;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MessageFormat.format("Converting PatientsAppointmentsDTO :{0}, failed with error: {1}.", patientAppointments, e));
			throw e;
		}
	}


}
