package gebd.jdbc;


/*
 * La filosofia portata avanti è quella per la quale i dati sono mantenuti replicati in memoria e su DB
 * - in memoria vista l'efficienza
 * - sul DB per motivi di persistenza
 * Quando si eseguono alcune operazioni non è necessario eseguirle sia in memoria che sul DB, ma è possibile
 * scegliere l'opzione più veloce 
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.sql.*;

/*
 * Nell'impostazione tradizionale di utilizzo della libreria JDBC le query 
 * sono assemblate ed eseguite laddove ve ne è bisogno
 * Ogni qualvolta il DBMS riceve la richiesta di esecuzione di una query
 * avvia una serie di controlli per assicurarsi che la query sia corretta, che chi ne chiede l'esecuzione
 * ne sia autorizzato, nonché per determinare il piano di esecuzione più efficiente
 * Tali controlli si ripetono identici se il client esegue ripetutamente sempre la stessa query
 * 
 * Numerosi DBMS relazionali (ed anche la libreria JDBC) prevedono la possibilità 
 * di definire delle query preparate
 * Si tratta di query predigerite che vengono sottoposte al DMBS una volta soltanto per la verifica della loro 
 * correttezza e per l'elaborazione del piano di esecuzione
 * Una volta che sono state installate presso il DBMS, l'applicazione client potrà richiamarle
 * quante volte vuole 
 * 
 * Le query non sono più definite alla bisogna ma vengono comunicate in anticipo al DBMS 
 * alla partenza dell'applicazione, inserendo all'occorrenza la parametrizzazione di specifici inserimenti
 * 
 * Per ogni tipo di query che si ha bisogno di eseguire si va a creare un PREPARED STATEMENT 
 * Tali prepared statements vengono trasmessi al DBMS a partire dal costruttore e possono essere utilizzati alla bisogna
 */

public class GestioneSoggetti {
	/*
	 * Si introduce una serie di variabili membro nella classe in modo da fissare permanentemente
	 * il testo delle query SQL che voglio eseguire
	 */
	private final String sqlCaricaSoggetti =  " SELECT ID, NOME, COGNOME, ANNONASCITA FROM Anagrafe";
	
	/*
	 * Quando si lavora con prepared statements è possibile definire delle query parametrizzate:
	 * alcuni inserimenti non sono definiti ma occupati da dei segnaposto che saranno rimpiazzati 
	 * al momento dell'esecuzione con dei valori concreti
	 */
	private final String sqlEliminaSoggetto = "DELETE FROM Anagrafe WHERE ID=?";
	private final String sqlEliminaSoggettiCognome = "DELETE FROM Anagrafe WHERE Cognome=?";
	private final String sqlEliminaSoggettiAnno = "DELETE FROM Anagrafe WHERE ANNONASCITA=?";            
	// La libreria gestisce autonomamente il tipo di parametro 
	private final String sqlModificaNomeSoggetto =  "UPDATE Anagrafe SET Nome=? WHERE ID=?";
	private final String sqlInserisciSoggetto =  "INSERT INTO Anagrafe(Nome,Cognome,ANNONASCITA) VALUES(?,?,?)";
	
	// Variabili PreparedStatement
	private PreparedStatement psCaricaSoggetti;
	private PreparedStatement psEliminaSoggetto;
	private PreparedStatement psEliminaSoggettiCognome;
	private PreparedStatement psEliminaSoggettiAnno;
	private PreparedStatement psModificaNomeSoggetto;
	private PreparedStatement psInserisciSoggetto;

	
	// Dizionario utilizzato per mantenere in memoria le corrispondenze tra codici identificativi e soggetti
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
	private void preparaQuery () throws Exception{
		
		// Gestione eccezione delegata al livello superiore
		psCaricaSoggetti = conn.prepareStatement(sqlCaricaSoggetti);
		psEliminaSoggetto = conn.prepareStatement(sqlEliminaSoggetto);
		psEliminaSoggettiCognome = conn.prepareStatement(sqlEliminaSoggettiCognome);
		psEliminaSoggettiAnno = conn.prepareStatement(sqlEliminaSoggettiAnno);
		psModificaNomeSoggetto = conn.prepareStatement(sqlModificaNomeSoggetto);
		
		/*
		 * Dal momento che occorre conoscere il codice identificativo attribuito ad ogni record aggiunto, è necessario
		 * prevedere già da ora l'opzione per conoscerlo
		 */
		psInserisciSoggetto = conn.prepareStatement(sqlInserisciSoggetto, PreparedStatement.RETURN_GENERATED_KEYS);
	}
	
	public GestioneSoggetti() {
		// Gestione eccezione legata ad APRI CONNESSIONE e a PREPARA QUERY
		// Eccezioni sollevate da tali due metodi portano all'interruzione dell'applicazione
		try {
		apriConnessione();
		preparaQuery();
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
		System.out.println("La connessione è stata aperta");
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
		System.out.println("La connessione è stata chiusa");
		} catch(Exception e) {
			System.out.println("Errore nella chiusura della connessione");
			System.out.println("Motivo " + e.getMessage());
		}
	}
	
	
	/*
	 * Metodo CARICA SOGGETTI
	 * Caricare in memoria il contenuto della tabella anagrafica
	 * Inizialmente si crea un nuovo arraylist vuoto da assegnare alla variabile soggetti
	 * Dopodiché si esegue la query di selezione per recuperare ID, nome e cognome di ciascun soggetto
	 * questi saranno utilizzati per creare nuovo oggetti di tipo soggetto da salvare nell'arraylist SOGGETTI
	 * Nel caso fallisca viene generata una Exception generica da gestire a livello superiore (metodo gestioneStudenti)
	 */
	private void caricaSoggetti() throws Exception{
		idToSoggetto = new HashMap<Integer, Soggetto>();
		
		ResultSet rs = psCaricaSoggetti.executeQuery();
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
	 * connessione interrotta con il DB (SQL Exception)
	 * problema di permessi con il DB (SQL exception)
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
		
		try {
		/*
		 * Se il prepared statement da eseguire prevede al presenza di parametri, questi 
		 * dovranno essere esplicitati
		 * Lo si fa utilizzando i metodi set ed indicando la posizione del parametro da definire (partendo da 1)
		 */
		psEliminaSoggetto.setInt(1, idSoggetto); 
		psEliminaSoggetto.executeUpdate();
		} catch(SQLException e) {
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		
		// Eliminare soggetto dalla memoria
		idToSoggetto.remove(idSoggetto);
	}
	
	/*
	 * Metodo ELIMINA SOGGETTI COGNOME
	 * Elimina tutti i soggetti il cui cognome coincide con una stringa indicata da input
	 * Il metodo opera sia in memoria che sul DB
	 */
	public void eliminaSoggettiCognome (String cognome) {
		Set<Integer> set = new HashSet<> ();
		for(Integer key : idToSoggetto.keySet()){
			Soggetto s = idToSoggetto.get(key);
			if (s.getCognome() != null && s.getCognome().equals(cognome)) {
				set.add(key);
			} else {
				continue;
			}
		}
	
		if(set.size()==0) {
			System.out.println("Non vi sono soggetti con cognome: " + cognome);
			return;
		} 
		
		// Eliminare soggetti dal DB
		try {
		psEliminaSoggettiCognome.setString(1, cognome); 
		psEliminaSoggettiCognome.executeUpdate();
		} catch(SQLException e) {
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		
		// Eliminare soggetti dalla memoria
		idToSoggetto.keySet().removeAll(set);
	}
	
	/*
	 * Metodo ELIMINA SOGGETTI ANNO
	 * Il metodo elimina tutti i soggetti nati un certo anno
	 * Il metodo agisce tanto in memoria quanto sul DB
	 */
	public void eliminaSoggettiAnno(String annoNascita) {
		Set<Integer> set = new HashSet<> ();
		for(Integer key : idToSoggetto.keySet()) {
			Soggetto s = idToSoggetto.get(key);
			if (s.getAnnoNascita() != null && s.getAnnoNascita().equals(annoNascita)) {
				set.add(key);
			} else {
				continue;
			}
		}
		
		if(set.size()==0) {
			System.out.println("Non vi sono soggetti nati nell'anno: " + annoNascita);
			return;
		} 
			
		// Eliminare soggetti dal DB
		
		try {
		psEliminaSoggettiAnno.setString(1, annoNascita); 
		psEliminaSoggettiAnno.executeUpdate();
		} catch(SQLException e) {
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		
		// Eliminare soggetti dalla memoria
		idToSoggetto.keySet().removeAll(set);
		
	}
	
	public void inserisciSoggetto (String nome, String cognome, String annoNascita) {
		int id;
		
		// Inserire il soggetto nel DB       
		try {
		psInserisciSoggetto.setString(1,nome);
		psInserisciSoggetto.setString(2,cognome);
		psInserisciSoggetto.setString(3,annoNascita);
		psInserisciSoggetto.executeUpdate();
		// ID attribuito in automatico da MySQL: da recuperare 
		// Le generated keys ora sono nel prepared statement
		ResultSet rs = psInserisciSoggetto.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		} catch(SQLException e) {
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		
		// Inserire in memoria
		Soggetto s = new Soggetto(id, nome, cognome, annoNascita);
		idToSoggetto.put(id, s);
		
		System.out.println("Aggiunta di un nuovo soggetto con ID: " + id);
	}
	
	public void modificaNomeSoggetto  (int idSoggetto, String nuovoNome) {
		Soggetto s = idToSoggetto.get(idSoggetto);
		if (s == null) {
			return;
		}
		
		// Modificare il nome del soggetto sul DB    
		
		
		try {
		psModificaNomeSoggetto.setString(1, nuovoNome);
		psModificaNomeSoggetto.setInt(2, idSoggetto);
		psModificaNomeSoggetto.executeUpdate();
		} catch(SQLException e) {
			System.out.println("Errore: " + e.getMessage());
			return;
		}
		
		// Modificare il nome del soggetto in memoria
		s.setNome(nuovoNome);
	}
	
	/*
	 * METODO MOSTRA SOGGETTI
	 * Mostra a schermo tutti i soggetti il cui cognome coincide con una stringa indicata da input 
	 * Il metodo lavora solo in memoria per questioni di efficienza 
	 */
	public void mostraSoggetti (String cognome) {
		int contatore = 0;
		
		for(Integer key : idToSoggetto.keySet()) {
				Soggetto s = idToSoggetto.get(key);
				if(s.getCognome() != null && s.getCognome().equals(cognome)){
				System.out.println("ID: " + s.getId() + ", Nome: " + s.getNome() + ", Cognome: " + s.getCognome() + 
				", Anno di nascita: " + s.getAnnoNascita());
				contatore++ ;
				} else {
					continue;
				}
		}
		
		// Se non vi sono soggetti con quel cognome
		if(contatore == 0) {
			System.out.println("Non sono stati trovati soggetti con cognome: " + cognome);
		}
	}
	
	// Metodo Main
	public static void main(String[] args) {
		GestioneSoggetti gs = new GestioneSoggetti();
		
		// Prima esecuzione, segnarsi il codice
		// gs.inserisciSoggetto("Bob","Williams" , "1897");
		// gs.mostraSoggetti("Williams");
		
		// Seconda esecuzione modifiche ed eliminazione
		// System.out.println("Modifica del nome");
		// gs.modificaNomeSoggetto(1309, "Antonio");
		// gs.mostraSoggetti("Williams");
		// System.out.println("Eliminazione");
		// gs.eliminaSoggetto(1322);
		// gs.mostraSoggetti("Williams");
				
		System.out.println("\n");
		System.out.println("Albino Crespi: inserimento e rimozione");
		gs.inserisciSoggetto("Albino","Crespi","1930");
		gs.inserisciSoggetto("Mariella","Crespi","1928");
		gs.mostraSoggetti("Crespi");
		System.out.println("Rimozione");
		gs.eliminaSoggettiCognome("Crespi");
		gs.mostraSoggetti("Crespi");
		
		System.out.println("\n");
		System.out.println("Inserimento e rimozione di due nati nel 1756");
		gs.inserisciSoggetto("Piero", "Persico", "1756");
		gs.inserisciSoggetto("Marc", "Sardelli", "1756");
		gs.mostraSoggetti("Persico");
		gs.mostraSoggetti("Sardelli");
		System.out.println("Rimozione");
		gs.eliminaSoggettiAnno("1756");
		gs.mostraSoggetti("Persico");
		gs.mostraSoggetti("Sardelli");
		
		System.out.println("\n");
		System.out.println("Tentativo di eliminazione dei soggetti nati nel 1838");
		gs.eliminaSoggettiAnno("1838");
	
		System.out.println("\n");
		gs.chiudiConnessione();
	}
	
	
}
