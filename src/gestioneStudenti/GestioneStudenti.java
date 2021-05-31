package gestioneStudenti;
import java.util.*;
/*
 * Si faccia riferimento al pacchetto GESTIONE STUDENTI BASE
 * per le principali informazioni circa l'implementazione di tale applicazione
 * Nella versione presente nel pacchetto GEBD sono previste le medesime operazioni 
 * implementate in meniera più raffinata ed efficiente
 */

public class GestioneStudenti {
	
	/*
	 * Utilizzare ArrayList, si veda quanto discusso nella classe STUDENTE
	 * Creazione della variabile STUDENTI puntatore ad un ARRAYLIST contenente oggetti di tipo
	 * STUDENTE
	 */
	private ArrayList<Studente> studenti;
	
	/*
	 * Creazione della variabile ID TO STUDENTE puntatore ad un dizionario che conterrà
	 * un codice numerico intero identificativo ed il corrispondente oggetto di tipo STUDENTE
	 */
	private HashMap<Integer, Studente> idToStudente;
	
	/*
	 * E' possibile definire delle variabili statiche che non esistono in copie diverse 
	 * all'interno di ciasun oggetto ma esistono in un'unica copia a livello di classe 
	 */
	private static int numeroStudenti = 0;
	
	
	/*
	 * COSTRUTTORE
	 */
	public GestioneStudenti () {
		studenti = new ArrayList<Studente>();
		idToStudente = new HashMap<Integer, Studente>();
	}
	
	/*
	 * METODO AGGIUNGI STUDENTI
	 */
	public int aggiungiStudente(String cognome, String nome, String cdl) {
		
		Studente s = null;
		if (cdl == "SEFA") {
			 s = new StudenteSEFA(cognome,nome);
		} else if (cdl == "SG") {
			 s = new StudenteSG(cognome, nome);
		}
		
		// Aggiungere in coda all'ARRAY LIST
		studenti.add(s);
		
		/*
		 * Si desidera identificare ciascuno studente con un codice numerico che non verrà modificato
		 * Si adoperi come codice identificativo il valore corrente di NUMERO STUDENTI per poi 
		 * post incrementarlo
		 * Inserire l'oggetto STUDENTE appena creato nel dizionario con chiave NUMERO STUDENTI
		 */
		idToStudente.put(numeroStudenti, s);
		
		// Restituzione del codice identificativo
		return numeroStudenti++; 
	}
	
	/*
	 * METODO RIMUOVI STUDENTE 
	 */
	public boolean rimuoviStudente(int idStudente) throws StudenteException{
		// Interrogare il dizionario 
		Studente s = idToStudente.get(idStudente);
		if (s == null) {
			throw new StudenteException();
		}
		
		// Rimozione da dizionario e rimozione da ARRAY LIST
		idToStudente.remove(idToStudente, s);
		studenti.remove(s);
		return true;
	}
	
	/*
	 * METODO RETTIFICA NOME STUDENTE
	 */
	public boolean rettificaNomeStudente(int idStudente, String nome) throws StudenteException {
		Studente s = idToStudente.get(idStudente);
		if (s == null) {
			throw new StudenteException();
		}
		
		// Richiamare il metodo SET NOME definito nella classe STUDENTE
		s.setNome(nome);
		return true;
	}
	
	/*
	 * METODO MOSTRA STUDENTI
	 */
	public void mostraStudenti() {
		for (Studente s: studenti) {
			// Richiamare metodi GET NOME e GET COGNOME definiti nella classe STUDENTE
			System.out.println(s.getCognome() + " " + s.getNome());
		}
	}
	
	
	// Mostrare studenti di un dato corso di laurea
	public void mostraStudenti(String cdl) {
		for (Studente s : studenti) {
			
			/*
			 * Controllare se CDL coincide con quello da input
			 * L'operatore INSTANCE OF consente di indagare la natura di un certo oggetto
			 * rispondendo a domande del tipo:
			 * un certo oggetto è di un certo tipo?
			 */
			if (cdl == "SEFA") {
				if (s instanceof StudenteSEFA) {
					System.out.println(s.getCognome() + " " + s.getNome());				}
			} else if (cdl == "SG") {
				if (s instanceof StudenteSG) {
					System.out.println(s.getCognome() + " " + s.getNome());				}
			} 
		}
	}
	
	/*
	 * METODO VERBALIZZA ESAME
	 */
	public int verbalizzaEsame(int idStudente, int voto, int cfu, String insegnamento) throws VotoException, StudenteException {
		Studente s = idToStudente.get(idStudente);
		
		if (s == null) {
			throw new StudenteException();
		}
		
		/*
		* Verbalizzazione dell'esame richiamando il metodo VERBALIZZA ESAME definito nella classe
		* STUDENTE
		*/
		return s.verbalizzaEsame(voto, cfu, insegnamento);
		
		/*
		 * Nel momento in cui si esegue un metodo che può lanciare una eccezione si può
		 * - gestire immediatamente l'eccezione 
		 * - passare la palla al metodo ordinante (MAIN)
		 * 
		 * CASO 1 - Gestione in prima persona delle eccezioni
		 * Il codice all'interno del quale si può generare una certa eccezione deve essere racchiuso 
		 * all'intenro di un blocco TRY/CATCH
		 * Il blocco di istruzioni preceduto dall'istruzione TRY è quello che Java proverà ad eseguire
		 * e contiene al suo interno almeno una istruzione che può sollevare almeno una eccezione
		 * Il blocco CATCH elenca tutte le possibili sitazioni di errore definendo per ciasuna di essa 
		 * la strategia risolutiva
		 * Segue un esempio alternativo di codice per la gestione delle eccezioni in caso ne siano 
		 * sollevate (codice sostitutivo del precedente RETURN)
		 */
		
		/*
		 * int esito;
		 * try {
		 * esito = s.verbalizzaEsame(voto, cfu, insegnamento);
		 * } catch (VotoException e) {
		 *	System.out.println("Voto non valido");
		 * }
		 * return esito;
     	*/
		
		/* CASO 2 
		 * Si rimandi la gestione al metodo ordinanante dell'operazione (MAIN)
		 */
	}
	
	/*
	 * METODO MOSTRA STUDENTE
	 */
	public void mostraStudente(int idStudente) throws StudenteException{
		Studente s = idToStudente.get(idStudente);
		if (s == null) {
			throw new StudenteException();
		}
		
		// RIferimento al metodo GET DESCRIZIONE definito nella classe STUDENTE
		System.out.println(s.getDescrizione());
	}
	
	/*
	 * METODO ELIMINA ESAME
	 * Il metodo elimina un esame precedentemente verbalizzato
	 * Il metodo riceve in input il codice identificativo di studente ed esame
	 * Il metodo non restituisce formalmente alcunché (VOID)
	 */
	public void eliminaEsame(int idStudente, int idEsame) throws StudenteException{
		Studente s = idToStudente.get(idStudente);
		if(s == null) {
			throw new StudenteException();
		}
		
		/*
		 * Eliminazione dell'esame facendo riferimento al metodo ELIMINA ESAME definito 
		 * nella classe STUDENTE
		 */
		s.eliminaEsame(idEsame);
	}
	
	public static void main(String[] args) {
		GestioneStudenti gs = new GestioneStudenti();
		int idStudente1 = gs.aggiungiStudente("Bianchi", "Maria", "SEFA");
		int idStudente2 = gs.aggiungiStudente("Rossi", "Franco", "SG");
		int idStudente3 = gs.aggiungiStudente("Verdi", "Alessandro", "SG");
		
		int voto; 
		// CASO 2 - Gestione eccezioni
		boolean retry = true;
		while(retry) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Inserire il voto da verbalizzare");
			voto = sc.nextInt();
		
			try {
				int idEsame1 = gs.verbalizzaEsame(idStudente1, voto, 9, "Statistica di Base");
				// Se tutto va bene si esca dal ciclo
				retry = false;
			} catch (VotoException e) {
				System.out.println("Il voto inserito non è valido: " + voto);
				e.printStackTrace();
			}
		}
		
		
		int idEsame1 = gs.verbalizzaEsame(idStudente1, 28, 9,"Statistica di Base");
		int idEsame2 = gs.verbalizzaEsame(idStudente1, 25, 9,"Fondamenti di Informatica");
		System.out.println("Prima");
		gs.mostraStudente(idStudente1);
		gs.eliminaEsame(idStudente1, idEsame1);
		System.out.println("Dopo");
		gs.mostraStudente(idStudente1);
		
		System.out.println("Mostrare studenti");
		gs.mostraStudenti();
		gs.rimuoviStudente(idStudente2);
		System.out.println("Mostrare studenti SEFA");
		gs.mostraStudenti("SEFA");
		System.out.println("Mostrare studenti SG");
		gs.mostraStudenti("SG");
	
		/*
		 * Una istanza della classe STUDENTE non condivide alcuna relazione di parentela
		 * con una istanza delle classe ESAME
		 */
		
		// Studente s = null;
		// Esame e = null;
		
		/*
		 * E' possibile far riferimento ad un oggetto non solo attraverso la classe
		 * di cui è istanza oppure attraverso lee sue classi antenate, 
		 * ma anche attraverso l'interfaccia che implementa
		 * 
		 * Un oggetto di tipo STUDENTE è anche un oggetto di tipo AUTODESCRIVENTE
		 * Un oggetto di tipo ESAME è anche un oggetto di tipo AUTODESCRIVENTE
		 * Su tutti tali oggetti posso chiamare get.descrizione
		 */
		
		// Autodescrivente a1 = s;
		// Autodescrivente a2 = e;
		
		/*
		 * Oggetti di natura diversa diventano assimilabili tra loro poiché implementano tutti la stessa
		 * interfaccia, diviene possibile fare genericamente riferimento ad essi 
		 * attraverso la comune interfaccia
		 */
		
		// a1.getDescrizione();
		// a2.getDescrizione();
	}
}
