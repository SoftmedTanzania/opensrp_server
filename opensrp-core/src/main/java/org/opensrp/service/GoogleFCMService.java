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
            androidFcmKey = "AAAA4qLMiVw:APA91bE99vHoRdUA2anZPz2oHM3zqO2uLcM02cqdQKfb9MvdR4yaSqVtaU2H3piCH2a7IT5yyS1KbvEeV_XJotDgB4beWphJDu8Txx0eaB4iSBQRf3Ki8oBCuIKR7vQFbHzyGED4sNLK";

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
