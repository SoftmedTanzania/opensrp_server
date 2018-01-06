package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.BoreshaAfyaService;
import org.opensrp.domain.Multimedia;
import org.opensrp.domain.TBPatient;
import org.opensrp.domain.TBPatientType;
import org.opensrp.dto.BoreshaAfyaServiceDTO;
import org.opensrp.dto.TBPatientTypesDTO;
import org.opensrp.dto.form.MultimediaDTO;
import org.opensrp.repository.BoreshaAfyaServiceRepository;
import org.opensrp.repository.TBPatientTypeRepository;
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
    private BoreshaAfyaServiceRepository boreshaAfyaServiceRepository;
    private TBPatientTypeRepository tbPatientTypeRepository;
    private TaskSchedulerService scheduler;

    @Autowired
    public ServiceController(BoreshaAfyaServiceRepository boreshaAfyaServiceRepository, TaskSchedulerService scheduler,TBPatientTypeRepository tbPatientTypeRepository) {
        this.boreshaAfyaServiceRepository = boreshaAfyaServiceRepository;
        this.tbPatientTypeRepository = tbPatientTypeRepository;
        this.scheduler = scheduler;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/add-boresha-afya-services")
    public ResponseEntity<HttpStatus> saveBoreshaAfyaServices(@RequestBody List<BoreshaAfyaServiceDTO> boreshaAfyaServiceDTOS) {
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
        	e.printStackTrace();
            logger.error(format("Boresha Afya Service processing failed with exception {0}.\nSubmissions: {1}", e, boreshaAfyaServiceDTOS));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/boresha-afya-services")
    @ResponseBody
    public List<BoreshaAfyaServiceDTO> getBoreshaAfyaServices() {

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


    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/save-tb-patient-type")
    public ResponseEntity<HttpStatus> savePatientType(@RequestBody List<TBPatientTypesDTO> tbPatientTypesDTOS) {
        try {
            if (tbPatientTypesDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.HEALTH_FACILITY_SUBMISSION, tbPatientTypesDTOS));

            String json = new Gson().toJson(tbPatientTypesDTOS);
            List<TBPatientTypesDTO> tbPatientTypesDTOS1 = new Gson().fromJson(json, new TypeToken<List<TBPatientTypesDTO>>() {
            }.getType());

            List<TBPatientType> tbPatientTypes =  with(tbPatientTypesDTOS1).convert(new Converter<TBPatientTypesDTO, TBPatientType>() {
                @Override
                public TBPatientType convert(TBPatientTypesDTO tbPatientTypesDTO) {
                    TBPatientType tbPatientType = new TBPatientType();
                    tbPatientType.setPatientTypeName(tbPatientTypesDTO.getPatientTypeName());
                    tbPatientType.setIsActive(tbPatientTypesDTO.isActive());
                    return tbPatientType;
                }
            });

            for (TBPatientType tbPatientType : tbPatientTypes) {
                tbPatientTypeRepository.save(tbPatientType);
            }

            logger.debug(format("Saved TB Patient types to queue.\nSubmissions: {0}", tbPatientTypesDTOS));
        } catch (Exception e) {
            logger.error(format("TB Patient Types processing failed with exception {0}.\nSubmissions: {1}", e, tbPatientTypesDTOS));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/tb-patient-types")
    @ResponseBody
    public List<TBPatientTypesDTO> getTBPatientTypes() {

        List<TBPatientType> tbPatientTypes = null;
        try {
            tbPatientTypes = tbPatientTypeRepository.getTBPatientTypes("Select * from "+ TBPatientType.tbName,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return with(tbPatientTypes).convert(new Converter<TBPatientType, TBPatientTypesDTO>() {
            @Override
            public TBPatientTypesDTO convert(TBPatientType tbPatientType) {
                return new TBPatientTypesDTO(tbPatientType.getId(),tbPatientType.getPatientTypeName(),tbPatientType.getIsActive(),tbPatientType.getCreatedAt(),tbPatientType.getUpdatedAt());
            }
        });
    }
}
