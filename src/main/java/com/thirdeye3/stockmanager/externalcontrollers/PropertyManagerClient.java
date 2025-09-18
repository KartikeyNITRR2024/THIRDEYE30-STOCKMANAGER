package com.thirdeye3.stockmanager.externalcontrollers;

import com.thirdeye3.stockmanager.dtos.MachineInfo;
import com.thirdeye3.stockmanager.dtos.Response;
import com.thirdeye3.stockmanager.configs.FeignConfig;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
		name = "THIRDEYE30-PROPERTYMANAGER",
		configuration = FeignConfig.class
)
public interface PropertyManagerClient {

    @GetMapping("/pm/machines/webscrapper")
    Response<MachineInfo> getMachines();

    @GetMapping("/pm/properties")
    Response<Map<String, Object>> getProperties();

    @GetMapping("/pm/allserviceinitiatier/{priority}")
    Response<Object> updateInitiatier(@PathVariable("priority") Integer priority);
}
