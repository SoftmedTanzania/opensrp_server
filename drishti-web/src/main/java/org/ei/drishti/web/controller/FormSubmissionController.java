package org.ei.drishti.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.ei.drishti.dto.form.FormSubmission;
import org.ei.drishti.event.FormSubmissionEvent;
import org.ei.drishti.service.FormSubmissionService;
import org.motechproject.scheduler.gateway.OutboundEventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/form-submissions")
public class FormSubmissionController {
    private static Logger logger = LoggerFactory.getLogger(FormSubmissionController.class.toString());
    private FormSubmissionService formSubmissionService;
    private OutboundEventGateway gateway;

    @Autowired
    public FormSubmissionController(FormSubmissionService formSubmissionService, OutboundEventGateway gateway) {
        this.formSubmissionService = formSubmissionService;
        this.gateway = gateway;
    }

    @RequestMapping(method = GET)
    @ResponseBody
    private List<FormSubmission> getNewSubmissionsForANM(@RequestParam("anm-id") String anmIdentifier, @RequestParam("timestamp") Long timeStamp) {
        List<org.ei.drishti.domain.form.FormSubmission> newSubmissionsForANM = formSubmissionService.getNewSubmissionsForANM(anmIdentifier, timeStamp);
        return with(newSubmissionsForANM).convert(new Converter<org.ei.drishti.domain.form.FormSubmission, FormSubmission>() {
            @Override
            public FormSubmission convert(org.ei.drishti.domain.form.FormSubmission submission) {
                return FormSubmissionConvertor.from(submission);
            }
        });
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST)
    public ResponseEntity<HttpStatus> submitForms(@RequestBody List<FormSubmission> formSubmissions) {
        try {
            if (formSubmissions.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            List<org.ei.drishti.domain.form.FormSubmission> submissions = with(getSubmissionsInProperFormat(formSubmissions))
                    .convert(new Converter<FormSubmission, org.ei.drishti.domain.form.FormSubmission>() {
                        @Override
                        public org.ei.drishti.domain.form.FormSubmission convert(FormSubmission submission) {
                            return FormSubmissionConvertor.toFormSubmission(submission);
                        }
                    });
            gateway.sendEventMessage(new FormSubmissionEvent(submissions).toEvent());
            logger.debug(format("Added Form submissions to queue.\nSubmissions: {0}", formSubmissions));
        } catch (Exception e) {
            logger.error(format("Form submissions processing failed with exception {0}.\nSubmissions: {1}", e, formSubmissions));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }

    private List<FormSubmission> getSubmissionsInProperFormat(List<FormSubmission> formSubmissions) {
        return new Gson().fromJson(new Gson().toJson(formSubmissions), new TypeToken<List<FormSubmission>>() {
        }.getType());
    }
}