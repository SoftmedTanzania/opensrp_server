package org.opensrp.web.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.domain.Time;
import org.opensrp.api.domain.User;
import org.opensrp.api.util.LocationTree;
import org.opensrp.common.domain.UserDetail;
import org.opensrp.connector.openmrs.service.OpenmrsLocationService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.HealthFacilities;
import org.opensrp.repository.HealthFacilityRepository;
import org.opensrp.web.security.DrishtiAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.*;

import static org.opensrp.web.HttpHeaderFactory.allowOrigin;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserController {
    private String opensrpSiteUrl;
    private DrishtiAuthenticationProvider opensrpAuthenticationProvider;
    private OpenmrsLocationService openmrsLocationService;
    private OpenmrsUserService openmrsUserService;

    private HealthFacilityRepository facilityRepository;

    @Autowired
    public UserController(OpenmrsLocationService openmrsLocationService, OpenmrsUserService openmrsUserService,
                          DrishtiAuthenticationProvider opensrpAuthenticationProvider, HealthFacilityRepository facilityRepository) {
        this.openmrsLocationService = openmrsLocationService;
        this.openmrsUserService = openmrsUserService;
        this.opensrpAuthenticationProvider = opensrpAuthenticationProvider;
        this.facilityRepository = facilityRepository;
    }

    @RequestMapping(method = GET, value = "/authenticate-user")
    public ResponseEntity<String> authenticateUser(HttpServletRequest request) {
        User u = currentUser(request);
        JSONObject tm = null;
        try {
            tm = openmrsUserService.getTeamMember(u.getAttribute("_PERSON_UUID").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(tm.toString(), allowOrigin(opensrpSiteUrl), OK);
    }

    public Authentication getAuthenticationAdvisor(HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.decode(base64Credentials.getBytes()), Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":", 2);

            return new UsernamePasswordAuthenticationToken(values[0], values[1]);
        }
        return null;
    }

    public DrishtiAuthenticationProvider getAuthenticationProvider() {
        return opensrpAuthenticationProvider;
    }

    public User currentUser(HttpServletRequest request) {
        Authentication a = getAuthenticationAdvisor(request);
        return getAuthenticationProvider().getDrishtiUser(a, a.getName());
    }

    public Time getServerTime() {
        return new Time(Calendar.getInstance().getTime(), TimeZone.getDefault());
    }

    @RequestMapping(method = GET, value = "/user-details")
    public ResponseEntity<UserDetail> userDetail(@RequestParam("anm-id") String anmIdentifier, HttpServletRequest request) {
        Authentication a = getAuthenticationAdvisor(request);
        User user = opensrpAuthenticationProvider.getDrishtiUser(a, anmIdentifier);
        return new ResponseEntity<>(new UserDetail(user.getUsername(), user.getRoles()), allowOrigin(opensrpSiteUrl), OK);
    }

    @RequestMapping("/security/authenticate")
    @ResponseBody
    public ResponseEntity<String> authenticate(HttpServletRequest request) throws JSONException {
        User u = currentUser(request);
        String lid = "";
        JSONObject tm = null;
        try {
            tm = openmrsUserService.getTeamMember(u.getAttribute("_PERSON_UUID").toString());
            JSONArray locs = tm.getJSONArray("locations");
            for (int i = 0; i < locs.length(); i++) {
                lid += locs.getJSONObject(i).getString("uuid") + ";;";
            }
        } catch (Exception e) {
            System.out.println("USER Location info not mapped in team management module. Now trying Person Attribute");
        }
        if (StringUtils.isEmptyOrWhitespaceOnly(lid)) {
            lid = (String) u.getAttribute("Location");
            if (StringUtils.isEmptyOrWhitespaceOnly(lid)) {
                String lids = (String) u.getAttribute("Locations");

                if (lids == null) {
                    throw new RuntimeException("User not mapped on any location. Make sure that user have a person attribute Location or Locations with uuid(s) of valid OpenMRS Location(s) separated by ;;");
                }

                lid = lids;
            }
        }
        LocationTree l = openmrsLocationService.getLocationTreeOf(lid.split(";;"));
        Map<String, Object> map = new HashMap<>();
        map.put("user", u);
        try {
            Map<String, Object> tmap = new Gson().fromJson(tm.toString(), new TypeToken<HashMap<String, Object>>() {
            }.getType());
            map.put("team", tmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("locations", l);
        Time t = getServerTime();
        map.put("time", t);
        return new ResponseEntity<>(new Gson().toJson(map), allowOrigin(opensrpSiteUrl), OK);
    }

    @RequestMapping("/security/delete_session")
    @ResponseBody
    public ResponseEntity<String> deleteSession(HttpServletRequest request) throws JSONException {
        Authentication a = getAuthenticationAdvisor(request);

        Boolean b = getAuthenticationProvider().deleteCurrentUserSession(a, a.getName());

        return new ResponseEntity<>(new Gson().toJson(b), allowOrigin(opensrpSiteUrl), OK);
    }

    @RequestMapping("/security/configuration")
    @ResponseBody
    public ResponseEntity<String> configuration() throws JSONException {
        Map<String, Object> map = new HashMap<>();
        map.put("serverDatetime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        return new ResponseEntity<>(new Gson().toJson(map), allowOrigin(opensrpSiteUrl), OK);
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/get-team-members-by-facility-uuid")
    public ResponseEntity<String> getTeamMembers(@RequestBody String jsonData) {
        List<String> facilitiesOPENMRSUUids = new Gson().fromJson(jsonData, new TypeToken<List<String>>() {
        }.getType());



        System.out.println("FACILITY-UUID-LIST : " + new Gson().toJson(facilitiesOPENMRSUUids));
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = openmrsUserService.getCHWsByFacilityId(facilitiesOPENMRSUUids);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(jsonArray.toString(), OK);
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/get-team-members-by-facility-hierarchy/{facilityUUID}")
    public ResponseEntity<String> getCHWsCount(@PathVariable("facilityUUID") String facilityUUID) {
        List<String> facilityUUIDs = new ArrayList<>();
        try {
            JSONArray allLocations = openmrsLocationService.getAllLocations();
            for (int i = 0; i < allLocations.length(); i++) {
                JSONObject facilityObject = allLocations.getJSONObject(i);
                if (facilityObject.getString("uuid").equalsIgnoreCase(facilityUUID)) {
                    facilityUUIDs = getChildrenFacilities(facilityObject, allLocations);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        System.out.println("Location Tree : " + new Gson().toJson(facilityUUIDs));
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = openmrsUserService.getCHWsByFacilityId(facilityUUIDs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(jsonArray.length() + "", OK);

    }

    public List<String> getChildrenFacilities(JSONObject facilityObject, JSONArray allFacilities) {
        List<String> uuids = new ArrayList<>();

        JSONArray childrenLocations = null;
        try {
            childrenLocations = facilityObject.getJSONArray("childLocations");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (childrenLocations.length() == 0) {
            try {
                uuids.add(facilityObject.getString("uuid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        for (int i = 0; i < childrenLocations.length(); i++) {
            JSONObject childLocation = null;
            try {
                childLocation = childrenLocations.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < allFacilities.length(); j++) {
                JSONObject childFacility = null;
                try {
                    childFacility = allFacilities.getJSONObject(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (childFacility.getString("uuid").equalsIgnoreCase(childLocation.getString("uuid"))) {
                        List<String> childLocationsUUIDs = getChildrenFacilities(childFacility, allFacilities);
                        uuids.addAll(childLocationsUUIDs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        return uuids;

    }
}
