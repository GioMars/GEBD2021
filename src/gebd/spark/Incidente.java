package gebd.spark;

import java.io.Serializable;

/* 
 * Classe strumentale alla classe AnalizzaIncidentiStradali
 * 
 * Classe wrapper, artificio che consente di incapsulare assieme tante variabili di interesse, 
 * descritte attraverso variabili membro, sotto un unico oggetto
 * 
 * Il nostro obiettivo è poter definire ed utilizzare delle RDD che contengano istanze di questa classe
 * E' necessario che la classe in questione soddisfi tre requisiti
 * - Deve implementare l'interfaccia SERIALIZABLE (java.io)
 * - Deve implementare un costruttore senza argomenti e vuoto
 * - Per ogni variabile membro che si vuole rendere accessibile in distribuito è necessario che siano presenti
 * 	 i corrispondenti metodi GETTERS e SETTERS
 */

public class Incidente implements Serializable{
	
	public Incidente(){}
	
	// Variabili di interesse 
	private int idGruppo;
	private String strada1;
	private String strada2;
	private int protocollo;
	private int nrIllesi;
	
	// Un secondo costruttore con l'inizializzazione delle variabili 
	public Incidente(int idGruppo, String strada1, String strada2, int protocollo, int nrIllesi) {
		super();
		this.idGruppo = idGruppo;
		this.strada1 = strada1;
		this.strada2 = strada2;
		this.protocollo = protocollo;
		this.nrIllesi = nrIllesi;
	}
	
	public int getIdGruppo() {
		return idGruppo;
	}
	public void setIdGruppo(int idGruppo) {
		this.idGruppo = idGruppo;
	}
	public String getStrada1() {
		return strada1;
	}
	public void setStrada1(String strada1) {
		this.strada1 = strada1;
	}
	public String getStrada2() {
		return strada2;
	}
	public void setStrada2(String strada2) {
		this.strada2 = strada2;
	}
	public int getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(int protocollo) {
		this.protocollo = protocollo;
	}
	public int getNrIllesi() {
		return nrIllesi;
	}
	public void setNrIllesi(int nrIllesi) {
		this.nrIllesi = nrIllesi;
	}
	
	
	
}
