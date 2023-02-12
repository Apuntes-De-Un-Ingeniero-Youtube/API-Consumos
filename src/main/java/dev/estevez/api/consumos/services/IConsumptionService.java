package dev.estevez.api.consumos.services;

import java.util.List;
import java.util.Map;

import dev.estevez.api.consumos.entities.ElectricalConsumption;

public interface IConsumptionService {

	List<ElectricalConsumption> list();
	Map<String, Double> getConsumptionByDate(String meterDate);
	Map<String, Double> getConsumptionByMonth(String meterDate);
	Map<String, Double> getConsumptionByWeek(String meterDate);
	
}
