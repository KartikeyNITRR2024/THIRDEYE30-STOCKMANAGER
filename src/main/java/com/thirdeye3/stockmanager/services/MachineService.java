package com.thirdeye3.stockmanager.services;

import java.util.Map;

import com.thirdeye3.stockmanager.dtos.Machine;

public interface MachineService {

	void fetchMachines();

	Map<String, Machine> getMachines();

	Integer getType1MachinesCount();

	Integer getType2MachinesCount();

	Integer validateMachine(Integer machineId, String machineUniqueCode);

}
