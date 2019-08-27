package org.opensrp.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleFCMService {

    public void SendPushNotification(JSONObject msg, JSONArray to, boolean toFacilityUsers) {
        try {
            String androidFcmKey;
            if(toFacilityUsers) {
                //Facility client app FCM server key
                androidFcmKey = "AAAATCjcWhU:APA91bEit29ckqT15O-xmdEwHi2B0t6aP87qC7blWbJ1PEmC0pZ1q7h6dN6EIcwY2QWKVlnZd9fnriXuFNW4z7_8alawRPDyrUWSnBG_oS5ri4PBkAbb6vRyZCt8d56crY7Az3LUiM_1";
            }else{
                //CHW app FCM server key

                //staging server key
				androidFcmKey = "AAAA1dpcyn4:APA91bFMMs4j70dcm9G914cSxEGnVPMREOb9eqwqPYFq4mX6Bqp-GpiIBPGLcJUS0Ux1DzukFCdRQQe6_gcIzdlkObQw9jOeO681QYYnpoCfeTTVou17tG7dv83hiT38zBXOkKcpQw9D";

                //Live server key
//                androidFcmKey = "AAAA4qLMiVw:APA91bE99vHoRdUA2anZPz2oHM3zqO2uLcM02cqdQKfb9MvdR4yaSqVtaU2H3piCH2a7IT5yyS1KbvEeV_XJotDgB4beWphJDu8Txx0eaB4iSBQRf3Ki8oBCuIKR7vQFbHzyGED4sNLK";

            }
            String androidFcmUrl = "https://fcm.googleapis.com/fcm/send";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "key=" + androidFcmKey);
            httpHeaders.set("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            json.put("data", msg);
            json.put("registration_ids", to);

            System.out.println("FCM Data:" + json.toString());

            HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), httpHeaders);
            String response = restTemplate.postForObject(androidFcmUrl, httpEntity, String.class);
            System.out.println("FCM:" + response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
