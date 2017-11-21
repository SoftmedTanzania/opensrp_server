package org.opensrp.repository;

import org.opensrp.domain.ReferralPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

//import org.opensrp.domain.ReferralPatients;

@Repository
public class PatientsRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;

	
	public int save(ReferralPatients patients) throws Exception {
		String insertQuery = "insert into " + ReferralPatients.tbName + " (" +
				ReferralPatients.COL_PATIENT_ID + "," +
				ReferralPatients.COL_PATIENT_FIRST_NAME + "," +
				ReferralPatients.COL_PATIENT_SURNAME + "," +
				ReferralPatients.COL_CONTACTS + "," +
				ReferralPatients.COL_DATE_OF_BIRTH + "," +
				ReferralPatients.COL_GENDER + "," +
				ReferralPatients.COL_DATE_OF_DEATH + "," +
				ReferralPatients.COL_CREATED_AT + ") values (?,?,?,?,?,?,?,?,?,?,?) ";

		Object[] params = new Object[] {
				patients.getPatientId(),
				patients.getPatientFirstName(),
				patients.getPatientSurname(),
		        patients.getContacts(),
		        patients.getDateOfBirth(),
		        patients.getGender(),
		        patients.getDateOfDeath(),
				patients.getCreatedAt() };
		int[] types = new int[] {
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.DATE,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.DATE,
				Types.DATE,
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
		String query = "DELETE FROM " + ReferralPatients.tbName;
		executeQuery(query);
	}
	

	
	public class CTC_patientsRowMapper implements RowMapper<ReferralPatients> {
		public ReferralPatients mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReferralPatients patients = new ReferralPatients();
			patients.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ReferralPatients.COL_CREATED_AT)).getTime()));
			patients.setPatientId(rs.getString(rs.findColumn(ReferralPatients.COL_PATIENT_ID)));
			patients.setPatientFirstName(rs.getString(rs.findColumn(ReferralPatients.COL_PATIENT_FIRST_NAME)));
			patients.setPatientSurname(rs.getString(rs.findColumn(ReferralPatients.COL_PATIENT_SURNAME)));
			patients.setContacts(rs.getString(rs.findColumn(ReferralPatients.COL_CONTACTS)));
			patients.setDateOfBirth(rs.getDate(rs.findColumn(ReferralPatients.COL_DATE_OF_BIRTH)));
			patients.setGender(rs.getString(rs.findColumn(ReferralPatients.COL_GENDER)));
			patients.setDateOfDeath(rs.getDate(rs.findColumn(ReferralPatients.COL_DATE_OF_DEATH)));
			return patients;
		}
		
	}

}
