package org.opensrp.web.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.connector.openmrs.service.OpenmrsReportingService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.ClientReferrals;
import org.opensrp.domain.HealthFacilities;
import org.opensrp.domain.ReferralReport;
import org.opensrp.domain.ReferralService;
import org.opensrp.dto.*;
import org.opensrp.repository.ClientReferralRepository;
import org.opensrp.repository.ReferralReportRepository;
import org.opensrp.service.ReferralsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ReportController {

    private OpenmrsReportingService reportService;
    private ClientReferralRepository clientReferralRepository;
    private OpenmrsUserService openmrsUserService;
    private ReferralsReportService referralsReportService;
    private ReferralReportRepository referralReportRepository;

    @Autowired
    public ReportController(OpenmrsReportingService reportService, ClientReferralRepository clientReferralRepository, OpenmrsUserService openmrsUserService, ReferralsReportService referralsReportService,ReferralReportRepository referralReportRepository) {
        this.reportService = reportService;
        this.openmrsUserService = openmrsUserService;
        this.clientReferralRepository = clientReferralRepository;
        this.referralsReportService = referralsReportService;
        this.referralReportRepository = referralReportRepository;
    }

    public static byte[] exportReportToHtmlStream(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        HtmlExporter exporter = new HtmlExporter();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
        exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");

        exporter.exportReport();

        return baos.toByteArray();
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

    //	Jasper report request handlers


    //    Total Registered Clients

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


    @RequestMapping(value = "/reports/{reportName}/{reportType}", method = RequestMethod.POST)
    public void reportLauncher(@PathVariable("reportName") String reportName, @PathVariable("reportType") String reportType, @RequestBody String json, HttpServletRequest request,
                               HttpServletResponse response) {
        JSONObject object = null;
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String toDate = "2020-01-01";
        String fromDate = "2017-01-01";
        try {
            fromDate = object.getString("from_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Date d = new Date(object.getString("to_date"));
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DATE,1);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            toDate = formatter.format(c.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray facilitiesArray = null;
        try {
            facilitiesArray = object.getJSONArray("facilities");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html");
        generateReport(reportName,reportType,fromDate,toDate,facilitiesArray,request,response);
    }


    @RequestMapping(value = "/available_reports", method = RequestMethod.GET)
    public ResponseEntity<List<ReferralReport>> availableReports() {
        try {
            List<ReferralReport>referralReports=  referralReportRepository.referralReports("SELECT * FROM "+ ReferralReport.tbName,null);
            return new ResponseEntity<List<ReferralReport>>(referralReports, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<ReferralReport>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



        //Total Successful Referrals
    /**
     * Retrieves Report in HTML & PDF format
     *
     * @return
     */
    @RequestMapping(value = "/reports/{reportName}/{reportType}", method = RequestMethod.GET)
    public void reportLauncher(@PathVariable("reportName") String reportName, @PathVariable("reportType") String reportType, HttpServletRequest request,
                               HttpServletResponse response) {
        response.setContentType("text/html");

        LocalDate firstDateOfTheMonth = LocalDate.now();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = formatter.format(c.getTime());

        generateReport(reportName,reportType,firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,new JSONArray(),request,response);
    }

    private void generateReport(String reportName,String reportType,String startDate, String endDate, JSONArray facilities,HttpServletRequest request,
                                HttpServletResponse response){
        File sourceFile = null;

        try {

            Map<String, Object> parameters = new HashMap<String, Object>();
            JasperReport jasperReport=null;
            JRDataSource datasource =null;

            switch (reportName) {
                case "total_registered_clients":
                    sourceFile = ResourceUtils.getFile("classpath:/jasper/TotalRegisteredClients.jasper");

                    datasource = referralsReportService.newRegistrationByReasonsReport(startDate,endDate,facilities);
                    jasperReport = (JasperReport) JRLoader.loadObjectFromFile(sourceFile.getPath());
                    parameters.put("Total Registered Clients", "Address Report");

                    break;
                case "total_successful_referrals":
                    sourceFile = ResourceUtils.getFile("classpath:/jasper/TotalSuccessfulReferrals.jasper");

                    datasource = referralsReportService.referralsSummaryReport(true,startDate,endDate,facilities);
                    jasperReport = (JasperReport) JRLoader.loadObjectFromFile(sourceFile.getPath());
                    parameters.put("Total Successful Referrals", "Address Report");

                    break;
                case "total_referrals_issued":
                    sourceFile = ResourceUtils.getFile("classpath:/jasper/TotalReferralsIssued.jasper");

                    datasource = referralsReportService.referralsSummaryReport(false,startDate,endDate,facilities);
                    jasperReport = (JasperReport) JRLoader.loadObjectFromFile(sourceFile.getPath());
                    parameters.put("Total Referrals Issued", "Address Report");

                    break;

                case "ltfs_feedback":
                    sourceFile = ResourceUtils.getFile("classpath:/jasper/LTFFeedbackReport.jasper");

                    datasource = referralsReportService.lTFsFeedbacksReport(startDate,endDate,facilities);
                    jasperReport = (JasperReport) JRLoader.loadObjectFromFile(sourceFile.getPath());
                    parameters.put("LTFs Feedback", "Address Report");

                    break;

                case "total_issued_ltfs":
                    sourceFile = ResourceUtils.getFile("classpath:/jasper/TotalNumberOfLTFSToCBHSReport.jasper");

                    datasource = referralsReportService.totalIssuedLTFsSummaryReport(startDate,endDate,facilities);
                    jasperReport = (JasperReport) JRLoader.loadObjectFromFile(sourceFile.getPath());
                    parameters.put("Total Issued LTFs", "Address Report");

                    break;

                default:
                    PrintWriter out = null;
                    try {
                        out = response.getWriter();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    errorPage(out, "Report not found.");
                    break;


            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
            request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);

            Exporter exporter = null;
            if (reportType.equalsIgnoreCase("html")) {
                exporter = export(jasperPrint, 1, response);
            } else if (reportType.equalsIgnoreCase("pdf")) {
                exporter = export(jasperPrint, 5, response);
            }

            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            errorPage(out, "Error generating report");
        }
    }

    private void errorPage(PrintWriter out, String message){

        out.println("<html>");
        out.println("<head>");
        out.println("<title>JasperReports - Web Application Sample</title>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
        out.println("</head>");

        out.println("<body bgcolor=\"white\">");

        out.println("<span class=\"bnew\"> "+message+" :</span>");
        out.println("<pre>");

        out.println("</pre>");

        out.println("</body>");
        out.println("</html>");

    }

    private Exporter export(final JasperPrint print,int printType,HttpServletResponse response) throws JRException {
        final Exporter exporter;
        boolean html = false;

        switch (printType) {
            case 1:
                exporter = new HtmlExporter();
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(out);
                output.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
                exporter.setExporterOutput(output);


                SimpleHtmlExporterConfiguration exporterConfig = new SimpleHtmlExporterConfiguration();
                exporterConfig.setBetweenPagesHtml("");
                exporter.setConfiguration(exporterConfig);

                SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();
                reportConfig.setRemoveEmptySpaceBetweenRows(true);
                exporter.setConfiguration(reportConfig);

                html = true;
                break;

            case 2:
                exporter = new JRCsvExporter();
                break;

            case 3:
                exporter = new JRXmlExporter();
                break;

            case 4:
                exporter = new JRXlsxExporter();
                break;

            case 5:
                exporter = new JRPdfExporter();
                break;
            default:
                PrintWriter out2 = null;
                try {
                    out2 = response.getWriter();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exporter = new HtmlExporter();
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(out2));
                html = true;
                break;
        }

        if (printType != 1) {
            ServletOutputStream out1=null;
            try {
                out1 = response.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out1));
        }

        exporter.setExporterInput(new SimpleExporterInput(print));


        return exporter;
    }

}
