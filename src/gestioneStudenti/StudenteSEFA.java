package gestioneStudenti;

/*
 * Classe utilizzata per descrivere e per gestire gli studenti iscritti a SEFA
 * E' ottenuta derivando la classe padre STUDENTE
 * Una classe che deriva un'altra classe ne eredita tutti gli attributi (variabili/oggetti) e tutti i metodi
 * La nuova classe, figlia, avrà accesso a metodi e attributi della classe padre
 * purché questi siano pubblici o protetti
 * 
 * Nel linguaggio Java non è ammessa l'ereditarietà multipla, ciò implica che
 * - una stessa classe può essere derivata numerose volte (si veda il caso della classe STUDENTE)
 * - una stessa classe può derivare 'solo' un'altra classe 
 * (una stessa classe può avere molteplici classi figlie ma solo una classe padre)
 * - una classe che deriva un'altra classe può comunque essere derivata
 * 
 * E' possibile impedire che una classe venga derivata definendola come FINAL
 */

public final class StudenteSEFA extends Studente {
	
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
		if (esami.size() == 0) {
			return 0.0;
		}

		if (esami.size() == 1) {
			return (double) esami.get(0).getVoto();
		}

		int somma = 0;
		for (Esame e : esami) {
			somma += e.getVoto();
		}

		return (double) somma / esami.size();

	}
	
}
