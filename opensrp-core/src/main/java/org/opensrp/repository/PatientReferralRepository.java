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


	public long save(PatientReferral healthFacilities) throws Exception {


		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(PatientReferral.tbName).usingGeneratedKeyColumns("_id");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(PatientReferral.COL_PATIENT_ID ,healthFacilities.getPatient().getPatientId());
		parameters.put(PatientReferral.COL_COMMUNITY_BASED_HIV_SERVICE , healthFacilities.getCommunityBasedHivService());
		parameters.put(PatientReferral.COL_REFERRAL_REASON , healthFacilities.getReferralReason());
		parameters.put(PatientReferral.COL_SERVICE_ID , healthFacilities.getServiceId());
		parameters.put(PatientReferral.COL_FACILITY_ID ,healthFacilities.getFacilityId());
		parameters.put(PatientReferral.COL_CTC_NUMBER , healthFacilities.getCtcNumber());
		parameters.put(PatientReferral.COL_HAS_2WEEKS_COUGH , healthFacilities.getHas2WeeksCough());
		parameters.put(PatientReferral.COL_HAS_BLOOD_COUGH ,  healthFacilities.getHasBloodCough());
		parameters.put(PatientReferral.COL_HAS_SEVERE_SWEATING , healthFacilities.getHasSevereSweating());
		parameters.put(PatientReferral.COL_HAS_FEVER ,  healthFacilities.getHasFever());
		parameters.put(PatientReferral.COL_HAD_WEIGHT_LOSS ,healthFacilities.getHadWeightLoss());
		parameters.put(PatientReferral.COL_SERVICE_PROVIDER_UIID , healthFacilities.getServiceProviderUIID());
		parameters.put(PatientReferral.COL_SERVICE_PROVIDER_GROUP , healthFacilities.getServiceProviderGroup());
		parameters.put(PatientReferral.COL_VILLAGE_LEADER , healthFacilities.getVillageLeader());
		parameters.put(PatientReferral.COL_FROM_FACILITY_ID , healthFacilities.getFromFacilityId());
		parameters.put(PatientReferral.COL_OTHER_CLINICAL_INFORMATION , healthFacilities.getOtherClinicalInformation());
		parameters.put(PatientReferral.COL_SERVICES_GIVEN_TO_PATIENT , healthFacilities.getServiceGivenToPatient());
		parameters.put(PatientReferral.COL_OTHER_NOTES , healthFacilities.getOtherNotes());
		parameters.put(PatientReferral.COL_REFERRAL_SOURCE , healthFacilities.getReferralSource());
		parameters.put(PatientReferral.COL_REFERRAL_DATE , healthFacilities.getReferralDate());
		parameters.put(PatientReferral.COL_REFERRAL_STATUS , healthFacilities.getReferralStatus());
		parameters.put(PatientReferral.COL_INSTANCE_ID ,  healthFacilities.getInstanceId());
		parameters.put(PatientReferral.COL_CREATED_AT , healthFacilities.getCreatedAt());
		parameters.put(PatientReferral.COL_UPDATED_AT , healthFacilities.getCreatedAt());

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
			patientReferral.setId(rs.getLong(rs.findColumn("_id")));

			Patients patient = new Patients();
			patient.setPatientId(rs.getLong(rs.findColumn(PatientReferral.COL_PATIENT_ID)));
			patientReferral.setPatient(patient);

			patientReferral.setCommunityBasedHivService(rs.getString(rs.findColumn(PatientReferral.COL_COMMUNITY_BASED_HIV_SERVICE)));
			patientReferral.setReferralReason(rs.getString(rs.findColumn(PatientReferral.COL_REFERRAL_REASON)));
			patientReferral.setServiceId(rs.getInt(rs.findColumn(PatientReferral.COL_SERVICE_ID)));
			patientReferral.setCtcNumber(rs.getString(rs.findColumn(PatientReferral.COL_CTC_NUMBER)));
			patientReferral.setHas2WeeksCough(rs.getBoolean(rs.findColumn(PatientReferral.COL_HAS_2WEEKS_COUGH)));
			patientReferral.setHasBloodCough(rs.getBoolean(rs.findColumn(PatientReferral.COL_HAS_BLOOD_COUGH)));
			patientReferral.setHasSevereSweating(rs.getBoolean(rs.findColumn(PatientReferral.COL_HAS_SEVERE_SWEATING)));
			patientReferral.setHasFever(rs.getBoolean(rs.findColumn(PatientReferral.COL_HAS_FEVER)));
			patientReferral.setHadWeightLoss(rs.getBoolean(rs.findColumn(PatientReferral.COL_HAD_WEIGHT_LOSS)));
			patientReferral.setServiceProviderUIID(rs.getString(rs.findColumn(PatientReferral.COL_SERVICE_PROVIDER_UIID)));
			patientReferral.setServiceProviderGroup(rs.getString(rs.findColumn(PatientReferral.COL_SERVICE_PROVIDER_GROUP)));
			patientReferral.setVillageLeader(rs.getString(rs.findColumn(PatientReferral.COL_VILLAGE_LEADER)));
			patientReferral.setReferralDate(rs.getDate(rs.findColumn(PatientReferral.COL_REFERRAL_DATE)));
			patientReferral.setFacilityId(rs.getString(rs.findColumn(PatientReferral.COL_FACILITY_ID)));
			patientReferral.setReferralStatus(rs.getInt(rs.findColumn(PatientReferral.COL_REFERRAL_STATUS)));
			patientReferral.setInstanceId(rs.getString(rs.findColumn(PatientReferral.COL_INSTANCE_ID)));
			patientReferral.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(PatientReferral.COL_CREATED_AT)).getTime()));
			patientReferral.setUpdatedAt(rs.getDate(rs.findColumn(PatientReferral.COL_UPDATED_AT)));
			return patientReferral;
		}
		
	}

}
