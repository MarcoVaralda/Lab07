package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;

public class PowerOutage {
	
	private int id;
	private LocalDateTime dataInizio;
	private LocalDateTime dataFine;
	private int personeColpite;
	private int differenza; // Differenza tra data fine e data inizio
	
	public PowerOutage(int id, LocalDateTime dataInizio, LocalDateTime dataFine, int personeColpite) {
		this.id = id;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.personeColpite = personeColpite;
	}

	public int getDifferenza() {
		return differenza;
	}


	public void setDifferenza(int differenza) {
		this.differenza = differenza;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public LocalDateTime getDataInizio() {
		return dataInizio;
	}


	public void setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
	}


	public LocalDateTime getDataFine() {
		return dataFine;
	}


	public void setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
	}


	public int getPersoneColpite() {
		return personeColpite;
	}


	public void setPersoneColpite(int personeColpite) {
		this.personeColpite = personeColpite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PowerOutage other = (PowerOutage) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
