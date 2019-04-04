package org.opensrp.service;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.*;

public class ReferralsReportServiceTest {


    @Test
    public void newRegistrationByReasonsReport() {
    }

    @Test
    public void  generateReferralsReportSql() {
        ReferralsReportService referralsReportService = new ReferralsReportService();

        LocalDate firstDateOfTheMonth = LocalDate.now();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = formatter.format(c.getTime());

        String sql = referralsReportService.generateReferralsReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, referralsReportService.getDateByYearString(-1), currentDate, 1,true);

        System.out.println("SQL : "+sql);
    }
}