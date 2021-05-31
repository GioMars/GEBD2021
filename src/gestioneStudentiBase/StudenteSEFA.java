package gestioneStudentiBase;

/*
 * Classe utilizzata per descrievere e per gestire gli studenti iscritti a SEFA
 * E' ottenuta derivando la classe padre STUDENTE
 * Una classe che deriva un'altra classe ne eredita tutti gli attributi (variabili/oggetti) e tutti i metodi
 * La nuova classe, figlia, avrà accesso a metodi e attributi della classe padre
 * purché questi siano pubblici o protetti
 */

public class StudenteSEFA extends Studente {
	
	/*
	 * Dal momento che la classe STUDENTE padre possiede un costruttore, 
	 * lo si deve riproporre anche qui
	 * Nel costruttore aggiunto si prevedono come argomenti di input
	 * gli stessi presenti nella classe padre
	 * Per evitare di ripetere le stesse istruzioni presenti nella classe padre, si 
	 * richiamano le medesime istruzioni mediante SUPER
	 */
	public StudenteSEFA(String nome, String cognome) {
		super(nome, cognome, "SEFA");
	}
	
	
	
	/*
	 * METODO CALCOLA MEDIA 
	 */
	public double calcolaMedia() {
		if (numeroEsami == 0) {
			return 0.0;
		}

		if (numeroEsami == 1) {
			return (double) esami[0].getVoto();
		}

		int somma = 0;
		for (int i = 0; i < numeroEsami; i++) {
			somma += esami[i].getVoto();
		}

		return (double) somma / numeroEsami;

	}
	
	/*
	 * L'implementazione che segue sovrascrive quella già presente nella classe padre
	 * poiché la struttura del metodo è identica ma il contenuto differisce
	 */
	
	public void mostraInformazioni() {
		System.out.println("Nome: " + this.nome);
		System.out.println("Cognome: " + this.cognome);
		System.out.println("Iscritto a SEFA");
		mostraVoti();
	}
	

}
