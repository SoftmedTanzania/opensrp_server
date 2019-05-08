package org.opensrp.service;

import com.google.gson.Gson;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.json.JSONArray;
import org.opensrp.domain.*;
import org.opensrp.dto.AgeGroupReportsReportDTO;
import org.opensrp.dto.GenderReportsDTO;
import org.opensrp.dto.MalariaReportDTO;
import org.opensrp.dto.report.MaleFemaleCountObject;
import org.opensrp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ReferralsReportService {
    private static Logger logger = LoggerFactory.getLogger(ReferralsReportService.class.toString());

    @Autowired
    private ClientRegistrationReasonRepository clientRegistrationReasonRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    private ReferralFeedbackRepository referralFeedbackRepository;

    @Autowired
    private ReferralServiceRepository referralServiceRepository;

    @Autowired
    private HealthFacilityRepository healthFacilityRepository;

    private String getFacilityOpenMRSUUIDs(JSONArray facilities) {
        int size = facilities.length();
        String openmrsUuids = "";
        for (int i = 0; i < size; i++) {
            try {
                Object[] params = new Object[]{
                        facilities.getString(i)};
                List<HealthFacilities> healthFacilities = healthFacilityRepository.getHealthFacility("SELECT * FROM " + HealthFacilities.tbName + " WHERE " + HealthFacilities.COL_OPENMRS_UUID + "=?", params);
                openmrsUuids += healthFacilities.get(0).getId() + ",";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        openmrsUuids = openmrsUuids+"0";
        return openmrsUuids;
    }

    public List<AgeGroupReportsReportDTO> newRegistrationByReasonsReport(String startDate, String endDate, JSONArray facilities) {
        String ids = getFacilityOpenMRSUUIDs(facilities);

        List<ClientRegistrationReason> clientRegistrationReasons = null;
        try {
            clientRegistrationReasons = clientRegistrationReasonRepository.geRegistrationReasons("SELECT * FROM " + ClientRegistrationReason.tbName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int sn = 0;
        List<AgeGroupReportsReportDTO> ageGroupReportsReportDTOS = new ArrayList<>();
        for (ClientRegistrationReason clientRegistrationReason : clientRegistrationReasons) {
            sn++;
            AgeGroupReportsReportDTO ageGroupReportsReportDTO = new AgeGroupReportsReportDTO();

            ageGroupReportsReportDTO.setSn(String.valueOf(sn));
            ageGroupReportsReportDTO.setItemName(clientRegistrationReason.getDescSw());
            long totalMale = 0, totalFemale = 0;

            String lessThan1year = generateRegistationReportSql(startDate, endDate, getDateByYearString(-1), endDate, clientRegistrationReason.getRegistrationId(), ids);

            List<MaleFemaleCountObject> lessThan1yearRegistrationsList = null;
            try {
                logger.info("Less than one year SQL = " + lessThan1year);
                lessThan1yearRegistrationsList = clientsRepository.getMaleFemaleCountReports(lessThan1year, null);

                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setLessThan1Male(lessThan1yearRegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(lessThan1yearRegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setLessThan1Female(lessThan1yearRegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(lessThan1yearRegistrationsList.get(0).getFemale());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            String _1to5 = generateRegistationReportSql(startDate, endDate, getDateByYearString(-5), getDateByYearString(-1), clientRegistrationReason.getRegistrationId(), ids);

            List<MaleFemaleCountObject> _1to5RegistrationsList = null;
            try {
                logger.info("One to Five years SQL = " + _1to5);
                _1to5RegistrationsList = clientsRepository.getMaleFemaleCountReports(_1to5, null);

                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setOneTofiveMale(_1to5RegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(_1to5RegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setOneTofiveFemale(_1to5RegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(_1to5RegistrationsList.get(0).getFemale());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            String _6to9 = generateRegistationReportSql(startDate, endDate, getDateByYearString(-9), getDateByYearString(-5), clientRegistrationReason.getRegistrationId(), ids);

            List<MaleFemaleCountObject> _6to9RegistrationsList = null;
            try {
                _6to9RegistrationsList = clientsRepository.getMaleFemaleCountReports(_6to9, null);

                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setSixToNineMale(_6to9RegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(_6to9RegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setSixToNineFemale(_6to9RegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(_6to9RegistrationsList.get(0).getFemale());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            String _10To14 = generateRegistationReportSql(startDate, endDate, getDateByYearString(-14), getDateByYearString(-9), clientRegistrationReason.getRegistrationId(), ids);

            List<MaleFemaleCountObject> _10To14RegistrationsList = null;
            try {
                _10To14RegistrationsList = clientsRepository.getMaleFemaleCountReports(_10To14, null);

                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setTenToFourteenMale(_10To14RegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(_10To14RegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setTenToFourteenFemale(_10To14RegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(_10To14RegistrationsList.get(0).getFemale());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            String _15To19 = generateRegistationReportSql(startDate, endDate, getDateByYearString(-19), getDateByYearString(-14), clientRegistrationReason.getRegistrationId(), ids);
            List<MaleFemaleCountObject> _15To19RegistrationsList = null;
            try {
                _15To19RegistrationsList = clientsRepository.getMaleFemaleCountReports(_15To19, null);

                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setFifteenToNineteenMale(_15To19RegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(_15To19RegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setFifteenToNineteenFemale(_15To19RegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(_15To19RegistrationsList.get(0).getFemale());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            String _20To24 = generateRegistationReportSql(startDate, endDate, getDateByYearString(-24), getDateByYearString(-19), clientRegistrationReason.getRegistrationId(), ids);

            List<MaleFemaleCountObject> _20To24RegistrationsList = null;
            try {
                _20To24RegistrationsList = clientsRepository.getMaleFemaleCountReports(_20To24, null);

                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setTwentyToTwentyFourMale(_20To24RegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(_20To24RegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setTwentyToTwentyFourFemale(_20To24RegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(_20To24RegistrationsList.get(0).getFemale());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            String _25To49 = generateRegistationReportSql(startDate, endDate, getDateByYearString(-49), getDateByYearString(-24), clientRegistrationReason.getRegistrationId(), ids);

            List<MaleFemaleCountObject> _25To49RegistrationsList = null;
            try {
                System.out.println("report Test : " + _25To49);
                _25To49RegistrationsList = clientsRepository.getMaleFemaleCountReports(_25To49, null);

                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setTwentyFiveToFourtyNineMale(_25To49RegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(_25To49RegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setTwentyFiveToFourtyNineFemale(_25To49RegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(_25To49RegistrationsList.get(0).getFemale());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            String _50To59 = generateRegistationReportSql(startDate, endDate, getDateByYearString(-59), getDateByYearString(-49), clientRegistrationReason.getRegistrationId(), ids);

            List<MaleFemaleCountObject> _50To59RegistrationsList = null;
            try {
                _50To59RegistrationsList = clientsRepository.getMaleFemaleCountReports(_50To59, null);
                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setFiftyToFiftyNineMale(_50To59RegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(_50To59RegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setFiftyToFiftyNineFemale(_50To59RegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(_50To59RegistrationsList.get(0).getFemale());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            String _60Above = generateRegistationReportSql(startDate, endDate, "", getDateByYearString(-59), clientRegistrationReason.getRegistrationId(), ids);
            List<MaleFemaleCountObject> _60AboveRegistrationsList = null;
            try {
                _60AboveRegistrationsList = clientsRepository.getMaleFemaleCountReports(_60Above, null);

                if (clientRegistrationReason.isApplicableToMen()) {
                    ageGroupReportsReportDTO.setAboveSixtyMale(_60AboveRegistrationsList.get(0).getMale());
                    totalMale += Integer.parseInt(_60AboveRegistrationsList.get(0).getMale());
                }

                if (clientRegistrationReason.isApplicableToWomen()) {
                    ageGroupReportsReportDTO.setAboveSixtyFemale(_60AboveRegistrationsList.get(0).getFemale());
                    totalFemale += Integer.parseInt(_60AboveRegistrationsList.get(0).getFemale());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ageGroupReportsReportDTO.setTotalMale(totalMale + "");
            ageGroupReportsReportDTO.setTotalFemale(totalFemale + "");

            ageGroupReportsReportDTOS.add(ageGroupReportsReportDTO);
        }

        logger.info("Report data source = " + new Gson().toJson(ageGroupReportsReportDTOS));

        return ageGroupReportsReportDTOS;
    }

    public List<AgeGroupReportsReportDTO> referralsSummaryReport(String referralStatus, String startDate, String endDate, JSONArray facilities) {

        String ids = getFacilityOpenMRSUUIDs(facilities);
        List<ReferralService> referralServices = null;
        try {
            referralServices = referralServiceRepository.getReferralServices("SELECT * FROM " + ReferralService.tbName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int sn = 0;
        List<AgeGroupReportsReportDTO> ageGroupReportsReportDTOS = new ArrayList<>();
        for (ReferralService referralService : referralServices) {
            sn++;
            AgeGroupReportsReportDTO referralReportDTO = new AgeGroupReportsReportDTO();

            referralReportDTO.setSn(String.valueOf(sn));
            referralReportDTO.setItemName(referralService.getServiceNameSw());
            long totalMale = 0, totalFemale = 0;

            String lessThan1year = generateReferralsReportSql(startDate, endDate, getDateByYearString(-1), endDate, referralService.getServiceId(), referralStatus, ids);

            List<MaleFemaleCountObject> lessThan1yearReferralsList = null;
            try {
                logger.info("Less than one year SQL = " + lessThan1year);
                lessThan1yearReferralsList = clientsRepository.getMaleFemaleCountReports(lessThan1year, null);
                referralReportDTO.setLessThan1Male(lessThan1yearReferralsList.get(0).getMale());
                referralReportDTO.setLessThan1Female(lessThan1yearReferralsList.get(0).getFemale());

                totalMale += Integer.parseInt(lessThan1yearReferralsList.get(0).getMale());
                totalFemale += Integer.parseInt(lessThan1yearReferralsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _1to5 = generateReferralsReportSql(startDate, endDate, getDateByYearString(-5), getDateByYearString(-1), referralService.getServiceId(), referralStatus, ids);

            List<MaleFemaleCountObject> _1to5RegistrationsList = null;
            try {
                logger.info("One to Five years SQL = " + _1to5);
                _1to5RegistrationsList = clientsRepository.getMaleFemaleCountReports(_1to5, null);
                referralReportDTO.setOneTofiveMale(_1to5RegistrationsList.get(0).getMale());
                referralReportDTO.setOneTofiveFemale(_1to5RegistrationsList.get(0).getFemale());

                totalMale += Integer.parseInt(_1to5RegistrationsList.get(0).getMale());
                totalFemale += Integer.parseInt(_1to5RegistrationsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _6to9 = generateReferralsReportSql(startDate, endDate, getDateByYearString(-9), getDateByYearString(-5), referralService.getServiceId(), referralStatus, ids);

            List<MaleFemaleCountObject> _6to9RegistrationsList = null;
            try {
                _6to9RegistrationsList = clientsRepository.getMaleFemaleCountReports(_6to9, null);
                referralReportDTO.setSixToNineMale(_6to9RegistrationsList.get(0).getMale());
                referralReportDTO.setSixToNineFemale(_6to9RegistrationsList.get(0).getFemale());

                totalMale += Integer.parseInt(_6to9RegistrationsList.get(0).getMale());
                totalFemale += Integer.parseInt(_6to9RegistrationsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _10To14 = generateReferralsReportSql(startDate, endDate, getDateByYearString(-14), getDateByYearString(-9), referralService.getServiceId(), referralStatus, ids);

            List<MaleFemaleCountObject> _10To14RegistrationsList = null;
            try {
                _10To14RegistrationsList = clientsRepository.getMaleFemaleCountReports(_10To14, null);
                referralReportDTO.setTenToFourteenMale(_10To14RegistrationsList.get(0).getMale());
                referralReportDTO.setTenToFourteenFemale(_10To14RegistrationsList.get(0).getFemale());

                totalMale += Integer.parseInt(_10To14RegistrationsList.get(0).getMale());
                totalFemale += Integer.parseInt(_10To14RegistrationsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _15To19 = generateReferralsReportSql(startDate, endDate, getDateByYearString(-19), getDateByYearString(-14), referralService.getServiceId(), referralStatus, ids);
            List<MaleFemaleCountObject> _15To19RegistrationsList = null;
            try {
                _15To19RegistrationsList = clientsRepository.getMaleFemaleCountReports(_15To19, null);
                referralReportDTO.setFifteenToNineteenMale(_15To19RegistrationsList.get(0).getMale());
                referralReportDTO.setFifteenToNineteenFemale(_15To19RegistrationsList.get(0).getFemale());

                totalMale += Integer.parseInt(_15To19RegistrationsList.get(0).getMale());
                totalFemale += Integer.parseInt(_15To19RegistrationsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _20To24 = generateReferralsReportSql(startDate, endDate, getDateByYearString(-24), getDateByYearString(-19), referralService.getServiceId(), referralStatus, ids);
            List<MaleFemaleCountObject> _20To24RegistrationsList = null;
            try {
                _20To24RegistrationsList = clientsRepository.getMaleFemaleCountReports(_20To24, null);
                referralReportDTO.setTwentyToTwentyFourMale(_20To24RegistrationsList.get(0).getMale());
                referralReportDTO.setTwentyToTwentyFourFemale(_20To24RegistrationsList.get(0).getFemale());


                totalMale += Integer.parseInt(_20To24RegistrationsList.get(0).getMale());
                totalFemale += Integer.parseInt(_20To24RegistrationsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _25To49 = generateReferralsReportSql(startDate, endDate, getDateByYearString(-49), getDateByYearString(-24), referralService.getServiceId(), referralStatus, ids);

            List<MaleFemaleCountObject> _25To49RegistrationsList = null;
            try {
                System.out.println("report Test : " + _25To49);
                _25To49RegistrationsList = clientsRepository.getMaleFemaleCountReports(_25To49, null);
                referralReportDTO.setTwentyFiveToFourtyNineMale(_25To49RegistrationsList.get(0).getMale());
                referralReportDTO.setTwentyFiveToFourtyNineFemale(_25To49RegistrationsList.get(0).getFemale());

                totalMale += Integer.parseInt(_25To49RegistrationsList.get(0).getMale());
                totalFemale += Integer.parseInt(_25To49RegistrationsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _50To59 = generateReferralsReportSql(startDate, endDate, getDateByYearString(-59), getDateByYearString(-49), referralService.getServiceId(), referralStatus, ids);

            List<MaleFemaleCountObject> _50To59RegistrationsList = null;
            try {
                _50To59RegistrationsList = clientsRepository.getMaleFemaleCountReports(_50To59, null);
                referralReportDTO.setFiftyToFiftyNineMale(_50To59RegistrationsList.get(0).getMale());
                referralReportDTO.setFiftyToFiftyNineFemale(_50To59RegistrationsList.get(0).getFemale());

                totalMale += Integer.parseInt(_50To59RegistrationsList.get(0).getMale());
                totalFemale += Integer.parseInt(_50To59RegistrationsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _60Above = generateReferralsReportSql(startDate, endDate, "", getDateByYearString(-59), referralService.getServiceId(), referralStatus, ids);

            List<MaleFemaleCountObject> _60AboveRegistrationsList = null;
            try {
                _60AboveRegistrationsList = clientsRepository.getMaleFemaleCountReports(_60Above, null);
                referralReportDTO.setAboveSixtyMale(_60AboveRegistrationsList.get(0).getMale());
                referralReportDTO.setAboveSixtyFemale(_60AboveRegistrationsList.get(0).getFemale());


                totalMale += Integer.parseInt(_60AboveRegistrationsList.get(0).getMale());
                totalFemale += Integer.parseInt(_60AboveRegistrationsList.get(0).getFemale());
            } catch (Exception e) {
                e.printStackTrace();
            }

            referralReportDTO.setTotalMale(totalMale + "");
            referralReportDTO.setTotalFemale(totalFemale + "");

            ageGroupReportsReportDTOS.add(referralReportDTO);
        }

        logger.info("Report data source = " + new Gson().toJson(ageGroupReportsReportDTOS));

        return ageGroupReportsReportDTOS;
    }

    public  List<MalariaReportDTO> successfulMalariaReferralsReport(String startDate, String endDate, JSONArray facilities) {
        String ids = getFacilityOpenMRSUUIDs(facilities);
        List<ReferralService> referralServices = null;
        try {
            referralServices = referralServiceRepository.getReferralServices("SELECT * FROM " + ReferralService.tbName + " WHERE " + ReferralService.COL_CATEGORY_NAME + " = 'malaria'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int sn = 0;
        List<MalariaReportDTO> malariaReportDTOS = new ArrayList<>();


        int totalMale = 0, totalFemale = 0;

        MalariaReportDTO malariaReportDTO = new MalariaReportDTO();

        String lessThan5years = generateReferralsReportSql(startDate, endDate, getDateByYearString(-5), endDate, referralServices.get(0).getServiceId(), "1", ids);

        List<MaleFemaleCountObject> lessThan5yearsReferralsList = null;
        try {

            logger.info("Less than 5 years SQL = " + lessThan5years);
            lessThan5yearsReferralsList = clientsRepository.getMaleFemaleCountReports(lessThan5years, null);
            malariaReportDTO.setLessThanFiveMale(lessThan5yearsReferralsList.get(0).getMale());
            malariaReportDTO.setLessThanFiveFemale(lessThan5yearsReferralsList.get(0).getFemale());

            totalMale += Integer.parseInt(lessThan5yearsReferralsList.get(0).getMale());
            totalFemale += Integer.parseInt(lessThan5yearsReferralsList.get(0).getFemale());
        } catch (Exception e) {
            e.printStackTrace();
        }


        String greaterThan5years = generateReferralsReportSql(startDate, endDate, "1900/01/01", getDateByYearString(-5), referralServices.get(0).getServiceId(), "1", ids);
        List<MaleFemaleCountObject> greaterThan5yearsReferralsList = null;
        try {
            logger.info("greater than 5 years SQL = " + greaterThan5years);

            greaterThan5yearsReferralsList = clientsRepository.getMaleFemaleCountReports(greaterThan5years, null);
            malariaReportDTO.setGreaterThan5Male(greaterThan5yearsReferralsList.get(0).getMale());
            malariaReportDTO.setGreaterThan5Female(greaterThan5yearsReferralsList.get(0).getFemale());

            totalMale += Integer.parseInt(greaterThan5yearsReferralsList.get(0).getMale());
            totalFemale += Integer.parseInt(greaterThan5yearsReferralsList.get(0).getFemale());
        } catch (Exception e) {
            e.printStackTrace();
        }

        malariaReportDTO.setTotalMale(totalMale + "");
        malariaReportDTO.setTotalFemale(totalFemale + "");

        malariaReportDTOS.add(malariaReportDTO);

        logger.info("Report data source = " + new Gson().toJson(malariaReportDTOS));
        return malariaReportDTOS;
    }

    public List<GenderReportsDTO> totalIssuedLTFsSummaryReport(String startDate, String endDate, JSONArray facilities) {
        String ids = getFacilityOpenMRSUUIDs(facilities);
        List<AppointmentType> appointmentTypes = null;

        try {
            appointmentTypes = appointmentTypeRepository.getAppointmentTypes("SELECT * FROM " + AppointmentType.tbName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int sn = 0;
        List<GenderReportsDTO> genderReportsDTOS = new ArrayList<>();


        for (AppointmentType appointmentType : appointmentTypes) {
            sn++;
            GenderReportsDTO genderReportsDTO = new GenderReportsDTO();
            genderReportsDTO.setSn(sn + "");
            genderReportsDTO.setItemName(appointmentType.getName());
            String lftIssuedSql = getLTFCountsReportSQL(appointmentType.getId(), startDate, endDate, ids);

            System.out.println("LTF SQLs = "+lftIssuedSql);

            List<MaleFemaleCountObject> lftIssuedList = null;
            try {
                lftIssuedList = appointmentTypeRepository.getMaleFemaleCountReports(lftIssuedSql, null);
                genderReportsDTO.setMale(lftIssuedList.get(0).getMale());
                genderReportsDTO.setFemale(lftIssuedList.get(0).getFemale());
                genderReportsDTO.setTotal((Integer.parseInt(lftIssuedList.get(0).getMale()) + Integer.parseInt(lftIssuedList.get(0).getFemale())) + "");

            } catch (Exception e) {
                e.printStackTrace();
                genderReportsDTO.setMale("0");
                genderReportsDTO.setFemale("0");
                genderReportsDTO.setTotal("0");
            }
            genderReportsDTOS.add(genderReportsDTO);
        }

        logger.info("Report data source = " + new Gson().toJson(genderReportsDTOS));
        return genderReportsDTOS;
    }

    public List<GenderReportsDTO> lTFsFeedbacksReport(String startDate, String endDate, JSONArray facilities) {
        String ids = getFacilityOpenMRSUUIDs(facilities);
        List<ReferralFeedback> referralFeedbacks = null;
        try {
            referralFeedbacks = referralFeedbackRepository.getReferralFeedback("SELECT * FROM " + ReferralFeedback.tbName + " WHERE " + ReferralFeedback.COL_REFERRAL_TYPE_ID + " = 1", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int sn = 0;
        List<GenderReportsDTO> genderReportsDTOS = new ArrayList<>();

        for (ReferralFeedback referralFeedback : referralFeedbacks) {
            sn++;
            GenderReportsDTO genderReportsDTO = new GenderReportsDTO();
            genderReportsDTO.setSn(sn + "");
            genderReportsDTO.setItemName(referralFeedback.getDescSw());
            String lftFoundSql = getFoundLTFCountsReportSQL(referralFeedback.getId(),  startDate,endDate,ids);

            System.out.println(" LFT FOUND SQL = "+lftFoundSql);

            List<MaleFemaleCountObject> lftFoundList = null;
            try {
                lftFoundList = appointmentTypeRepository.getMaleFemaleCountReports(lftFoundSql, null);
                genderReportsDTO.setMale(lftFoundList.get(0).getMale());
                genderReportsDTO.setFemale(lftFoundList.get(0).getFemale());
                genderReportsDTO.setTotal((Integer.parseInt(lftFoundList.get(0).getMale()) + Integer.parseInt(lftFoundList.get(0).getFemale())) + "");
            } catch (Exception e) {
                e.printStackTrace();
                genderReportsDTO.setMale("0");
                genderReportsDTO.setFemale("0");
                genderReportsDTO.setTotal("0");
            }
            genderReportsDTOS.add(genderReportsDTO);
        }

        logger.info("Report data source = " + new Gson().toJson(genderReportsDTOS));
        return genderReportsDTOS;
    }

    public String getDateByYearString(int year) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        //Less than a year old
        Calendar aYearAgo = Calendar.getInstance();
        aYearAgo.add(Calendar.YEAR, year);
        String dateString = formatter.format(aYearAgo.getTime());
        return dateString;
    }

    private String generateRegistationReportSql(String startDate, String endDate, String startBirthDate, String endBirthDate, long reasons_for_registration, String facilityIds) {
        return "SELECT (SELECT COUNT(" + ReferralClient.COL_CLIENT_ID + ") FROM " + ReferralClient.tbName +
                " INNER JOIN " + HealthFacilitiesReferralClients.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " WHERE " + ReferralClient.COL_GENDER + "='Male' AND " +
                ReferralClient.tbName+"."+ReferralClient.COL_CREATED_AT + ">='" + startDate + "' AND " +
                ReferralClient.tbName+"."+ReferralClient.COL_CREATED_AT + "<'" + endDate + "'" +
                (!startBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " >= '" + startBirthDate + "'" : "") +
                (!endBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " < '" + endBirthDate + "'" : "") +
                (reasons_for_registration != 0 ? " AND " + ReferralClient.COL_REGISTRATION_REASON + " = " + reasons_for_registration : "") +
                (!facilityIds.equals("") ? " AND " + HealthFacilitiesReferralClients.tbName+"."+HealthFacilitiesReferralClients.COL_FACILITY_ID + " IN (" + facilityIds + ")" : "")+
                ") as Male, " +
                "(SELECT COUNT(" + ReferralClient.COL_CLIENT_ID + ") FROM " + ReferralClient.tbName +
                " INNER JOIN " + HealthFacilitiesReferralClients.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " WHERE " + ReferralClient.COL_GENDER + "='Female' AND " +
                ReferralClient.tbName+"."+ReferralClient.COL_CREATED_AT + ">='" + startDate + "' AND " +
                ReferralClient.tbName+"."+ReferralClient.COL_CREATED_AT + "<'" + endDate + "' " +
                (!startBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " >= '" + startBirthDate + "'" : "") +
                (!endBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " < '" + endBirthDate + "'" : "") +
                (reasons_for_registration != 0 ? " AND " + ReferralClient.COL_REGISTRATION_REASON + " = " + reasons_for_registration : "") +
                (!facilityIds.equals("") ? " AND " + HealthFacilitiesReferralClients.tbName+"."+HealthFacilitiesReferralClients.COL_FACILITY_ID + " IN (" + facilityIds + ")" : "")
                + ") as Female FROM " + ReferralClient.tbName + " Limit 1";
    }

    public String generateReferralsReportSql(String startDate, String endDate, String startBirthDate, String endBirthDate, long referral_service, String referralStatus, String facilityIds) {
        return "SELECT (SELECT COUNT(" + ReferralClient.COL_CLIENT_ID + ") FROM " + ReferralClient.tbName +
                " INNER JOIN " + ClientReferrals.tbName + " ON " + ClientReferrals.tbName + "." + ClientReferrals.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " INNER JOIN " + HealthFacilitiesReferralClients.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " WHERE " + ReferralClient.COL_GENDER + "='Male' AND " +
                ClientReferrals.COL_REFERRAL_DATE + ">='" + startDate + "' AND " +
                ClientReferrals.COL_REFERRAL_DATE + "<'" + endDate + "'" +
                (!referralStatus.equals("0")? " AND " + ClientReferrals.COL_REFERRAL_STATUS + " = "+referralStatus : "") +
                (!startBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " >= '" + startBirthDate + "'" : "") +
                (!endBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " < '" + endBirthDate + "'" : "") +
                (referral_service != 0 ? " AND " + ClientReferrals.COL_SERVICE_ID + " = " + referral_service : "") +
                (!facilityIds.equals("") ? " AND " + HealthFacilitiesReferralClients.tbName+"."+HealthFacilitiesReferralClients.COL_FACILITY_ID + " IN (" + facilityIds + ")" : "")+
                ") as Male, " +
                "(SELECT COUNT(" + ReferralClient.COL_CLIENT_ID + ") FROM " + ReferralClient.tbName +
                " INNER JOIN " + ClientReferrals.tbName + " ON " + ClientReferrals.tbName + "." + ClientReferrals.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " INNER JOIN " + HealthFacilitiesReferralClients.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " WHERE " + ReferralClient.COL_GENDER + "='Female' AND " +
                ClientReferrals.COL_REFERRAL_DATE + ">='" + startDate + "' AND " +
                ClientReferrals.COL_REFERRAL_DATE + "<'" + endDate + "' " +
                (!referralStatus.equals("0")? " AND " + ClientReferrals.COL_REFERRAL_STATUS + " = "+referralStatus : "") +
                (!startBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " >= '" + startBirthDate + "'" : "") +
                (!endBirthDate.equals("") ? " AND " + ReferralClient.COL_DATE_OF_BIRTH + " < '" + endBirthDate + "'" : "") +
                (referral_service != 0 ? " AND " + ClientReferrals.COL_SERVICE_ID + " = " + referral_service : "") +
                (!facilityIds.equals("") ? " AND " + HealthFacilitiesReferralClients.tbName+"."+HealthFacilitiesReferralClients.COL_FACILITY_ID + " IN (" + facilityIds + ")" : "")+
                ") as Female FROM " + ReferralClient.tbName + " Limit 1";
    }

    public String getLTFCountsReportSQL(long appointmentType, String startDate, String endDate, String facilityIds) {
        return "SELECT (" +
                "    SELECT COUNT(" + ClientAppointments.tbName + "." + ClientAppointments.COL_APPOINTMENT_ID + ")as count FROM " + ClientAppointments.tbName +
                " INNER JOIN " + HealthFacilitiesReferralClients.tbName + " USING (" + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + ")\n" +
                " INNER JOIN " + ReferralClient.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " WHERE status = -1 AND " +
                (!facilityIds.equals("") ? HealthFacilitiesReferralClients.tbName+"."+HealthFacilitiesReferralClients.COL_FACILITY_ID + " IN (" + facilityIds + ") AND " : "")+
                ClientAppointments.tbName + ".created_at>'" + startDate + "' AND " +
                ClientAppointments.tbName + ".created_at<'" + endDate + "'  AND " +
                ClientAppointments.COL_APPOINTMENT_TYPE + " = " + appointmentType + " AND " +
                ReferralClient.COL_GENDER + " = 'male'" +
                ") as Male, (" +
                "    SELECT COUNT(" + ClientAppointments.tbName + "." + ClientAppointments.COL_APPOINTMENT_ID + ")as count FROM " + ClientAppointments.tbName +
                " INNER JOIN " + HealthFacilitiesReferralClients.tbName + " USING (" + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + ") " +
                " INNER JOIN " + ReferralClient.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " WHERE status = -1 AND " +
                (!facilityIds.equals("") ? HealthFacilitiesReferralClients.tbName+"."+HealthFacilitiesReferralClients.COL_FACILITY_ID + " IN (" + facilityIds + ") AND " : "")+
                ClientAppointments.tbName + ".created_at>'" + startDate + "' AND " +
                ClientAppointments.tbName + ".created_at<'" + endDate + "'  AND " +
                ClientAppointments.COL_APPOINTMENT_TYPE + " = " + appointmentType + " AND " +
                ReferralClient.COL_GENDER + " = 'female'" +
                ") as female FROM " + ClientAppointments.tbName + " Limit 1";
    }

    public String getFoundLTFCountsReportSQL(long referralFeedbackId, String startDate, String endDate, String facilityIds) {
        return "SELECT (" +
                "    SELECT COUNT(" + ClientAppointments.tbName + "." + ClientAppointments.COL_APPOINTMENT_ID + ")as count FROM " + ClientAppointments.tbName +
                " INNER JOIN " + HealthFacilitiesReferralClients.tbName + " USING (" + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + ")\n" +
                " INNER JOIN " + ReferralClient.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " INNER JOIN " + ClientReferrals.tbName + " ON " + ClientAppointments.tbName + "." + ClientAppointments.COL_FOLLOWUP_REFERRAL_ID + " = " + ClientReferrals.tbName + "." + ClientReferrals.COL_REFERRAL_ID +
                " WHERE status = -1 AND " +
                (!facilityIds.equals("") ? HealthFacilitiesReferralClients.tbName+"."+HealthFacilitiesReferralClients.COL_FACILITY_ID + " IN (" + facilityIds + ") AND " : "")+
                ClientAppointments.tbName + ".created_at > '" + startDate + "' AND " +
                ClientAppointments.tbName + ".created_at < '" + endDate + "'  AND " +
                ClientReferrals.COL_REFERRAL_FEEDBACK_ID + " = " + referralFeedbackId + " AND " +
                ClientReferrals.COL_REFERRAL_STATUS + " = 1 AND " +
                ReferralClient.COL_GENDER + " = 'male'" +
                ") as Male, (" +
                "    SELECT COUNT(" + ClientAppointments.tbName + "." + ClientAppointments.COL_APPOINTMENT_ID + ")as count FROM " + ClientAppointments.tbName +
                " INNER JOIN " + HealthFacilitiesReferralClients.tbName + " USING (" + HealthFacilitiesReferralClients.COL_HEALTH_FACILITY_CLIENT_ID + ")\n" +
                " INNER JOIN " + ReferralClient.tbName + " ON " + HealthFacilitiesReferralClients.tbName + "." + HealthFacilitiesReferralClients.COL_CLIENT_ID + " = " + ReferralClient.tbName + "." + ReferralClient.COL_CLIENT_ID +
                " INNER JOIN " + ClientReferrals.tbName + " ON " + ClientAppointments.tbName + "." + ClientAppointments.COL_FOLLOWUP_REFERRAL_ID + " = " + ClientReferrals.tbName + "." + ClientReferrals.COL_REFERRAL_ID +
                " WHERE status = -1 AND " +
                (!facilityIds.equals("") ? HealthFacilitiesReferralClients.tbName+"."+HealthFacilitiesReferralClients.COL_FACILITY_ID + " IN (" + facilityIds + ") AND " : "")+
                ClientAppointments.tbName + ".created_at>'" + startDate + "' AND " +
                ClientAppointments.tbName + ".created_at<'" + endDate + "'  AND " +
                ClientReferrals.COL_REFERRAL_FEEDBACK_ID + " = " + referralFeedbackId + " AND " +
                ClientReferrals.COL_REFERRAL_STATUS + " = 1 AND " +
                ReferralClient.COL_GENDER + " = 'female'" +
                ") as female FROM " + ClientAppointments.tbName + " Limit 1";
    }

    private String getLTFFollowupReportSQL(long referral_feedback_id, String startDate, String endDate) {
        return "SELECT COUNT(" + ClientReferrals.COL_REFERRAL_ID + ")as count FROM " + ClientReferrals.tbName +
                " WHERE " +
                (referral_feedback_id != 0 ? ClientReferrals.COL_REFERRAL_FEEDBACK_ID + " =" + referral_feedback_id : ClientReferrals.COL_REFERRAL_FEEDBACK_ID + " <>8") + " AND " +
                ClientReferrals.COL_REFERRAL_ID + " IN (SELECT " + ClientAppointments.COL_FOLLOWUP_REFERRAL_ID + " FROM " + ClientAppointments.tbName + " WHERE " + ClientAppointments.COL_STATUS + " = -1 AND " + ClientAppointments.COL_APPOINTMENT_DATE + ">'" + startDate + "' AND " + ClientAppointments.COL_APPOINTMENT_DATE + "<'" + endDate + "')";
    }

    private String getReferralSummaryReportSql(boolean successfulReferrals, long serviceId, String startDate, String endDate) {
        return "SELECT COUNT(*) as count FROM " + ClientReferrals.tbName +
                " WHERE " +
                (successfulReferrals ? ClientReferrals.COL_REFERRAL_STATUS + " = 1 AND " : "") +
                ClientReferrals.COL_REFERRAL_TYPE + "=1 AND " +
                ClientReferrals.COL_SERVICE_ID + " = " + serviceId + " AND " +
                ClientReferrals.COL_REFERRAL_DATE + ">='" + startDate + "' AND " +
                ClientReferrals.COL_REFERRAL_DATE + "<='" + endDate + "'";
    }


}