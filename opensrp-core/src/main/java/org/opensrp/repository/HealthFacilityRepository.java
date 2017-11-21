package org.opensrp.repository;

import org.opensrp.domain.HealthFacilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;


@Repository
public class HealthFacilityRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;

	
	public int save(HealthFacilities healthFacilities) throws Exception {
		String insertQuery = "insert into " + HealthFacilities.tbName + " (" +
				HealthFacilities.COL_FACILITY_ID + "," +
				HealthFacilities.COL_FACILITY_NAME + "," +
				HealthFacilities.COL_FACILITY_CODE + "," +
				HealthFacilities.COL_PARENT_ID + "," +
				HealthFacilities.COL_UPDATED_AT + "," +
				HealthFacilities.COL_CREATED_AT + ") values (?,?,?,?,?,?) ";

		Object[] params = new Object[] {
				healthFacilities.getFacilityId(),
				healthFacilities.getFacilityName(),
				healthFacilities.getFacilityCode(),
		        healthFacilities.getParentId(),
		        healthFacilities.getUpdatedAt(),
				healthFacilities.getCreatedAt() };
		int[] types = new int[] {
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
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
		String query = "DELETE FROM " + HealthFacilities.tbName;
		executeQuery(query);
	}
	

	
	public class HealthFacilitiesRowMapper implements RowMapper<HealthFacilities> {
		public HealthFacilities mapRow(ResultSet rs, int rowNum) throws SQLException {
			HealthFacilities healthFacilitie = new HealthFacilities();
			healthFacilitie.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(HealthFacilities.COL_CREATED_AT)).getTime()));
			healthFacilitie.setFacilityId(rs.getString(rs.findColumn(HealthFacilities.COL_FACILITY_ID)));
			healthFacilitie.setFacilityName(rs.getString(rs.findColumn(HealthFacilities.COL_FACILITY_NAME)));
			healthFacilitie.setFacilityCode(rs.getString(rs.findColumn(HealthFacilities.COL_FACILITY_CODE)));
			healthFacilitie.setParentId(rs.getString(rs.findColumn(HealthFacilities.COL_PARENT_ID)));
			healthFacilitie.setUpdatedAt(rs.getDate(rs.findColumn(HealthFacilities.COL_UPDATED_AT)));
			return healthFacilitie;
		}
		
	}

}
