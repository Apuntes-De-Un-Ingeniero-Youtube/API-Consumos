package dev.estevez.api.consumos.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.estevez.api.consumos.entities.ElectricalConsumption;
import dev.estevez.api.consumos.repository.IConsumptionRepository;

@Service
public class ConsumptionServiceImpl implements IConsumptionService {

	@Autowired
	private IConsumptionRepository consumptionRepository;

	@Override
	public List<ElectricalConsumption> list() {
		return consumptionRepository.findAll();
	}

	@Override
	public Map<String, Double> getConsumptionByDate(String meterDate) {
		double maximumConsumption = 0; // Consumo máximo por hora
		double minimumConsumption; // Consumo mínimo por hota
		double dailyConsumption = 0; // Consumo diario
		String startWith = "00"; // Inicia con

		SortedMap<String, Double> consumptionPerHour = new TreeMap<>(); // Data de salida ordenada
		List<ElectricalConsumption> consumptionPerDay = consumptionRepository.getConsumptionByDate(meterDate);

		for (int i = 1; i <= 24; i++) { // Recorriendo la 24 horas del día

			maximumConsumption = 0;
			minimumConsumption = 9999999;

			for (ElectricalConsumption consumption : consumptionPerDay) { // Por cada consumo
				if (consumption.getMeterHour().startsWith(startWith)) {
					if (consumption.getActiveEnergy() > maximumConsumption) {
						maximumConsumption = consumption.getActiveEnergy();
					}
					if (consumption.getActiveEnergy() < minimumConsumption) {
						minimumConsumption = consumption.getActiveEnergy();
					}
				}
			}

			dailyConsumption = maximumConsumption - minimumConsumption;

			if (dailyConsumption != -9999999) {
				consumptionPerHour.put(startWith + ":00:00", dailyConsumption);
			} else {
				consumptionPerHour.put(startWith + ":00:00", 0.0);
			}
			
			if (i >= 10) {
				startWith = String.valueOf(i);
			} else {
				startWith = String.valueOf("0" + i);
			}
		}

		return consumptionPerHour;
	}

	@Override
	public Map<String, Double> getConsumptionByMonth(String meterDate) {
		SortedMap<String, Double> mapa = new TreeMap<>();
		String[] dateParts = meterDate.split("-");
		String year = dateParts[0];
		String month = dateParts[1];

		for (int i = 1; i <= 31; i++) {
			String datettt;
			if (i >= 1 && i <= 9) {
				datettt = String.valueOf(year + "-" + month + "-0" + i);
			} else {
				datettt = String.valueOf(year + "-" + month + "-" + i);
			}

			getConsumptionsByDay(datettt, mapa);

		}

		return mapa;
	}

	@Override
	public Map<String, Double> getConsumptionByWeek(String meterDate) {
SortedMap<String, Double> consumptionPerDays = new TreeMap<>();
		
		// Separando los datos de la fecha
		String[] dateParts = meterDate.split("-");
		String year = dateParts[0];
		String month = dateParts[1];
		String day = dateParts[2];

		// Crea un objeto Calendar para representar la fecha de hoy
		Calendar date = Calendar.getInstance();

		// Establece la fecha en la que quieres calcular la semana
		date.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

		// Obtiene el día de la semana de la fecha establecida
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

		// Obtiene el primer día de la semana (lunes)
		date.add(Calendar.DATE, -dayOfWeek - 1 + Calendar.MONDAY);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < 7; i++) {
			date.add(Calendar.DATE, 1);
			getConsumptionsByDay(sdf.format(date.getTime()), consumptionPerDays);
		}

		return consumptionPerDays;
	}
	
	/**
	 * Calcula los consumos por día de una fecha específica.
	 * 
	 * @param meter_date    a calcular los consumos por día.
	 * @param consumosByDay lista a agregar el consumo diario.
	 * @author Juan Carlos Estevez Vargas.
	 */
	private void getConsumptionsByDay(String meterDate, SortedMap<String, Double> map) {
		try {
			List<ElectricalConsumption> consumptionsPerDay = consumptionRepository.getConsumptionByDate(meterDate);
			if (consumptionsPerDay != null) {
				Double minimunConsumption = consumptionsPerDay.get(0).getActiveEnergy();
				Double maximunConsumption = consumptionsPerDay.get(consumptionsPerDay.size() - 1).getActiveEnergy();
				Double consumptionPerDay = maximunConsumption - minimunConsumption;
				map.put(meterDate, consumptionPerDay);
			} else {
				map.put(meterDate, 0.0);
			}
		} catch (Exception e) {
			map.put(meterDate, 0.0);
		}

	}

}
