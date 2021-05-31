package gestioneStudentiBase;
/*
 * Consente la gestione della carriera accademica di numerosi studenti, supporta la presenza
 * di diverse operazioni quali
 * - aggiunta di un nuovo studente
 * - rimozione di uno studente pre-esistente
 * 
 * In particolare, per ciasuno studente, è possibile effettuare operazioni di 
 * - rettifica dei dati inseriti
 * - verbalizzazione di un nuovo esame
 * - illustrazione della carriera 
 */

public class GestioneStudenti {
	/*
	 * E' necessario gestire un certo numero di studenti non noto a priori
	 * mediante una struttura dati dinamica
	 *
	 * Creazione di una variabile puntatore ad un ARRAY 
	 * utilizzato per tenere traccia di oggetti di tipo STUDENTE (definiti nella omonima classe)
	 * presenti nella applicazione 
	 */
	private Studente[] studenti;
	
	/*
	 * Si fissi il numero massimo di studenti che è possibile trattenere in memoria all'interno 
	 * dell'ARRAY puntato dalla variabile STUDENTI
	 */
	private int numeroMassimoStudenti = 10;
	
	/*
	 * Numero di studenti attualmente presenti in memoria
	 */
	private int numeroStudenti = 0;
	
	/*
	 * COSTRUTTORE
	 * Creazione immediata dell'array puntato dalla variabile STUDENTI 
	 * si prenotano 'numeroMassimoStudenti' caselle 
	 */
	public GestioneStudenti () {
		studenti = new Studente[numeroMassimoStudenti];
	}
	
	/*
	 * METODO AGGIUNGI STUDENTI
	 * consente la creazione di un nuovo oggetto di tipo STUDENTE adoperando come informazioni
	 * di base quelle fornite da input (COGNOME, NOME e CORSO DI LAUREA)
	 * Il metodo restituisce il valore intero rappresentante la posizione indice dell'elemento
	 * inserito nell'ARRAY puntato dalla variabile STUDENTI
	 */
	public int aggiungiStudente(String cognome, String nome, String cdl) {
		/*
		 *  Se nell'applicazione sono presenti diversi oggetti appartenenti a classi
		 *  ottenute per derivazione da una stessa classe padre
		 *  (classe padre -> STUDENTE, classe figlia -> STUDENTE SEFA),
		 *  è possibile far riferimento a tali oggetti attraverso la loro classe oppure
		 *  codificandoli come oggetti della classe padre
		 */
		
		/*
		 * - creare un nuovo oggetto di tipo STUDENTE con le informazioni ricevute da input
		 * - specificazione della classe figlia 
		 * - memorizzare 
		 * - utilizzare come puntatore la variabile locale 's'
		 */
		
		Studente s = null;
		if (cdl == "SEFA") {
			 s = new StudenteSEFA(cognome,nome);
		} else if (cdl == "SG") {
			 s = new StudenteSG(cognome, nome);
		}
		
		// - riportare le informazioni in coda all'ARRAY puntato dalla variabile STUDENTI
		// - incrementare il contatore NUMERO STUDENTI
		studenti[numeroStudenti++] = s;

		return (numeroStudenti-1); // Gli indici partono da 0!!!
	}
	
	/*
	 * METODO RIMUOVI STUDENTE
	 * Tale metodo consente la rimozione di uno studente dalla lista studenti
	 * Il metodo prende come input l'indice dell'elemento che si vuole rimuovere e
	 * restituisce un valore Booleano (TRUE) se l'operazione è andata a buon fine
	 */
	public boolean rimuoviStudente(int idStudente) {
		/*
		 * Per evitare la creazione di caselle vuote o NULL, assegnare la casella dello studente 
		 * da eliminare all'ultimo studente inserito
		 * PROBLEMA APERTO bisogna aggiornare ID STUDENTE
		 */
		studenti[idStudente] = studenti[numeroStudenti-1];
		// L'ultima casella diviene NULL
		studenti[numeroStudenti-1] = null;
		// Aggiornare il numero di studenti (sottrarre 1)
		numeroStudenti--;
		return true;
	}
	
	/*
	 * METODO RETTIFICA NOME STUDENTE
	 * Tale metodo sovrascrive il nome (precedentemente inserito) di uno studente 
	 * con quello indicato da input,
	 * se l'operazione ha successo si restituisca il valore booleano TRUE
	 */
	public boolean rettificaNomeStudente(int idStudente, String nome) {
		// Riferimento al metodo SET NOME della classe STUDENTE
		studenti[idStudente].setNome(nome);
		return true;
	}
	
	/*
	 * METODO MOSTRA STUDENTI
	 * Tale metodo passa in rassegna tutti gli studenti presenti in memoeria nell'ARRAY 
	 * e mostra a schermo nome e cognome
	 */
	public void mostraStudenti() {
		// Riferimento ai metodo GET NOME e GET COGNOME della classe STUDENTE
		for(int i=0; i<numeroStudenti;i++) {
			System.out.println(studenti[i].getCognome() + " " + studenti[i].getNome());
		}
	}
	
	/*
	 * METODO VERBALIZZA ESAME
	 * Il metodo recupera la scheda dello studente il cui codice identificativo è indicato in INPUT 
	 * e la utilizza per verbalizzare l'esame in questione
	 * Il metodo non restituisce alcunché (VOID)
	 */
	public void verbalizzaEsame(int idStudente, int voto, int cfu, String insegnamento) {
		// Lookup dello studente prescelto
		// La variabile locale 's' punta la scheda dello studente prescelto
		Studente s = studenti[idStudente];
		
		/*
		* Verbalizzazione dell'esame richiamando il metodo VERBALIZZA ESAME definito nella classe
		* STUDENTE
		* */
		s.verbalizzaEsame(voto, cfu, insegnamento);
	}
	
	/*
	 * METODO MOSTRA STUDENTE
	 * Il metodo riceve in input il codice identificativo dello studente
	 * per resituirne a schermo le principali informazioni
	 * (si consulti il metodo MOSTRA INFORMAZIONI definito nella classe STUDENTE)
	 */
	public void mostraStudente(int idStudente) {
		Studente s = studenti[idStudente];
		/*
		 * POLIMORFISMO
		 * Quando eseguiamo un metodo su un oggetto cui si fa riferimento 
		 * atraverso una variabile del tipo della classe padre, il metodo 
		 * effettivamente eseguito sarà quello della classe figlia (se presente)
		 */
		s.mostraInformazioni();
	}
	
	
	
	public static void main(String[] args) {
		GestioneStudenti gs = new GestioneStudenti();
		// Salvare in memoria gli indici degli studenti inseriti
		int idStudente1 = gs.aggiungiStudente("Bianchi", "Maria", "SEFA");
		int idStudente2 = gs.aggiungiStudente("Rossi", "Franco", "SG");
		int idStudente3 = gs.aggiungiStudente("Verdi", "Alessandro", "SG");
		
		gs.verbalizzaEsame(idStudente1, 28, 9, "Statistica di Base");
		gs.verbalizzaEsame(idStudente1, 25, 9, "Fondamenti di Informatica");
		
		gs.mostraStudenti();
		gs.mostraStudente(idStudente1);
		
		// gs.mostraStudenti();
		// gs.rimuoviStudente(idStudente1);
		// gs.mostraStudenti();
	
	}
}
