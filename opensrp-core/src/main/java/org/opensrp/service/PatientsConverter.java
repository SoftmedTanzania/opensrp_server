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


    public static ReferralClient toPatients(PatientsDTO patientsDTO) {
        try {
            ReferralClient referralClient = new ReferralClient();


            referralClient.setClientId(patientsDTO.getPatientId());
            referralClient.setFirstName(patientsDTO.getFirstName());
            referralClient.setMiddleName(patientsDTO.getMiddleName());
            referralClient.setSurname(patientsDTO.getSurname());

            if(patientsDTO.getPhoneNumber()!=null)
                referralClient.setPhoneNumber(patientsDTO.getPhoneNumber());
            else
                referralClient.setPhoneNumber("");

            Date dob = new Date();
            dob.setTime(patientsDTO.getDateOfBirth());

            referralClient.setDateOfBirth(dob);
            referralClient.setGender(patientsDTO.getGender());
            referralClient.setWard(patientsDTO.getWard());
            referralClient.setVillage(patientsDTO.getVillage());
            referralClient.setHamlet(patientsDTO.getHamlet());
            referralClient.setCareTakerName(patientsDTO.getCareTakerName());
            referralClient.setCareTakerPhoneNumber(patientsDTO.getPhoneNumber());
            referralClient.setCareTakerRelationship(patientsDTO.getCareTakerRelationship());
            referralClient.setCommunityBasedHivService(patientsDTO.getCommunityBasedHivService());

            try {
                Date deathDate = new Date();
                deathDate.setTime(patientsDTO.getDateOfDeath());
                referralClient.setDateOfDeath(deathDate);
            }catch (Exception e){
                e.printStackTrace();
            }

            referralClient.setHivStatus(patientsDTO.isHivStatus());
            referralClient.setCreatedAt(Calendar.getInstance().getTime());
            referralClient.setUpdatedAt(Calendar.getInstance().getTime());

            return referralClient;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }



    public static ReferralClient toPatients(CTCPatientsDTO patientsDTO) {
        try {
            ReferralClient referralClient = new ReferralClient();
            referralClient.setFirstName(patientsDTO.getFirstName());
	        referralClient.setMiddleName(patientsDTO.getMiddleName());
            referralClient.setSurname(patientsDTO.getSurname());
            referralClient.setPhoneNumber(patientsDTO.getPhoneNumber());
            Date dob = new Date();
            dob.setTime(patientsDTO.getDateOfBirth());
            referralClient.setDateOfBirth(dob);
            referralClient.setGender(patientsDTO.getGender());

            referralClient.setWard(patientsDTO.getWard());
            referralClient.setVillage(patientsDTO.getVillage());

            try {
                Date deathDate = new Date();
                deathDate.setTime(patientsDTO.getDateOfDeath());
                referralClient.setDateOfDeath(deathDate);
            }catch (Exception e){
                e.printStackTrace();
            }

            referralClient.setCareTakerName(patientsDTO.getCareTakerName());
            referralClient.setCareTakerPhoneNumber(patientsDTO.getCareTakerPhoneNumber());
            referralClient.setCareTakerRelationship(patientsDTO.getCareTakerRelationship());

            referralClient.setCreatedAt(Calendar.getInstance().getTime());
            referralClient.setUpdatedAt(Calendar.getInstance().getTime());
            referralClient.setHivStatus(patientsDTO.isHivStatus());

            return referralClient;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }


    public static ReferralClient toPatients(TBPatientMobileClientDTO patientsDTO) {
        try {
            ReferralClient referralClient = new ReferralClient();


            referralClient.setFirstName(patientsDTO.getFirstName());
            referralClient.setSurname(patientsDTO.getSurname());
            referralClient.setMiddleName(patientsDTO.getMiddleName());
            referralClient.setPhoneNumber(patientsDTO.getPhoneNumber());
            referralClient.setHamlet(patientsDTO.getHamlet());
            referralClient.setVillage(patientsDTO.getVillage());
            referralClient.setHamlet(patientsDTO.getHamlet());
            referralClient.setPhoneNumber(patientsDTO.getPhoneNumber());
            Date dob = new Date();
            dob.setTime(patientsDTO.getDateOfBirth());
            referralClient.setDateOfBirth(dob);
            referralClient.setGender(patientsDTO.getGender());
            referralClient.setHivStatus(patientsDTO.isHivStatus());
            referralClient.setHivStatus(patientsDTO.isHivStatus());

            Date deathDate = new Date();
            deathDate.setTime(patientsDTO.getDateOfDeath());

            referralClient.setDateOfDeath(deathDate);
            referralClient.setCreatedAt(Calendar.getInstance().getTime());
            referralClient.setUpdatedAt(Calendar.getInstance().getTime());

            return referralClient;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting TBPatientMobileClientDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }

    public static TBPatient toTBPatients(TBPatientMobileClientDTO patientsDTO) {
        try {
            TBPatient tbPatient = new TBPatient();
            tbPatient.setOutcomeDetails(patientsDTO.getOutcomeDetails());
            tbPatient.setOutcome(patientsDTO.getOutcome());
            tbPatient.setTbClientId(patientsDTO.getPatientId());

	        Date outcomeDate = new Date();
	        outcomeDate.setTime(patientsDTO.getOutcomeDate());

            tbPatient.setOutcomeDate(outcomeDate);
            tbPatient.setOtherTestsDetails(patientsDTO.getOtherTestsDetails());
            tbPatient.setTestType(patientsDTO.getTestType());
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
            logger.error(MessageFormat.format("Converting TBPatientMobileClientDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }


    public static List<ClientAppointments> toPatientsAppointments(CTCPatientsDTO patientsDTO) {
        try {

            List<ClientAppointments> clientAppointments = new ArrayList<>();
            List<CTCPatientsAppointmentDTO> appointments = patientsDTO.getPatientAppointments();

            if (appointments!=null) {
	            for (CTCPatientsAppointmentDTO appointment : appointments) {
		            ClientAppointments patientAppointment = new ClientAppointments();
		            Date appointDate = new Date();
		            appointDate.setTime(appointment.getDateOfAppointment());
		            patientAppointment.setAppointmentDate(appointDate);

		            Status status = new Status();
		            status.setStatusId(appointment.getStatus());
		            patientAppointment.setStatus(status);

		            AppointmentType  appointmentType = new AppointmentType();

		            //CTC appointment
                    appointmentType.setId(1);

		            patientAppointment.setAppointmentType(appointmentType);
		            clientAppointments.add(patientAppointment);
	            }
            }else{
                System.out.println("coze patients appointment is empty");
            }

            return clientAppointments;

        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", patientsDTO, e));
            throw e;
        }
    }


    public static PatientsDTO toPatientsDTO(ReferralClient referralClient) {
        try {
            PatientsDTO patientsDTO = new PatientsDTO();

            patientsDTO.setPatientId(referralClient.getClientId());
            patientsDTO.setFirstName(referralClient.getFirstName());
            patientsDTO.setSurname(referralClient.getSurname());
            patientsDTO.setMiddleName(referralClient.getMiddleName());
            patientsDTO.setPhoneNumber(referralClient.getPhoneNumber());

            try {
                patientsDTO.setDateOfBirth(referralClient.getDateOfBirth().getTime());
            }catch (Exception e){
                e.printStackTrace();
            }

            patientsDTO.setGender(referralClient.getGender());
            patientsDTO.setWard(referralClient.getWard());
            patientsDTO.setVillage(referralClient.getVillage());
            patientsDTO.setHamlet(referralClient.getHamlet());

            patientsDTO.setCareTakerName(referralClient.getCareTakerName());

            patientsDTO.setCareTakerPhoneNumber(referralClient.getCareTakerPhoneNumber());
            patientsDTO.setCareTakerRelationship(referralClient.getCareTakerRelationship());

            try {
	            patientsDTO.setDateOfDeath(referralClient.getDateOfDeath().getTime());
            }catch (Exception e){
	            patientsDTO.setDateOfDeath((long)0);
            	e.printStackTrace();
            }

            try {
                patientsDTO.setUpdatedAt(referralClient.getUpdatedAt().getTime());
            }catch (Exception e){
                e.printStackTrace();
            }
            patientsDTO.setHivStatus(referralClient.isHivStatus());

            return patientsDTO;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting Patient :{0}, failed with error: {1}.", referralClient, e));
            throw e;
        }
    }

    public static TBPatientDTO toTbPatientDTO(TBPatient patient,long patientId) {
        try {
            TBPatientDTO tbPatientDTO = new TBPatientDTO();

            tbPatientDTO.setHealthFacilityPatientId(patientId);
            tbPatientDTO.setMakohozi(patient.getMakohozi());
            tbPatientDTO.setOtherTestsDetails(patient.getOtherTestsDetails());
            tbPatientDTO.setTestType(patient.getTestType());
            tbPatientDTO.setOutcome(patient.getOutcome());
            tbPatientDTO.setOutcomeDate(patient.getOutcomeDate().getTime());
            tbPatientDTO.setOutcomeDetails(patient.getOutcomeDetails());
            tbPatientDTO.setPatientType(patient.getPatientType());
            tbPatientDTO.setPregnant(patient.isPregnant());
            tbPatientDTO.setReferralType(patient.getReferralType());
            tbPatientDTO.setTbPatientId(patient.getTbClientId());
            tbPatientDTO.setTransferType(patient.getTransferType());
            tbPatientDTO.setTreatment_type(patient.getTreatment_type());
            tbPatientDTO.setVeo(patient.getVeo());
            tbPatientDTO.setWeight(patient.getWeight());
            tbPatientDTO.setXray(patient.getXray());

            return tbPatientDTO;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting TBPatientDTO :{0}, failed with error: {1}.", patient, e));
            throw e;
        }
    }


    public static List<ClientReferrals> toPatientReferralsList(List<ReferralsDTO> referralsDTOs) {
        try {

            List<ClientReferrals> clientReferrals = new ArrayList<>();
            for(ReferralsDTO referralsDTO:referralsDTOs){
                clientReferrals.add(toPatientReferral(referralsDTO));
            }

            return clientReferrals;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting List<ReferralsDTO> :{0}, failed with error: {1}.", referralsDTOs, e));
            throw e;
        }
    }



    public static List<ReferralsDTO> toPatientReferralDTOsList(List<ClientReferrals> referrals) {
        try {

            List<ReferralsDTO> referralsDTOS = new ArrayList<>();
            for(ClientReferrals referral:referrals){
                referralsDTOS.add(toPatientDTO(referral));
            }

            return referralsDTOS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MessageFormat.format("Converting List<ClientReferrals> :{0}, failed with error: {1}.", referrals, e));
            throw e;
        }
    }


    public static ClientReferrals toPatientReferral(ReferralsDTO referralsDTO) {
        try {
            ClientReferrals referral = new ClientReferrals();

	        ReferralClient patient  = new ReferralClient();
	        patient.setClientId(referralsDTO.getPatientId());
	        referral.setId(referralsDTO.getReferralId());
            referral.setPatient(patient);
            referral.setReferralReason(referralsDTO.getReferralReason());
            referral.setServiceId(referralsDTO.getServiceId());
            referral.setReferralUUID(referralsDTO.getReferralUUID());

            ReferralType referralType = new ReferralType();
            referralType.setReferralTypeId(((long)referralsDTO.getReferralType()));

            referral.setReferralType(referralType);

            referral.setReferralStatus(referralsDTO.getReferralStatus());
            referral.setServiceProviderUIID(referralsDTO.getServiceProviderUIID());
            referral.setTestResults(referralsDTO.getTestResults());
            referral.setLabTest(referralsDTO.getLabTest());


            Date referralDate = new Date();
            referralDate.setTime(referralsDTO.getReferralDate());
            referral.setReferralDate(referralDate);

            try {
                Date appointmentDate = new Date();
                appointmentDate.setTime(referralsDTO.getAppointmentDate());
                referral.setAppointmentDate(appointmentDate);
            }catch (Exception e){
                e.printStackTrace();

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(referralsDTO.getReferralDate());
                c.add(Calendar.DATE,3);
                referral.setAppointmentDate(c.getTime());
            }

            referral.setFacilityId(referralsDTO.getFacilityId());

            try {
                referral.setEmergency(referralsDTO.isEmergency());
            }catch (Exception e){
                e.printStackTrace();
                referral.setEmergency(false);
            }

            referral.setReferralSource(referralsDTO.getReferralSource());
            //TODO implement saving of referral feedback

            referral.setFromFacilityId(referralsDTO.getFromFacilityId());
            referral.setOtherClinicalInformation(referralsDTO.getOtherClinicalInformation());
            referral.setOtherNotes(referralsDTO.getOtherNotes());

	        referral.setInstanceId(referralsDTO.getIntanceId());

            referral.setCreatedAt(Calendar.getInstance().getTime());
            referral.setUpdatedAt(Calendar.getInstance().getTime());

            return referral;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting CTCPatientDTO :{0}, failed with error: {1}.", referralsDTO, e));
            throw e;
        }
    }


    public static ReferralsDTO toPatientDTO(ClientReferrals referral) {
        try {
            ReferralsDTO referralsDTO = new ReferralsDTO();

            referralsDTO.setPatientId(referral.getPatient().getClientId());
            referralsDTO.setReferralReason(referral.getReferralReason());
            referralsDTO.setServiceId(referral.getServiceId());
            referralsDTO.setReferralId(referral.getId());
            referralsDTO.setReferralStatus(referral.getReferralStatus());
            referralsDTO.setReferralUUID(referral.getReferralUUID());
            referralsDTO.setReferralSource(referral.getReferralSource());

            try {
                referralsDTO.setReferralType(Integer.parseInt(referral.getReferralType().getReferralTypeId().toString()));
            }catch (Exception e){
                e.printStackTrace();
            }

            referralsDTO.setOtherNotes(referral.getOtherNotes());


            //TODO implement obtaining of referral feedback
//            referralsDTO.setServiceGivenToPatient(referral.getServiceGivenToPatient());
            referralsDTO.setTestResults(referral.isTestResults());
            referralsDTO.setLabTest(referral.getLabTest());
            referralsDTO.setFromFacilityId(referral.getFromFacilityId());
            referralsDTO.setOtherClinicalInformation(referral.getOtherClinicalInformation());

            referralsDTO.setIntanceId(referral.getInstanceId());
            try {
                referralsDTO.setUpdatedAt(referral.getUpdatedAt().getTime());
            }catch (Exception e){
                e.printStackTrace();
            }



            try {

                referralsDTO.setAppointmentDate(referral.getAppointmentDate().getTime());
            }catch (Exception e){
                e.printStackTrace();

                Calendar c = Calendar.getInstance();
                c.setTime(referral.getReferralDate());
                c.add(Calendar.DATE,3);
                referralsDTO.setAppointmentDate(c.getTime().getTime());
            }

            try {
                referralsDTO.setEmergency(referral.isEmergency());
            }catch (Exception e){
                e.printStackTrace();
                referralsDTO.setEmergency(false);
            }


            referralsDTO.setServiceProviderUIID(referral.getServiceProviderUIID());
            referralsDTO.setReferralDate(referral.getReferralDate().getTime());
            referralsDTO.setFacilityId(referral.getFacilityId());

            return referralsDTO;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting ReferralsDTO :{0}, failed with error: {1}.", referral, e));
            e.printStackTrace();
            throw e;
        }
    }


    public static TBEncounter toTBEncounter(TBEncounterDTO tbEncounterDTO) {
        try {
           TBEncounter encounter = new TBEncounter();
           encounter.setTbPatientId(tbEncounterDTO.getTbPatientId());
           Date medicationDate = new Date();
           medicationDate.setTime(tbEncounterDTO.getMedicationDate());

           encounter.setLocalID(tbEncounterDTO.getLocalID());
           encounter.setWeight(tbEncounterDTO.getWeight());
           encounter.setMedicationDate(medicationDate);
           encounter.setMedicationStatus(tbEncounterDTO.isMedicationStatus());

           encounter.setEncounterYear(tbEncounterDTO.getEncounterYear());

           Date scheduledDate = new Date();
           scheduledDate.setTime(tbEncounterDTO.getScheduledDate());
           encounter.setScheduledDate(scheduledDate);
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


	public static List<PatientsAppointmentsDTO> toPatientAppointmentDTOsList(List<ClientAppointments> clientAppointments, long patientId) {
		try {

			List<PatientsAppointmentsDTO> patientsAppointmentsDTOS = new ArrayList<>();
			for(ClientAppointments appointments: clientAppointments){
				patientsAppointmentsDTOS.add(toPatientAppointmentsDTO(appointments, patientId));
			}

			return patientsAppointmentsDTOS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MessageFormat.format("Converting  List<PatientsAppointmentsDTO> :{0}, failed with error: {1}.", clientAppointments, e));
			throw e;
		}
	}

	public static PatientsAppointmentsDTO toPatientAppointmentsDTO(ClientAppointments clientAppointments, long patientId) {
		try {
			PatientsAppointmentsDTO patientsAppointmentsDTO = new PatientsAppointmentsDTO();

			patientsAppointmentsDTO.setAppointment_id(clientAppointments.getAppointment_id());
			patientsAppointmentsDTO.setAppointmentDate(clientAppointments.getAppointmentDate().getTime());
			patientsAppointmentsDTO.setIsCancelled(clientAppointments.getIsCancelled());
			patientsAppointmentsDTO.setHealthFacilityPatientId(patientId);
			patientsAppointmentsDTO.setStatus(clientAppointments.getStatus().getStatusId());
			patientsAppointmentsDTO.setAppointmentType(clientAppointments.getAppointmentType().getId());

			return patientsAppointmentsDTO;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MessageFormat.format("Converting PatientsAppointmentsDTO :{0}, failed with error: {1}.", clientAppointments, e));
			throw e;
		}
	}

    public static List<TBEncounterDTO> toTbPatientEncounterDTOsList(List<TBEncounter> tbEncounters) {
        try {

            List<TBEncounterDTO> tbEncounterDTOS = new ArrayList<>();
            for(TBEncounter tbEncounter :tbEncounters){
                tbEncounterDTOS.add(toTbEncounterDTO(tbEncounter));
            }

            return tbEncounterDTOS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MessageFormat.format("Converting   List<TBEncounterDTO> :{0}, failed with error: {1}.", tbEncounters, e));
            throw e;
        }
    }

	public static TBEncounterDTO toTbEncounterDTO(TBEncounter tbEncounter) {
		try {
            TBEncounterDTO tbEncounterDTO = new TBEncounterDTO();

            tbEncounterDTO.setLocalID(tbEncounter.getLocalID());
            tbEncounterDTO.setAppointmentId(tbEncounter.getAppointmentId());
            tbEncounterDTO.setEncounterMonth(tbEncounter.getEncounterMonth());
            tbEncounterDTO.setEncounterYear(tbEncounter.getEncounterYear());
            tbEncounterDTO.setHasFinishedPreviousMonthMedication(tbEncounter.isHasFinishedPreviousMonthMedication());
            tbEncounterDTO.setId(tbEncounter.getId());
            tbEncounterDTO.setMakohozi(tbEncounter.getMakohozi());
            tbEncounterDTO.setMedicationDate(tbEncounter.getMedicationDate().getTime());
            tbEncounterDTO.setMedicationStatus(tbEncounter.isMedicationStatus());
            tbEncounterDTO.setTbPatientId(tbEncounter.getTbPatientId());
            tbEncounterDTO.setWeight(tbEncounter.getWeight());
            tbEncounterDTO.setScheduledDate(tbEncounter.getScheduledDate().getTime());

			return tbEncounterDTO;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MessageFormat.format("Converting TBEncounterDTO :{0}, failed with error: {1}.", tbEncounter, e));
			throw e;
		}
	}


}
