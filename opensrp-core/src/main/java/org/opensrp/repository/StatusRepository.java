package org.opensrp.repository;

import org.opensrp.domain.Status;
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
public class StatusRepository {


    @Autowired
    JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;


    public Long save(Status status) throws Exception {

        insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(Status.tbName).usingGeneratedKeyColumns(Status.COL_STATUS_ID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Status.COL_STATUS_ID, status.getStatusId());
        parameters.put(Status.COL_NAME, status.getName());
        parameters.put(Status.COL_NOTES, status.getNotes());
        parameters.put(Status.COL_IS_ACTIVE, status.isActive());
        parameters.put(Status.COL_CREATED_AT, status.getCreatedAt());
        parameters.put(Status.COL_UPDATED_AT, status.getCreatedAt());

        return insert.executeAndReturnKey(parameters).longValue();

    }

    public void executeQuery(String query) throws Exception {
        jdbcTemplate.execute(query);
    }

    public void clearTable() throws Exception {
        String query = "DELETE FROM " + Status.tbName;
        executeQuery(query);
    }


    public List<Status> geRegistrationReasons(String sql, Object[] args) throws Exception {
        return this.jdbcTemplate.query(sql, args, new RegistrationIdsRowMapper());
    }


    public class RegistrationIdsRowMapper implements RowMapper<Status> {
        public Status mapRow(ResultSet rs, int rowNum) throws SQLException {
            Status appointmentType = new Status();

            appointmentType.setStatusId(rs.getInt(rs.findColumn(Status.COL_STATUS_ID)));
            appointmentType.setName(rs.getString(rs.findColumn(Status.COL_NAME)));
            appointmentType.setNotes(rs.getString(rs.findColumn(Status.COL_NOTES)));
            appointmentType.setActive(rs.getBoolean(rs.findColumn(Status.COL_IS_ACTIVE)));
            appointmentType.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(Status.COL_CREATED_AT)).getTime()));
            appointmentType.setUpdatedAt(rs.getDate(rs.findColumn(Status.COL_UPDATED_AT)));
            return appointmentType;
        }

    }

}
