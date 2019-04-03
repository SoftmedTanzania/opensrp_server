package org.opensrp.service;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.LocalDate;
import org.opensrp.domain.ClientAppointments;
import org.opensrp.domain.ClientReferrals;
import org.opensrp.domain.ClientRegistrationReason;
import org.opensrp.domain.ReferralClient;
import org.opensrp.dto.RegistrationReasonsReportDTO;
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


    private ReferralServiceRepository referralServiceRepository;


    public JRDataSource newRegistrationByReasonsReport() {
        LocalDate firstDateOfTheMonth = LocalDate.now();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = formatter.format(c.getTime());

        List<ClientRegistrationReason> clientRegistrationReasons = null;
        try {
            clientRegistrationReasons = clientRegistrationReasonRepository.geRegistrationReasons("SELECT * FROM " + ClientRegistrationReason.tbName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<RegistrationReasonsReportDTO> registrationReasonsReportDTOS = new ArrayList<>();
        for (ClientRegistrationReason clientRegistrationReason : clientRegistrationReasons) {
            RegistrationReasonsReportDTO registrationReasonsReportDTO = new RegistrationReasonsReportDTO();
            registrationReasonsReportDTO.setRegistrationReason(clientRegistrationReason.getDescEn());
            int totalMale = 0, totalFemale = 0;

            String lessThan1year = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, getDateByYearString(-1), currentDate, clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> lessThan1yearRegistrationsList = null;
            try {
                lessThan1yearRegistrationsList = clientsRepository.getMaleFemaleCountReports(lessThan1year, null);
                registrationReasonsReportDTO.setLessThan1Male(lessThan1yearRegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setLessThan1Female(lessThan1yearRegistrationsList.get(0).getFemale());

                totalMale += lessThan1yearRegistrationsList.get(0).getMale();
                totalFemale += lessThan1yearRegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _1to5 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, getDateByYearString(-5), getDateByYearString(-1), clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> _1to5RegistrationsList = null;
            try {
                _1to5RegistrationsList = clientsRepository.getMaleFemaleCountReports(_1to5, null);
                registrationReasonsReportDTO.setOneTofiveMale(_1to5RegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setOneTofiveFemale(_1to5RegistrationsList.get(0).getFemale());

                totalMale += _1to5RegistrationsList.get(0).getMale();
                totalFemale += _1to5RegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _6to9 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, getDateByYearString(-9), getDateByYearString(-5), clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> _6to9RegistrationsList = null;
            try {
                _6to9RegistrationsList = clientsRepository.getMaleFemaleCountReports(_6to9, null);
                registrationReasonsReportDTO.setSixToNineMale(_6to9RegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setSixToNineFemale(_6to9RegistrationsList.get(0).getFemale());

                totalMale += _6to9RegistrationsList.get(0).getMale();
                totalFemale += _6to9RegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _10To14 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, getDateByYearString(-14), getDateByYearString(-9), clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> _10To14RegistrationsList = null;
            try {
                _10To14RegistrationsList = clientsRepository.getMaleFemaleCountReports(_10To14, null);
                registrationReasonsReportDTO.setTenToFourteenMale(_10To14RegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setSixToNineFemale(_10To14RegistrationsList.get(0).getFemale());

                totalMale += _10To14RegistrationsList.get(0).getMale();
                totalFemale += _10To14RegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _15To19 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, getDateByYearString(-19), getDateByYearString(-14), clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> _15To19RegistrationsList = null;
            try {
                _15To19RegistrationsList = clientsRepository.getMaleFemaleCountReports(_15To19, null);
                registrationReasonsReportDTO.setFifteenToNineteenMale(_15To19RegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setFifteenToNineteenFemale(_15To19RegistrationsList.get(0).getFemale());

                totalMale += _15To19RegistrationsList.get(0).getMale();
                totalFemale += _15To19RegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _20To24 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, getDateByYearString(-24), getDateByYearString(-19), clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> _20To24RegistrationsList = null;
            try {
                _20To24RegistrationsList = clientsRepository.getMaleFemaleCountReports(_20To24, null);
                registrationReasonsReportDTO.setTwentyToTwentyFourMale(_20To24RegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setTwentyToTwentyFourFemale(_20To24RegistrationsList.get(0).getFemale());


                totalMale += _20To24RegistrationsList.get(0).getMale();
                totalFemale += _20To24RegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _25To49 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, getDateByYearString(-49), getDateByYearString(-24), clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> _25To49RegistrationsList = null;
            try {
                _25To49RegistrationsList = clientsRepository.getMaleFemaleCountReports(_25To49, null);
                registrationReasonsReportDTO.setTwentyFiveToFourtyNineMale(_25To49RegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setTwentyFiveToFourtyNineFemale(_25To49RegistrationsList.get(0).getFemale());


                totalMale += _25To49RegistrationsList.get(0).getMale();
                totalFemale += _25To49RegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _50To59 = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, getDateByYearString(-59), getDateByYearString(-49), clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> _50To59RegistrationsList = null;
            try {
                _50To59RegistrationsList = clientsRepository.getMaleFemaleCountReports(_50To59, null);
                registrationReasonsReportDTO.setFiftyToFiftyNineMale(_50To59RegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setFiftyToFiftyNineFemale(_50To59RegistrationsList.get(0).getFemale());

                totalMale += _50To59RegistrationsList.get(0).getMale();
                totalFemale += _50To59RegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String _60Above = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, "", getDateByYearString(-59), clientRegistrationReason.getRegistrationId());

            List<MaleFemaleCountObject> _60AboveRegistrationsList = null;
            try {
                _60AboveRegistrationsList = clientsRepository.getMaleFemaleCountReports(_60Above, null);
                registrationReasonsReportDTO.setAboveSixtyMale(_60AboveRegistrationsList.get(0).getMale());
                registrationReasonsReportDTO.setAboveSixtyFemale(_60AboveRegistrationsList.get(0).getFemale());


                totalMale += _60AboveRegistrationsList.get(0).getMale();
                totalFemale += _60AboveRegistrationsList.get(0).getFemale();
            } catch (Exception e) {
                e.printStackTrace();
            }

            registrationReasonsReportDTO.setTotalMale(totalMale);
            registrationReasonsReportDTO.setTotalFemale(totalFemale);

            registrationReasonsReportDTOS.add(registrationReasonsReportDTO);
        }

        JRDataSource ds = new JRBeanCollectionDataSource(registrationReasonsReportDTOS);
        return ds;
    }


    private String getDateByYearString(int year) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        //Less than a year old
        Calendar aYearAgo = Calendar.getInstance();
        aYearAgo.add(Calendar.YEAR, year);
        String dateString = formatter.format(aYearAgo.getTime());
        return dateString;
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

    private String getLTFCountsReportSQL(long appointmentType, String startDate, String endDate) {
        return "SELECT COUNT(" + ClientAppointments.COL_APPOINTMENT_ID + ")as count FROM " + ClientAppointments.tbName +
                " WHERE " +
                ClientAppointments.COL_STATUS + " = -1 AND " +
                ClientAppointments.COL_APPOINTMENT_DATE + ">'" + startDate + "' AND " +
                ClientAppointments.COL_APPOINTMENT_DATE + "<'" + endDate + "' " +
                (appointmentType != 0 ? " AND " + ClientAppointments.COL_APPOINTMENT_TYPE + " = " + appointmentType : "");
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


//	//Obtain registrations before end of last month
//	LocalDate firstDateOfTheMonth = LocalDate.now();
//	JSONObject referralSummaryReport = new JSONObject();
//
//        try {
//				referralSummaryReport.put("organisation",new JSONObject("{\n" +
//				"      \"organisation_name\": \"HOSPITALI YA MANISPAA YA MOROGORO\",\n" +
//				"      \"organisation_email\": \"dsh@gmail.com\",\n" +
//				"      \"organisation_tel\": \"0789-121 321\",\n" +
//				"      \"organisation_address\": \"Morogoro\"\n" +
//				"    }"));
//				} catch (JSONException e) {
//				e.printStackTrace();
//				}
//
//				String previousMonthRegistrations = generateRegistationReportSql("1970-01-01", firstDateOfTheMonth.withDayOfMonth(1).toString(), "", "", 0);
//
//				System.out.println("previousMonthRegistrations SQL " + previousMonthRegistrations);
//				List<MaleFemaleCountObject> accumulativeTotalPreviousMonthsRegistrations = null;
//		try {
//		accumulativeTotalPreviousMonthsRegistrations = clientsRepository.getMaleFemaleCountReports(previousMonthRegistrations, null);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//		try {
//		referralSummaryReport.put("cumulativeRegistrations", new JSONObject(new Gson().toJson(accumulativeTotalPreviousMonthsRegistrations.get(0))));
//		} catch (JSONException e) {
//		e.printStackTrace();
//		}
//
//
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DATE, 1);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
//		String currentDate = formatter.format(c.getTime());
//
//
//		JSONObject thisMonthRegistrationsObject = new JSONObject();
//
//		String thisMonthRegistrations = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, "", "", 0);
//
//		List<MaleFemaleCountObject> thisMonthRegistrationsList = null;
//		try {
//		thisMonthRegistrationsList = clientsRepository.getMaleFemaleCountReports(thisMonthRegistrations, null);
//		thisMonthRegistrationsObject.put("JUMLA", new JSONObject(new Gson().toJson(thisMonthRegistrationsList.get(0))));
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//		System.out.println("reports this month registrations total = " + new Gson().toJson(thisMonthRegistrationsList));
//
//
//
//
//
//
//		try {
//		referralSummaryReport.put("thisMonthRegistrations", thisMonthRegistrationsObject);
//		} catch (JSONException e) {
//		e.printStackTrace();
//		}
//
//
//		JSONArray registationReasonsObject = new JSONArray();
//
//		List<ClientRegistrationReason> clientRegistrationReasons = null;
//		try {
//		clientRegistrationReasons = clientRegistrationReasonRepository.geRegistrationReasons("SELECT * FROM " + ClientRegistrationReason.tbName, null);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//		for (ClientRegistrationReason clientRegistrationReason : clientRegistrationReasons) {
//		String sqlString = generateRegistationReportSql(firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate, "", "", clientRegistrationReason.getRegistrationId());
//		List<MaleFemaleCountObject> objects = null;
//		try {
//		objects = clientsRepository.getMaleFemaleCountReports(sqlString, null);
//		JSONObject o = new JSONObject();
//		o.put(clientRegistrationReason.getDescSw(), new JSONObject(new Gson().toJson(objects.get(0))));
//
//		registationReasonsObject.put(o);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//		}
//
//		try {
//		referralSummaryReport.put("Registered Clients By Registration Reasons", registationReasonsObject);
//		} catch (JSONException e) {
//		e.printStackTrace();
//		}
//
//
//		String totalLTFsReferrals = getLTFCountsReportSQL(0, firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
//
//		JSONObject ltfReferrals = new JSONObject();
//		List<TotalCountObject> totalObjects = null;
//		try {
//		totalObjects = clientsAppointmentsRepository.getCount(totalLTFsReferrals, null);
//		ltfReferrals.put("Jumla", totalObjects.get(0).getCount());
//		} catch (Exception e) {
//		e.printStackTrace();
//		try {
//		ltfReferrals.put("Jumla", 0);
//		} catch (JSONException e1) {
//		e1.printStackTrace();
//		}
//
//		}
//
//
//		List<AppointmentType> appointmentTypes = null;
//		try {
//		appointmentTypes = appointmentTypeRepository.getAppointmentTypes("Select * from " + AppointmentType.tbName, null);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//
//		for (int i = 1; i < 5; i++) {
//		List<TotalCountObject> objectList = null;
//		try {
//		String sqlString = getLTFCountsReportSQL(i, firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
//		objectList = clientsAppointmentsRepository.getCount(sqlString, null);
//		ltfReferrals.put(appointmentTypes.get(i - 1).getName(), objectList.get(0).getCount());
//		} catch (Exception e) {
//		e.printStackTrace();
//		try {
//		ltfReferrals.put(appointmentTypes.get(i - 1).getName(), 0);
//		} catch (JSONException e1) {
//		e1.printStackTrace();
//		}
//		}
//		}
//
//		try {
//		referralSummaryReport.put("All Issued Lost Followup Referrals", ltfReferrals);
//		} catch (JSONException e) {
//		e.printStackTrace();
//		}
//
//
//		JSONObject ltfReferralResults = new JSONObject();
//
//		String totalFound = getLTFFollowupReportSQL(0, firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
//		List<TotalCountObject> totalFoundList = null;
//		try {
//		totalFoundList = clientReferralRepository.getCount(totalFound, null);
//		ltfReferralResults.put("Jumla ya waliopatikana", totalFoundList.get(0).getCount());
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//
//		List<ReferralFeedback> referralFeedbacks = null;
//		try {
//		referralFeedbacks = referralFeedbackRepository.getReferralFeedback("SELECT * FROM " + ReferralFeedback.tbName, null);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//		for (ReferralFeedback referralFeedback : referralFeedbacks) {
//		String foundLTFSQL = getLTFFollowupReportSQL(referralFeedback.getId(), firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
//
//		List<TotalCountObject> foundLTFSList = null;
//		try {
//		foundLTFSList = clientReferralRepository.getCount(foundLTFSQL, null);
//		ltfReferralResults.put(referralFeedback.getDescSw(), foundLTFSList.get(0).getCount());
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//		}
//
//
//		try {
//		referralSummaryReport.put("Found LTF Referrals Results", ltfReferralResults);
//		} catch (JSONException e) {
//		e.printStackTrace();
//		}
//
//
//		//issued referrals
//		JSONObject issuedReferrals = new JSONObject();
//		List<ReferralService> allReferralServices = null;
//		try {
//		allReferralServices = referralServiceRepository.getReferralServices("Select * from " + ReferralService.tbName, null);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//		int totalIssuedReferrals = 0;
//		for (int i = 1; i < 14; i++) {
//
//		String serviceReferralsSQL = getReferralSummaryReportSql(false, allReferralServices.get(i - 1).getServiceId(), firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
//		List<TotalCountObject> serviceReferralsList = null;
//		try {
//		serviceReferralsList = clientReferralRepository.getCount(serviceReferralsSQL, null);
//		issuedReferrals.put(allReferralServices.get(i - 1).getServiceNameSw(), serviceReferralsList.get(0).getCount());
//		totalIssuedReferrals += serviceReferralsList.get(0).getCount();
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//		}
//
//
//		try {
//		issuedReferrals.put("Jumla ", totalIssuedReferrals);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//		try {
//		referralSummaryReport.put("Issued Referrals ", issuedReferrals);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//
//		//successful referrals
//		JSONObject succcessfulReferrals = new JSONObject();
//
//		int totalSuccessfulReferrals = 0;
//		for (int i = 1; i < 14; i++) {
//		String successfulReferralsSQL = getReferralSummaryReportSql(false, allReferralServices.get(i - 1).getServiceId(), firstDateOfTheMonth.withDayOfMonth(1).toString(), currentDate);
//		List<TotalCountObject> successfulReferralsList = null;
//		try {
//		successfulReferralsList = clientReferralRepository.getCount(successfulReferralsSQL, null);
//		succcessfulReferrals.put(allReferralServices.get(i - 1).getServiceNameSw(), successfulReferralsList.get(0).getCount());
//		totalSuccessfulReferrals += successfulReferralsList.get(0).getCount();
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//		}
//
//		try {
//		succcessfulReferrals.put("Jumla ", totalSuccessfulReferrals);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//		try {
//		referralSummaryReport.put("Successful Referrals ", succcessfulReferrals);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//
//
//		return new ResponseEntity<String>(referralSummaryReport.toString(), HttpStatus.OK);