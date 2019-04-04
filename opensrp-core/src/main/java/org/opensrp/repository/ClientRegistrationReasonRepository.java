package org.opensrp.repository;

import org.opensrp.domain.ClientRegistrationReason;
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
public class ClientRegistrationReasonRepository {


    @Autowired
    JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;


    public Long save(ClientRegistrationReason clientRegistrationReason) throws Exception {

        insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(ClientRegistrationReason.tbName).usingGeneratedKeyColumns(ClientRegistrationReason.COL_REGISTRATION__ID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ClientRegistrationReason.COL_REGISTRATION__ID, clientRegistrationReason.getRegistrationId());
        parameters.put(ClientRegistrationReason.COL_DESC_EN, clientRegistrationReason.getDescEn());
        parameters.put(ClientRegistrationReason.COL_DESC_SW, clientRegistrationReason.getDescSw());
        parameters.put(ClientRegistrationReason.COL_IS_ACTIVE, clientRegistrationReason.isActive());
        parameters.put(ClientRegistrationReason.COL_APPLICABLE_TO_MEN, clientRegistrationReason.isApplicableToMen());
        parameters.put(ClientRegistrationReason.COL_APPLICABLE_TO_WOMEN, clientRegistrationReason.isApplicableToWomen());
        parameters.put(ClientRegistrationReason.COL_CREATED_AT, clientRegistrationReason.getCreatedAt());
        parameters.put(ClientRegistrationReason.COL_UPDATED_AT, clientRegistrationReason.getCreatedAt());

        return insert.executeAndReturnKey(parameters).longValue();

    }

    public void executeQuery(String query) throws Exception {
        jdbcTemplate.execute(query);
    }

    public void clearTable() throws Exception {
        String query = "DELETE FROM " + ClientRegistrationReason.tbName;
        executeQuery(query);
    }


    public List<ClientRegistrationReason> geRegistrationReasons(String sql, Object[] args) throws Exception {
        return this.jdbcTemplate.query(sql, args, new RegistrationIdsRowMapper());
    }


    public class RegistrationIdsRowMapper implements RowMapper<ClientRegistrationReason> {
        public ClientRegistrationReason mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClientRegistrationReason clientRegistrationReason = new ClientRegistrationReason();

            clientRegistrationReason.setRegistrationId(rs.getInt(rs.findColumn(ClientRegistrationReason.COL_REGISTRATION__ID)));
            clientRegistrationReason.setDescEn(rs.getString(rs.findColumn(ClientRegistrationReason.COL_DESC_EN)));
            clientRegistrationReason.setDescSw(rs.getString(rs.findColumn(ClientRegistrationReason.COL_DESC_SW)));
            clientRegistrationReason.setActive(rs.getBoolean(rs.findColumn(ClientRegistrationReason.COL_IS_ACTIVE)));
            clientRegistrationReason.setApplicableToMen(rs.getBoolean(rs.findColumn(ClientRegistrationReason.COL_APPLICABLE_TO_MEN)));
            clientRegistrationReason.setApplicableToWomen(rs.getBoolean(rs.findColumn(ClientRegistrationReason.COL_APPLICABLE_TO_WOMEN)));
            clientRegistrationReason.setCreatedAt(new Date(rs.getTimestamp(rs.findColumn(ClientRegistrationReason.COL_CREATED_AT)).getTime()));
            clientRegistrationReason.setUpdatedAt(rs.getDate(rs.findColumn(ClientRegistrationReason.COL_UPDATED_AT)));
            return clientRegistrationReason;
        }

    }

}
