package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.ReferralIndicator;
import org.opensrp.domain.ReferralService;
import org.opensrp.domain.ReferralServiceIndicator;
import org.opensrp.domain.TBPatientType;
import org.opensrp.dto.*;
import org.opensrp.repository.ReferralIndicatorRepository;
import org.opensrp.repository.ReferralServiceIndicatorRepository;
import org.opensrp.repository.ReferralServiceRepository;
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

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ServiceController {
    private static Logger logger = LoggerFactory.getLogger(ServiceController.class.toString());
    private ReferralServiceRepository referralServiceRepository;
    private ReferralIndicatorRepository referralIndicatorRepository;
    private ReferralServiceIndicatorRepository referralServiceIndicatorRepository;
    private TBPatientTypeRepository tbPatientTypeRepository;
    private TaskSchedulerService scheduler;

    @Autowired
    public ServiceController(ReferralServiceRepository referralServiceRepository, TaskSchedulerService scheduler, TBPatientTypeRepository tbPatientTypeRepository,
                             ReferralServiceIndicatorRepository referralServiceIndicatorRepository,ReferralIndicatorRepository referralIndicatorRepository) {
        this.referralServiceRepository = referralServiceRepository;
        this.tbPatientTypeRepository = tbPatientTypeRepository;
        this.scheduler = scheduler;
        this.referralServiceIndicatorRepository = referralServiceIndicatorRepository;
        this.referralIndicatorRepository =referralIndicatorRepository;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/add-boresha-afya-services")
    public ResponseEntity<HttpStatus> saveBoreshaAfyaServices(@RequestBody String json) {
        try {
	        List<BoreshaAfyaServiceDTO> afyaServiceDTOS = new Gson().fromJson(json, new TypeToken<List<BoreshaAfyaServiceDTO>>() {
	        }.getType());

            if (afyaServiceDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.HEALTH_FACILITY_SUBMISSION, afyaServiceDTOS));


            List<ReferralService> referralServices =  with(afyaServiceDTOS).convert(new Converter<BoreshaAfyaServiceDTO, ReferralService>() {
                @Override
                public ReferralService convert(BoreshaAfyaServiceDTO boreshaAfyaServiceDTO) {
                    ReferralService referralService = new ReferralService();
                    referralService.setServiceName(boreshaAfyaServiceDTO.getServiceName());
                    referralService.setCategory(boreshaAfyaServiceDTO.getCategory());
                    referralService.setActive(boreshaAfyaServiceDTO.isActive());
	                System.out.println("coze:service name = "+ referralService.getServiceName());
                    return referralService;
                }
            });

            for (ReferralService referralService : referralServices) {
                referralServiceRepository.save(referralService);
            }

            logger.debug(format("Saved Boresha Afya Service to queue.\nSubmissions: {0}", afyaServiceDTOS));
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error(format("Boresha Afya Service processing failed with exception {0}.\nSubmissions: {1}", e, json));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/boresha-afya-services")
    @ResponseBody
    public List<BoreshaAfyaServiceDTO> getBoreshaAfyaServices() {

        List<ReferralService> allReferralServices = null;
        try {
            allReferralServices = referralServiceRepository.getBoreshaAfyaServices("Select * from "+ ReferralService.tbName,null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<ReferralServiceIndicatorsDTO> referralServiceIndicatorsDTOS = new ArrayList<>();
        for(ReferralService referralService:allReferralServices) {
            ReferralServiceIndicatorsDTO referralServiceIndicatorsDTO = new ReferralServiceIndicatorsDTO();

            referralServiceIndicatorsDTO.setCategory(referralService.getCategory());
            referralServiceIndicatorsDTO.setServiceId(referralService.getServiceId());
            referralServiceIndicatorsDTO.setServiceName(referralService.getServiceName());
            referralServiceIndicatorsDTO.setActive(referralService.isActive());


            List<ReferralServiceIndicator> referralServiceIndicators = null;
            try {
                Object[] objects = new Object[]{
                        referralService.getServiceId()
                };
                referralServiceIndicators =
                        referralServiceIndicatorRepository.getReferralServicesIndicators("SELECT * FROM " + ReferralServiceIndicator.tbName+" WHERE "+ReferralServiceIndicator.COL_REFERRAL_SERVICE_ID+" =?", objects);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<ReferralIndicatorDTO> indicatorDTOS = new ArrayList<>();
            for(ReferralServiceIndicator serviceIndicator:referralServiceIndicators){
                ReferralIndicatorDTO referralIndicatorDTO = new ReferralIndicatorDTO();
                referralIndicatorDTO.setReferralServiceIndicatorId(serviceIndicator.getReferralServiceIndicatorId());


                Object[] objects = new Object[]{
                        serviceIndicator.getReferralIndicator().getReferralIndicatorId()
                };
                List<ReferralIndicator> referralIndicators = null;
                try {
                    referralIndicators = referralIndicatorRepository.getReferralIndicators("SELECT * FROM "+ ReferralIndicator.tbName+" WHERE "+ReferralIndicator.COL_REFERRAL_INDICATOR_ID+" =?",objects);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                referralIndicatorDTO.setIndicatorName(referralIndicators.get(0).getReferralIndicatorName());
                referralIndicatorDTO.setReferralIndicatorId(referralIndicators.get(0).getReferralIndicatorId());
                referralIndicatorDTO.setActive(referralIndicators.get(0).isActive());

                indicatorDTOS.add(referralIndicatorDTO);

            }
            referralServiceIndicatorsDTO.setIndicators(indicatorDTOS);

            referralServiceIndicatorsDTOS.add(referralServiceIndicatorsDTO)
        }




        return with(allReferralServices).convert(new Converter<ReferralService, BoreshaAfyaServiceDTO>() {
            @Override
            public BoreshaAfyaServiceDTO convert(ReferralService referralService) {
                return new BoreshaAfyaServiceDTO(referralService.getServiceId(), referralService.getServiceName(), referralService.getCategory(),referralService.isActive());
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
                return new TBPatientTypesDTO(tbPatientType.getId(),tbPatientType.getPatientTypeName(),tbPatientType.getIsActive());
            }
        });
    }
}
