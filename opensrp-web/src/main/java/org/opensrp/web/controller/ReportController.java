package org.opensrp.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.connector.openmrs.service.OpenmrsReportingService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.HealthFacilities;
import org.opensrp.domain.PatientReferral;
import org.opensrp.domain.ReferralService;
import org.opensrp.dto.*;
import org.opensrp.repository.PatientReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ReportController {

	private OpenmrsReportingService reportService;
	private PatientReferralRepository patientReferralRepository;
	private OpenmrsUserService openmrsUserService;
	
	@Autowired
	public ReportController(OpenmrsReportingService reportService,PatientReferralRepository patientReferralRepository,OpenmrsUserService openmrsUserService) {
		this.reportService = reportService;
		this.openmrsUserService = openmrsUserService;
		this.patientReferralRepository = patientReferralRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/report/report-definitions")
    public ResponseEntity<String> reportDefinitions() throws JSONException {
		return new ResponseEntity<>(reportService.getReportDefinitions().toString(),HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/report/report-data")
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
			List<CHWReferralsSummaryDTO> chwReferralsSummaryDTOS = patientReferralRepository.getCHWReferralsSummary(
					"SELECT COUNT("+ PatientReferral.tbName+"."+PatientReferral.COL_SERVICE_ID+") as count ,"+ ReferralService.COL_REFERRAL_SERVICE_NAME+" as service_name FROM "+PatientReferral.tbName +
							" INNER JOIN "+ReferralService.tbName+" ON "+PatientReferral.tbName+"."+PatientReferral.COL_SERVICE_ID+" = "+ReferralService.tbName+"."+ReferralService.COL_REFERRAL_SERVICE_ID +
							" WHERE "+PatientReferral.COL_REFERRAL_TYPE+"=1 AND " +
							PatientReferral.COL_SERVICE_PROVIDER_UIID+" IN ("+chwUIIDs+") AND "+
							PatientReferral.COL_REFERRAL_DATE+" > '"+fromDate+"' AND "+
							PatientReferral.COL_REFERRAL_DATE+" <= '"+toDate+"' "+
							" GROUP BY "+ReferralService.COL_REFERRAL_SERVICE_NAME,null);


			return new ResponseEntity<List<CHWReferralsSummaryDTO>>(chwReferralsSummaryDTOS,HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<List<CHWReferralsSummaryDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/report/get-facility-referrals-summary")
	@ResponseBody
	public ResponseEntity<List<FacilityReferralsSummaryDTO>> getFacilityReferralsSummaryReport(@RequestBody String json) {
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

				List<FacilityDepartmentReferralSummaryDTO> facilityReferralsSummaryDTOS = patientReferralRepository.getFacilityDepartmentReferralsSummary(
						"SELECT COUNT("+PatientReferral.tbName+"."+PatientReferral.COL_SERVICE_ID+") as count, "+
								PatientReferral.COL_REFERRAL_SOURCE+" as referral_source, "+
								PatientReferral.COL_REFERRAL_STATUS+" as referral_status " +
								" FROM "+PatientReferral.tbName +
								" INNER JOIN "+ HealthFacilities.tbName +" ON "+PatientReferral.tbName+"."+PatientReferral.COL_FROM_FACILITY_ID+" = "+HealthFacilities.tbName+"."+HealthFacilities.COL_OPENMRS_UIID+
								" WHERE "+PatientReferral.COL_REFERRAL_TYPE+"=2 AND " +
								HealthFacilities.COL_HFR_CODE+" = '"+facilityId+"' AND "+
								PatientReferral.COL_REFERRAL_DATE+" > '"+fromDate+"' AND "+
								PatientReferral.COL_REFERRAL_DATE+" <= '"+toDate+"' "+
								" GROUP BY "+PatientReferral.COL_REFERRAL_SOURCE+" , "+PatientReferral.COL_REFERRAL_STATUS,null);


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


	@RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/report/get-facility-providers-referrals-summary")
	@ResponseBody
	public ResponseEntity<List<FacilityReferralsProvidersSummaryDTO>> getFacilityProvidersReferralsSummaryReport(@RequestBody String json) {
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

		List<FacilityReferralsProvidersSummaryDTO> referralsSummaryDTOS = new ArrayList<>();
		for(int i=0;i<facilitiesArray.length();i++){
			try {
				String facilityName = facilitiesArray.getJSONObject(i).getString("facility_name");
				String facilityId   = facilitiesArray.getJSONObject(i).getString("facility_id");

				List<FacilityProvidersReferralSummaryDTO> facilityProvidersReferralsSummaries = patientReferralRepository.getFacilityProvidersReferralsSummary(
						"SELECT COUNT("+PatientReferral.tbName+"."+PatientReferral.COL_SERVICE_ID+") as count, "+
								PatientReferral.COL_REFERRAL_SOURCE+" as referral_source, "+
								PatientReferral.COL_REFERRAL_STATUS+" as referral_status, " +
								PatientReferral.COL_SERVICE_PROVIDER_UIID+" as provider_uuid " +
								" FROM "+PatientReferral.tbName +
								" INNER JOIN "+HealthFacilities.tbName +" ON "+PatientReferral.tbName+"."+PatientReferral.COL_FROM_FACILITY_ID+" = "+HealthFacilities.tbName+"."+HealthFacilities.COL_OPENMRS_UIID+
								" WHERE "+PatientReferral.COL_REFERRAL_TYPE+"=2 AND " +
								HealthFacilities.COL_HFR_CODE+" = '"+facilityId+"' AND "+
								PatientReferral.COL_REFERRAL_DATE+" > '"+fromDate+"' AND "+
								PatientReferral.COL_REFERRAL_DATE+" <= '"+toDate+"' "+
								" GROUP BY "+PatientReferral.COL_REFERRAL_SOURCE+" , "+PatientReferral.COL_REFERRAL_STATUS+","+PatientReferral.COL_SERVICE_PROVIDER_UIID ,null);

				for (FacilityProvidersReferralSummaryDTO facilityProvidersReferralsSummary:facilityProvidersReferralsSummaries) {
					facilityProvidersReferralsSummary.setProviderName(openmrsUserService.getTeamMember(facilityProvidersReferralsSummary.getProviderUuid()).getString("display"));
				}

				FacilityReferralsProvidersSummaryDTO facilityReferralsSummaryDTO = new FacilityReferralsProvidersSummaryDTO();

				facilityReferralsSummaryDTO.setFacilityName(facilityName);
				facilityReferralsSummaryDTO.setSummaryDTOS(facilityProvidersReferralsSummaries);

				referralsSummaryDTOS.add(facilityReferralsSummaryDTO);



			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ResponseEntity<List<FacilityReferralsProvidersSummaryDTO>>(referralsSummaryDTOS,HttpStatus.OK);
	}
}
