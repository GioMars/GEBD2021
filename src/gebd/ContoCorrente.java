package gebd;

import java.util.ArrayList;

public class ContoCorrente {
	
	private String intestatario;
	
	private ArrayList<Operazione> operazioni;
	
	private int codiceOperazione = 0;
	private int idContoCorrente;
	
	public ContoCorrente(String intestatario, int idContoCorrente) {
		this.intestatario = intestatario;
		this.idContoCorrente = idContoCorrente;
		operazioni = new ArrayList<Operazione>();
	}
	
	public int getIdContoCorrente() {
		return idContoCorrente;
	}

	public String getIntestatario() {
		return intestatario;
	}
	
	public Operazione BonificoEntrata(int importo, String data, String originante, String beneficiario) {
		Operazione x = new Entrata(importo, data, originante, beneficiario, 1, codiceOperazione++);
		operazioni.add(x);
		return x;

	}
	
	public Operazione BonificoUscita(int importo, String data,String originante, String beneficiario) {
		Operazione x = new Uscita(importo, data, originante, beneficiario, 1, codiceOperazione++);
		operazioni.add(x);
		return x;
	}
	
	public void mostraOperazioni() {
		System.out.println("Lista Movimenti");
		System.out.println("Cliente: " + this.intestatario);
		for (Operazione x : operazioni) {
			String t;
			if (x.getTipo()==1) {
				t = "Bonifico";
			} else {
				t = "Giroconto";
			}
			if (x instanceof Entrata) {
				System.out.println("Codice Operazione: " + x.getCodiceOperazioneLocale() + 
						", Entrata, " + " Tipologia: " + t + ", Importo: " + x.getImporto());		}
			else if (x instanceof Uscita) {
				System.out.println("Codice Operazione: " + x.getCodiceOperazioneLocale() + 
					", Uscita, " + " Tipologia: " + t + ", Importo: " + x.getImporto());
			}
		}
	}
	
	public void mostraOperazioniEntrata() {
		System.out.println("Lista Movimenti: operazioni in entrata");
		System.out.println("Cliente: " + this.intestatario);
		for (Operazione x : operazioni) {
			String t;
			if (x.getTipo()==1) {
				t = "Bonifico";
			} else {
				t = "Giroconto";
			}
			if (x instanceof Entrata) {
				System.out.println("Codice operazione: " + x.getCodiceOperazioneLocale() + 
						", Entrata, " + t + ", Importo: " + x.getImporto());		
			}
		}
	}
	
	public void mostraOperazioniUscita() {
		System.out.println("Lista Movimenti: operazioni in uscita");
		System.out.println("Cliente: " + this.intestatario);
		for (Operazione x : operazioni) {
			String t;
			if (x.getTipo()==1) {
				t = "Bonifico";
			} else {
				t = "Giroconto";
			}
			if (x instanceof Uscita) {
				System.out.println("Codice operazione: " + x.getCodiceOperazioneLocale() + 
						", Uscita, " + t + ", Importo: " + x.getImporto());		
			}
		}
	}
	
	public int mostraSaldo() {
		int saldo = 0;
		for (Operazione x : operazioni) {
			if (x instanceof Entrata) {
				saldo += x.getImporto();
			} else if (x instanceof Uscita) {
				saldo -= x.getImporto();
			}
		}
		return saldo;
	}
	
	
	// Giroconto
	public Operazione GirocontoUscita(int importo, String originante, String beneficiario, String data, 
			int codice) {
		Operazione x = new Uscita(importo, data,originante, beneficiario, 2, codice, 
				codiceOperazione++);
		operazioni.add(x);
		return x;
	}
	
	public Operazione GirocontoEntrata(int importo, String originante, String beneficiario, String data, 
			int codice) {
		Operazione x = new Entrata(importo, data, originante, beneficiario, 2, codice, 
				codiceOperazione++);
		operazioni.add(x);
		return x;
	}
	
	
}
	
	
	
	
	
