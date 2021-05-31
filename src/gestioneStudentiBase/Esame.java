package gestioneStudentiBase;

/*
 * Classe utilizzata per ospitare i dettagli circa ciascun singolo esame verbalizzato
 */

public class Esame {
	
	/*
	 * Variabili membro della classe ESAME,
	 * assieme costituiscono lo stato della classe
	 */
	private int voto;    
	private int cfu;
	private String insegnamento;
	
	// COSTRUTTORE -> SOURCE -> GENERATE COSTRUCTOR USING FIELDS
	public Esame(int voto, int cfu, String insegnamento) {
		this.voto = voto;
		this.cfu = cfu;
		this.insegnamento = insegnamento;
	}
	
	/*
	 * Accesso in lettura ad una copia dei dati GET
	 * Accesso in scrittura ai dati per una rettifica SET 
	 * (imponendo una logica filtro per le richieste)
	 */
	public int getVoto() {
		return voto;
	}

	public void setVoto(int voto) {
		if (voto > 31) {
			return;
		}
		this.voto = voto;
	}

	public int getCfu() {
		return cfu;
	}

	public void setCfu(int cfu) {
		this.cfu = cfu;
	}

	public String getInsegnamento() {
		return insegnamento;
	}

	public void setInsegnamento(String insegnamento) {
		this.insegnamento = insegnamento;
	}
	
	
	
	
}
