package gestioneStudenti;

public class Esame implements Autodescrivente{

	private int voto;    
	private int cfu;
	private String insegnamento;
	private int id;                 // Identificativo esame
	
	/*
	 * E' possibile definire delle variabili statiche che non esistono in copie diverse 
	 * all'interno di ciasun oggetto ma esistono in un'unica copia a livello di classe 
	 */
	private static int numeroEsami = 0;
	
	
	
	
	// COSTRUTTORE -> SOURCE -> GENERATE COSTRUCTOR USING FIELDS
	public Esame(int voto, int cfu, String insegnamento) {
		this.voto = voto;
		this.cfu = cfu;
		this.insegnamento = insegnamento;
		
		/*
		 * Ogni oggetto di tipo esame si autoattribuisce un codice identificativo univoco
		 * Segue post-incremento del numero degli esami
		 */
		this.id = numeroEsami++;
	}
	
	/*
	 * Accesso in lettura ad una copia dei dati GET
	 * Accesso in scrittura ai dati per una rettifica SET 
	 * (imponendo una logica filtro per le richieste)
	 */
	
	public int getId() {
		return id;
	}
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
	
	// Descrizione sintetica dell'esame sostenuto
	public String getDescrizione() {
		return "id: " + this.id + " ins: " + this.insegnamento + " voto: " + this.voto +  " CFU: " + this.cfu;
	}
	
	
}
