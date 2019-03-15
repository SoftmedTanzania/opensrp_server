package org.opensrp.repository;

import org.opensrp.domain.HealthFacilitiesReferralClients;
import org.opensrp.domain.ReferralClient;
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
public class HealthFacilitiesClientsRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;

	
	public Long save(HealthFacilitiesReferralClients healthFacilitiesReferralClients) throws Exception {
		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(HealthFacilitiesReferralClients.tbName).usingGeneratedKeyColumns(HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(HealthFacilitiesReferralClients.COL_CLIENT_ID, healthFacilitiesReferralClients.getPatient().getClientId());
		parameters.put(HealthFacilitiesReferralClients.COL_FACILITY_ID  , healthFacilitiesReferralClients.getFacilityId());
		parameters.put(HealthFacilitiesReferralClients.COL_CTC_NUMBER , healthFacilitiesReferralClients.getCtcNumber());
		parameters.put(HealthFacilitiesReferralClients.COL_CREATED_AT , healthFacilitiesReferralClients.getCreatedAt());
		parameters.put(HealthFacilitiesReferralClients.COL_UPDATED_AT , healthFacilitiesReferralClients.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
		
	}
	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + HealthFacilitiesReferralClients.tbName;
		executeQuery(query);
	}



	public List<HealthFacilitiesReferralClients> getHealthFacilityPatients(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new HealthFacilityPatientsRowMapper());
	}



	public class HealthFacilityPatientsRowMapper implements RowMapper<HealthFacilitiesReferralClients> {
		public HealthFacilitiesReferralClients mapRow(ResultSet rs, int rowNum) throws SQLException {
			HealthFacilitiesReferralClients facilitiesPatients = new HealthFacilitiesReferralClients();

			facilitiesPatients.setHealthFacilityClientId(rs.getLong(rs.findColumn(HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID)));

			ReferralClient referralClient = new ReferralClient();
			referralClient.setClientId(rs.getLong(rs.findColumn(HealthFacilitiesReferralClients.COL_CLIENT_ID)));

			facilitiesPatients.setPatient(referralClient);
			facilitiesPatients.setCtcNumber(rs.getString(rs.findColumn(HealthFacilitiesReferralClients.COL_CTC_NUMBER)));
			facilitiesPatients.setFacilityId(rs.getLong(rs.findColumn(HealthFacilitiesReferralClients.COL_FACILITY_ID)));
			facilitiesPatients.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(HealthFacilitiesReferralClients.COL_CREATED_AT)).getTime()));
			facilitiesPatients.setUpdatedAt(rs.getDate(rs.findColumn(HealthFacilitiesReferralClients.COL_UPDATED_AT)));
			return facilitiesPatients;
		}
		
	}

}
