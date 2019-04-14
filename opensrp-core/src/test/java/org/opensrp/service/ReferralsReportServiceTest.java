package org.opensrp.service;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.*;

public class ReferralsReportServiceTest {
    LocalDate firstDateOfTheMonth = LocalDate.now();
    String currentDate ="";
    ReferralsReportService referralsReportService = new ReferralsReportService();

    public ReferralsReportServiceTest() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        currentDate = formatter.format(c.getTime());
    }

    @Test
    public void newRegistrationByReasonsReport() {
    }

    @Test
    public void  generateReferralsReportSql() {
        String sql = referralsReportService.generateReferralsReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, referralsReportService.getDateByYearString(-1), currentDate, 1,true,"100");

        System.out.println("SQL : "+sql);
    }

    @Test
    public void getLTFCountsReportSQL() {
        String sql = referralsReportService.getLTFCountsReportSQL(1,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate,null);

        System.out.println("SQL : "+sql);
    }
}