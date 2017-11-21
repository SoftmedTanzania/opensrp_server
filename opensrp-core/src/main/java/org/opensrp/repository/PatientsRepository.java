package org.opensrp.repository;

import org.opensrp.domain.ReferalPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

//import org.opensrp.domain.ReferalPatients;

@Repository
public class PatientsRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;

	
	public int save(ReferalPatients patients) throws Exception {
		String insertQuery = "insert into " + ReferalPatients.tbName + " (" +
				ReferalPatients.COL_PATIENT_ID + "," +
				ReferalPatients.COL_PATIENT_FIRST_NAME + "," +
				ReferalPatients.COL_PATIENT_SURNAME + "," +
				ReferalPatients.COL_CONTACTS + "," +
				ReferalPatients.COL_DATE_OF_BIRTH + "," +
				ReferalPatients.COL_GENDER + "," +
				ReferalPatients.COL_TRANSFER_IN_ID + "," +
				ReferalPatients.COL_DATE_OF_FIRST_POSITIVE_HIV_TEST + "," +
				ReferalPatients.COL_DATE_OF_CONFIRMED_HIV_POSITIVE + "," +
				ReferalPatients.COL_DATE_OF_DEATH + "," +
				ReferalPatients.COL_CREATED_AT + ") values (?,?,?,?,?,?,?,?,?,?,?) ";

		Object[] params = new Object[] {
				patients.getPatientId(),
				patients.getPatientFirstName(),
				patients.getPatientSurname(),
		        patients.getContacts(),
		        patients.getDateOfBirth(),
		        patients.getGender(),
		        patients.getTransferInId(),
		        patients.getDateOfFirstPositiveHIVTest(),
		        patients.getDateOfConfirmedHIVPositive(),
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
		String query = "DELETE FROM " + ReferalPatients.tbName;
		executeQuery(query);
	}
	

	
	public class CTC_patientsRowMapper implements RowMapper<ReferalPatients> {
		public ReferalPatients mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReferalPatients patients = new ReferalPatients();
			patients.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ReferalPatients.COL_CREATED_AT)).getTime()));
			patients.setPatientId(rs.getString(rs.findColumn(ReferalPatients.COL_PATIENT_ID)));
			patients.setPatientFirstName(rs.getString(rs.findColumn(ReferalPatients.COL_PATIENT_FIRST_NAME)));
			patients.setPatientSurname(rs.getString(rs.findColumn(ReferalPatients.COL_PATIENT_SURNAME)));
			patients.setContacts(rs.getString(rs.findColumn(ReferalPatients.COL_CONTACTS)));
			patients.setDateOfBirth(rs.getDate(rs.findColumn(ReferalPatients.COL_DATE_OF_BIRTH)));
			patients.setGender(rs.getString(rs.findColumn(ReferalPatients.COL_GENDER)));
			patients.setTransferInId(rs.getString(rs.findColumn(ReferalPatients.COL_TRANSFER_IN_ID)));
			patients.setDateOfFirstPositiveHIVTest(rs.getDate(rs.findColumn(ReferalPatients.COL_DATE_OF_FIRST_POSITIVE_HIV_TEST)));
			patients.setDateOfConfirmedHIVPositive(rs.getDate(rs.findColumn(ReferalPatients.COL_DATE_OF_CONFIRMED_HIV_POSITIVE)));
			patients.setDateOfDeath(rs.getDate(rs.findColumn(ReferalPatients.COL_DATE_OF_DEATH)));
			return patients;
		}
		
	}

}
