package org.opensrp.repository;

import org.opensrp.domain.AppointmentType;
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
public class AppointmentTypeRepository {


    @Autowired
    JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;


    public Long save(AppointmentType appointmentType) throws Exception {

        insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(AppointmentType.tbName).usingGeneratedKeyColumns(AppointmentType.COL_ID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(AppointmentType.COL_ID, appointmentType.getId());
        parameters.put(AppointmentType.COL_NAME, appointmentType.getName());
        parameters.put(AppointmentType.COL_NOTES, appointmentType.getNotes());
        parameters.put(AppointmentType.COL_IS_ACTIVE, appointmentType.isActive());
        parameters.put(AppointmentType.COL_CREATED_AT, appointmentType.getCreatedAt());
        parameters.put(AppointmentType.COL_UPDATED_AT, appointmentType.getCreatedAt());

        return insert.executeAndReturnKey(parameters).longValue();

    }

    public void executeQuery(String query) throws Exception {
        jdbcTemplate.execute(query);
    }

    public void clearTable() throws Exception {
        String query = "DELETE FROM " + AppointmentType.tbName;
        executeQuery(query);
    }


    public List<AppointmentType> geRegistrationReasons(String sql, Object[] args) throws Exception {
        return this.jdbcTemplate.query(sql, args, new RegistrationIdsRowMapper());
    }


    public class RegistrationIdsRowMapper implements RowMapper<AppointmentType> {
        public AppointmentType mapRow(ResultSet rs, int rowNum) throws SQLException {
            AppointmentType appointmentType = new AppointmentType();

            appointmentType.setId(rs.getInt(rs.findColumn(AppointmentType.COL_ID)));
            appointmentType.setName(rs.getString(rs.findColumn(AppointmentType.COL_NAME)));
            appointmentType.setNotes(rs.getString(rs.findColumn(AppointmentType.COL_NOTES)));
            appointmentType.setActive(rs.getBoolean(rs.findColumn(AppointmentType.COL_IS_ACTIVE)));
            appointmentType.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(AppointmentType.COL_CREATED_AT)).getTime()));
            appointmentType.setUpdatedAt(rs.getDate(rs.findColumn(AppointmentType.COL_UPDATED_AT)));
            return appointmentType;
        }

    }

}
