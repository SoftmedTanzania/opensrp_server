package org.opensrp.repository;

import org.opensrp.domain.ReferralFeedback;
import org.opensrp.domain.ReferralType;
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
public class ReferralFeedbackRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;


	public long save(ReferralFeedback referralFeedback) throws Exception {


		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ReferralFeedback.tbName).usingGeneratedKeyColumns("_id");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("_id", referralFeedback.getId());
		parameters.put(ReferralFeedback.COL_DESC, referralFeedback.getDesc());
		parameters.put(ReferralFeedback.COL_DESC_SW, referralFeedback.getDescSw());
		parameters.put(ReferralFeedback.COL_REFERRAL_TYPE_ID, referralFeedback.getReferralType().getReferralTypeId());
		parameters.put(ReferralFeedback.COL_CREATED_AT, referralFeedback.getCreatedAt());
		parameters.put(ReferralFeedback.COL_UPDATED_AT, referralFeedback.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();

	}

	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}

	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);

	}

	public void clearTable() throws Exception {
		String query = "DELETE FROM " + ReferralFeedback.tbName;
		executeQuery(query);
	}


	public List<ReferralFeedback> getReferralFeedback(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new ReferralFeedbackRowMapper());
	}


	public class ReferralFeedbackRowMapper implements RowMapper<ReferralFeedback> {
		public ReferralFeedback mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReferralFeedback referralFeedback = new ReferralFeedback();

			referralFeedback.setId(rs.getLong(rs.findColumn("_id")));

			ReferralType referralType = new ReferralType();
			referralType.setReferralTypeId(rs.getLong(rs.findColumn(ReferralFeedback.COL_REFERRAL_TYPE_ID)));
			referralFeedback.setReferralType(referralType);

			referralFeedback.setDesc(rs.getString(rs.findColumn(ReferralFeedback.COL_DESC)));
			referralFeedback.setDescSw(rs.getString(rs.findColumn(ReferralFeedback.COL_DESC_SW)));

			referralFeedback.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ReferralFeedback.COL_CREATED_AT)).getTime()));
			referralFeedback.setUpdatedAt(rs.getDate(rs.findColumn(ReferralFeedback.COL_UPDATED_AT)));
			return referralFeedback;
		}

	}


}
