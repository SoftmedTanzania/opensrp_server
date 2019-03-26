package org.opensrp.web.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.connector.openmrs.service.OpenmrsReportingService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.*;
import org.opensrp.dto.*;
import org.opensrp.dto.report.MaleFemaleCountObject;
import org.opensrp.dto.report.TotalCountObject;
import org.opensrp.repository.ClientReferralRepository;
import org.opensrp.repository.ClientsAppointmentsRepository;
import org.opensrp.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ReportController {

    private OpenmrsReportingService reportService;
    private ClientReferralRepository clientReferralRepository;
    private ClientsRepository clientsRepository;
    private ClientsAppointmentsRepository clientsAppointmentsRepository;
    private OpenmrsUserService openmrsUserService;

    @Autowired
    public ReportController(OpenmrsReportingService reportService, ClientReferralRepository clientReferralRepository, ClientsRepository clientsRepository, OpenmrsUserService openmrsUserService,ClientsAppointmentsRepository clientsAppointmentsRepository) {
        this.reportService = reportService;
        this.openmrsUserService = openmrsUserService;
        this.clientReferralRepository = clientReferralRepository;
        this.clientsRepository = clientsRepository;
        this.clientsAppointmentsRepository = clientsAppointmentsRepository;
    }

    @RequestMapping(method = GET, value = "/report/report-definitions")
    public ResponseEntity<String> reportDefinitions() throws JSONException {
        return new ResponseEntity<>(reportService.getReportDefinitions().toString(), HttpStatus.OK);
    }

    @RequestMapping(method = GET, value = "/report/report-data")
    public ResponseEntity<String> reportData(@RequestParam("uuid") String uuid, @RequestParam Map<String, String> params) throws JSONException {
        return new ResponseEntity<>(reportService.getReportData(uuid, params).toString(), HttpStatus.OK);
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/report/get-chw-referrals-summary")
    @ResponseBody
    public ResponseEntity<List<CHWReferralsSummaryDTO>> getCHWReferralsSummaryReport(@RequestBody String json) {
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray array = null;
        try {
            array = object.getJSONArray("chw_uuid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Default dates if the date range is not passed
        String fromDate = "2017-01-01";
        String toDate = "2020-01-01";
        try {
            toDate = object.getString("to_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            fromDate = object.getString("from_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int size = array.length();
        String chwUIIDs = "";
        for (int i = 0; i < size; i++) {
            try {
                chwUIIDs += "'" + array.getString(i) + "',";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (chwUIIDs.length() > 0 && chwUIIDs.charAt(chwUIIDs.length() - 1) == ',') {
            chwUIIDs = chwUIIDs.substring(0, chwUIIDs.length() - 1);
        }

        try {
            List<CHWReferralsSummaryDTO> chwReferralsSummaryDTOS = clientReferralRepository.getCHWReferralsSummary(
                    "SELECT COUNT(" + ClientReferrals.tbName + "." + ClientReferrals.COL_SERVICE_ID + ") as count ," + ReferralService.COL_SERVICE_NAME + " as service_name FROM " + ClientReferrals.tbName +
                            " INNER JOIN " + ReferralService.tbName + " ON " + ClientReferrals.tbName + "." + ClientReferrals.COL_SERVICE_ID + " = " + ReferralService.tbName + "." + ReferralService.COL_SERVICE_ID +
                            " WHERE " + ClientReferrals.COL_REFERRAL_TYPE + "=1 AND " +
                            ClientReferrals.COL_SERVICE_PROVIDER_UIID + " IN (" + chwUIIDs + ") AND " +
                            ClientReferrals.COL_REFERRAL_DATE + " > '" + fromDate + "' AND " +
                            ClientReferrals.COL_REFERRAL_DATE + " <= '" + toDate + "' " +
                            " GROUP BY " + ReferralService.COL_SERVICE_NAME, null);


            return new ResponseEntity<List<CHWReferralsSummaryDTO>>(chwReferralsSummaryDTOS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<CHWReferralsSummaryDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO clear this report, only used for testing jasper server reports
    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/report/get-all-chw-referrals-summary")
    @ResponseBody
    public ResponseEntity<List<CHWReferralsSummaryDTO>> getAllCHWReferralsSummaryReport() {

        //Default dates if the date range is not passed
        String fromDate = "2017-01-01";
        String toDate = "2020-01-01";

        try {
            List<CHWReferralsSummaryDTO> chwReferralsSummaryDTOS = clientReferralRepository.getCHWReferralsSummary(
                    "SELECT COUNT(" + ClientReferrals.tbName + "." + ClientReferrals.COL_SERVICE_ID + ") as count ," + ReferralService.COL_SERVICE_NAME + " as service_name FROM " + ClientReferrals.tbName +
                            " INNER JOIN " + ReferralService.tbName + " ON " + ClientReferrals.tbName + "." + ClientReferrals.COL_SERVICE_ID + " = " + ReferralService.tbName + "." + ReferralService.COL_SERVICE_ID +
                            " WHERE " + ClientReferrals.COL_REFERRAL_TYPE + "=1 AND " +
//							ClientReferrals.COL_SERVICE_PROVIDER_UIID+" IN ("+chwUIIDs+") AND "+
                            ClientReferrals.COL_REFERRAL_DATE + " > '" + fromDate + "' AND " +
                            ClientReferrals.COL_REFERRAL_DATE + " <= '" + toDate + "' " +
                            " GROUP BY " + ReferralService.COL_SERVICE_NAME, null);


            return new ResponseEntity<List<CHWReferralsSummaryDTO>>(chwReferralsSummaryDTOS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<CHWReferralsSummaryDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/report/get-intra-facility-departments-referrals-summary")
    @ResponseBody
    public ResponseEntity<List<FacilityReferralsSummaryDTO>> getIntraFacilityDepartmentsReferralsSummaryReport(@RequestBody String json) {
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray array = null;
        try {
            array = object.getJSONArray("facilities");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String toDate = "2020-01-01";
        String fromDate = "2017-01-01";
        try {
            toDate = object.getString("to_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            fromDate = object.getString("from_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<FacilityReferralsSummaryDTO> referralsSummaryDTOS = new ArrayList<>();
        int size = array.length();
        for (int i = 0; i < size; i++) {
            try {

                String facilityName = array.getJSONObject(i).getString("facility_name");
                String facilityId = array.getJSONObject(i).getString("facility_id");

                List<FacilityDepartmentReferralSummaryDTO> facilityReferralsSummaryDTOS = clientReferralRepository.getFacilityDepartmentReferralsSummary(
                        "SELECT COUNT(" + ClientReferrals.tbName + "." + ClientReferrals.COL_SERVICE_ID + ") as count, " +
                                ClientReferrals.COL_REFERRAL_SOURCE + " as referral_source, " +
                                ClientReferrals.COL_REFERRAL_STATUS + " as referral_status " +
                                " FROM " + ClientReferrals.tbName +
                                " INNER JOIN " + HealthFacilities.tbName + " ON " + ClientReferrals.tbName + "." + ClientReferrals.COL_FROM_FACILITY_ID + " = " + HealthFacilities.tbName + "." + HealthFacilities.COL_OPENMRS_UUID +
                                " WHERE " + ClientReferrals.COL_REFERRAL_TYPE + "=2 AND " +
                                HealthFacilities.COL_OPENMRS_UUID + " = '" + facilityId + "' AND " +
                                ClientReferrals.COL_REFERRAL_DATE + " > '" + fromDate + "' AND " +
                                ClientReferrals.COL_REFERRAL_DATE + " <= '" + toDate + "' " +
                                " GROUP BY " + ClientReferrals.COL_REFERRAL_SOURCE + " , " + ClientReferrals.COL_REFERRAL_STATUS, null);


                FacilityReferralsSummaryDTO facilityReferralsSummaryDTO = new FacilityReferralsSummaryDTO();

                facilityReferralsSummaryDTO.setFacilityName(facilityName);
                facilityReferralsSummaryDTO.setDepartmentReferralSummaryDTOList(facilityReferralsSummaryDTOS);

                referralsSummaryDTOS.add(facilityReferralsSummaryDTO);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return new ResponseEntity<List<FacilityReferralsSummaryDTO>>(referralsSummaryDTOS, HttpStatus.OK);


    }


    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/report/get-intra-facility-providers-referrals-summary")
    @ResponseBody
    public ResponseEntity<List<IntraFacilityReferralsProvidersSummaryReport>> getIntraFacilityProvidersReferralsSummaryReport(@RequestBody String json) {
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray facilitiesArray = null;
        try {
            facilitiesArray = object.getJSONArray("facilities");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String toDate = "2020-01-01";
        String fromDate = "2017-01-01";
        try {
            toDate = object.getString("to_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            fromDate = object.getString("from_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<IntraFacilityReferralsProvidersSummaryReport> referralsSummaryDTOS = new ArrayList<>();
        for (int i = 0; i < facilitiesArray.length(); i++) {
            try {
                String facilityName = facilitiesArray.getJSONObject(i).getString("facility_name");
                String facilityId = facilitiesArray.getJSONObject(i).getString("facility_id");

                List<FacilityProvidersReferralSummaryDTO> facilityProvidersReferralsSummaries = clientReferralRepository.getFacilityProvidersReferralsSummary(
                        "SELECT COUNT(" + ClientReferrals.tbName + "." + ClientReferrals.COL_SERVICE_ID + ") as count, " +
                                ClientReferrals.COL_REFERRAL_SOURCE + " as referral_source, " +
                                ClientReferrals.COL_REFERRAL_STATUS + " as referral_status, " +
                                ClientReferrals.COL_SERVICE_PROVIDER_UIID + " as provider_uuid " +
                                " FROM " + ClientReferrals.tbName +
                                " INNER JOIN " + HealthFacilities.tbName + " ON " + ClientReferrals.tbName + "." + ClientReferrals.COL_FROM_FACILITY_ID + " = " + HealthFacilities.tbName + "." + HealthFacilities.COL_OPENMRS_UUID +
                                " WHERE " + ClientReferrals.COL_REFERRAL_TYPE + "=2 AND " +
                                HealthFacilities.COL_OPENMRS_UUID + " = '" + facilityId + "' AND " +
                                ClientReferrals.COL_REFERRAL_DATE + " > '" + fromDate + "' AND " +
                                ClientReferrals.COL_REFERRAL_DATE + " <= '" + toDate + "' " +
                                " GROUP BY " + ClientReferrals.COL_REFERRAL_SOURCE + " , " + ClientReferrals.COL_REFERRAL_STATUS + "," + ClientReferrals.COL_SERVICE_PROVIDER_UIID, null);

                for (FacilityProvidersReferralSummaryDTO facilityProvidersReferralsSummary : facilityProvidersReferralsSummaries) {
                    facilityProvidersReferralsSummary.setProviderName(openmrsUserService.getTeamMember(facilityProvidersReferralsSummary.getProviderUuid()).getString("display"));
                }

                IntraFacilityReferralsProvidersSummaryReport facilityReferralsSummaryDTO = new IntraFacilityReferralsProvidersSummaryReport();

                facilityReferralsSummaryDTO.setFacilityName(facilityName);
                facilityReferralsSummaryDTO.setSummaryDTOS(facilityProvidersReferralsSummaries);

                referralsSummaryDTOS.add(facilityReferralsSummaryDTO);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<List<IntraFacilityReferralsProvidersSummaryReport>>(referralsSummaryDTOS, HttpStatus.OK);
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/report/get-inter-facility-referrals-summary")
    @ResponseBody
    public ResponseEntity<List<InterFacilityReferralsSummaryReport>> getInterFacilityReferralsSummaryReport(@RequestBody String json) {
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray facilitiesArray = null;
        try {
            facilitiesArray = object.getJSONArray("facilities");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String toDate = "2020-01-01";
        String fromDate = "2017-01-01";
        try {
            toDate = object.getString("to_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            fromDate = object.getString("from_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<InterFacilityReferralsSummaryReport> referralsSummaryDTOS = new ArrayList<>();
        for (int i = 0; i < facilitiesArray.length(); i++) {
            try {
                String facilityName = facilitiesArray.getJSONObject(i).getString("facility_name");
                String facilityId = facilitiesArray.getJSONObject(i).getString("facility_id");

                List<InterFacilityReferralSummaryDTO> facilityProvidersReferralsSummaries = clientReferralRepository.getInterFacilityReferralsSummary(
                        "SELECT COUNT(" + ClientReferrals.tbName + "." + ClientReferrals.COL_REFERRAL_ID + ") as count, " +
                                ClientReferrals.COL_REFERRAL_STATUS + " as referral_status, " +
                                "H2." + HealthFacilities.COL_FACILITY_NAME + " as to_facility_name " +
                                " FROM " + ClientReferrals.tbName +
                                " INNER JOIN " + HealthFacilities.tbName + " H1 ON " + ClientReferrals.tbName + "." + ClientReferrals.COL_FROM_FACILITY_ID + " = H1." + HealthFacilities.COL_OPENMRS_UUID +
                                " INNER JOIN " + HealthFacilities.tbName + " H2 ON " + ClientReferrals.tbName + "." + ClientReferrals.COL_FACILITY_ID + " = H2." + HealthFacilities.COL_OPENMRS_UUID +
                                " WHERE " + ClientReferrals.COL_REFERRAL_TYPE + "= 3 AND " +
                                "H1." + HealthFacilities.COL_OPENMRS_UUID + " = '" + facilityId + "' AND " +
                                ClientReferrals.COL_REFERRAL_DATE + " > '" + fromDate + "' AND " +
                                ClientReferrals.COL_REFERRAL_DATE + " <= '" + toDate + "' " +
                                " GROUP BY H2." + HealthFacilities.COL_FACILITY_NAME + " , " + ClientReferrals.COL_REFERRAL_STATUS, null);

                InterFacilityReferralsSummaryReport interFacilityReferralsSummaryReport = new InterFacilityReferralsSummaryReport();

                interFacilityReferralsSummaryReport.setFacilityName(facilityName);
                interFacilityReferralsSummaryReport.setSummaryDTOS(facilityProvidersReferralsSummaries);

                referralsSummaryDTOS.add(interFacilityReferralsSummaryReport);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<List<InterFacilityReferralsSummaryReport>>(referralsSummaryDTOS, HttpStatus.OK);
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/report/referrals-summary")
    @ResponseBody
    public ResponseEntity<JSONObject> getReferralsSummaryReport() {

        //Obtain registrations before end of last month
        LocalDate firstDateOfTheMonth = LocalDate.now();
        JSONObject referralSummaryReport = new JSONObject();

        System.out.println("Months first date in yyyy-mm-dd: " + firstDateOfTheMonth.withDayOfMonth(1));
        String previousMonthRegistrations = generateRegistationReportSql("1970-01-01", firstDateOfTheMonth.withDayOfMonth(1).toString(), "", "",0);
        List<MaleFemaleCountObject> accumulativeTotalPreviousMonthsRegistrations= null;
        try {
            accumulativeTotalPreviousMonthsRegistrations = clientsRepository.getMaleFemaleCountReports(previousMonthRegistrations,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            referralSummaryReport.put("cumulativeRegistrations",new JSONObject(new Gson().toJson(accumulativeTotalPreviousMonthsRegistrations)));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = formatter.format(c.getTime());


        JSONObject thisMonthRegistrationsObject = new JSONObject();

        String thisMonthRegistrations = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, "", "",0);

        List<MaleFemaleCountObject> thisMonthRegistrationsList= null;
        try {
            thisMonthRegistrationsList = clientsRepository.getMaleFemaleCountReports(thisMonthRegistrations,null);
            thisMonthRegistrationsObject.put("JUMLA",new JSONObject(new Gson().toJson(thisMonthRegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("reports this month registrations total = "+new Gson().toJson(thisMonthRegistrationsList));


        //Less than a year old
        Calendar aYearAgo = Calendar.getInstance();
        aYearAgo.add(Calendar.YEAR, -1);
        String ayearAgoDateString = formatter.format(aYearAgo.getTime());
        String lessThan1year = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, ayearAgoDateString, currentDate,0);

        List<MaleFemaleCountObject> lessThan1yearRegistrationsList= null;
        try {
            lessThan1yearRegistrationsList = clientsRepository.getMaleFemaleCountReports(lessThan1year,null);
            thisMonthRegistrationsObject.put("CHINI YA MWAKA MMOJA",new JSONObject(new Gson().toJson(lessThan1yearRegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //1-5 Years
        Calendar fiveYearsAgo = Calendar.getInstance();
        fiveYearsAgo.add(Calendar.YEAR, -5);
        String fiveYearsAgoDateString = formatter.format(fiveYearsAgo.getTime());
        String _1to5 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, fiveYearsAgoDateString, ayearAgoDateString,0);

        List<MaleFemaleCountObject> _1to5RegistrationsList= null;
        try {
            _1to5RegistrationsList = clientsRepository.getMaleFemaleCountReports(_1to5,null);
            thisMonthRegistrationsObject.put("MIAKA1-5",new JSONObject(new Gson().toJson(_1to5RegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //6 to 9 years
        Calendar nineYearsAgo = Calendar.getInstance();
        nineYearsAgo.add(Calendar.YEAR, -9);
        String nineYearsAgoDateString = formatter.format(nineYearsAgo.getTime());
        String _6to9 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, nineYearsAgoDateString, fiveYearsAgoDateString,0);

        List<MaleFemaleCountObject> _6to9RegistrationsList= null;
        try {
            _6to9RegistrationsList = clientsRepository.getMaleFemaleCountReports(_6to9,null);
            thisMonthRegistrationsObject.put("MIAKA 6-9",new JSONObject(new Gson().toJson(_6to9RegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //10 to 14 years
        Calendar fourteenYearsAgo = Calendar.getInstance();
        fourteenYearsAgo.add(Calendar.YEAR, -14);
        String fourteenYearsAgoAgoDateString = formatter.format(fourteenYearsAgo.getTime());
        String _10To14 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, fourteenYearsAgoAgoDateString, nineYearsAgoDateString,0);

        List<MaleFemaleCountObject> _10To14RegistrationsList= null;
        try {
            _10To14RegistrationsList = clientsRepository.getMaleFemaleCountReports(_10To14,null);
            thisMonthRegistrationsObject.put("MIAKA 10-14",new JSONObject(new Gson().toJson(_10To14RegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }



        //15 to 19 years
        Calendar nineteenYearsAgo = Calendar.getInstance();
        nineteenYearsAgo.add(Calendar.YEAR, -19);
        String nineteenYearsAgoDateString = formatter.format(nineteenYearsAgo.getTime());
        String _15To19 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, nineteenYearsAgoDateString, fourteenYearsAgoAgoDateString,0);

        List<MaleFemaleCountObject> _15To19RegistrationsList= null;
        try {
            _15To19RegistrationsList = clientsRepository.getMaleFemaleCountReports(_15To19,null);
            thisMonthRegistrationsObject.put("MIAKA 15-19",new JSONObject(new Gson().toJson(_15To19RegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //20 to 24 years
        Calendar twentyFourYearsAgo = Calendar.getInstance();
        twentyFourYearsAgo.add(Calendar.YEAR, -24);
        String twentyFourYearsAgoDateString = formatter.format(twentyFourYearsAgo.getTime());
        String _20To24 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, twentyFourYearsAgoDateString, nineteenYearsAgoDateString,0);

        List<MaleFemaleCountObject> _20To24RegistrationsList= null;
        try {
            _20To24RegistrationsList = clientsRepository.getMaleFemaleCountReports(_20To24,null);
            thisMonthRegistrationsObject.put("MIAKA 20-24",new JSONObject(new Gson().toJson(_20To24RegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }



        //25 to 49 years
        Calendar fortyNineYearsAgo = Calendar.getInstance();
        fortyNineYearsAgo.add(Calendar.YEAR, -49);
        String fortyNineYearsAgosAgoDateString = formatter.format(fortyNineYearsAgo.getTime());
        String _25To49 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, fortyNineYearsAgosAgoDateString, twentyFourYearsAgoDateString,0);

        List<MaleFemaleCountObject> _25To49RegistrationsList= null;
        try {
            _25To49RegistrationsList = clientsRepository.getMaleFemaleCountReports(_25To49,null);
            thisMonthRegistrationsObject.put("MIAKA 25-49",new JSONObject(new Gson().toJson(_25To49RegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }



        //50 to 59 years
        Calendar fiftyNineYearsAgo = Calendar.getInstance();
        fiftyNineYearsAgo.add(Calendar.YEAR, -59);
        String fiftyNineYearsAgoDateString = formatter.format(fiftyNineYearsAgo.getTime());
        String _50To59 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, fiftyNineYearsAgoDateString, fortyNineYearsAgosAgoDateString,0);

        List<MaleFemaleCountObject> _50To59RegistrationsList= null;
        try {
            _50To59RegistrationsList = clientsRepository.getMaleFemaleCountReports(_50To59,null);
            thisMonthRegistrationsObject.put("MIAKA 50-59",new JSONObject(new Gson().toJson(_50To59RegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }



        String _60Above = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, "", fiftyNineYearsAgoDateString,0);

        List<MaleFemaleCountObject> _60AboveRegistrationsList= null;
        try {
            _60AboveRegistrationsList = clientsRepository.getMaleFemaleCountReports(_60Above,null);
            thisMonthRegistrationsObject.put("MIAKA 60+",new JSONObject(new Gson().toJson(_60AboveRegistrationsList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            referralSummaryReport.put("thisMonthRegistrations",thisMonthRegistrationsObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String totalLTFsReferrals = getLTFCountsReportSQL(0,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);

        JSONObject ltfReferrals = new JSONObject();
        List<TotalCountObject> totalObjects = null;
        try {
            totalObjects = clientsAppointmentsRepository.getCount(totalLTFsReferrals,null);
            ltfReferrals.put("Jumla",new JSONObject(new Gson().toJson(totalObjects.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ltfReferrals.put("Jumla",0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }


        List<TotalCountObject> ctcObjects = null;
        try {
            String ctcLTFsReferrals = getLTFCountsReportSQL(1,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
            ctcObjects = clientsAppointmentsRepository.getCount(ctcLTFsReferrals,null);
            ltfReferrals.put("Kliniki ya Tiba na Matunzo (CTC)",new JSONObject(new Gson().toJson(ctcObjects.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ltfReferrals.put("Kliniki ya Tiba na Matunzo (CTC)",0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        List<TotalCountObject> pmtct = null;
        try {
            String pmtctLTFsReferrals = getLTFCountsReportSQL(2,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
            pmtct = clientsAppointmentsRepository.getCount(pmtctLTFsReferrals,null);
            ltfReferrals.put("PMTCT",new JSONObject(new Gson().toJson(pmtct.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ltfReferrals.put("PMTCT",0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        List<TotalCountObject> tb = null;
        try {
            String tbLTFsReferrals = getLTFCountsReportSQL(2,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
            tb = clientsAppointmentsRepository.getCount(tbLTFsReferrals,null);
            ltfReferrals.put("TB",new JSONObject(new Gson().toJson(tb.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ltfReferrals.put("PMTCT",0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        List<TotalCountObject> drugClinic = null;
        try {
            String drugClinicLTFsReferrals = getLTFCountsReportSQL(2,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
            drugClinic = clientsAppointmentsRepository.getCount(drugClinicLTFsReferrals,null);
            ltfReferrals.put("Drug",new JSONObject(new Gson().toJson(drugClinic.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ltfReferrals.put("Drug",0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        try {
            referralSummaryReport.put("Total LTF",ltfReferrals);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JSONObject referralResults = new JSONObject();

        String totalFound = getLTFFollowupReportSQL(0,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
        List<TotalCountObject> totalFoundList = null;
        try {
            totalFoundList = clientReferralRepository.getCount(totalFound,null);
            referralResults.put("Jumla ya waliopatikana",new JSONObject(new Gson().toJson(totalFoundList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }


        String returningBackToClinick = getLTFFollowupReportSQL(1,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);

        List<TotalCountObject> returningBackToClinicList = null;
        try {
            returningBackToClinicList = clientReferralRepository.getCount(returningBackToClinick,null);
            referralResults.put("Mteja anahudhuria kliniki",new JSONObject(new Gson().toJson(returningBackToClinicList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }


        String foundAndReadyToResumeTreatment = getLTFFollowupReportSQL(2,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
        List<TotalCountObject> foundAndReadyToResumeTreatmentList = null;
        try {
            foundAndReadyToResumeTreatmentList = clientReferralRepository.getCount(foundAndReadyToResumeTreatment,null);
            referralResults.put("Amepatikana na yupo tayari kurudi kliniki",new JSONObject(new Gson().toJson(foundAndReadyToResumeTreatmentList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }



        String foundAndReturnedToTreatment = getLTFFollowupReportSQL(3,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
        List<TotalCountObject> foundAndReturnedToTreatmentList = null;
        try {
            foundAndReturnedToTreatmentList = clientReferralRepository.getCount(foundAndReturnedToTreatment,null);
            referralResults.put("Amepatikana na amerudi kliniki",new JSONObject(new Gson().toJson(foundAndReturnedToTreatmentList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dead = getLTFFollowupReportSQL(4,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
        List<TotalCountObject> deadList = null;
        try {
            deadList = clientReferralRepository.getCount(dead,null);
            referralResults.put("Amefariki",new JSONObject(new Gson().toJson(deadList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String movedToAnotherFacilityWithoutInfo = getLTFFollowupReportSQL(5,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
        List<TotalCountObject> movedToAnotherFacilityWithoutInfoList = null;
        try {
            movedToAnotherFacilityWithoutInfoList = clientReferralRepository.getCount(movedToAnotherFacilityWithoutInfo,null);
            referralResults.put("Amehamia kituo kingine bila taarifa",new JSONObject(new Gson().toJson(movedToAnotherFacilityWithoutInfoList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String shiftedThePlaceOfDemicile = getLTFFollowupReportSQL(6,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
        List<TotalCountObject> shiftedThePlaceOfDemicileList = null;
        try {
            shiftedThePlaceOfDemicileList = clientReferralRepository.getCount(shiftedThePlaceOfDemicile,null);
            referralResults.put("Amehama makazi",new JSONObject(new Gson().toJson(shiftedThePlaceOfDemicileList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String foundAndNotReadyToReturnToTreatment = getLTFFollowupReportSQL(7,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
        List<TotalCountObject> foundAndNotReadyToReturnToTreatmentList = null;
        try {
            foundAndNotReadyToReturnToTreatmentList = clientReferralRepository.getCount(foundAndNotReadyToReturnToTreatment,null);
            referralResults.put("Amepatikana lakini hayupo tayari kurudi klinik",new JSONObject(new Gson().toJson(foundAndNotReadyToReturnToTreatmentList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String notFound = getLTFFollowupReportSQL(8,firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
        List<TotalCountObject> notFoundList = null;
        try {
            notFoundList = clientReferralRepository.getCount(notFound,null);
            referralResults.put("Hajapatikana",new JSONObject(new Gson().toJson(notFoundList.get(0))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            referralSummaryReport.put("Referral Results",notFoundList);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return new ResponseEntity<JSONObject>(referralSummaryReport,HttpStatus.OK);
    }

    private String generateRegistationReportSql(String startDate, String endDate, String startBirthDate, String endBirthDate, long reasons_for_registration) {
        return "SELECT (SELECT COUNT(" + ReferralClient.COL_CLIENT_ID + ") FROM " + ReferralClient.tbName +
                " WHERE " + ReferralClient.COL_GENDER + "='Male' AND " +
                ReferralClient.COL_CREATED_AT + ">='" + startDate + "' AND " +
                ReferralClient.COL_CREATED_AT + "<'" + endDate + "'" +
                (!startBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " >= '" + startBirthDate + "'" : "") +
                (!endBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " < '" + endBirthDate + "'" : "") +
                (reasons_for_registration != 0 ? " AND " + ReferralClient.COL_REGISTRATION_REASON + " = " + reasons_for_registration : "") +
                ") as Male, " +
                "(SELECT COUNT(" + ReferralClient.COL_CLIENT_ID + ") FROM " + ReferralClient.tbName +
                " WHERE " + ReferralClient.COL_GENDER + "='Female' AND " +
                ReferralClient.COL_CREATED_AT + ">='" + startDate + "' AND " +
                ReferralClient.COL_CREATED_AT + "<'" + endDate + "' " +
                (!startBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " >= '" + startBirthDate + "'" : "") +
                (!endBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " < '" + endBirthDate + "'" : "") +
                (reasons_for_registration != 0 ? " AND " + ReferralClient.COL_REGISTRATION_REASON + " = " + reasons_for_registration : "")
                + ") as Female FROM " + ReferralClient.tbName + " Limit 1";
    }

    private String getLTFCountsReportSQL(long appointmentType, String startDate, String endDate){
        return "SELECT COUNT("+ ClientAppointments.COL_APPOINTMENT_ID +") FROM "+ClientAppointments.tbName+
                " WHERE "+
                ClientAppointments.COL_STATUS+" = -1 AND "+
                ClientAppointments.COL_APPOINTMENT_DATE+">'"+startDate+"' AND "+
                ClientAppointments.COL_APPOINTMENT_DATE+"<'"+endDate+"' " +
                (appointmentType!=0?" AND " + ClientAppointments.COL_APPOINTMENT_TYPE+ " = "+appointmentType:"");
    }

    private String getLTFFollowupReportSQL(long referral_feedback_id, String startDate, String endDate){
        return "SELECT COUNT("+ ClientReferrals.COL_REFERRAL_ID +") FROM "+ClientReferrals.tbName+
                " WHERE "+
                (referral_feedback_id!=0? ClientReferrals.COL_REFERRAL_FEEDBACK_ID+" ="+referral_feedback_id:ClientReferrals.COL_REFERRAL_FEEDBACK_ID+" <>8") +" AND "+
                ClientReferrals.COL_REFERRAL_ID+" IN (SELECT "+ ClientAppointments.COL_FOLLOWUP_REFERRAL_ID +" FROM "+ClientAppointments.tbName+" WHERE "+ClientAppointments.COL_STATUS+" = -1 AND "+ClientAppointments.COL_APPOINTMENT_DATE+">'"+startDate+"' AND "+ClientAppointments.COL_APPOINTMENT_DATE+"<'"+endDate+"')";
    }

    private String getReferralSummaryReportSql(boolean successfulReferrals,long serviceId, String startDate, String endDate){
        return "SELECT COUNT(*) FROM "+ClientReferrals.tbName+
                " WHERE "+
                ClientReferrals.COL_REFERRAL_TYPE+"=1 AND "+
                ClientReferrals.COL_SERVICE_ID+" = "+serviceId+" AND "+
                ClientReferrals.COL_REFERRAL_DATE+">='"+startDate+"' AND "+
                ClientReferrals.COL_REFERRAL_DATE+"<='"+endDate+"'";
    }

}
