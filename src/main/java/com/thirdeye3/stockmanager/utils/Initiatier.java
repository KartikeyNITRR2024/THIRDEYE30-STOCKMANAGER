package com.thirdeye3.stockmanager.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thirdeye3.stockmanager.services.MachineService;
import com.thirdeye3.stockmanager.services.PropertyService;
import com.thirdeye3.stockmanager.services.StockService;

import jakarta.annotation.PostConstruct;

@Component
public class Initiatier {
	
    private static final Logger logger = LoggerFactory.getLogger(Initiatier.class);
    
	@Autowired
	MachineService machineService;
	
	@Autowired
	PropertyService propertyService;
	
	@Autowired
	StockService stockService;
	
    @Value("${thirdeye.priority}")
    private Integer priority;
    
    
	@PostConstruct
    public void init() throws Exception{
        logger.info("Initializing Initiatier...");
    	TimeUnit.SECONDS.sleep(priority * 3);
        machineService.fetchMachines();
        propertyService.fetchProperties();
        logger.info("Initiatier initialized.");
    }
	
	public void refreshMemory()
	{
		logger.info("Going to refersh memory...");
		stockService.resetAllOpeningPrice();
		stockService.updateStocks();
		logger.info("Memory refreshed.");
	}

}


