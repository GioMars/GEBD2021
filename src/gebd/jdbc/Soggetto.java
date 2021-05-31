package gebd.jdbc;

/*
 * Classe utilizzata per custodire le generalità di un singolo soggetto
 * Si impone che la classe non possa essere derivata mediante indicazione FINAL
 */

public final class Soggetto {
	
	private int id;
	private String nome;
	private String cognome;
	private String annoNascita;
	
	// Costruttore degli elementi da richiedere 
	public Soggetto(int id, String nome, String cognome, String annoNascita) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.annoNascita = annoNascita;
	}
	
	/*
	 * Metodi GETTERS and SETTERS
	 * Metodi SETTERS solo su nome e su cognome
	 * ID non può essere modificato 
	 */
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public int getId() {
		return id;
	}

	public String getAnnoNascita() {
		return annoNascita;
	}

	public void setAnnoNascita(String annoNascita) {
		this.annoNascita = annoNascita;
	}
	
	
	
}
