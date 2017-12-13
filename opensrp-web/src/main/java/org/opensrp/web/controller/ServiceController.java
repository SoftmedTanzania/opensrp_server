package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.BoreshaAfyaService;
import org.opensrp.domain.Multimedia;
import org.opensrp.dto.BoreshaAfyaServiceDTO;
import org.opensrp.dto.form.MultimediaDTO;
import org.opensrp.repository.BoreshaAfyaServiceRepository;
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
public class ServiceController {
    private static Logger logger = LoggerFactory.getLogger(ServiceController.class.toString());
    private BoreshaAfyaService boreshaAfyaService;
    private BoreshaAfyaServiceRepository boreshaAfyaServiceRepository;
    private TaskSchedulerService scheduler;

    @Autowired
    public ServiceController(BoreshaAfyaServiceRepository boreshaAfyaServiceRepository, TaskSchedulerService scheduler) {
        this.boreshaAfyaServiceRepository = boreshaAfyaServiceRepository;
        this.scheduler = scheduler;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/add-boresha-afya-services")
    public ResponseEntity<HttpStatus> savePatient(@RequestBody List<BoreshaAfyaServiceDTO> boreshaAfyaServiceDTOS) {
        try {
            if (boreshaAfyaServiceDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.HEALTH_FACILITY_SUBMISSION, boreshaAfyaServiceDTOS));

            String json = new Gson().toJson(boreshaAfyaServiceDTOS);
            List<BoreshaAfyaServiceDTO> afyaServiceDTOS = new Gson().fromJson(json, new TypeToken<List<BoreshaAfyaServiceDTO>>() {
            }.getType());

            List<BoreshaAfyaService> boreshaAfyaServices =  with(afyaServiceDTOS).convert(new Converter<BoreshaAfyaServiceDTO, BoreshaAfyaService>() {
                @Override
                public BoreshaAfyaService convert(BoreshaAfyaServiceDTO boreshaAfyaServiceDTO) {
                    BoreshaAfyaService boreshaAfyaService = new BoreshaAfyaService();
                    boreshaAfyaService.setServiceName(boreshaAfyaServiceDTO.getServiceName());
                    boreshaAfyaService.setIsActive(boreshaAfyaService.getIsActive());
                    return boreshaAfyaService;
                }
            });

            for (BoreshaAfyaService boreshaAfyaService : boreshaAfyaServices) {
                boreshaAfyaServiceRepository.save(boreshaAfyaService);
            }

            logger.debug(format("Saved Boresha Afya Service to queue.\nSubmissions: {0}", boreshaAfyaServiceDTOS));
        } catch (Exception e) {
            logger.error(format("Boresha Afya Service processing failed with exception {0}.\nSubmissions: {1}", e, boreshaAfyaServiceDTOS));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/boresha-afya-services")
    @ResponseBody
    public List<BoreshaAfyaServiceDTO> getFiles() {

        List<BoreshaAfyaService> allBoreshaAfyaServices = null;
        try {
            allBoreshaAfyaServices = boreshaAfyaServiceRepository.getBoreshaAfyaServices("Select * from "+ BoreshaAfyaService.tbName,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return with(allBoreshaAfyaServices).convert(new Converter<BoreshaAfyaService, BoreshaAfyaServiceDTO>() {
            @Override
            public BoreshaAfyaServiceDTO convert(BoreshaAfyaService boreshaAfyaService) {
                return new BoreshaAfyaServiceDTO(boreshaAfyaService.getId(),boreshaAfyaService.getServiceName(),boreshaAfyaService.getIsActive(),boreshaAfyaService.getCreatedAt(),boreshaAfyaService.getUpdatedAt());
            }
        });
    }
}
