package org.opensrp.repository;

import org.opensrp.domain.CTC_patients;
import org.opensrp.domain.CTC_patients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

//import org.opensrp.domain.CTC_patients;

@Repository
public class PatientsRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;

	
	public int save(CTC_patients patients) throws Exception {
		String insertQuery = "insert into " + CTC_patients.tbName + " (" +
				CTC_patients.COL_PATIENT_ID + "," +
				CTC_patients.COL_PATIENT_FIRST_NAME + "," +
				CTC_patients.COL_PATIENT_SURNAME + "," +
				CTC_patients.COL_CONTACTS + "," +
				CTC_patients.COL_DATE_OF_BIRTH + "," +
				CTC_patients.COL_GENDER + "," +
				CTC_patients.COL_TRANSFER_IN_ID + "," +
				CTC_patients.COL_DATE_OF_FIRST_POSITIVE_HIV_TEST + "," +
				CTC_patients.COL_DATE_OF_CONFIRMED_HIV_POSITIVE + "," +
				CTC_patients.COL_DATE_OF_DEATH + "," +
				CTC_patients.COL_CREATED_AT + ") values (?,?,?,?,?,?,?,?,?,?,?) ";

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
		String query = "DELETE FROM " + CTC_patients.tbName;
		executeQuery(query);
	}
	

	
	public class CTC_patientsRowMapper implements RowMapper<CTC_patients> {
		public CTC_patients mapRow(ResultSet rs, int rowNum) throws SQLException {
			CTC_patients patients = new CTC_patients();
			patients.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(CTC_patients.COL_CREATED_AT)).getTime()));
			patients.setPatientId(rs.getString(rs.findColumn(CTC_patients.COL_PATIENT_ID)));
			patients.setPatientFirstName(rs.getString(rs.findColumn(CTC_patients.COL_PATIENT_FIRST_NAME)));
			patients.setPatientSurname(rs.getString(rs.findColumn(CTC_patients.COL_PATIENT_SURNAME)));
			patients.setContacts(rs.getString(rs.findColumn(CTC_patients.COL_CONTACTS)));
			patients.setDateOfBirth(rs.getDate(rs.findColumn(CTC_patients.COL_DATE_OF_BIRTH)));
			patients.setGender(rs.getString(rs.findColumn(CTC_patients.COL_GENDER)));
			patients.setTransferInId(rs.getString(rs.findColumn(CTC_patients.COL_TRANSFER_IN_ID)));
			patients.setDateOfFirstPositiveHIVTest(rs.getDate(rs.findColumn(CTC_patients.COL_DATE_OF_FIRST_POSITIVE_HIV_TEST)));
			patients.setDateOfConfirmedHIVPositive(rs.getDate(rs.findColumn(CTC_patients.COL_DATE_OF_CONFIRMED_HIV_POSITIVE)));
			patients.setDateOfDeath(rs.getDate(rs.findColumn(CTC_patients.COL_DATE_OF_DEATH)));
			return patients;
		}
		
	}

}
