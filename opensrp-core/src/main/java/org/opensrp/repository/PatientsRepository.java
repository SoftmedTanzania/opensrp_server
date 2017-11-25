package org.opensrp.repository;

import org.opensrp.domain.Patients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

//import org.opensrp.domain.Patients;

@Repository
public class PatientsRepository {


	@Autowired
	JdbcTemplate jdbcTemplate;


	public int save(Patients patients) throws Exception {
		String insertQuery = "insert into " + Patients.tbName + " (" +
				Patients.COL_PATIENT_ID + "," +
				Patients.COL_PATIENT_FIRST_NAME + "," +
				Patients.COL_PATIENT_SURNAME + "," +
				Patients.COL_PHONE_NUMBER + "," +
				Patients.COL_DATE_OF_BIRTH + "," +
				Patients.COL_GENDER + "," +
				Patients.COL_DATE_OF_DEATH + "," +
				Patients.COL_CREATED_AT + ") values (?,?,?,?,?,?,?,?,?,?,?) ";

		Object[] params = new Object[]{
				patients.getPatientId(),
				patients.getPatientFirstName(),
				patients.getPatientSurname(),
				patients.getPhone_number(),
				patients.getDateOfBirth(),
				patients.getGender(),
				patients.getDateOfDeath(),
				patients.getCreatedAt()};
		int[] types = new int[]{
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
				Types.TIMESTAMP};

		return jdbcTemplate.update(insertQuery, params, types);

	}

	public void executeQuery(String query) throws Exception {
		jdbcTemplate.execute(query);
	}

	public int checkIfExists(String query, String[] args) throws Exception {
		return this.jdbcTemplate.queryForObject(query, args, Integer.class);

	}

	public void clearTable() throws Exception {
		String query = "DELETE FROM " + Patients.tbName;
		executeQuery(query);
	}


	public List<Patients> getPatientReferrals(String sql, String[] args) throws Exception {
		return this.jdbcTemplate.query(sql, args, new referralPatientsRowMapper());
	}


	public class referralPatientsRowMapper implements RowMapper<Patients> {
		public Patients mapRow(ResultSet rs, int rowNum) throws SQLException {
			Patients patients = new Patients();
			patients.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(Patients.COL_CREATED_AT)).getTime()));
			patients.setPatientId(rs.getString(rs.findColumn(Patients.COL_PATIENT_ID)));
			patients.setPatientFirstName(rs.getString(rs.findColumn(Patients.COL_PATIENT_FIRST_NAME)));
			patients.setPatientSurname(rs.getString(rs.findColumn(Patients.COL_PATIENT_SURNAME)));
			patients.setPhone_number(rs.getString(rs.findColumn(Patients.COL_PHONE_NUMBER)));
			patients.setDateOfBirth(rs.getDate(rs.findColumn(Patients.COL_DATE_OF_BIRTH)));
			patients.setGender(rs.getString(rs.findColumn(Patients.COL_GENDER)));
			patients.setDateOfDeath(rs.getDate(rs.findColumn(Patients.COL_DATE_OF_DEATH)));
			return patients;
		}

	}

}
