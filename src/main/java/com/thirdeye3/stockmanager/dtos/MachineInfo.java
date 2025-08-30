package com.thirdeye3.stockmanager.dtos;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MachineInfo {
	Map<String, Machine> machineDtos;
	Integer type1Machines;
	Integer type2Machines;
}
