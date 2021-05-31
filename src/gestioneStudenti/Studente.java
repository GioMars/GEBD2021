package gestioneStudenti;

/*
 * In tale classe si farà uso di altre classi presenti nel pacchetto JAVA.UTIL
 */
import java.util.*;
	
/*
 * Elencare anche le eventuali interfacce implementate da tale classe 
 */
public abstract class Studente implements Autodescrivente {

	private String nome;
	private String cognome;
	private String cdl;       // Corso di laurea
	
	
	/*
	 * La scelta di gestire l'insieme degli esami sostenuti da un particolare soggetto 
	 * utilizzando un array è discutibile, soprattutto in merito alla dinamicità della struttura dati
	 * (si veda versione GESTIONE STUDENTI BASE)
	 * Come fare quando l'array è pieno?
	 * Come fare quando è quasi del tutto inutilizzato?
	 * Quando si ha bisogno di gestire collezioni di diverso tipo, in Java, è comodo 
	 * utilizzare delle classi COLLECTION
	 * Il termine COLLECTION indica una libreria di classi presenti nell'installazione di Java che 
	 * consentono di memorizzare, mantenere e processare delle collezioni di valori ottimizzate 
	 * rispetto determinate operazioni
	 * 
	 * Esistono diversi tipi di COLLECTIONS, per esempio il tipo LIST
	 * Con il termine LIST si intende una struttura dati lineare che tiene traccia dell'ordine con 
	 * cui gli elementi sono al suo interno inseriti e rende possibile l'accesso ad essi mediante indice
	 * Esistono in Java diverse implementazioni di LIST
	 * per esempio si utilizzi ARRAYLIST
	 */

	/*
	 * L'elenco degli esami è mantenuto da una istanza della classe ARRAYLIST,
	 * tale classe implementa una COLLECTION di tipo LIST
	 * Quando si implementa una classe ARRAYLIST, non si conosce a priori il tipo di dato con cui si 
	 * lavorerà; una lista potrebbe essere utilizzata per mantenere un elenco di stringhe, numeri...
	 * Per gestire casi come questi è possibile definire in Java delle classi che fanno uso di tipi GENERICS,
	 * il codice di tali classi non è specializzato per gestire un particolare tipo di dato, ma funziona 
	 * con un tipo di dato variabile indicato da input al momento della creazione dell'oggetto
	 * 
	 * protected ArrayList<> NomeVariabile;
	 * 
	 * Se si vuole creare un ARRAYLIST che contenga oggetti di tipo ESAME, dichiarare una variabile di tipo
	 * ARRAYLIST ed indicare tra parentesi <> il tipo desiderato
	 */
	protected ArrayList<Esame> esami;
	
	/*
	 * I contenitori di tipo collection contengono anche altre classi ottimizzate per l'accesso ai dati:
	 * i DIZIONARI
	 * Ci si riferisce a strutture nelle quali i nostri dati sono organizzati sotto forma di coppie
	 * <CHIAVE, VALORE>
	 * Su queste strutture dati sono presenti principalmente le seguenti operazioni
	 * - insert (K,V)
	 * - search (K)
	 * - delete (K)
	 * si assume che i valori V siano contrassegnati con delle chiavi K attraverso le quali è possibile
	 * indicizzarli, inserirli nel dizionario e richiamarli efficientemente
	 * La particolarità dei dizionari risiede in una implementazione interna altamente
	 * efficiente nell'esecuzione delle operazioni sopra descritte
	 * 
	 * Java offre molte implementazioni di dizionari 
	 * Si utilizzi HASHMAP
	 * HASHMAP richiede in input due tipi corrispondenti a
	 * - il tipo utilizzato per codificare la chiave K
	 * - il tipo utilizzato per codificare il valore V
	 * 
	 * Nella corrente implementazione si ha la corrispondenza tra 
	 * - chiavi <-> codici identificativi interi 
	 * - valori <-> oggetti di tipo esame
	 * 
	 * La classe HASHMAP non accetta tipi primitivi per costruzione
	 * Nel caso in cui una delle due classi corrisponda ad un tipo primitivo è necessario 
	 * ricorrere alle classi Wrapper (contenitori di tipi primitivi)
	 * Integer
	 * Long 
	 * Double
	 * Boolean
	 * Float
	 * Short
	 */
	
	/*
	 * Creazione di una variabile puntatore al dizionario sopra descritto
	 * */
	protected HashMap<Integer, Esame> idToEsame;
	
	/*
	 * COSTRUTTORE
	 */
	public Studente(String cognome, String nome, String cdl) {
		this.cognome = cognome; 
		this.nome = nome;
		this.cdl = cdl;
		
		/*
		 * Quando si crea un nuovo ARRAYLIST non è necessario specificarne la taglia, 
		 * si tratta difatti di una struttura dati dinamica
		 * Se necessario, la taglia di un ARRAYLIST è automaticamente incrementata o decrementata sulla base 
		 * del fattore di carico
		 */
		esami = new ArrayList<Esame>();
		
		idToEsame = new HashMap<Integer, Esame>();
	}
	
	// COSTRUTTORE ALTERNATIVO
	public Studente(String cognome, String nome) {
		this(cognome,nome,"na");
	}
	
	
	
	/*
	 * GETTERS/SETTERS
	 */
	public void setNome(String nome) {
		if (nome.length() == 0) {
			return;
		}
		this.nome = nome;
		}
	
	public String getNome() {
		return nome;
	}
	
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
 	
	public String getCdl() {
		return cdl;
	}

	public void setCdl(String cdl) {
		this.cdl = cdl;
	}

	
	/*
	* METODO VERBALIZZA ESAME
	* Si supponga il caso di inserimento di un voto non valido:
	* laddove questo dovesse accadere il metodo terminerà immediatamente per resituire
	* una eccezione di tipo VOTO EXCEPTION
	* Per indicare la presenza di una possibile eccezione si adopera la keyword THROWS nella firma 
	* del metodo
	*/
	public int verbalizzaEsame(int voto, int cfu, String insegnamento) throws VotoException {
		if ((voto < 18) || (voto > 31)) {
		/*
		 * Se si incappa in una delle situazioni per cui il voto viene ritenuto non valido,
		 * è necessario confezionare una nuova eccezione di tipo VotoException e 
		 * restuirla al chiamante
		 */
			throw new VotoException();
		}
		
	    /*
         * Creare un oggetto di tipo ESAME per ospitare i dati forniti da input
		 */
		Esame e = new Esame(voto, cfu, insegnamento);
		
		// Aggiunta in coda all'ARRAY LIST mediante metodo pre-implementato ADD
		esami.add(e);
		
		/*
		 * Richiamare il metodo GET ID definito nella classe ESAME
		 * Inserire nel dizionario la coppia
		 * (ID esame generato, oggetto ESAME)
		 */
		idToEsame.put(e.getId(), e);
		
		/*
		 * Restituire come valore di ritorno il codice identificativo dell'esame appena creato
		 */
		return e.getId();
	}
	
	
	/*
	 * METODO MOSTRA VOTI  
	 */
	public String getVoti() {
		/* 
		 * DUE METODI ALTERNATIVI
		 * METODO 1
		 * Il metodo SIZE restituisce la taglia della lista
		 * Utilizzare il metodo GET per prelevare un elemento dalla lista 
		 * ( + metodo GETVOTO definito nella classe ESAME per prelevare
		 * il voto dall'oggetto ESAME)
		 * 
		for(int i=0; i<esami.size(); i++) {
			System.out.println(esami.get(i).getVoto());   //Ogni voto è stampato su una riga
		}
		 *
		 * Non si è interessati a conoscere la struttura dati utilizzata internamente dalla classe 
		 * ARRAYLIST per conservare i valori in memoria
		 * In un caso come questo si è esclusivamente interessati ad accedere ad ogni valore presente nella
		 * lista, tenendo conto dell'ordine di inserimento
		 * Java mette a disposizione una variante per il ciclo FOR utilizzabile per passare in rassegna
		 * il contenuto di una COLLECTION  
		 * 
		 * NOTA BENE: tale iterazione salta eventuali 'buchi' presenti nella lista sfruttando
		 * un metodo pre-implementato NEXT NOT NULL
		 * Un problema risolto rispetto il tradizionale FOR
		 *
		 * PER CIASUN ESAME PRESENTE NELLA COLLECTION ESAMI, ESEGUIRE
		 * 'e' variabile temporanea che a turno punta ad ogni elemento nella lista
		 * 
		 * Inserire il tutto in una STRINGA 
		 */
		
		String voti = "";
		for (Esame e : esami) {
			voti += e.getVoto() + "\n";
		}
		return voti;
	}
	
	/*
	 * METODO GET DESCRIZIONE
	 * Fornisce una stringa descrittiva dello studente in forma sintetica
	 */
	public String getDescrizione() {
		String descrizione = "Nome: " + this.nome + "\n";
		descrizione += "Cognome: " + this.cognome + "\n";
		descrizione += "Corso di Laurea: " + this.cdl + "\n";
		descrizione += getVoti();
		return descrizione;
	}
	
	
	/*
	 * METODO CERCA MINIMO
	 */
	public int cercaMinimo() {
		if (esami.size() == 0) {
			return 0;
		}
		
		// Un solo voto nell'array, si restituisca direttamente 
		if (esami.size() == 1) {
			return esami.get(0).getVoto();
		}
		
		int votoMinimo = 999;
		for(Esame e : esami) {
			if (e.getVoto() < votoMinimo) {
				votoMinimo = e.getVoto();
			}
		}
		return votoMinimo;
	}
	
	// CERCA MASSIMO 
	public int cercaMassimo() {
		if (esami.size() == 0) {
			return 0;
		}
		
		// Un solo voto nell'array, si restituisca direttamente 
		if (esami.size() == 1) {
			return esami.get(0).getVoto();
		}
		
		int votoMassimo = 0;
		for(Esame e : esami) {
			if (e.getVoto() > votoMassimo) {
				votoMassimo = e.getVoto();
			}
		}
		return votoMassimo;
	}
	
	/* METODO CERCA ESAME
	 * Dato in input il codice identificativo di un esame, 
	 * tale metodo restituisce NULL se l'esame non è esistente 
	 * (per costruzione )
	 * viceversa restituisce nuovamente 
	 */
	public Esame cercaEsame(int id) {
		return idToEsame.get(id);
	}

	/*
	 * METODO ELIMINA ESAME
	 * Tale metodo consente di rimuovere un esame precedentemente verbalizzato
	 */
	public void eliminaEsame(int idEsame) {
		Esame e = cercaEsame(idEsame);
		if (e != null) {
		// Metodi REMOVE in ARRAYLIST
		esami.remove(e);
		}
	}
	
	public abstract double calcolaMedia();

	
}
