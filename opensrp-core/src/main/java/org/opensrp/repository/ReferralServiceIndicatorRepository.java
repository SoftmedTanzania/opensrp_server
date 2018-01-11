package org.opensrp.repository;

import org.opensrp.domain.ReferralIndicator;
import org.opensrp.domain.ReferralService;
import org.opensrp.domain.ReferralServiceIndicator;
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
public class ReferralServiceIndicatorRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;

	
	public Long save(ReferralServiceIndicator referralServiceIndicator) throws Exception {

		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ReferralServiceIndicator.tbName).usingGeneratedKeyColumns(ReferralServiceIndicator.COL_REFERRAL_SERVICE_INDICATOR_ID);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ReferralServiceIndicator.COL_REFERRAL_INDICATOR_ID , referralServiceIndicator.getReferralIndicator().getReferralIndicatorId());
		parameters.put(ReferralServiceIndicator.COL_REFERRAL_SERVICE_ID , referralServiceIndicator.getReferralService().getServiceId());
		parameters.put(ReferralServiceIndicator.COL_IS_ACTIVE  , referralServiceIndicator.isActive());
		parameters.put(ReferralServiceIndicator.COL_CREATED_AT , referralServiceIndicator.getCreatedAt());
		parameters.put(ReferralServiceIndicator.COL_UPDATED_AT , referralServiceIndicator.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
		
	}
	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + ReferralServiceIndicator.tbName;
		executeQuery(query);
	}



	public List<ReferralServiceIndicator> getBoreshaAfyaServices(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new ServiceRowMapper());
	}



	public class ServiceRowMapper implements RowMapper<ReferralServiceIndicator> {
		public ReferralServiceIndicator mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReferralServiceIndicator referralService = new ReferralServiceIndicator();

			referralService.setReferralServiceIndicatorId(rs.getLong(rs.findColumn(ReferralServiceIndicator.COL_REFERRAL_SERVICE_INDICATOR_ID)));

			ReferralService service = new ReferralService();
			service.setServiceId(rs.getLong(rs.findColumn(ReferralServiceIndicator.COL_REFERRAL_SERVICE_ID)));

			referralService.setReferralService(service);

			ReferralIndicator referralIndicator = new ReferralIndicator();
			referralIndicator.setReferralIndicatorId(rs.getLong(rs.findColumn(ReferralServiceIndicator.COL_REFERRAL_INDICATOR_ID)));

			referralService.setReferralIndicator(referralIndicator);


			referralService.setActive(rs.getBoolean(rs.findColumn(ReferralServiceIndicator.COL_IS_ACTIVE)));
			referralService.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ReferralServiceIndicator.COL_CREATED_AT)).getTime()));
			referralService.setUpdatedAt(rs.getDate(rs.findColumn(ReferralServiceIndicator.COL_UPDATED_AT)));
			return referralService;
		}
		
	}

}
