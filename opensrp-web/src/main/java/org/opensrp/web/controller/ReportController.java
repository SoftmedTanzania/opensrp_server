package org.opensrp.web.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.connector.openmrs.service.OpenmrsReportingService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.ClientReferrals;
import org.opensrp.domain.HealthFacilities;
import org.opensrp.domain.ReferralService;
import org.opensrp.dto.*;
import org.opensrp.repository.ClientReferralRepository;
import org.opensrp.service.ReferralsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ReportController {

    private OpenmrsReportingService reportService;
    private ClientReferralRepository clientReferralRepository;
    private OpenmrsUserService openmrsUserService;
    private ReferralsReportService referralsReportService;

    @Autowired
    public ReportController(OpenmrsReportingService reportService, ClientReferralRepository clientReferralRepository, OpenmrsUserService openmrsUserService, ReferralsReportService referralsReportService) {
        this.reportService = reportService;
        this.openmrsUserService = openmrsUserService;
        this.clientReferralRepository = clientReferralRepository;
        this.referralsReportService = referralsReportService;
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

    /**
     * Retrieves Report in XLS format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_registered_clients_xls", method = RequestMethod.GET)
    public ModelAndView totalRegisteredClientsXLS(ModelAndView modelAndView) {


        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalRegisteredClientsXlsReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in HTML format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_registered_clients_html", method = RequestMethod.GET)
    public ModelAndView totalRegisteredClientsHtml(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalRegisteredClientsHtmlReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in HTML format
     *
     * @return
     */
    @RequestMapping(value = "/reports/htmlTest", method = RequestMethod.GET)
    public void html(HttpServletRequest request,
                     HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File sourceFile = null;
        try {
            sourceFile = ResourceUtils.getFile("classpath:/jasper/TotalRegisteredClients.jasper");

            JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(sourceFile.getPath());
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("ReportTitle", "Address Report");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);

            HtmlExporter exporter = new HtmlExporter();

            request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(out);
            output.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));

            SimpleHtmlExporterConfiguration exporterConfig = new SimpleHtmlExporterConfiguration();
            exporterConfig.setBetweenPagesHtml("");
            exporter.setConfiguration(exporterConfig);

            SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();
            reportConfig.setRemoveEmptySpaceBetweenRows(true);
            exporter.setConfiguration(reportConfig);

            exporter.setExporterOutput(output);
        } catch (Exception e) {
            e.printStackTrace();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>JasperReports - Web Application Sample</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
            out.println("</head>");

            out.println("<body bgcolor=\"white\">");

            out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");
            out.println("<pre>");

            e.printStackTrace(out);

            out.println("</pre>");

            out.println("</body>");
            out.println("</html>");

        }
    }

    @RequestMapping(value = "/reports/htmlTest1", method = RequestMethod.GET)
    public void getReport(HttpServletRequest request,
                          HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File sourceFile = null;
        try {
            sourceFile = ResourceUtils.getFile("classpath:/jasper/TotalRegisteredClients.jasper");

            JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();


            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(sourceFile.getPath());

            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("ReportTitle", "Address Report");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);


            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType("text/html");
            response.setHeader("Content-disposition", "inline");


            byte[] aux = exportReportToHtmlStream(jasperPrint);

            request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
            Enumeration temp = request.getSession().getAttributeNames();
            servletOutputStream.write(aux);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves Report in PDF format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_registered_clients_pdf", method = RequestMethod.GET)
    public ModelAndView totalRegisteredClientsPDF(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // pdfReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalRegisteredClientsPdfReport", parameterMap);

        // Return the View and the Model combined
        return modelAndView;
    }


    //    Total Referrals Issued

    /**
     * Retrieves Report in XLS format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_referrals_issued_xls", method = RequestMethod.GET)
    public ModelAndView totalReferralsIssuedXLS(ModelAndView modelAndView) {


        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalReferralsIssuedXlsReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in HTML format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_referrals_issued_html", method = RequestMethod.GET)
    public ModelAndView totalReferralsIssuedHTML(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalReferralsIssuedHtmlReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in PDF format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_referrals_issued_pdf", method = RequestMethod.GET)
    public ModelAndView totalReferralsIssuedPDF(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // pdfReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalReferralsIssuedPdfReport", parameterMap);

        // Return the View and the Model combined
        return modelAndView;
    }


    //    Total Successful Referrals

    /**
     * Retrieves Report in XLS format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_successful_referrals_xls", method = RequestMethod.GET)
    public ModelAndView totalSuccessfulReferralsXLS(ModelAndView modelAndView) {


        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalSuccessfulReferralsXlsReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in HTML format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_successful_referrals_html", method = RequestMethod.GET)
    public ModelAndView totalSuccessfulReferralsHTML(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalSuccessfulReferralsHtmlReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in PDF format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_successful_referrals_pdf", method = RequestMethod.GET)
    public ModelAndView totalSuccessfulReferralsPDF(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // pdfReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalSuccessfulReferralsPdfReport", parameterMap);

        // Return the View and the Model combined
        return modelAndView;
    }


    //    Total issued LTFS to CBHS

    /**
     * Retrieves Report in XLS format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_issued_ltfs_to_cbhs_xls", method = RequestMethod.GET)
    public ModelAndView totalIssuedLTFSToCBHSXLS(ModelAndView modelAndView) {


        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalIssuedLTFSToCBHSXlsReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in HTML format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_issued_ltfs_to_cbhs_html", method = RequestMethod.GET)
    public ModelAndView totalIssuedLTFSToCBHSHTML(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalIssuedLTFSToCBHSHtmlReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in PDF format
     *
     * @return
     */
    @RequestMapping(value = "/reports/total_issued_ltfs_to_cbhs_pdf", method = RequestMethod.GET)
    public ModelAndView totalIssuedLTFSToCBHSPDF(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // pdfReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("totalIssuedLTFSToCBHSPdfReport", parameterMap);

        // Return the View and the Model combined
        return modelAndView;
    }


    //    LTF Feeback

    /**
     * Retrieves Report in XLS format
     *
     * @return
     */
    @RequestMapping(value = "/reports/ltf_feedback_xls", method = RequestMethod.GET)
    public ModelAndView ltfFeedbackXLS(ModelAndView modelAndView) {


        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("ltfFeedbackXlsReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in HTML format
     *
     * @return
     */
    @RequestMapping(value = "/reports/ltf_feedback_html", method = RequestMethod.GET)
    public ModelAndView ltfFeedbackHTML(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // xlsReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("ltfFeedbackHtmlReport", parameterMap);
        // Return the View and the Model combined
        return modelAndView;
    }

    /**
     * Retrieves Report in PDF format
     *
     * @return
     */
    @RequestMapping(value = "/reports/ltf_feedback_pdf", method = RequestMethod.GET)
    public ModelAndView ltfFeedbackPDF(ModelAndView modelAndView) {


        // Assign the datasource to an instance of JRDataSource
        // JRDataSource is the datasource that Jasper understands
        // This is basically a wrapper to Java's collection classes
        JRDataSource datasource = referralsReportService.newRegistrationByReasonsReport();

        // In order to use Spring's built-in Jasper support,
        // We are required to pass our datasource as a map parameter
        // parameterMap is the Model of our application
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("datasource", datasource);

        // pdfReport is the View of our application
        // This is declared inside the /WEB-INF/jasper-views.xml
        modelAndView = new ModelAndView("ltfFeedbackPdfReport", parameterMap);

        // Return the View and the Model combined
        return modelAndView;
    }

}
