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
public class CTC2Service {

	private static Logger logger = LoggerFactory.getLogger(CTC2Service.class.toString());

	private HttpClient client;

	@Autowired
	private PatientsRepository patientsRepository;


	public CTC2Service() {
		this.client = HttpClientBuilder.create().build();
	}
	
//	public List<String> downloadOpenmrsIds(int size) {
//		List<String> ids = new ArrayList<String>();
//		String openmrsQueryUrl = this.openmrsUrl + OPENMRS_IDGEN_URL;
//		// Add query parameters
//		openmrsQueryUrl += "?source=" + this.openmrsSourceId + "&numberToGenerate=" + size;
//		openmrsQueryUrl += "&username=" + this.openmrsUserName + "&password=" + this.openmrsPassword;
//
//		HttpGet get = new HttpGet(openmrsQueryUrl);
//		try {
//			HttpResponse response = client.execute(get);
//			String jsonResponse = EntityUtils.toString(response.getEntity());
//
//			JSONObject responseJson = new JSONObject(jsonResponse);
//			JSONArray jsonArray = responseJson.getJSONArray("identifiers");
//
//			if (jsonArray != null && jsonArray.length() > 0) {
//				for (int i = 0; i < jsonArray.length(); i++) {
//					ids.add(jsonArray.getString(i));
//				}
//			}
//		}
//		catch (IOException | JSONException e) {
//			logger.error("", e);
//			return null;
//		}
//		// import IDs and client data to database together with assignments
//		return ids;
//	}

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

			return rowCount >= 1 ? true : false;
		}
		catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}
	
}
