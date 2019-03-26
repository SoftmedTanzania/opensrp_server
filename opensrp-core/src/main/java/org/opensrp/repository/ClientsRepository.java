package org.opensrp.repository;

import org.opensrp.domain.ClientRegistrationReason;
import org.opensrp.domain.ReferralClient;
import org.opensrp.dto.report.MaleFemaleCountObject;
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

//import org.opensrp.domain.ReferralClient;

@Repository
public class ClientsRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;


	public Long save(ReferralClient referralClient) throws Exception {
		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ReferralClient.tbName).usingGeneratedKeyColumns(ReferralClient.COL_CLIENT_ID);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ReferralClient.COL_PATIENT_FIRST_NAME , referralClient.getFirstName());
		parameters.put(ReferralClient.COL_PATIENT_MIDDLE_NAME , referralClient.getMiddleName());
		parameters.put(ReferralClient.COL_PATIENT_SURNAME , referralClient.getSurname());
		parameters.put(ReferralClient.COL_PHONE_NUMBER , referralClient.getPhoneNumber());
		parameters.put(ReferralClient.COL_DATE_OF_BIRTH , referralClient.getDateOfBirth());
		parameters.put(ReferralClient.COL_GENDER , referralClient.getGender());
		parameters.put(ReferralClient.COL_DATE_OF_DEATH , referralClient.getDateOfDeath());
		parameters.put(ReferralClient.COL_HIV_STATUS , referralClient.isHivStatus());
		parameters.put(ReferralClient.COL_VILLAGE , referralClient.getVillage());
		parameters.put(ReferralClient.COL_WARD , referralClient.getWard());
		parameters.put(ReferralClient.COL_HAMLET , referralClient.getHamlet());
		parameters.put(ReferralClient.COL_COMMUNITY_BASED_HIV_SERVICE , referralClient.getCommunityBasedHivService());
		parameters.put(ReferralClient.COL_CARE_TAKER_NAME , referralClient.getCareTakerName());
		parameters.put(ReferralClient.COL_CARE_TAKER_PHONE_NUMBER, referralClient.getCareTakerPhoneNumber());
		parameters.put(ReferralClient.COL_CARE_TAKER_RELATIONSHIP, referralClient.getCareTakerRelationship());
		parameters.put(ReferralClient.COL_REGISTRATION_REASON, referralClient.getClientRegistrationReason().getRegistrationId());
		parameters.put(ReferralClient.COL_VEO, referralClient.getVeo());
		parameters.put(ReferralClient.COL_CREATED_AT , referralClient.getCreatedAt());
		parameters.put(ReferralClient.COL_UPDATED_AT , referralClient.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
	}

	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}

	public int checkIfExists(String query, Object[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);

	}

	public void clearTable() throws Exception {
		String query = "DELETE FROM " + ReferralClient.tbName;
		executeQuery(query);
	}


	public List<ReferralClient> getPatients(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new referralPatientsRowMapper());
	}


	public class referralPatientsRowMapper implements RowMapper<ReferralClient> {
		public ReferralClient mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReferralClient referralClient = new ReferralClient();
			referralClient.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ReferralClient.COL_CREATED_AT)).getTime()));
			referralClient.setUpdatedAt(new Date(rs.getTimestamp(rs.findColumn(ReferralClient.COL_UPDATED_AT)).getTime()));
			referralClient.setClientId(rs.getLong(rs.findColumn(ReferralClient.COL_CLIENT_ID)));

			ClientRegistrationReason reason = new ClientRegistrationReason();
			reason.setRegistrationId(rs.getInt(rs.findColumn(ReferralClient.COL_REGISTRATION_REASON)));

			referralClient.setClientRegistrationReason(reason);

			referralClient.setFirstName(rs.getString(rs.findColumn(ReferralClient.COL_PATIENT_FIRST_NAME)));
			referralClient.setSurname(rs.getString(rs.findColumn(ReferralClient.COL_PATIENT_SURNAME)));
			referralClient.setMiddleName(rs.getString(rs.findColumn(ReferralClient.COL_PATIENT_MIDDLE_NAME)));
			referralClient.setWard(rs.getString(rs.findColumn(ReferralClient.COL_WARD)));
			referralClient.setVillage(rs.getString(rs.findColumn(ReferralClient.COL_VILLAGE)));
			referralClient.setHamlet(rs.getString(rs.findColumn(ReferralClient.COL_HAMLET)));
			referralClient.setPhoneNumber(rs.getString(rs.findColumn(ReferralClient.COL_PHONE_NUMBER)));
			referralClient.setDateOfBirth(rs.getDate(rs.findColumn(ReferralClient.COL_DATE_OF_BIRTH)));
			referralClient.setCommunityBasedHivService(rs.getString(rs.findColumn(ReferralClient.COL_COMMUNITY_BASED_HIV_SERVICE)));
			referralClient.setGender(rs.getString(rs.findColumn(ReferralClient.COL_GENDER)));
			referralClient.setCareTakerName(rs.getString(rs.findColumn(ReferralClient.COL_CARE_TAKER_NAME)));
			referralClient.setCareTakerPhoneNumber(rs.getString(rs.findColumn(ReferralClient.COL_CARE_TAKER_PHONE_NUMBER)));
			referralClient.setCareTakerRelationship(rs.getString(rs.findColumn(ReferralClient.COL_CARE_TAKER_RELATIONSHIP)));
			referralClient.setVeo(rs.getString(rs.findColumn(ReferralClient.COL_VEO)));
			referralClient.setHivStatus(rs.getBoolean(rs.findColumn(ReferralClient.COL_HIV_STATUS)));
			referralClient.setDateOfDeath(rs.getDate(rs.findColumn(ReferralClient.COL_DATE_OF_DEATH)));
			return referralClient;
		}

	}


	public List<MaleFemaleCountObject> getMaleFemaleCountReports(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new ReportObjectRowMapper());
	}


	public class ReportObjectRowMapper implements RowMapper<MaleFemaleCountObject> {
		public MaleFemaleCountObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			MaleFemaleCountObject maleFemaleCountObject = new MaleFemaleCountObject();

			maleFemaleCountObject.setMale(rs.getInt(rs.findColumn("Male")));
			maleFemaleCountObject.setFemale(rs.getInt(rs.findColumn("Female")));

			return maleFemaleCountObject;
		}

	}

}
