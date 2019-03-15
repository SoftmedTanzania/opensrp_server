package org.opensrp.repository;

import org.opensrp.domain.ClientReferrals;
import org.opensrp.domain.ClientReferralIndicators;
import org.opensrp.domain.ServiceIndicator;
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
public class ClientReferralIndicatorRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;

	
	public Long save(ClientReferralIndicators patientReferralIndicator) throws Exception {

		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ClientReferralIndicators.tbName).usingGeneratedKeyColumns(ClientReferralIndicators.COL_ID);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ClientReferralIndicators.COL_REFERRAL_ID , patientReferralIndicator.getClientReferrals().getId());
		parameters.put(ClientReferralIndicators.COL_REFERRAL_SERVICE_INDICATOR_ID , patientReferralIndicator.getServiceIndicator().getServiceIndicatorId());
		parameters.put(ClientReferralIndicators.COL_IS_ACTIVE  , patientReferralIndicator.isActive());
		parameters.put(ClientReferralIndicators.COL_CREATED_AT , patientReferralIndicator.getCreatedAt());
		parameters.put(ClientReferralIndicators.COL_UPDATED_AT , patientReferralIndicator.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
		
	}
	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + ClientReferralIndicators.tbName;
		executeQuery(query);
	}



	public List<ClientReferralIndicators> getPatientReferralIndicators(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new ServiceRowMapper());
	}



	public class ServiceRowMapper implements RowMapper<ClientReferralIndicators> {
		public ClientReferralIndicators mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClientReferralIndicators patientReferralIndicator = new ClientReferralIndicators();

			patientReferralIndicator.setId(rs.getLong(rs.findColumn(ClientReferralIndicators.COL_ID)));

			ClientReferrals clientReferrals = new ClientReferrals();
			clientReferrals.setId(rs.getLong(rs.findColumn(ClientReferralIndicators.COL_REFERRAL_ID)));

			patientReferralIndicator.setClientReferrals(clientReferrals);

			ServiceIndicator serviceIndicator = new ServiceIndicator();
			serviceIndicator.setServiceIndicatorId(rs.getLong(rs.findColumn(ClientReferralIndicators.COL_REFERRAL_SERVICE_INDICATOR_ID)));

			patientReferralIndicator.setServiceIndicator(serviceIndicator);

			patientReferralIndicator.setActive(rs.getBoolean(rs.findColumn(ClientReferralIndicators.COL_IS_ACTIVE)));
			patientReferralIndicator.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ClientReferralIndicators.COL_CREATED_AT)).getTime()));
			patientReferralIndicator.setUpdatedAt(rs.getDate(rs.findColumn(ClientReferralIndicators.COL_UPDATED_AT)));
			return patientReferralIndicator;
		}
		
	}

}
