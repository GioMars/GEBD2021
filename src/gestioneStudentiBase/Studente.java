package gestioneStudentiBase;
/*
 * Tale classe è utilizzata per incapsulare gli attributi relativi al 'concetto' di 
 * uno studente per poterli manipolare in accordo alle esigenze applicative
 * 
 * Attraverso questa classe sarà possibile eseguire tutte le operazioni che riguardano il ciclo di
 * vita delle variabili che descrivono la carriera di uno studente
 * 
 * All'interno della classe sono presenti delle variabili che prendono il nome
 * di 'attributi'/'fields'/'variabili membro' 
 * Le variabili membro descrivono le diverse sfaccettature di un concetto
 * 
 * In questo modo è possibile utilizzare una classe come contenitore di dati tenendo assieme
 * valori/variabili altrimenti indipendenti
 * 
 * La classe STUDENTE è costruita come classe PROTOTIPO (PADRE)
 * da utilizzare per fissare tutti gli aspetti, in termini di variabili membro e metodi, comuni alle diverse 
 * classi specializzate per gestire gli iscritti ai diversi corsi di laurea
 * 
 * Non si desidera che le applicazioni abbiano la possibilità di creare direttamente
 * oggetti del tipo STUDENTE,
 * si pretende piuttosto che passino sempre attraverso una delle classi figlie,
 * per imporre tale vincolo definisco questa classe come ASTRATTA
 * (non è possibile creare oggetti di un tipo ASTRATTO)
 */


public abstract class Studente {
	/*
	 * Le variabili membro sono dotate di un indicatore di visibilità che ne stabilisce 
	 * la leggibilità anche al di fuori della classe (PUBLIC)
	 * Le variabili membro di una classe possono avere diversi livelli di visibilità
	 * - PUBLIC
	 * - PROTECTED
	 * - PRIVATE
	 * - PACKAGE
	 * Per default (senza alcuna indicazione) una variabile membro è visibile a livello package, 
	 * qualsiasi classe appartenente allo stesso pacchetto può accedervi in lettura ed in scrittura
	 * (Lo stesso vale, come logica, per i metodi)
	 * 
	 * A livello PRIVATE una variabile è visibile esclusivamente ai metodi della classe 
	 * A livello PROTECTED una variabile è visibile ai metodi della classe ed ai suoi discententi
	 */
	protected String nome;
	protected String cognome;
	protected String cdl;       // Corso di laurea
	
	/*
	 * GESTIONE DEGLI ESAMI
	 * Codificare ogni esame sostenuto dallo studente tramite una nuova classe che al suo interno contenga 
	 * - 1 voto
	 * - numero di crediti corrispondenti all'esame sostenuto
	 * - il nome dell'insegnamento
	 */
	
	/*
	 * Creazione di una variabile puntatore ad un ARRAY 
	 * utilizzato per tenere traccia di oggetti di tipo ESAME (definiti nella omonima classe)
	 * presenti nella applicazione 
	 * Si fissi il numero massimo di esami che è possibile trattenere in memoria all'interno 
	 * dell'ARRAY puntato dalla variabile ESAMI
	 */
	private int numeroMassimoEsami = 30;
	protected Esame[] esami;
	
	// Contatore del numero di esami inseriti in memoria
	protected int numeroEsami = 0;
	
	/*
	 * OGGETTO -> ISTANZA di tipo STUDENTE
	 * Nel momento in cui è richiesta la creazione di un oggetto di tipo STUDENTE
	 * è necessario specificarne da subito
	 * NOME e COGNOME
	 * E' possibile definire dei blocchi di istruzioni la cui esecuzione è contestuale alla
	 * creazione di nuovi oggetti
	 * Tali blocchi di istruzioni portano il nome di COSTRUTTORI
	 * I costruttori sono particolari metodi eseguiti all'atto della creazione 
	 * di un oggetto del tipo in cui sono presenti
	 * Siano le seguenti tre condizioni
	 * - il nome del metodo costruttore deve coincidere con il nome della classe che lo ospita
	 * - i metodi costruttori non presentano alcun valore di ritorno
	 * - se in una classe Java è presente un costruttore occorre necessariamente passare attraverso di esso
	 * per creare oggetti del tipo desiderato
	 */
	
	/*
	 * COSTRUTTORE
	 * E' possibile avere in una stessa classe Java numerosi costruttori (aventi stesso nome,
	 * OVERLOADING, a patto che differiscano per l'elenco degli argomenti) per fornire all'utente
	 * maggiore flessibilità nelle possibilità degli input da inserire 
	 */
	public Studente(String cognome, String nome, String cdl) {
		/*
		 * Se in un metodo è presente una variabile il cui nome coincide con quello di una variabile
		 * membro -> la variabile membro viene "oscurata", all'interno del metodo, dalla variabile locale
		 * Le variabili locali sono quelle variabili che assumono significato unicamente all'interno del metodo 
		 * in questione 
		 * ESEMPIO
		 * cognome = cognome;
		 * nome = nome;
		 * E' possibile fare riferimento ad una variabile membro di una classe attraverso la keyword
		 * THIS (un puntatore implicito) 
		 */
		this.cognome = cognome; 
		this.nome = nome;
		this.cdl = cdl;
		
		/*
		 * Creazione di un oggetto di tipo esame in cui si preallocano 
		 * NUMEROMASSIMOESAMI caselle vuote
		 */
		esami = new Esame[numeroMassimoEsami];
	}
	
	/*
	 * Spesso i diversi costruttori sono una variante l'uno degli altri, 
	 * piuttosto che portare avanti numerose implementazioni di costruttori tutte simili tra loro
	 * è utile individuarne uno sufficientemente generale da includere anche gli altri
	 */
	public Studente(String cognome, String nome) {
		/*
		 * Piuttosto che ripetere la definizione di ciascun costruttore, è possibile richiamare
		 * il costruttore più generale mediante la keyword THIS
		 * 
		 * Tale costruttore, a differenza del primo, prevede che il dato circa il corso di laurea 
		 * non sia inserito
		 */
		this(cognome,nome,"na");
	}
	
	
	
	/*
	 * GETTERS/SETTERS
	 * Il metodo SETNOME richiede in input una stringa con il fine di scrittura della casella 
	 * puntata dalla variabile NOME
	 * (Per esempio a fini di aggiornamento)
	 * Il metodo non restituisce alcunché (VOID)
	 */
	public void setNome(String nome) {
		/*
		 * Il fatto di convogliare tutte le richieste di accesso in lettura o in scrittura 
		 * attraverso un unico metodo consente di codificare nel medesimo metodo la logica per cui le 
		 * richieste possono essere accolte o meno
		 * Ad esempio si consideri la lunghezza della stringa inserita
		 */
		if (nome.length() == 0) {
			return;
		}
		
		this.nome = nome;
		}
	
	/*
	 *  Il metodo GET regolamenta l'accesso in sola lettura al contenuto delle variabili
	 *  Il metodo, se richiamato, restituisce in output un variabile puntatore al contenuto desiderato, 
	 *  in tal caso una stringa
	 */
	public String getNome() {
		return nome;
	}
	
	// Cognome SOURCE -> GENERATE GETTERS AND SETTERS
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
	* Il metodo crea un nuovo oggetto di tipo ESAME utilizzando le informazioni acquisite da input
	* e lo aggiunge all'array ESAMI
	*/
	public void verbalizzaEsame(int voto, int cfu, String insegnamento){
		Esame e = new Esame(voto, cfu, insegnamento);
		esami[numeroEsami++] = e; // (Post-incrementare il numero di esami tramite ++)
	}
		
	/* METODI ISTANZA 
	 * Per default ciascun metodo istanza di una classe ha accesso alle variabili membro presenti
	 * nella classe medesima senza dover utilizzare alcuno strumento di accesso 
	 * (come se le variabili in questione fossero definite all'interno del metodo)
	 */
	
	/*
	 * METODO MOSTRA VOTI
	 * Implementare un metodo che mostri a schermo tutti i voti registrati in memoria e contenuti
	 * nell'ARRAY puntato dalla variabile ESAMI
	 * Formalmente il metodo non restituisce alcunché (VOID)
	 * Nel definire il contratto del metodo non è necessario fornire alcun argomento in quanto il metodo 
	 * ha già accesso al contenuto degli oggetti presenti nella classe  
	 */
	public void mostraVoti() {
		for(int i=0; i<numeroEsami; i++) {
			System.out.println(esami[i].getVoto());   //Ogni voto è stampato su una riga
		}
	}
	
	public void mostraInformazioni() {
		System.out.println("Nome: " + this.nome);
		System.out.println("Cognome: " + this.cognome);
		mostraVoti();
	}
	
	
	/*
	 * METODO CERCA MINIMO
	 * Processare i voti salvati in memoria nell'ARRAY puntato dalla variabile ESAMI
	 * Il voto minimo individuato è restituito all'applicazione che ha richiesto l'esecuzione 
	 * del metodo
	 * Operativamente si scorra la sequenza di voti determinando e restituendo il voto più basso
	 * Nel caso in cui l'array sia vuoto, si restituisca 0
	 */
	public int cercaMinimo() {
		if (numeroEsami == 0) {
			return 0;
		}
		
		// Un solo voto nell'array, si restituisca direttamente 
		if (numeroEsami == 1) {
			return esami[0].getVoto();
		}
		
		int votoMinimo = 999;
		for(int i=0; i<numeroEsami; i++) {
			if (esami[i].getVoto() < votoMinimo) {
				votoMinimo = esami[i].getVoto();
			}
		}
		return votoMinimo;
	}
	
	// CERCA MASSIMO 
	public int cercaMassimo() {
		if (numeroEsami == 0) {
			return 0;
		}
		
		// Un solo voto nell'array, si restituisca direttamente 
		if (numeroEsami == 1) {
			return esami[0].getVoto();
		}
		
		int votoMassimo = 0;
		for(int i=0; i<numeroEsami; i++) {
			if (esami[i].getVoto() > votoMassimo) {
				votoMassimo = esami[i].getVoto();
			}
		}
		return votoMassimo;
	}
	
	/*
	 * Il metodo per il calcolo della media non è presente nella classe STUDENTE
	 * Ciascuna classe che deriva la classe STUDENTE dovrà implementarlo,
	 * altrimenti non sarà possibile istanziarla
	 * In sostanza: ogni classe figlia deve possedere un proprio metodo per il calcolo della media
	 */
	public abstract double calcolaMedia();

	
}
