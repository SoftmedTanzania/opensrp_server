package org.opensrp.repository;

import org.opensrp.domain.ReferralIndicator;
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
public class ReferralIndicatorRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;

	
	public Long save(ReferralIndicator referralIndicator) throws Exception {

		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ReferralIndicator.tbName).usingGeneratedKeyColumns(ReferralIndicator.COL_REFERRAL_INDICATOR_ID);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ReferralIndicator.COL_REFERRAL_INDICATOR_NAME , referralIndicator.getReferralIndicatorName());
		parameters.put(ReferralIndicator.COL_IS_ACTIVE  , referralIndicator.isActive());
		parameters.put(ReferralIndicator.COL_CREATED_AT , referralIndicator.getCreatedAt());
		parameters.put(ReferralIndicator.COL_UPDATED_AT , referralIndicator.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
		
	}
	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + ReferralIndicator.tbName;
		executeQuery(query);
	}



	public List<ReferralIndicator> getReferralIndicators(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new ServiceRowMapper());
	}



	public class ServiceRowMapper implements RowMapper<ReferralIndicator> {
		public ReferralIndicator mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReferralIndicator referralIndicator = new ReferralIndicator();

			referralIndicator.setReferralIndicatorId(rs.getLong(rs.findColumn(ReferralIndicator.COL_REFERRAL_INDICATOR_ID)));
			referralIndicator.setReferralIndicatorName(rs.getString(rs.findColumn(ReferralIndicator.COL_REFERRAL_INDICATOR_NAME)));
			referralIndicator.setActive(rs.getBoolean(rs.findColumn(ReferralIndicator.COL_IS_ACTIVE)));
			referralIndicator.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ReferralIndicator.COL_CREATED_AT)).getTime()));
			referralIndicator.setUpdatedAt(rs.getDate(rs.findColumn(ReferralIndicator.COL_UPDATED_AT)));
			return referralIndicator;
		}
		
	}

}
