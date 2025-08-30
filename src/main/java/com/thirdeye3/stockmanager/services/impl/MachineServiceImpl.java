package com.thirdeye3.stockmanager.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import com.thirdeye3.stockmanager.dtos.Machine;
import com.thirdeye3.stockmanager.dtos.MachineInfo;
import com.thirdeye3.stockmanager.dtos.Response;
import com.thirdeye3.stockmanager.exceptions.InvalidMachineException;
import com.thirdeye3.stockmanager.externalcontrollers.PropertyManagerClient;
import com.thirdeye3.stockmanager.services.MachineService;

@Service
public class MachineServiceImpl implements MachineService {

    private Map<String, Machine> machines = null;
    private Integer type1MachinesCount = null;
    private Integer type2MachinesCount = null;
    private static final Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);

    @Autowired
    private PropertyManagerClient propertyManager;

    @Override
    public void fetchMachines() {
        Response<MachineInfo> response = propertyManager.getMachines();
        if (response.isSuccess()) {
            MachineInfo machineInfo = response.getResponse();
            machines = machineInfo.getMachineDtos();
            type1MachinesCount = machineInfo.getType1Machines();
            type2MachinesCount = machineInfo.getType2Machines();
            logger.info("Request {}, {}, {}", machines, type1MachinesCount, type2MachinesCount);
        } else {
            machines = new HashMap<>();
            type1MachinesCount = 0;
            type2MachinesCount = 0;
            logger.error("Request error");
        }
    }

    @Override
    public Integer validateMachine(Integer machineId, String machineUniqueCode) {
        if (machines == null) {
            fetchMachines();
        }
        String key = machineId + machineUniqueCode;
        if (!machines.containsKey(key)) {
            throw new InvalidMachineException("Invalid machine with ID: " + machineId + " and code: " + machineUniqueCode);
        }
        return machines.get(key).getMachineNo();
    }

    @Override
    public Map<String, Machine> getMachines() {
        return machines;
    }

    @Override
    public Integer getType1MachinesCount() {
        return type1MachinesCount;
    }

    @Override
    public Integer getType2MachinesCount() {
        return type2MachinesCount;
    }
}
