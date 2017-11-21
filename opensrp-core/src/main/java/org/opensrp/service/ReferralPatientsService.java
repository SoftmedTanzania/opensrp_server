package org.opensrp.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.opensrp.domain.ReferralPatients;
import org.opensrp.repository.PatientsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ReferralPatientsService {

	private static Logger logger = LoggerFactory.getLogger(ReferralPatientsService.class.toString());

	private HttpClient client;

	@Autowired
	private PatientsRepository patientsRepository;


	public ReferralPatientsService() {
		this.client = HttpClientBuilder.create().build();
	}

	public void storeCTCPatients(ReferralPatients patient) throws SQLException {
		// create jdbc template to persist the ids
		try {
			if (!this.checkIfClientExists(patient)) {
				patientsRepository.save(patient);
				logger.info("Successfully saved client " + patient.getPatientFirstName());
			}
		}
		catch (Exception e) {
			logger.error("", e);
		}
	}

	public Boolean checkIfClientExists(ReferralPatients patient) throws SQLException {
		try {
			String checkIfExistQuery = "SELECT count(*) from " + ReferralPatients.tbName + " WHERE " + ReferralPatients.COL_PATIENT_ID +" = ?";
			String[] args = new String[1];
			args[0] = (String) patient.getPatientId();

			int rowCount = patientsRepository.checkIfExists(checkIfExistQuery, args);

			logger.info(
					"[checkIfClientExists] - Card Number:" + args[0] + " - [Exists] " + (rowCount == 0 ? "false" : "true"));

			return rowCount >= 1;
		}
		catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}
	
}
