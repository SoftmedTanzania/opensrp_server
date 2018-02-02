package org.opensrp.repository;

import org.opensrp.domain.PatientReferral;
import org.opensrp.domain.Patients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class PatientReferralRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;


	public long save(PatientReferral patientReferral) throws Exception {


		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(PatientReferral.tbName).usingGeneratedKeyColumns(PatientReferral.COL_REFERRAL_ID);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(PatientReferral.COL_PATIENT_ID ,patientReferral.getPatient().getPatientId());
		parameters.put(PatientReferral.COL_COMMUNITY_BASED_HIV_SERVICE , patientReferral.getCommunityBasedHivService());
		parameters.put(PatientReferral.COL_REFERRAL_REASON , patientReferral.getReferralReason());
		parameters.put(PatientReferral.COL_SERVICE_ID , patientReferral.getServiceId());
		parameters.put(PatientReferral.COL_REFERRAL_UUID , patientReferral.getReferralUUID());
		parameters.put(PatientReferral.COL_FACILITY_ID ,patientReferral.getFacilityId());
		parameters.put(PatientReferral.COL_CTC_NUMBER , patientReferral.getCtcNumber());
		parameters.put(PatientReferral.COL_SERVICE_PROVIDER_UIID , patientReferral.getServiceProviderUIID());
		parameters.put(PatientReferral.COL_SERVICE_PROVIDER_GROUP , patientReferral.getServiceProviderGroup());
		parameters.put(PatientReferral.COL_VILLAGE_LEADER , patientReferral.getVillageLeader());
		parameters.put(PatientReferral.COL_FROM_FACILITY_ID , patientReferral.getFromFacilityId());
		parameters.put(PatientReferral.COL_OTHER_CLINICAL_INFORMATION , patientReferral.getOtherClinicalInformation());
		parameters.put(PatientReferral.COL_SERVICES_GIVEN_TO_PATIENT , patientReferral.getServiceGivenToPatient());
		parameters.put(PatientReferral.COL_OTHER_NOTES , patientReferral.getOtherNotes());
		parameters.put(PatientReferral.COL_REFERRAL_SOURCE , patientReferral.getReferralSource());
		parameters.put(PatientReferral.COL_REFERRAL_DATE , patientReferral.getReferralDate());
		parameters.put(PatientReferral.COL_REFERRAL_STATUS , patientReferral.getReferralStatus());
		parameters.put(PatientReferral.COL_INSTANCE_ID ,  patientReferral.getInstanceId());
		parameters.put(PatientReferral.COL_REFERRAL_TYPE ,  patientReferral.getReferralType());
		parameters.put(PatientReferral.COL_TEST_RESULTS ,  patientReferral.isTestResults());
		parameters.put(PatientReferral.COL_CREATED_AT , patientReferral.getCreatedAt());
		parameters.put(PatientReferral.COL_UPDATED_AT , patientReferral.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();

	}

	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}

	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);

	}

	public void clearTable() throws Exception {
		String query = "DELETE FROM " + PatientReferral.tbName;
		executeQuery(query);
	}



	public List<PatientReferral> getReferrals(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new HealthFacilityRefferalRowMapper());
	}



	public class HealthFacilityRefferalRowMapper implements RowMapper<PatientReferral> {
		public PatientReferral mapRow(ResultSet rs, int rowNum) throws SQLException {
			PatientReferral patientReferral = new PatientReferral();
			patientReferral.setId(rs.getLong(rs.findColumn(PatientReferral.COL_REFERRAL_ID)));

			Patients patient = new Patients();
			patient.setPatientId(rs.getLong(rs.findColumn(PatientReferral.COL_PATIENT_ID)));
			patientReferral.setPatient(patient);

			patientReferral.setCommunityBasedHivService(rs.getString(rs.findColumn(PatientReferral.COL_COMMUNITY_BASED_HIV_SERVICE)));
			patientReferral.setReferralReason(rs.getString(rs.findColumn(PatientReferral.COL_REFERRAL_REASON)));
			patientReferral.setFromFacilityId(rs.getString(rs.findColumn(PatientReferral.COL_FROM_FACILITY_ID)));
			patientReferral.setServiceId(rs.getInt(rs.findColumn(PatientReferral.COL_SERVICE_ID)));
			patientReferral.setReferralUUID(rs.getString(rs.findColumn(PatientReferral.COL_REFERRAL_UUID)));
			patientReferral.setCtcNumber(rs.getString(rs.findColumn(PatientReferral.COL_CTC_NUMBER)));
			patientReferral.setServiceProviderUIID(rs.getString(rs.findColumn(PatientReferral.COL_SERVICE_PROVIDER_UIID)));
			patientReferral.setServiceProviderGroup(rs.getString(rs.findColumn(PatientReferral.COL_SERVICE_PROVIDER_GROUP)));
			patientReferral.setVillageLeader(rs.getString(rs.findColumn(PatientReferral.COL_VILLAGE_LEADER)));
			patientReferral.setReferralDate(rs.getDate(rs.findColumn(PatientReferral.COL_REFERRAL_DATE)));
			patientReferral.setFacilityId(rs.getString(rs.findColumn(PatientReferral.COL_FACILITY_ID)));
			patientReferral.setReferralStatus(rs.getInt(rs.findColumn(PatientReferral.COL_REFERRAL_STATUS)));
			patientReferral.setInstanceId(rs.getString(rs.findColumn(PatientReferral.COL_INSTANCE_ID)));
			patientReferral.setReferralType(rs.getLong(rs.findColumn(PatientReferral.COL_REFERRAL_TYPE)));
			patientReferral.setReferralSource(rs.getInt(rs.findColumn(PatientReferral.COL_REFERRAL_SOURCE)));
			patientReferral.setServiceGivenToPatient(rs.getString(rs.findColumn(PatientReferral.COL_SERVICES_GIVEN_TO_PATIENT)));
			patientReferral.setTestResults(rs.getBoolean(rs.findColumn(PatientReferral.COL_TEST_RESULTS)));
			patientReferral.setOtherClinicalInformation(rs.getString(rs.findColumn(PatientReferral.COL_OTHER_CLINICAL_INFORMATION)));
			patientReferral.setOtherNotes(rs.getString(rs.findColumn(PatientReferral.COL_OTHER_NOTES)));
			patientReferral.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(PatientReferral.COL_CREATED_AT)).getTime()));
			patientReferral.setUpdatedAt(rs.getDate(rs.findColumn(PatientReferral.COL_UPDATED_AT)));
			return patientReferral;
		}
		
	}

}
