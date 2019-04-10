package org.opensrp.repository;

import org.opensrp.domain.*;
import org.opensrp.dto.report.TotalCountObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ClientsAppointmentsRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	
	public long save(ClientAppointments clientAppointments) throws Exception {


		insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ClientAppointments.tbName).usingGeneratedKeyColumns(ClientAppointments.COL_APPOINTMENT_ID);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ClientAppointments.COL_HEALTH_FACILITY_CLIENT_ID, clientAppointments.getHealthFacilitiesReferralClients().getHealthFacilityClientId());
		parameters.put(ClientAppointments.COL_APPOINTMENT_DATE, clientAppointments.getAppointmentDate());
		parameters.put(ClientAppointments.COL_IS_CANCELLED, clientAppointments.getIsCancelled());
		parameters.put(ClientAppointments.COL_STATUS, clientAppointments.getStatus().getStatusId());
		parameters.put(ClientAppointments.COL_APPOINTMENT_TYPE, clientAppointments.getAppointmentType().getId());
		try {
			parameters.put(ClientAppointments.COL_FOLLOWUP_REFERRAL_ID, clientAppointments.getClientReferrals().getId());
		}catch (Exception e){
			e.printStackTrace();
		}
		parameters.put(ClientAppointments.COL_CREATED_AT, clientAppointments.getUpdatedAt());
		parameters.put(ClientAppointments.COL_UPDATED_AT, clientAppointments.getCreatedAt());

		return insert.executeAndReturnKey(parameters).longValue();
	}


	
	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}
	
	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);
		
	}
	
	public void clearTable() throws Exception {
		String query = "DELETE FROM " + ClientAppointments.tbName;
		executeQuery(query);
	}



	public List<ClientAppointments> getAppointments(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new PatientsAppointmentsRowMapper());
	}



	public class PatientsAppointmentsRowMapper implements RowMapper<ClientAppointments> {
		public ClientAppointments mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClientAppointments clientAppointments = new ClientAppointments();

			clientAppointments.setAppointment_id(rs.getLong(rs.findColumn(ClientAppointments.COL_APPOINTMENT_ID)));

			HealthFacilitiesReferralClients healthFacilitiesReferralClients = new HealthFacilitiesReferralClients();
			healthFacilitiesReferralClients.setHealthFacilityClientId(rs.getLong(rs.findColumn(ClientAppointments.COL_HEALTH_FACILITY_CLIENT_ID)));

			clientAppointments.setHealthFacilitiesReferralClients(healthFacilitiesReferralClients);
			clientAppointments.setAppointmentDate(rs.getDate(rs.findColumn(ClientAppointments.COL_APPOINTMENT_DATE)));
			clientAppointments.setIsCancelled(rs.getBoolean(rs.findColumn(ClientAppointments.COL_IS_CANCELLED)));

			Status status = new Status();
			status.setStatusId(rs.getInt(rs.findColumn(ClientAppointments.COL_STATUS)));
			clientAppointments.setStatus(status);

			AppointmentType appointmentType = new AppointmentType();
			appointmentType.setId(rs.getInt(rs.findColumn(ClientAppointments.COL_APPOINTMENT_TYPE)));

			ClientReferrals clientReferrals = new ClientReferrals();
			clientReferrals.setId(rs.getLong(rs.findColumn(ClientAppointments.COL_FOLLOWUP_REFERRAL_ID)));

			clientAppointments.setClientReferrals(clientReferrals);

			clientAppointments.setAppointmentType(appointmentType);
			clientAppointments.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ClientAppointments.COL_CREATED_AT)).getTime()));
			clientAppointments.setUpdatedAt(rs.getDate(rs.findColumn(ClientAppointments.COL_UPDATED_AT)));
			return clientAppointments;
		}
		
	}



	public List<TotalCountObject> getCount(String sql, Object[] args) throws Exception {
		return this.jdbcTemplate.query(sql,args, new CountRowMapper());
	}

	public class CountRowMapper implements RowMapper<TotalCountObject> {
		public TotalCountObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			TotalCountObject countObject = new TotalCountObject();

			countObject.setCount(rs.getInt(rs.findColumn("count")));

			return countObject;
		}

	}

}
