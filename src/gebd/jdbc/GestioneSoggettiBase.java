package gebd.jdbc;

import java.util.HashMap;
import java.sql.*;

/*
 * La classe GESTIONE SOGGETTI mantiene in memoria e consente di interrogare/modificare
 * le informazioni anagrafiche di un insieme di soggetti inizialmente presenti su una base dati
 * In ogni istante le informazioni presenti in memoria saranno sincronizzate con quelle presenti nel Data Base (DB)
 */

public class GestioneSoggettiBase {
	
	/*
	 * Dizionario utilizzato per mantenere in memoria le corrispondenze tra codici identificativi e soggetti
	 */
	private HashMap<Integer, Soggetto> idToSoggetto;
	
	/*
	 * Salvare in una variabile membro il riferimento alla connessione alla base dati
	 * Una variabile privata poiché non deve essere possibile utilizzarla direttamente dall'esterno
	 */
	private Connection conn;
	
	/*
	 * Costruttore
	 * All'atto della creazione della classe, il costruttore stabilisce la connessione con il DB e scarica l'elenco dei soggetti
	 * Nel caso in cui non sia possibile stabilire la connessione, l'applicazione termina con un messaggio di errore esplicativo
	 */
	
	public GestioneSoggettiBase() {
		// Gestione eccezione legata ad APRI CONNESSIONE
		try {
		apriConnessione();
		} catch(Exception e) {
			System.out.println("Errore nella creazione della connessione");
			System.out.println("Motivo: " + e.getMessage());
			System.exit(-1);
			// Per convenzione scriviamo -1 in caso sia stata sollevata un'eccezione che ha condotto
			// all'interruzione della Virtual Machine
		}
		
		
		/*
		 * Gestione eccezione legata a CARICA SOGGETTI
		 * Nel caso in cui non sia possibile scaricare l'elenco dei soggetti:
		 * riprovare sino ad un numero massimo di tentativi
		 * Ad ogni tentativo, nel caso vada storto, si incremente di una unità la variabile RETRY
		 */
		boolean done = false;
		int retry = 0;
		
		while (!done) {
		try {
		caricaSoggetti();
		done = true;
		} catch (Exception e) {
			System.out.println("Errore nel caricamento dei soggetti");
			System.out.println("Motivo: " + e.getMessage());
			if (++retry == 3) {
				System.exit(-1);
			}
		}
		}
		
	}
	
	/*
	 * Metodi connessione
	 * 1) Aprire la connessione con la base dati
	 * Nel caso la connessione fallisca viene generata una Exception generica 
	 * da gestire a livello superiore (metodo gestioneStudenti)
	 */
	private void apriConnessione() throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://90.147.166.209:3306/db170?" +
				"user=utente170&password=d1r0mp3nt3");
	}
	
	
	/*
	 * 2) Chiudere la connessione con la base dati
	 * Il metodo gestisce internamente le proprie eccezioni
	 * Una eccezione legata a tale metodo si palesa nel momento in cui la connessione 
	 * sia stata interrotta brutalmente in un momento precedente alla chiamata del metodo medesimo
	 */
	public void chiudiConnessione() {
		try {
		conn.close();
		} catch(Exception e) {
			System.out.println("Errore nella chiusura della connessione");
			System.out.println("Motivo " + e.getMessage());
		}
	}
	
	
	/*
	 * Metodo CARICA SOGGETTI
	 * Caricare in memoria il contenuto della tabella anagrafica
	 * Inizialmente si crea un nuovo arraylist vuoto da assegnare alla variabile soggetti
	 * Dopodiché si esegue la query di selezione per recuperare ID, nome, cognome ed anno di nascita di ciascun soggetto
	 * questi saranno utilizzati per creare nuovo oggetti di tipo soggetto da salvare nell'arraylist SOGGETTI
	 * Nel caso fallisca viene generata una Exception generica da gestire a livello superiore (metodo gestioneStudenti)
	 */
	private void caricaSoggetti() throws Exception{
		idToSoggetto = new HashMap<Integer, Soggetto>();
		
		String sql = " select ID, Nome, Cognome, ANNONASCITA FROM Anagrafe";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			int id = rs.getInt("ID");
			String nome = rs.getString("Nome");
			String cognome = rs.getString("Cognome");
			String annoNascita = rs.getString("ANNONASCITA");
			Soggetto s = new Soggetto(id, nome, cognome, annoNascita);
			
			idToSoggetto.put(id, s);
		}
		System.out.println("Numero di record caricati: " + idToSoggetto.size());
		}
	
	// Operazioni da supportare a livello macro
	
	/*
	 * Eliminare dalla memoria e dal database il soggetto indicato da input
	 * Questo metodo potrebbe fallire per diversi motivi
	 * - connessione interrotta con il DB (SQL Exception)
	 * - problema di permessi con il DB (SQL Exception)
	 */
	public void eliminaSoggetto (int idSoggetto) {
		/*
		 * Controllo se il soggetto di cui è stata richiesta l'eliminazione esiste, 
		 * in caso negativo non si fa nulla, altrimenti si procede
		 */
		Soggetto s = idToSoggetto.get(idSoggetto);
		if (s == null) {
			return;
		}
		
		// Eliminare soggetto dal DB
		String sql = "DELETE FROM Anagrafe WHERE ID = " + idSoggetto;
		
		try {
		Statement st = conn.createStatement();
		st.executeUpdate(sql);
		} catch(SQLException e) {
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		
		// Eliminare soggetto dalla memoria richimandolo mediante chiave del dizionario
		idToSoggetto.remove(idSoggetto);
	}
	
	public void inserisciSoggetto (String nome, String cognome, String annoNascita) {
		int id;
		
		// Inserire il soggetto nel DB       
		String sql = "INSERT INTO Anagrafe(Nome, Cognome, ANNONASCITA) VALUES('" + nome +"','"+cognome+"','"+annoNascita+"')";
		try {
		Statement st = conn.createStatement();
		st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		// ID attribuito in automatico da MySQL: da recuperare 
		ResultSet rs = st.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		} catch(SQLException e) {
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		
		/*
		 * In questo particolare caso occorre sapere con quale codice identificativo è stato marcato il 
		 * record appena inserito
		 * Sarebbe possibile risolvere il problema mediante una query di selezione, 
		 * tale soluzione non è tuttavia efficiente
		 * Il driver JDBC prevede una particolare modalità di funzionamento per cui tale informazione viene
		 * automaticamente prelevata e restituita all'applicazione client
		 * Per attivare tale modalità è necessario passare il parametro STATEMENT.RETURN_GENERATED_KEYS al metodo EXECUTEUPDATE
		 * Una volta eseguito l'inserimento, lo statement utilizzato per la query conterrà al suo interno il 
		 * valore attribuito alla chiave appena inserita, per accedere a questo valore sarà necessario utilizzare il metodo GETGENERATEDKEYS,
		 * questi restituirà un resultset con la collezione di chiavi associate ai record inseriti, nel caso di tale metodo uno solo
		 * - mediante RS.NEXT è possibile spostarsi sul primo di tali record 
		 * - mediante RS.GETINT(1) è possibile leggerne il contenuto 
		 */
	
		
		// Inserire in memoria
		Soggetto s = new Soggetto(id, nome, cognome, annoNascita);
		idToSoggetto.put(id, s);
		
		// Stringa conclusiva 
		System.out.println("Aggiungo un nuovo soggetto con ID: " + id);
	}
	
	public void modificaNomeSoggetto  (int idSoggetto, String nuovoNome) {
		Soggetto s = idToSoggetto.get(idSoggetto);
		if (s == null) {
			return;
		}
		
		// Modificare il nome del soggetto sul DB    
		String sql = "UPDATE Anagrafe SET Nome='" + nuovoNome + "' WHERE ID=" + idSoggetto;
		
		try {
		Statement st = conn.createStatement();
		st.executeUpdate(sql);
		} catch(SQLException e) {
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		
		// Modificare il nome del soggetto in memoria
		s.setNome(nuovoNome);
	}
	
	// Metodo Main
	public static void main(String[] args) {
		// GestioneSoggettiBase gs = new GestioneSoggettiBase();
		
		// Controllare i risultati su MySQL WorkBench
		// Prima esecuzione, segnarsi il codice
		// gs.inserisciSoggetto("Bob","Williams" , "1897");
				
		// Seconda esecuzione modifiche ed eliminazione
		// gs.modificaNomeSoggetto(1326, "Antonio");
		// gs.eliminaSoggetto(1326);
	}
	
	
}
