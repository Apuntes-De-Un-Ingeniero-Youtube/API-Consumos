package dev.estevez.api.consumos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.estevez.api.consumos.entities.ElectricalConsumption;

@Repository
public interface IConsumptionRepository extends JpaRepository<ElectricalConsumption, Integer> {

	/**
	 * Obtiene los registros de consumo de electricidad en una fecha especifica.
	 * 
	 * @param meter_date por el cuál se listarán los registros.
	 * @return Lista de tipo Consumo con los registros encontrados.
	 * @author Juan Carlos Estevez Vargas.
	 */
	@Query(value = "SELECT * FROM CONSUMOS WHERE METER_DATE=:meterDate", nativeQuery = true)
	List<ElectricalConsumption> getConsumptionByDate(String meterDate);
	
}
