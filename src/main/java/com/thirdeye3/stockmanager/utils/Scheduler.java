package com.thirdeye3.stockmanager.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thirdeye3.stockmanager.utils.Initiatier;
import com.thirdeye3.stockmanager.utils.TimeManager;
import com.thirdeye3.stockmanager.dtos.Response;
import com.thirdeye3.stockmanager.externalcontrollers.SelfClient;

@Component
public class Scheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	
    @Autowired
    SelfClient selfClient;
    
    @Autowired
    TimeManager timeManager;
    
    @Autowired
    Initiatier initiatier;
    
    @Value("${thirdeye.uniqueId}")
    private Integer uniqueId;

    @Value("${thirdeye.uniqueCode}")
    private String uniqueCode;
    
    @Value("${thirdeye.priority}")
    private Integer priority;
	
	@Scheduled(fixedRate = 30000)
    public void checkStatusTask() {
        Response<String> response = selfClient.statusChecker(uniqueId, uniqueCode);
        logger.info("Status check response is {}", response.getResponse());
    }
	
	@Scheduled(cron = "${thirdeye.scheduler.cronToRefreshData}", zone = "${thirdeye.timezone}")
    public void runToRefreshdata() {
        try {
        	TimeUnit.SECONDS.sleep(priority * 3); 
            initiatier.init();
            logger.info("🔄 Data refreshed at {}", timeManager.getCurrentTime());
        } catch (Exception e) {
            logger.error("❌ Failed to refresh data at {}: {}", timeManager.getCurrentTime(), e.getMessage());
        }
    }

}

