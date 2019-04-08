package org.opensrp.repository;

import org.opensrp.domain.ReferralReport;
import org.opensrp.domain.ReferralReport;
import org.opensrp.dto.report.MaleFemaleCountObject;
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
public class ReferralReportRepository {


    @Autowired
    JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;


    public Long save(ReferralReport referralReport) throws Exception {

        insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ReferralReport.tbName).usingGeneratedKeyColumns(ReferralReport.COL_ID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ReferralReport.COL_ID, referralReport.getId());
        parameters.put(ReferralReport.COL_NAME, referralReport.getName());
        parameters.put(ReferralReport.COL_NOTES, referralReport.getNotes());
        parameters.put(ReferralReport.COL_URL, referralReport.getUrl());
        parameters.put(ReferralReport.COL_IS_ACTIVE, referralReport.isActive());
        parameters.put(ReferralReport.COL_CREATED_AT, referralReport.getCreatedAt());
        parameters.put(ReferralReport.COL_UPDATED_AT, referralReport.getCreatedAt());
        return insert.executeAndReturnKey(parameters).longValue();

    }

    public void executeQuery(String query) throws Exception {
        jdbcTemplate.execute(query);
    }

    public void clearTable() throws Exception {
        String query = "DELETE FROM " + ReferralReport.tbName;
        executeQuery(query);
    }


    public List<ReferralReport> getAppointmentTypes(String sql, Object[] args) throws Exception {
        return this.jdbcTemplate.query(sql, args, new RegistrationIdsRowMapper());
    }


    public class RegistrationIdsRowMapper implements RowMapper<ReferralReport> {
        public ReferralReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReferralReport referralReport = new ReferralReport();

            referralReport.setId(rs.getInt(rs.findColumn(ReferralReport.COL_ID)));
            referralReport.setName(rs.getString(rs.findColumn(ReferralReport.COL_NAME)));
            referralReport.setNotes(rs.getString(rs.findColumn(ReferralReport.COL_NOTES)));
            referralReport.setUrl(rs.getString(rs.findColumn(ReferralReport.COL_URL)));
            referralReport.setActive(rs.getBoolean(rs.findColumn(ReferralReport.COL_IS_ACTIVE)));
            referralReport.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ReferralReport.COL_CREATED_AT)).getTime()));
            referralReport.setUpdatedAt(rs.getDate(rs.findColumn(ReferralReport.COL_UPDATED_AT)));
            return referralReport;
        }

    }
}
