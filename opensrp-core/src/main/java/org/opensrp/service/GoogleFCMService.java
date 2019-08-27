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
            androidFcmKey = "AAAAEgpJVug:APA91bGCXo-2ut2gyxSmvOmSYSV_0W_RenJO-nkvqmD8wI-39LB0PQzRCMJxqtHKv0lyK22LrEwM3S4V8YJYlC3xKfqLo8pobONQ0fV9WNRqeJhvxe5F19-ql24THsqQ2ePVmp73mlyb";
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
