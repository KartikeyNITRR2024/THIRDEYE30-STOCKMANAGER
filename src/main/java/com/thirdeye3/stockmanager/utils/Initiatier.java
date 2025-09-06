package com.thirdeye3.stockmanager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    
	@PostConstruct
    public void init() throws Exception{
        logger.info("Initializing Initiatier...");
        machineService.fetchMachines();
        propertyService.fetchProperties();
        stockService.updateStocks();
        logger.info("Initiatier initialized.");
    }

}


