package org.opensrp.repository;

import org.opensrp.domain.TBTestType;
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
public class TBTestTypeRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;

	
	public Long save(TBTestType patientType) throws Exception {

		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(TBTestType.tbName).usingGeneratedKeyColumns("_id");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(TBTestType.COL_TEST_TYPE_NAME, patientType.getTestTypeName());
		parameters.put(TBTestType.COL_IS_ACTIVE  , patientType.getIsActive());
		parameters.put(TBTestType.COL_CREATED_AT , patientType.getCreatedAt());
		parameters.put(TBTestType.COL_UPDATED_AT , patientType.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
		
	}
	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + TBTestType.tbName;
		executeQuery(query);
	}



	public List<TBTestType> getTBPatientTypes(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new ServiceRowMapper());
	}



	public class ServiceRowMapper implements RowMapper<TBTestType> {
		public TBTestType mapRow(ResultSet rs, int rowNum) throws SQLException {
			TBTestType patientType = new TBTestType();

			patientType.setId(rs.getLong(rs.findColumn("_id")));
			patientType.setTestTypeName(rs.getString(rs.findColumn(TBTestType.COL_TEST_TYPE_NAME)));
			patientType.setIsActive(rs.getBoolean(rs.findColumn(TBTestType.COL_IS_ACTIVE)));
			patientType.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(TBTestType.COL_CREATED_AT)).getTime()));
			patientType.setUpdatedAt(rs.getDate(rs.findColumn(TBTestType.COL_UPDATED_AT)));
			return patientType;
		}
		
	}

}
