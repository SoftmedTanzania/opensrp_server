package org.opensrp.repository;

import org.opensrp.domain.PatientReferral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;


@Repository
public class PatientReferralRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;

	
	public int save(PatientReferral healthFacilities) throws Exception {
		String insertQuery = "insert into " + PatientReferral.tbName + " (" +
				PatientReferral.COL_REFERRAL_ID + "," +
				PatientReferral.COL_PATIENT_ID + "," +
				PatientReferral.COL_COMMUNITY_BASED_HIV_SERVICE + "," +
				PatientReferral.COL_REFERRAL_REASON + "," +
				PatientReferral.COL_SERVICE_ID + "," +
				PatientReferral.COL_FACILITY_ID + "," +
				PatientReferral.COL_CTC_NUMBER + "," +
				PatientReferral.COL_HAS_2WEEKS_COUGH + "," +
				PatientReferral.COL_HAS_BLOOD_COUGH + "," +
				PatientReferral.COL_HAS_SEVERE_SWEATING + "," +
				PatientReferral.COL_HAS_FEVER + "," +
				PatientReferral.COL_HAD_WEIGHT_LOSS + "," +
				PatientReferral.COL_SERVICE_PROVIDER_UIID + "," +
				PatientReferral.COL_SERVICE_PROVIDER_GROUP + "," +
				PatientReferral.COL_VILLAGE_LEADER + "," +
				PatientReferral.COL_REFERRAL_DATE + "," +
				PatientReferral.COL_REFERRAL_STATUS + "," +
				PatientReferral.COL_UPDATED_AT + "," +
				PatientReferral.COL_CREATED_AT + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

		Object[] params = new Object[] {
				healthFacilities.getReferral_id(),
				healthFacilities.getPatient_id(),
				healthFacilities.getCommunityBasedHivService(),
				healthFacilities.getReferralReason(),
				healthFacilities.getServiceId(),
		        healthFacilities.getFacilityId(),
		        healthFacilities.getCtcNumber(),
		        healthFacilities.getHas2WeeksCough(),
		        healthFacilities.getHasBloodCough(),
		        healthFacilities.getHasSevereSweating(),
		        healthFacilities.getHasFever(),
		        healthFacilities.getHadWeightLoss(),
		        healthFacilities.getServiceProviderUIID(),
		        healthFacilities.getServiceProviderGroup(),
		        healthFacilities.getVillageLeader(),
		        healthFacilities.getReferralDate(),
		        healthFacilities.getReferralStatus(),
		        healthFacilities.getUpdatedAt(),
				healthFacilities.getCreatedAt() };

		int[] types = new int[] {
				Types.VARCHAR,
				Types.INTEGER,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.INTEGER,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.BOOLEAN,
				Types.BOOLEAN,
				Types.BOOLEAN,
				Types.BOOLEAN,
				Types.BOOLEAN,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.DATE,
				Types.INTEGER,
				Types.DATE,
				Types.TIMESTAMP };
		
		return jdbcTemplate.update(insertQuery, params, types);
		
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



	public List<PatientReferral> getReferrals(String sql, String[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new HealthFacilityRefferalRowMapper());
	}



	public class HealthFacilityRefferalRowMapper implements RowMapper<PatientReferral> {
		public PatientReferral mapRow(ResultSet rs, int rowNum) throws SQLException {
			PatientReferral patientReferral = new PatientReferral();

			patientReferral.setId(rs.getLong(rs.findColumn("_id")));
			patientReferral.setReferral_id(rs.getString(rs.findColumn(PatientReferral.COL_REFERRAL_ID)));
			patientReferral.setPatient_id(rs.getLong(rs.findColumn(PatientReferral.COL_PATIENT_ID)));
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
			patientReferral.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(PatientReferral.COL_CREATED_AT)).getTime()));
			patientReferral.setUpdatedAt(rs.getDate(rs.findColumn(PatientReferral.COL_UPDATED_AT)));
			return patientReferral;
		}
		
	}

}
