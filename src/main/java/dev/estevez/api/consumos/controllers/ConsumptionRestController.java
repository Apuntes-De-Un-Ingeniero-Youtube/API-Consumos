package dev.estevez.api.consumos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.estevez.api.consumos.entities.ElectricalConsumption;
import dev.estevez.api.consumos.entities.RequestData;
import dev.estevez.api.consumos.services.IConsumptionService;

@RestController
@RequestMapping("/consumption")
public class ConsumptionRestController {
	
	@Autowired
	private IConsumptionService consumptionService;
	
	@GetMapping("/findAll")
	public List<ElectricalConsumption> list() {
		return consumptionService.list();
	}
	
	@GetMapping("/daily")
	public Map<String, Double> daily(@RequestBody RequestData request) {
		if (request.getPeriod().equalsIgnoreCase("daily")) {
			return consumptionService.getConsumptionByDate(request.getDate());
		}
		return new HashMap<>();
	}
	
	@GetMapping("/monthly")
	public Map<String, Double> monthly(@RequestBody RequestData request) {
		if (request.getPeriod().equalsIgnoreCase("monthly")) {
			return consumptionService.getConsumptionByMonth(request.getDate());
		}
		return new HashMap<>();
	}
	
	@GetMapping("/weekly")
	public Map<String, Double> weekly(@RequestBody RequestData request) {
		if (request.getPeriod().equalsIgnoreCase("weekly")) {
			return consumptionService.getConsumptionByWeek(request.getDate());
		}
		return new HashMap<>();
	}

}
