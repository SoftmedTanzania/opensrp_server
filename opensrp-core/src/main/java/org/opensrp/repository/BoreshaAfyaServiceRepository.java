package org.opensrp.repository;

import org.opensrp.domain.BoreshaAfyaService;
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
public class BoreshaAfyaServiceRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;

	
	public Long save(BoreshaAfyaService boreshaAfyaService) throws Exception {

		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(BoreshaAfyaService.tbName).usingGeneratedKeyColumns("_id");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(BoreshaAfyaService.COL_SERVICE_NAME , boreshaAfyaService.getServiceName());
		parameters.put(BoreshaAfyaService.COL_IS_ACTIVE  , boreshaAfyaService.getIsActive());
		parameters.put(BoreshaAfyaService.COL_CREATED_AT , boreshaAfyaService.getCreatedAt());
		parameters.put(BoreshaAfyaService.COL_UPDATED_AT , boreshaAfyaService.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
		
	}
	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + BoreshaAfyaService.tbName;
		executeQuery(query);
	}



	public List<BoreshaAfyaService> getBoreshaAfyaServices(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new ServiceRowMapper());
	}



	public class ServiceRowMapper implements RowMapper<BoreshaAfyaService> {
		public BoreshaAfyaService mapRow(ResultSet rs, int rowNum) throws SQLException {
			BoreshaAfyaService boreshaAfyaService = new BoreshaAfyaService();

			boreshaAfyaService.setId(rs.getLong(rs.findColumn("_id")));
			boreshaAfyaService.setServiceName(rs.getString(rs.findColumn(BoreshaAfyaService.COL_SERVICE_NAME)));
			boreshaAfyaService.setIsActive(rs.getBoolean(rs.findColumn(BoreshaAfyaService.COL_IS_ACTIVE)));
			boreshaAfyaService.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(BoreshaAfyaService.COL_CREATED_AT)).getTime()));
			boreshaAfyaService.setUpdatedAt(rs.getDate(rs.findColumn(BoreshaAfyaService.COL_UPDATED_AT)));
			return boreshaAfyaService;
		}
		
	}

}
