package com.thirdeye3.stockmanager.externalcontrollers;

import com.thirdeye3.stockmanager.dtos.MachineInfo;
import com.thirdeye3.stockmanager.dtos.Response;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "THIRDEYE30-PROPERTYMANAGER")
public interface PropertyManagerClient {

    @GetMapping("/api/machines/webscrapper")
    Response<MachineInfo> getMachines();
    
    @GetMapping("/api/properties")
    Response<Map<String, Object>> getProperties();
}
