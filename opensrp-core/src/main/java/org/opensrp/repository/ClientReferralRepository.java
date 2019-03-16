package org.opensrp.repository;

import org.opensrp.domain.ClientReferrals;
import org.opensrp.domain.ReferralClient;
import org.opensrp.domain.ReferralFeedback;
import org.opensrp.domain.ReferralType;
import org.opensrp.dto.CHWReferralsSummaryDTO;
import org.opensrp.dto.FacilityDepartmentReferralSummaryDTO;
import org.opensrp.dto.FacilityProvidersReferralSummaryDTO;
import org.opensrp.dto.InterFacilityReferralSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ClientReferralRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;


	public long save(ClientReferrals clientReferrals) throws Exception {


		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ClientReferrals.tbName).usingGeneratedKeyColumns(ClientReferrals.COL_REFERRAL_ID);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ClientReferrals.COL_CLIENT_ID, clientReferrals.getPatient().getClientId());
		parameters.put(ClientReferrals.COL_REFERRAL_REASON, clientReferrals.getReferralReason());
		parameters.put(ClientReferrals.COL_SERVICE_ID, clientReferrals.getServiceId());
		parameters.put(ClientReferrals.COL_REFERRAL_UUID, clientReferrals.getReferralUUID());
		parameters.put(ClientReferrals.COL_FACILITY_ID, clientReferrals.getFacilityId());
		parameters.put(ClientReferrals.COL_SERVICE_PROVIDER_UIID, clientReferrals.getServiceProviderUIID());
		parameters.put(ClientReferrals.COL_FROM_FACILITY_ID, clientReferrals.getFromFacilityId());
		parameters.put(ClientReferrals.COL_OTHER_CLINICAL_INFORMATION, clientReferrals.getOtherClinicalInformation());
		parameters.put(ClientReferrals.COL_OTHER_NOTES, clientReferrals.getOtherNotes());
		parameters.put(ClientReferrals.COL_REFERRAL_SOURCE, clientReferrals.getReferralSource());
		parameters.put(ClientReferrals.COL_REFERRAL_DATE, clientReferrals.getReferralDate());
		parameters.put(ClientReferrals.COL_APPOINTMENT_DATE, clientReferrals.getAppointmentDate());
		parameters.put(ClientReferrals.COL_REFERRAL_STATUS, clientReferrals.getReferralStatus());
		parameters.put(ClientReferrals.COL_INSTANCE_ID, clientReferrals.getInstanceId());
		parameters.put(ClientReferrals.COL_REFERRAL_TYPE, clientReferrals.getReferralType());
		parameters.put(ClientReferrals.COL_LAB_TEST, clientReferrals.getLabTest());
		parameters.put(ClientReferrals.COL_TEST_RESULTS, clientReferrals.isTestResults());
		parameters.put(ClientReferrals.COL_IS_EMERGENCY, clientReferrals.isEmergency());
		try {
			parameters.put(ClientReferrals.COL_REFERRAL_FEEDBACK_ID, clientReferrals.getReferralFeedback().getId());
		}catch (Exception e){
			e.printStackTrace();
		}
		parameters.put(ClientReferrals.COL_CREATED_AT, clientReferrals.getCreatedAt());
		parameters.put(ClientReferrals.COL_UPDATED_AT, clientReferrals.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();

	}

	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}

	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);

	}

	public void clearTable() throws Exception {
		String query = "DELETE FROM " + ClientReferrals.tbName;
		executeQuery(query);
	}


	public List<ClientReferrals> getReferrals(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new HealthFacilityRefferalRowMapper());
	}


	public List<CHWReferralsSummaryDTO> getCHWReferralsSummary(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new CHWReferralsSummaryRowMapper());
	}


	public class HealthFacilityRefferalRowMapper implements RowMapper<ClientReferrals> {
		public ClientReferrals mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClientReferrals clientReferrals = new ClientReferrals();
			clientReferrals.setId(rs.getLong(rs.findColumn(ClientReferrals.COL_REFERRAL_ID)));

			ReferralClient patient = new ReferralClient();
			patient.setClientId(rs.getLong(rs.findColumn(ClientReferrals.COL_CLIENT_ID)));
			clientReferrals.setPatient(patient);

			clientReferrals.setReferralReason(rs.getString(rs.findColumn(ClientReferrals.COL_REFERRAL_REASON)));
			clientReferrals.setFromFacilityId(rs.getString(rs.findColumn(ClientReferrals.COL_FROM_FACILITY_ID)));
			clientReferrals.setServiceId(rs.getInt(rs.findColumn(ClientReferrals.COL_SERVICE_ID)));
			clientReferrals.setReferralUUID(rs.getString(rs.findColumn(ClientReferrals.COL_REFERRAL_UUID)));
			clientReferrals.setServiceProviderUIID(rs.getString(rs.findColumn(ClientReferrals.COL_SERVICE_PROVIDER_UIID)));
			clientReferrals.setReferralDate(rs.getDate(rs.findColumn(ClientReferrals.COL_REFERRAL_DATE)));
			clientReferrals.setAppointmentDate(rs.getDate(rs.findColumn(ClientReferrals.COL_APPOINTMENT_DATE)));
			clientReferrals.setFacilityId(rs.getString(rs.findColumn(ClientReferrals.COL_FACILITY_ID)));
			clientReferrals.setReferralStatus(rs.getInt(rs.findColumn(ClientReferrals.COL_REFERRAL_STATUS)));
			clientReferrals.setInstanceId(rs.getString(rs.findColumn(ClientReferrals.COL_INSTANCE_ID)));

			ReferralType referralType = new ReferralType();
			referralType.setReferralTypeId(rs.getLong(rs.findColumn(ClientReferrals.COL_REFERRAL_TYPE)));
			clientReferrals.setReferralType(referralType);

			try {
				ReferralFeedback referralFeedback = new ReferralFeedback();
				referralFeedback.setId(rs.getLong(rs.findColumn(ClientReferrals.COL_REFERRAL_FEEDBACK_ID)));
				clientReferrals.setReferralFeedback(referralFeedback);
			}catch (Exception e){
				e.printStackTrace();
			}


			clientReferrals.setReferralSource(rs.getInt(rs.findColumn(ClientReferrals.COL_REFERRAL_SOURCE)));

			clientReferrals.setTestResults(rs.getInt(rs.findColumn(ClientReferrals.COL_TEST_RESULTS)));
			clientReferrals.setEmergency(rs.getBoolean(rs.findColumn(ClientReferrals.COL_IS_EMERGENCY)));
			clientReferrals.setLabTest(rs.getInt(rs.findColumn(ClientReferrals.COL_LAB_TEST)));
			clientReferrals.setOtherClinicalInformation(rs.getString(rs.findColumn(ClientReferrals.COL_OTHER_CLINICAL_INFORMATION)));
			clientReferrals.setOtherNotes(rs.getString(rs.findColumn(ClientReferrals.COL_OTHER_NOTES)));
			clientReferrals.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ClientReferrals.COL_CREATED_AT)).getTime()));
			clientReferrals.setUpdatedAt(rs.getDate(rs.findColumn(ClientReferrals.COL_UPDATED_AT)));
			return clientReferrals;
		}

	}

	public class CHWReferralsSummaryRowMapper implements RowMapper<CHWReferralsSummaryDTO> {
		public CHWReferralsSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CHWReferralsSummaryDTO chwReferralsSummaryDTO =new CHWReferralsSummaryDTO();

			chwReferralsSummaryDTO.setCount(rs.getInt(rs.findColumn("count")));
			chwReferralsSummaryDTO.setServiceName(rs.getString(rs.findColumn("service_name")));
			return  chwReferralsSummaryDTO;
		}

	}



	public List<FacilityDepartmentReferralSummaryDTO> getFacilityDepartmentReferralsSummary(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new HealthFacilityDepartmentReferralsSummaryRowMapper());
	}



	public class HealthFacilityDepartmentReferralsSummaryRowMapper implements RowMapper<FacilityDepartmentReferralSummaryDTO> {
		public FacilityDepartmentReferralSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			FacilityDepartmentReferralSummaryDTO facilityReferralsSummaryDTO =new FacilityDepartmentReferralSummaryDTO();


			facilityReferralsSummaryDTO.setCount(rs.getInt(rs.findColumn("count")));


			if(rs.getInt(rs.findColumn("referral_source")) == 0) {
				facilityReferralsSummaryDTO.setDepartmentName("OPD");
			}else if(rs.getInt(rs.findColumn("referral_source")) == 1){
				facilityReferralsSummaryDTO.setDepartmentName("HIV Clinic");
			}else if(rs.getInt(rs.findColumn("referral_source")) == 2){
				facilityReferralsSummaryDTO.setDepartmentName("TB Clinic");
			}


			if(rs.getInt(rs.findColumn("referral_status")) == 1 )
				facilityReferralsSummaryDTO.setStatus("Success");
			else if(rs.getInt(rs.findColumn("referral_status")) == 0 ){
				facilityReferralsSummaryDTO.setStatus("Pending");
			}else{
				facilityReferralsSummaryDTO.setStatus("Failed");
			}


			return  facilityReferralsSummaryDTO;
		}

	}

	public List<FacilityProvidersReferralSummaryDTO> getFacilityProvidersReferralsSummary(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new HealthFacilityDepartmentProvidersReferralsSummaryRowMapper());
	}



	public class HealthFacilityDepartmentProvidersReferralsSummaryRowMapper implements RowMapper<FacilityProvidersReferralSummaryDTO> {
		public FacilityProvidersReferralSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			FacilityProvidersReferralSummaryDTO facilityReferralsSummaryDTO =new FacilityProvidersReferralSummaryDTO();


			facilityReferralsSummaryDTO.setCount(rs.getInt(rs.findColumn("count")));


			if(rs.getInt(rs.findColumn("referral_source")) == 0) {
				facilityReferralsSummaryDTO.setDepartmentName("OPD");
			}else if(rs.getInt(rs.findColumn("referral_source")) == 1){
				facilityReferralsSummaryDTO.setDepartmentName("HIV Clinic");
			}else if(rs.getInt(rs.findColumn("referral_source")) == 2){
				facilityReferralsSummaryDTO.setDepartmentName("TB Clinic");
			}


			facilityReferralsSummaryDTO.setProviderUuid(rs.getString(rs.findColumn("provider_uuid")));

			if(rs.getInt(rs.findColumn("referral_status")) == 1 )
				facilityReferralsSummaryDTO.setStatus("Success");
			else if(rs.getInt(rs.findColumn("referral_status")) == 0 ){
				facilityReferralsSummaryDTO.setStatus("Pending");
			}else{
				facilityReferralsSummaryDTO.setStatus("Failed");
			}


			return  facilityReferralsSummaryDTO;
		}

	}


	public List<InterFacilityReferralSummaryDTO> getInterFacilityReferralsSummary(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new IntraHealthFacilityReferralsSummaryRowMapper());
	}


	public class IntraHealthFacilityReferralsSummaryRowMapper implements RowMapper<InterFacilityReferralSummaryDTO> {
		public InterFacilityReferralSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			InterFacilityReferralSummaryDTO interFacilityReferralSummaryDTO =new InterFacilityReferralSummaryDTO();

			interFacilityReferralSummaryDTO.setCount(rs.getInt(rs.findColumn("count")));
			interFacilityReferralSummaryDTO.setToFacilityName(rs.getString(rs.findColumn("to_facility_name")));

			if(rs.getInt(rs.findColumn("referral_status")) == 1 )
				interFacilityReferralSummaryDTO.setStatus("Success");
			else if(rs.getInt(rs.findColumn("referral_status")) == 0 ){
				interFacilityReferralSummaryDTO.setStatus("Pending");
			}else{
				interFacilityReferralSummaryDTO.setStatus("Failed");
			}

			return  interFacilityReferralSummaryDTO;
		}

	}


}
