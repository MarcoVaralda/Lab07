package it.polito.tdp.poweroutages.model;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO dao;
	List<PowerOutage> listaPowerOutages;
	List<PowerOutage> soluzioneOttima;
	
	int maxAnni;
	int maxOre;
	int maxPersoneColpite;
	double oreDissSoluzione;
	
	public Model() {
		dao = new PowerOutageDAO();
		maxAnni = 0;
		maxOre = 0;
		maxPersoneColpite = 0;
		oreDissSoluzione = 0;
	}
	
	public List<Nerc> getNercList() {
		return dao.getNercList();
	}

	public String trovaCombinazioneOttima(Nerc nercInserito, int maxAnni, int maxOre) {
		this.maxAnni=maxAnni;
		this.maxOre=maxOre;
		listaPowerOutages = new LinkedList<PowerOutage>(dao.getPowerOutages(nercInserito, maxOre));
		List<PowerOutage> parziale = new LinkedList<PowerOutage>();
		soluzioneOttima = new LinkedList<PowerOutage>();
		
		int livello=0;
		int totPersone=0;
		double totOreDiss = 0;
		
		long start = System.currentTimeMillis();
		
		ricorsivo(parziale, livello, totPersone,totOreDiss);

		long end = System.currentTimeMillis();
		
		String risultato = "Numero di persone colpite: "+this.maxPersoneColpite +"\n" +"Totale ore di disservizio: " +(int)this.oreDissSoluzione +"\n";		
		for(PowerOutage p : soluzioneOttima)
			risultato = risultato +p.getDataInizio().toString() +" " +p.getDataFine().toString() +" " +(int)p.getDifferenza()+" " +p.getPersoneColpite() +"\n";
		risultato = risultato +"Tempo impiegato: " +(end-start) +" millisecondi";
		return risultato;
	}
	
	public void ricorsivo(List<PowerOutage> parziale, int livello, int totPersone, double totOreDiss) {
		
		// Controllo se la soluzione parziale è ancora valida
		if(totOreDiss>this.maxOre || calcolaDifferenzaAnni(parziale)>this.maxAnni) {
			return;
		}
		
		// Controllo se parziale è la soluzione ottima finora
		if(totOreDiss<=this.maxOre && calcolaDifferenzaAnni(parziale)<=this.maxAnni && totPersone>this.maxPersoneColpite) {
			this.soluzioneOttima = new LinkedList<PowerOutage>(parziale);
			this.maxPersoneColpite = totPersone;
			this.oreDissSoluzione = totOreDiss;
		}
		
		// Caso terminale
		if(livello>=listaPowerOutages.size()) {
			return;
		}
		
		// Il prossimo poweroutage è da inserire? provo entrambe le soluzioni
		List<PowerOutage> nuovaParziale = new LinkedList<PowerOutage>(parziale);
		PowerOutage daAggiungere = listaPowerOutages.get(livello);
		nuovaParziale.add(daAggiungere);
		int nuovoTotPersone = totPersone + daAggiungere.getPersoneColpite();
		double nuovoTotOreDiss = totOreDiss + daAggiungere.getDifferenza();
		// Provo a inserirlo
		ricorsivo(nuovaParziale,livello+1,nuovoTotPersone,nuovoTotOreDiss);
		// Provo a non inserirlo
		ricorsivo(parziale,livello+1,totPersone,totOreDiss);
	}
	
	public int calcolaDifferenzaAnni(List<PowerOutage> parziale) {
		int annoMax=0;
		int annoMin=3000;
		
		if(parziale.isEmpty())
			return -1;
		
		for(PowerOutage p : parziale) {
			if(p.getDataFine().getYear()>annoMax)
				annoMax = p.getDataFine().getYear();
			if(p.getDataInizio().getYear()<annoMin)
				annoMin = p.getDataInizio().getYear();
		}
		
		return (annoMax-annoMin);
	}

}
