package dev.estevez.api.consumos.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class RequestData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String date;
	private String period;
}
