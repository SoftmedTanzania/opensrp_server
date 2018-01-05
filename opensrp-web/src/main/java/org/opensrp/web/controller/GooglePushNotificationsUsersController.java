package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.BoreshaAfyaService;
import org.opensrp.domain.GooglePushNotificationsUsers;
import org.opensrp.dto.BoreshaAfyaServiceDTO;
import org.opensrp.dto.GooglePushNotificationsUsersDTO;
import org.opensrp.repository.GooglePushNotificationsUsersRepository;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class GooglePushNotificationsUsersController {
    private static Logger logger = LoggerFactory.getLogger(GooglePushNotificationsUsersController.class.toString());
    private GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository;
    private TaskSchedulerService scheduler;

    @Autowired
    public GooglePushNotificationsUsersController(GooglePushNotificationsUsersRepository googlePushNotificationsUsersRepository, TaskSchedulerService scheduler) {
        this.googlePushNotificationsUsersRepository = googlePushNotificationsUsersRepository;
        this.scheduler = scheduler;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/add-google-push-notifications-user")
    public ResponseEntity<HttpStatus> saveToken(@RequestBody GooglePushNotificationsUsersDTO googlePushNotificationsUsersDTOS) {
        try {

            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.HEALTH_FACILITY_SUBMISSION, googlePushNotificationsUsersDTOS));

            GooglePushNotificationsUsers googlePushNotificationsUser = new Converter<GooglePushNotificationsUsersDTO, GooglePushNotificationsUsers>() {
                @Override
                public GooglePushNotificationsUsers convert(GooglePushNotificationsUsersDTO googlePushNotificationsUsersDTO) {

                    GooglePushNotificationsUsers googlePushNotificationsUsers = new GooglePushNotificationsUsers();
                    googlePushNotificationsUsers.setGooglePushNotificationToken(googlePushNotificationsUsersDTO.getGooglePushNotificationToken());
                    googlePushNotificationsUsers.setUserUiid(googlePushNotificationsUsersDTO.getUserUiid());
                    googlePushNotificationsUsers.setFacilityUiid(googlePushNotificationsUsersDTO.getFacilityUiid());

                    return googlePushNotificationsUsers;
                }
            }.convert(googlePushNotificationsUsersDTOS);

            googlePushNotificationsUsersRepository.save(googlePushNotificationsUser);

            logger.debug(format("Saved Google push notification user  queue.\nSubmissions: {0}", googlePushNotificationsUsersDTOS));
        } catch (Exception e) {
            logger.error(format("Google push notification user processing failed with exception {0}.\nSubmissions: {1}", e, googlePushNotificationsUsersDTOS));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/google-play-notification-users")
    @ResponseBody
    public List<GooglePushNotificationsUsersDTO> getFiles() {

        List<GooglePushNotificationsUsers> googlePushNotificationsUsers = null;
        try {
            googlePushNotificationsUsers = googlePushNotificationsUsersRepository.getGooglePushNotificationsUsers("Select * from "+ GooglePushNotificationsUsers.tbName,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return with(googlePushNotificationsUsers).convert(new Converter<GooglePushNotificationsUsers, GooglePushNotificationsUsersDTO>() {
            @Override
            public GooglePushNotificationsUsersDTO convert(GooglePushNotificationsUsers googlePushNotificationsUsers) {
                return new GooglePushNotificationsUsersDTO(googlePushNotificationsUsers.getId(),googlePushNotificationsUsers.getUserUiid(),googlePushNotificationsUsers.getGooglePushNotificationToken(),googlePushNotificationsUsers.getFacilityUiid(),googlePushNotificationsUsers.getCreatedAt(),googlePushNotificationsUsers.getUpdatedAt());
            }
        });
    }
}
