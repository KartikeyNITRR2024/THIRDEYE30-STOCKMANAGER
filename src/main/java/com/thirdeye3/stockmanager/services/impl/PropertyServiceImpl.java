package com.thirdeye3.stockmanager.services.impl;

import java.util.Map;
import java.time.LocalTime;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.thirdeye3.stockmanager.dtos.Response;
import com.thirdeye3.stockmanager.exceptions.PropertyFetchException;
import com.thirdeye3.stockmanager.externalcontrollers.PropertyManagerClient;
import com.thirdeye3.stockmanager.services.PropertyService;

@Service
public class PropertyServiceImpl implements PropertyService {

    private static final Logger logger = LoggerFactory.getLogger(PropertyServiceImpl.class);

    @Autowired
    private PropertyManagerClient propertyManager;

    private Map<String, Object> properties = null;
    private Integer noOFWebscrapperMarket = null;
    private Integer noOFWebscrapperUser = null;
    private LocalTime morningPriceUpdaterStartTime = null;
    private LocalTime morningPriceUpdaterEndTime = null;
    private LocalTime eveningPriceUpdaterStartTime = null;
    private LocalTime eveningPriceUpdaterEndTime = null;
    
    @Value("${thirdeye.priority}")
    private Integer priority;
    
    @Async
    @Override
    public void updateInitiatier()
    {
    	Response<Object> response = propertyManager.updateInitiatier(priority);
    	if(response.isSuccess())
    	{
    		logger.info("All services updated");
    	}
    	else
    	{
    		logger.info("Failed to update services");
    	}
    }


    @Override
    public void fetchProperties() {
        Response<Map<String, Object>> response = propertyManager.getProperties();
        if (response.isSuccess()) {
            properties = response.getResponse();
            noOFWebscrapperMarket = (Integer) properties.getOrDefault("NO_OF_WEBSCRAPPER_MARKET", 0);
            noOFWebscrapperUser = (Integer) properties.getOrDefault("NO_OF_WEBSCRAPPER_USER", 0);
            morningPriceUpdaterStartTime = LocalTime.of(
                    (int) properties.getOrDefault("MP_START_HOUR",9),
                    (int) properties.getOrDefault("MP_START_MINUTE",15),
                    (int) properties.getOrDefault("MP_START_SECOND",00)
            );

            morningPriceUpdaterEndTime = LocalTime.of(
                    (int) properties.getOrDefault("MP_END_HOUR",9),
                    (int) properties.getOrDefault("MP_END_MINUTE",16),
                    (int) properties.getOrDefault("MP_END_SECOND",30)
            );
            
            eveningPriceUpdaterStartTime = LocalTime.of(
                    (int) properties.getOrDefault("EP_START_HOUR",3),
                    (int) properties.getOrDefault("EP_START_MINUTE",30),
                    (int) properties.getOrDefault("EP_START_SECOND",00)
            );

            eveningPriceUpdaterEndTime = LocalTime.of(
                    (int) properties.getOrDefault("EP_END_HOUR",3),
                    (int) properties.getOrDefault("EP_END_MINUTE",30),
                    (int) properties.getOrDefault("EP_END_SECOND",30)
            );
            
            logger.info("Request {}, {}, {}", properties, noOFWebscrapperMarket, noOFWebscrapperUser);
        } else {
            properties = new HashMap<>();
            noOFWebscrapperMarket = 0;
            noOFWebscrapperUser = 0;
            morningPriceUpdaterStartTime = LocalTime.of(9,15,00);
            morningPriceUpdaterEndTime = LocalTime.of(9,16,30);
            eveningPriceUpdaterStartTime = LocalTime.of(3,30,00);
            eveningPriceUpdaterEndTime = LocalTime.of(3,30,30);
            logger.error("Failed to fetch properties");
            throw new PropertyFetchException("Unable to fetch properties from Property Manager");
        }
    }

    @Override
    public Integer getNoOFWebscrapperMarket() {
        if (noOFWebscrapperMarket == null) {
            throw new PropertyFetchException("Property NO_OF_WEBSCRAPPER_MARKET not initialized");
        }
        return noOFWebscrapperMarket;
    }

    @Override
    public Integer getNoOFWebscrapperUser() {
        if (noOFWebscrapperUser == null) {
            throw new PropertyFetchException("Property NO_OF_WEBSCRAPPER_USER not initialized");
        }
        return noOFWebscrapperUser;
    }

    @Override
	public LocalTime getMorningPriceUpdaterStartTime() {
		return morningPriceUpdaterStartTime;
	}

    @Override
	public LocalTime getMorningPriceUpdaterEndTime() {
		return morningPriceUpdaterEndTime;
	}

    @Override
	public LocalTime getEveningPriceUpdaterStartTime() {
		return eveningPriceUpdaterStartTime;
	}

    @Override
	public LocalTime getEveningPriceUpdaterEndTime() {
		return eveningPriceUpdaterEndTime;
	}
    
    
}
