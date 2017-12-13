package org.opensrp.repository;

import org.opensrp.domain.Service;
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
public class ServiceRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;

	
	public Long save(Service service) throws Exception {

		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(Service.tbName).usingGeneratedKeyColumns("_id");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(Service.COL_SERVICE_NAME , service.getServiceName());
		parameters.put(Service.COL_IS_ACTIVE  , service.getIsActive());
		parameters.put(Service.COL_CREATED_AT , service.getCreatedAt());
		parameters.put(Service.COL_UPDATED_AT , service.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
		
	}
	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + Service.tbName;
		executeQuery(query);
	}



	public List<Service> getHealthFacilityPatients(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new ServiceRowMapper());
	}



	public class ServiceRowMapper implements RowMapper<Service> {
		public Service mapRow(ResultSet rs, int rowNum) throws SQLException {
			Service service = new Service();

			service.setId(rs.getLong(rs.findColumn("_id")));
			service.setServiceName(rs.getString(rs.findColumn(Service.COL_SERVICE_NAME)));
			service.setIsActive(rs.getString(rs.findColumn(Service.COL_IS_ACTIVE)));
			service.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(Service.COL_CREATED_AT)).getTime()));
			service.setUpdatedAt(rs.getDate(rs.findColumn(Service.COL_UPDATED_AT)));
			return service;
		}
		
	}

}
