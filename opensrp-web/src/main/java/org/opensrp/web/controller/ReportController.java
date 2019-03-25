package org.opensrp.web.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.connector.openmrs.service.OpenmrsReportingService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.ClientReferrals;
import org.opensrp.domain.HealthFacilities;
import org.opensrp.domain.ReferralClient;
import org.opensrp.domain.ReferralService;
import org.opensrp.dto.*;
import org.opensrp.repository.ClientReferralRepository;
import org.opensrp.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ReportController {

	private OpenmrsReportingService reportService;
	private ClientReferralRepository clientReferralRepository;
	private ClientsRepository clientsRepository;
	private OpenmrsUserService openmrsUserService;
	
	@Autowired
	public ReportController(OpenmrsReportingService reportService, ClientReferralRepository clientReferralRepository,ClientsRepository clientsRepository, OpenmrsUserService openmrsUserService) {
		this.reportService = reportService;
		this.openmrsUserService = openmrsUserService;
		this.clientReferralRepository = clientReferralRepository;
		this.clientsRepository = clientsRepository;
	}
	
	@RequestMapping(method = GET, value = "/report/report-definitions")
    public ResponseEntity<String> reportDefinitions() throws JSONException {
		return new ResponseEntity<>(reportService.getReportDefinitions().toString(),HttpStatus.OK);
    }
	
	@RequestMapping(method = GET, value = "/report/report-data")
    public ResponseEntity<String> reportData(@RequestParam("uuid") String uuid, @RequestParam Map<String, String> params) throws JSONException {
		return new ResponseEntity<>(reportService.getReportData(uuid, params).toString(),HttpStatus.OK);
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
		for(int i=0;i<size;i++){
			try {
				chwUIIDs+="'"+array.getString(i)+"',";
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}


		if ( chwUIIDs.length() > 0 && chwUIIDs.charAt(chwUIIDs.length() - 1) == ',') {
			chwUIIDs = chwUIIDs.substring(0, chwUIIDs.length() - 1);
		}

		try {
			List<CHWReferralsSummaryDTO> chwReferralsSummaryDTOS = clientReferralRepository.getCHWReferralsSummary(
					"SELECT COUNT("+ ClientReferrals.tbName+"."+ ClientReferrals.COL_SERVICE_ID+") as count ,"+ ReferralService.COL_SERVICE_NAME +" as service_name FROM "+ ClientReferrals.tbName +
							" INNER JOIN "+ReferralService.tbName+" ON "+ ClientReferrals.tbName+"."+ ClientReferrals.COL_SERVICE_ID+" = "+ReferralService.tbName+"."+ReferralService.COL_SERVICE_ID +
							" WHERE "+ ClientReferrals.COL_REFERRAL_TYPE+"=1 AND " +
							ClientReferrals.COL_SERVICE_PROVIDER_UIID+" IN ("+chwUIIDs+") AND "+
							ClientReferrals.COL_REFERRAL_DATE+" > '"+fromDate+"' AND "+
							ClientReferrals.COL_REFERRAL_DATE+" <= '"+toDate+"' "+
							" GROUP BY "+ReferralService.COL_SERVICE_NAME,null);


			return new ResponseEntity<List<CHWReferralsSummaryDTO>>(chwReferralsSummaryDTOS,HttpStatus.OK);
		}catch (Exception e){
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
					"SELECT COUNT("+ ClientReferrals.tbName+"."+ ClientReferrals.COL_SERVICE_ID+") as count ,"+ ReferralService.COL_SERVICE_NAME +" as service_name FROM "+ ClientReferrals.tbName +
							" INNER JOIN "+ReferralService.tbName+" ON "+ ClientReferrals.tbName+"."+ ClientReferrals.COL_SERVICE_ID+" = "+ReferralService.tbName+"."+ReferralService.COL_SERVICE_ID +
							" WHERE "+ ClientReferrals.COL_REFERRAL_TYPE+"=1 AND " +
//							ClientReferrals.COL_SERVICE_PROVIDER_UIID+" IN ("+chwUIIDs+") AND "+
							ClientReferrals.COL_REFERRAL_DATE+" > '"+fromDate+"' AND "+
							ClientReferrals.COL_REFERRAL_DATE+" <= '"+toDate+"' "+
							" GROUP BY "+ReferralService.COL_SERVICE_NAME,null);


			return new ResponseEntity<List<CHWReferralsSummaryDTO>>(chwReferralsSummaryDTOS,HttpStatus.OK);
		}catch (Exception e){
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
		for(int i=0;i<size;i++){
			try {

				String facilityName = array.getJSONObject(i).getString("facility_name");
				String facilityId   = array.getJSONObject(i).getString("facility_id");

				List<FacilityDepartmentReferralSummaryDTO> facilityReferralsSummaryDTOS = clientReferralRepository.getFacilityDepartmentReferralsSummary(
						"SELECT COUNT("+ ClientReferrals.tbName+"."+ ClientReferrals.COL_SERVICE_ID+") as count, "+
								ClientReferrals.COL_REFERRAL_SOURCE+" as referral_source, "+
								ClientReferrals.COL_REFERRAL_STATUS+" as referral_status " +
								" FROM "+ ClientReferrals.tbName +
								" INNER JOIN "+ HealthFacilities.tbName +" ON "+ ClientReferrals.tbName+"."+ ClientReferrals.COL_FROM_FACILITY_ID+" = "+HealthFacilities.tbName+"."+HealthFacilities.COL_OPENMRS_UUID +
								" WHERE "+ ClientReferrals.COL_REFERRAL_TYPE+"=2 AND " +
								HealthFacilities.COL_OPENMRS_UUID +" = '"+facilityId+"' AND "+
								ClientReferrals.COL_REFERRAL_DATE+" > '"+fromDate+"' AND "+
								ClientReferrals.COL_REFERRAL_DATE+" <= '"+toDate+"' "+
								" GROUP BY "+ ClientReferrals.COL_REFERRAL_SOURCE+" , "+ ClientReferrals.COL_REFERRAL_STATUS,null);


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


		return new ResponseEntity<List<FacilityReferralsSummaryDTO>>(referralsSummaryDTOS,HttpStatus.OK);


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
		for(int i=0;i<facilitiesArray.length();i++){
			try {
				String facilityName = facilitiesArray.getJSONObject(i).getString("facility_name");
				String facilityId   = facilitiesArray.getJSONObject(i).getString("facility_id");

				List<FacilityProvidersReferralSummaryDTO> facilityProvidersReferralsSummaries = clientReferralRepository.getFacilityProvidersReferralsSummary(
						"SELECT COUNT("+ ClientReferrals.tbName+"."+ ClientReferrals.COL_SERVICE_ID+") as count, "+
								ClientReferrals.COL_REFERRAL_SOURCE+" as referral_source, "+
								ClientReferrals.COL_REFERRAL_STATUS+" as referral_status, " +
								ClientReferrals.COL_SERVICE_PROVIDER_UIID+" as provider_uuid " +
								" FROM "+ ClientReferrals.tbName +
								" INNER JOIN "+HealthFacilities.tbName +" ON "+ ClientReferrals.tbName+"."+ ClientReferrals.COL_FROM_FACILITY_ID+" = "+HealthFacilities.tbName+"."+HealthFacilities.COL_OPENMRS_UUID +
								" WHERE "+ ClientReferrals.COL_REFERRAL_TYPE+"=2 AND " +
								HealthFacilities.COL_OPENMRS_UUID +" = '"+facilityId+"' AND "+
								ClientReferrals.COL_REFERRAL_DATE+" > '"+fromDate+"' AND "+
								ClientReferrals.COL_REFERRAL_DATE+" <= '"+toDate+"' "+
								" GROUP BY "+ ClientReferrals.COL_REFERRAL_SOURCE+" , "+ ClientReferrals.COL_REFERRAL_STATUS+","+ ClientReferrals.COL_SERVICE_PROVIDER_UIID ,null);

				for (FacilityProvidersReferralSummaryDTO facilityProvidersReferralsSummary:facilityProvidersReferralsSummaries) {
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

		return new ResponseEntity<List<IntraFacilityReferralsProvidersSummaryReport>>(referralsSummaryDTOS,HttpStatus.OK);
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
		for(int i=0;i<facilitiesArray.length();i++){
			try {
				String facilityName = facilitiesArray.getJSONObject(i).getString("facility_name");
				String facilityId   = facilitiesArray.getJSONObject(i).getString("facility_id");

				List<InterFacilityReferralSummaryDTO> facilityProvidersReferralsSummaries = clientReferralRepository.getInterFacilityReferralsSummary(
						"SELECT COUNT("+ ClientReferrals.tbName+"."+ ClientReferrals.COL_REFERRAL_ID+") as count, "+
								ClientReferrals.COL_REFERRAL_STATUS+" as referral_status, " +
								"H2."+HealthFacilities.COL_FACILITY_NAME+" as to_facility_name " +
								" FROM "+ ClientReferrals.tbName +
								" INNER JOIN "+HealthFacilities.tbName +" H1 ON "+ ClientReferrals.tbName+"."+ ClientReferrals.COL_FROM_FACILITY_ID+" = H1."+HealthFacilities.COL_OPENMRS_UUID +
								" INNER JOIN "+HealthFacilities.tbName +" H2 ON "+ ClientReferrals.tbName+"."+ ClientReferrals.COL_FACILITY_ID+" = H2."+HealthFacilities.COL_OPENMRS_UUID +
								" WHERE "+ ClientReferrals.COL_REFERRAL_TYPE+"= 3 AND " +
								"H1."+HealthFacilities.COL_OPENMRS_UUID +" = '"+facilityId+"' AND "+
								ClientReferrals.COL_REFERRAL_DATE+" > '"+fromDate+"' AND "+
								ClientReferrals.COL_REFERRAL_DATE+" <= '"+toDate+"' "+
								" GROUP BY H2."+HealthFacilities.COL_FACILITY_NAME+" , "+ ClientReferrals.COL_REFERRAL_STATUS,null);

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

		return new ResponseEntity<List<InterFacilityReferralsSummaryReport>>(referralsSummaryDTOS,HttpStatus.OK);
	}



	@RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/report/referrals-summary")
	@ResponseBody
	public ResponseEntity<List<InterFacilityReferralsSummaryReport>> getReferralsSummaryReport() {


		//Obtain registrations before end of last month
		LocalDate firstDateOfTheMonth = LocalDate.now();

		System.out.println("Months first date in yyyy-mm-dd: " +firstDateOfTheMonth.withDayOfMonth(1));
		String previousMonthRegistrations = generateSql("1970-01-01",firstDateOfTheMonth.withDayOfMonth(1).toString(),"","");


		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE,1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String currentDate= formatter.format(c.getTime());

		String thisMonthRegistrations = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,"","");


		//Less than a year old
		Calendar aYearAgo = Calendar.getInstance();
		aYearAgo.add(Calendar.YEAR,-1);
		String ayearAgoDateString = formatter.format(aYearAgo.getTime());
		String lessThan1year = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,ayearAgoDateString,currentDate);

		//1-5 Years
		Calendar fiveYearsAgo = Calendar.getInstance();
		fiveYearsAgo.add(Calendar.YEAR,-5);
		String fiveYearsAgoDateString = formatter.format(fiveYearsAgo.getTime());
		String _1to5 = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,fiveYearsAgoDateString,ayearAgoDateString);

		//6 to 9 years
		Calendar nineYearsAgo = Calendar.getInstance();
		nineYearsAgo.add(Calendar.YEAR,-9);
		String nineYearsAgoDateString = formatter.format(nineYearsAgo.getTime());
		String _6to9 = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,nineYearsAgoDateString,fiveYearsAgoDateString);

		//10 to 14 years
		Calendar fourteenYearsAgo = Calendar.getInstance();
		fourteenYearsAgo.add(Calendar.YEAR,-14);
		String fourteenYearsAgoAgoDateString = formatter.format(fourteenYearsAgo.getTime());
		String _10To14 = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,fourteenYearsAgoAgoDateString,nineYearsAgoDateString);

		//15 to 19 years
		Calendar nineteenYearsAgo = Calendar.getInstance();
		nineteenYearsAgo.add(Calendar.YEAR,-19);
		String nineteenYearsAgoDateString = formatter.format(nineteenYearsAgo.getTime());
		String _15To19 = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,nineteenYearsAgoDateString,fourteenYearsAgoAgoDateString);

		//20 to 24 years
		Calendar twentyFourYearsAgo = Calendar.getInstance();
		twentyFourYearsAgo.add(Calendar.YEAR,-24);
		String twentyFourYearsAgoDateString = formatter.format(twentyFourYearsAgo.getTime());
		String _20To24 = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,twentyFourYearsAgoDateString,nineteenYearsAgoDateString);

		//25 to 49 years
		Calendar fortyNineYearsAgo = Calendar.getInstance();
		fortyNineYearsAgo.add(Calendar.YEAR,-49);
		String fortyNineYearsAgosAgoDateString = formatter.format(fortyNineYearsAgo.getTime());
		String _25To49 = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,fortyNineYearsAgosAgoDateString,twentyFourYearsAgoDateString);

		//50 to 59 years
		Calendar fiftyNineYearsAgo = Calendar.getInstance();
		fiftyNineYearsAgo.add(Calendar.YEAR,-59);
		String fiftyNineYearsAgoDateString = formatter.format(fiftyNineYearsAgo.getTime());
		String _50To59 = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,fiftyNineYearsAgoDateString,fortyNineYearsAgosAgoDateString);

		String _60Above = generateSql(firstDateOfTheMonth.withDayOfMonth(1).toString(),currentDate,fiftyNineYearsAgoDateString,"");

		System.out.println("Less than A year = "+lessThan1year);
		System.out.println("1 to 5 = "+_1to5);
		System.out.println("6 to 9 = "+_6to9);
		System.out.println("10 to 14 = "+_10To14);
		System.out.println("15 to 19 = "+_15To19);
		System.out.println("20 to 24 = "+_20To24);
		System.out.println("25 to 49 = "+_25To49);
		System.out.println("50 to 59 = "+_50To59);
		System.out.println("Above 60 = "+_60Above);


		return new ResponseEntity<List<InterFacilityReferralsSummaryReport>>(HttpStatus.OK);
	}


	private String generateSql(String startDate, String endDate, String startBirthDate,String endBirthDate){
		return  "SELECT (SELECT COUNT("+ ReferralClient.COL_CLIENT_ID+") FROM "+ ReferralClient.tbName +
				" WHERE "+ReferralClient.COL_GENDER+"='Male' AND "+
				ReferralClient.COL_CREATED_AT+">='"+startDate+"' AND " +
				ReferralClient.COL_CREATED_AT+"<'"+endDate+"'"+
				(!startBirthDate.equals("")?" AND "+ReferralClient.COL_DATE_OF_BIRTH+" >= '"+startBirthDate+"'":"")+
				(!endBirthDate.equals("")?" AND "+ReferralClient.COL_DATE_OF_BIRTH+" < '"+endBirthDate+"'":"")+
				") as Male, " +
				"(SELECT COUNT("+ReferralClient.COL_CLIENT_ID+") FROM "+ReferralClient.tbName+
				" WHERE "+ReferralClient.COL_GENDER+"='Female' AND " +
				ReferralClient.COL_CREATED_AT+">='"+startDate+"' AND " +
				ReferralClient.COL_CREATED_AT+"<'"+endDate+"' "+
				(!startBirthDate.equals("")?" AND "+ReferralClient.COL_DATE_OF_BIRTH+" >= '"+startBirthDate+"'":"")+
				(!endBirthDate.equals("")?" AND "+ReferralClient.COL_DATE_OF_BIRTH+" < '"+endBirthDate+"'":"")
				+") as Female FROM "+ReferralClient.tbName+" Limit 1";
	}
}
