package org.opensrp.repository;

import org.opensrp.domain.TBPatientType;
import org.opensrp.domain.TBPatientType;
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
public class TBPatientTypeRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;

	
	public Long save(TBPatientType patientType) throws Exception {

		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(TBPatientType.tbName).usingGeneratedKeyColumns("_id");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(TBPatientType.COL_PATIENT_TYPE_NAME , patientType.getPatientTypeName());
		parameters.put(TBPatientType.COL_IS_ACTIVE  , patientType.getIsActive());
		parameters.put(TBPatientType.COL_CREATED_AT , patientType.getCreatedAt());
		parameters.put(TBPatientType.COL_UPDATED_AT , patientType.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
		
	}
	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + TBPatientType.tbName;
		executeQuery(query);
	}



	public List<TBPatientType> getTBPatientTypes(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new ServiceRowMapper());
	}



	public class ServiceRowMapper implements RowMapper<TBPatientType> {
		public TBPatientType mapRow(ResultSet rs, int rowNum) throws SQLException {
			TBPatientType patientType = new TBPatientType();

			patientType.setId(rs.getLong(rs.findColumn("_id")));
			patientType.setPatientTypeName(rs.getString(rs.findColumn(TBPatientType.COL_PATIENT_TYPE_NAME)));
			patientType.setIsActive(rs.getBoolean(rs.findColumn(TBPatientType.COL_IS_ACTIVE)));
			patientType.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(TBPatientType.COL_CREATED_AT)).getTime()));
			patientType.setUpdatedAt(rs.getDate(rs.findColumn(TBPatientType.COL_UPDATED_AT)));
			return patientType;
		}
		
	}

}
