package org.opensrp.web.controller;

import ch.lambdaj.function.convert.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.*;
import org.opensrp.dto.*;
import org.opensrp.repository.IndicatorRepository;
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
    private IndicatorRepository indicatorRepository;
    private ReferralServiceIndicatorRepository referralServiceIndicatorRepository;
    private TBPatientTypeRepository tbPatientTypeRepository;
    private TaskSchedulerService scheduler;

    @Autowired
    public ServiceController(ReferralServiceRepository referralServiceRepository, TaskSchedulerService scheduler, TBPatientTypeRepository tbPatientTypeRepository,
                             ReferralServiceIndicatorRepository referralServiceIndicatorRepository, IndicatorRepository indicatorRepository) {
        this.referralServiceRepository = referralServiceRepository;
        this.tbPatientTypeRepository = tbPatientTypeRepository;
        this.scheduler = scheduler;
        this.referralServiceIndicatorRepository = referralServiceIndicatorRepository;
        this.indicatorRepository = indicatorRepository;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/add-referral-services")
    public ResponseEntity<HttpStatus> saveReferralServices(@RequestBody String json) {
        try {
	        List<ReferralServiceDTO> afyaServiceDTOS = new Gson().fromJson(json, new TypeToken<List<ReferralServiceDTO>>() {
	        }.getType());

            if (afyaServiceDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.HEALTH_FACILITY_SUBMISSION, afyaServiceDTOS));


            List<ReferralService> referralServices =  with(afyaServiceDTOS).convert(new Converter<ReferralServiceDTO, ReferralService>() {
                @Override
                public ReferralService convert(ReferralServiceDTO boreshaAfyaServiceDTO) {
                    ReferralService referralService = new ReferralService();
                    referralService.setReferralServiceName(boreshaAfyaServiceDTO.getServiceName());
                    referralService.setReferralCategoryName(boreshaAfyaServiceDTO.getCategory());
                    referralService.setActive(boreshaAfyaServiceDTO.isActive());
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

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/add-referral-indicators")
    public ResponseEntity<HttpStatus> saveReferralIndicators(@RequestBody String json) {
        try {
            List<IndicatorDTO> indicatorDTOS = new Gson().fromJson(json, new TypeToken<List<IndicatorDTO>>() {
            }.getType());

            if (indicatorDTOS.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.HEALTH_FACILITY_SUBMISSION, indicatorDTOS));

            List<Indicator> indicators =  with(indicatorDTOS).convert(new Converter<IndicatorDTO, Indicator>() {
                @Override
                public Indicator convert(IndicatorDTO referralIndicatorDTO) {
                    Indicator indicator = new Indicator();
                    indicator.setReferralIndicatorName(referralIndicatorDTO.getIndicatorName());
                    indicator.setActive(referralIndicatorDTO.isActive());
                    return indicator;
                }
            });

            Exception exp = null;
            for (Indicator indicator : indicators) {
                try {
                    indicatorRepository.save(indicator);
                }catch (Exception e){
                    exp=e;
                    e.printStackTrace();
                }
            }

            if(exp!=null)
                throw  exp;

            logger.debug(format("Saved Referral Indicator to queue.\nSubmissions: {0}", indicatorDTOS));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(format("Referral Indicators processing failed with exception {0}.\nSubmissions: {1}", e, json));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/add-referral-service-indicators")
    public ResponseEntity<HttpStatus> saveReferralServiceIndicators(@RequestBody String json) {
        try {
            List<ReferralServiceIndicatorDTO> referralServiceIndicators = new Gson().fromJson(json, new TypeToken<List<ReferralServiceIndicatorDTO>>() {
            }.getType());

            if (referralServiceIndicators.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.HEALTH_FACILITY_SUBMISSION, referralServiceIndicators));


            List<List<ReferralServiceIndicator>> referralIndicatorsList =  with(referralServiceIndicators).convert(new Converter<ReferralServiceIndicatorDTO, List<ReferralServiceIndicator>>() {
                @Override
                public List<ReferralServiceIndicator> convert(ReferralServiceIndicatorDTO referralServiceIndicatorDTO) {
                    List<ReferralServiceIndicator> referralIndicators = new ArrayList<>();
                    for (Long indicatorId:referralServiceIndicatorDTO.getReferralIndicatorId()) {
                        ReferralServiceIndicator referralServiceIndicator = new ReferralServiceIndicator();
                        PKReferralServiceIndicator pkReferralServiceIndicator = new PKReferralServiceIndicator(indicatorId, referralServiceIndicatorDTO.getReferralServiceId());
                        referralServiceIndicator.setPkReferralServiceIndicator(pkReferralServiceIndicator);

                        referralIndicators.add(referralServiceIndicator);
                    }


                    return referralIndicators;
                }
            });

            long id = 1;
            List<ReferralServiceIndicator> indicators =  referralServiceIndicatorRepository.getReferralServicesIndicators("SELECT * FROM "+ReferralServiceIndicator.tbName+" ORDER BY "+ReferralServiceIndicator.COL_SERVICE_ID+" LIMIT 1",null);
            if(indicators.size()>0){
                id = indicators.get(0).getReferralServiceIndicatorId()+1;
            }

            Exception exp = null;
            for (List<ReferralServiceIndicator> referralServiceIndicatorsList : referralIndicatorsList) {
                for (ReferralServiceIndicator referralServiceIndicator : referralServiceIndicatorsList) {
                    try {
                        referralServiceIndicator.setReferralServiceIndicatorId(id);
                        id++;
                        referralServiceIndicatorRepository.save(referralServiceIndicator);
                    }catch (Exception e){
                        exp = e;
                        e.printStackTrace();
                    }
                }
            }

            if(exp!=null)
                throw  exp;
            logger.debug(format("Saved Referral Indicator to queue.\nSubmissions: {0}", referralServiceIndicators));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(format("Referral Indicators processing failed with exception {0}.\nSubmissions: {1}", e, json));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }


    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/boresha-afya-services")
    @ResponseBody
    public List<ReferralServiceIndicatorsDTO> getBoreshaAfyaServices() {

        List<ReferralService> allReferralServices = null;
        try {
            allReferralServices = referralServiceRepository.getBoreshaAfyaServices("Select * from "+ ReferralService.tbName,null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<ReferralServiceIndicatorsDTO> referralServiceIndicatorsDTOS = new ArrayList<>();
        for(ReferralService referralService:allReferralServices) {
            ReferralServiceIndicatorsDTO referralServiceIndicatorsDTO = new ReferralServiceIndicatorsDTO();

            referralServiceIndicatorsDTO.setCategory(referralService.getReferralCategoryName());
            referralServiceIndicatorsDTO.setServiceId(referralService.getReferralServiceId());
            referralServiceIndicatorsDTO.setServiceName(referralService.getReferralServiceName());
            referralServiceIndicatorsDTO.setActive(referralService.isActive());


            List<ReferralServiceIndicator> referralServiceIndicators = null;
            try {
                Object[] objects = new Object[]{
                        referralService.getReferralServiceId()
                };
                referralServiceIndicators =
                        referralServiceIndicatorRepository.getReferralServicesIndicators("SELECT * FROM " + ReferralServiceIndicator.tbName+" WHERE "+ReferralServiceIndicator.COL_SERVICE_ID +" =?", objects);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<IndicatorDTO> indicatorDTOS = new ArrayList<>();
            for(ReferralServiceIndicator serviceIndicator:referralServiceIndicators){
                IndicatorDTO indicatorDTO = new IndicatorDTO();
                indicatorDTO.setReferralServiceIndicatorId(serviceIndicator.getReferralServiceIndicatorId());


                Object[] objects = new Object[]{
                        serviceIndicator.getPkReferralServiceIndicator().getIndicatorId()
                };
                List<Indicator> indicators = null;
                try {
                    indicators = indicatorRepository.getReferralIndicators("SELECT * FROM "+ Indicator.tbName+" WHERE "+ Indicator.COL_REFERRAL_INDICATOR_ID+" =?",objects);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(indicators.size()>0) {
                    indicatorDTO.setIndicatorName(indicators.get(0).getReferralIndicatorName());
                    indicatorDTO.setReferralIndicatorId(indicators.get(0).getReferralIndicatorId());
                    indicatorDTO.setActive(indicators.get(0).isActive());
                }

                indicatorDTOS.add(indicatorDTO);

            }
            referralServiceIndicatorsDTO.setIndicators(indicatorDTOS);

            referralServiceIndicatorsDTOS.add(referralServiceIndicatorsDTO);
        }




        return referralServiceIndicatorsDTOS;
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
