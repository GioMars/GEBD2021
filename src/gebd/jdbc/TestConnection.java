package gebd.jdbc;
import java.sql.*;

public class TestConnection  {
	
	/*
	 * Classe per introdurre le istruzioni necessarie ad interagire con una base
	 * dati MySQL
	 * 
	 * E' necessario aggiungere al progetto Java una libreria apposita
	 * Le librerie di Java sono file ZIP che hanno estensione.JAR
	 * Libreria -> contenitore al cui interno vi sono file compilati .class
	 * JRE System Library è un puntatore alla libreria preinstallata in Java
	 * Aggiungere al progetto il file .JAR contenente le classi necessarie per il dialogo con MySQL
	 * 
	 * Progetto -> tasto destro -> Configure -> Convert to Maven Project
	 * Maven è la tecnologia utilizzata nel mondo java per descrivere l’articolazione di un certo progetto (complesso)
	 * 
	 * Inserire 
	 * GROUP ID stringa per contrassegnare tutti i progetti che un gruppo realizza 
	 * ARTIFACT ID stringa per contrassegnare il progetto
	 * 
	 * Java introduce uno standard universale per l'interazione con basi dati relazionali 
	 * utilizzando il linguaggio SQL, tale standard prende il nome di JDBC (Java Database Connectivity)
	 * Tutti i database relazionali si assomigliano tra loro, pur non implementando esattamente 
	 * la stessa versione di un linguaggio, attraverso l'uso di JDBC è possibile interfacciarsi da java con qualsiasi 
	 * database relazionale allo stesso modo 
	 * In questo senso, JDBC implementa una architettura stratificata in cui è presente uno strato superiore
	 * identico per tutti i database ed utilizzabile per interagire con loro (agli occhi del programmatore)
	 * ed uno strato inferiore che cambia da database a database 
	 * In tale classe si carica il modulo necessario per interagire con il driver MySQL
	 * 
	 * Le istruzioni qui di seguito e la sequenza di operazioni necessarie per interagire con una base dati 
	 * ricordano quelle che un operatore umano eseguirebbe (utilizzando per esempio MySQL Workbench)
	 */
	
	/*
	 * Gestione rapida delle eccezioni THROWS EXCEPTION sul metodo Main
	 * Le potenziali eccezioni non vengono gestite, una volta sollevate portano all'interruzione 
	 * dell'esecuzione del metodo
	 */
	public static void main(String[] args) throws Exception{
		/*
		 * Per esigenze di semplicità il programma è implementato interamente all'interno del metodo MAIN
		 * Sequenza delle operazioni
		 * 1. Apertura del programma
		 * 2. Stabilire la connessione
		 * 3. Aprire finestra per le query 
		 * 4. Formulare la query SQL
		 * 5. Eseguire una query SQL
		 * 6. Attendere il risultato e processarlo 
		 * 7. Chiudere tutto 
		 */
		
		/*
		 * 1. Apertura del programma
		 * Per poter stabilire una connessione con MySQL occorre caricare in memoria il driver specializzato 
		 * per interagire con tale tipo di base dati 
		 * La classe che fornisce accesso a tale driver non è necessariamente già presente in memoria e risulta
		 * possibile richiamarla utilizzando la seguente istruzione
		 */
		
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		// Cercare tra le librerie una classe con nome richiesto e crearne una nuova istanza
		
		/*
		 * 2. Stabilire la connessione
		 * Creare una nuova connessione verso un dato DBMS indicandone la posizione nella "stringa di connessione",
		 * indicare inoltre le credenziali con cui collegarsi (username e password)
		 * ed il nome specifico del database che si intende utilizzare 
		 * 
		 * DriverManager è una classe standard di JDBC dove è presente il metodo getConnection;
		 * il database di interesse è MYSQL, tale database gira sull'indirizzo internet riportato e sulla porta 3306,
		 * il database selezionato ha nome db170
		 * username utente170
		 * password d1r0mp3nt3
		 * una variabile CONNECTION conserva un riferimento alla connessione, se essa ha successo, 
		 * sotto forma di un oggetto di tipo CONNECTION
		 * 
		 * DRIVERMANAGER e CONNECTION sono oggetti di tipo sconosciuto se non si importa nella classe il pacchetto JDBC
		 * mediante il comando 
		 * import java.sql.*;
		 */
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://90.147.166.209:3306/db170?" +
				"user=utente170&password=d1r0mp3nt3");
		
		/*
		 * 3. Aprire una finestra per le query
		 * E' possibile eseguire delle query SQL in JDBC attraverso degli statements,
		 * ciascuno statement può essere utilizzato per eseguire una query alla volta
		 * Gli statements si creano a partire dalle connessioni attraverso il metodo CREATE STATEMENT 
		 * ricevendo come risultato un oggetto di tipo STATEMENT
		 */
		
		
		System.out.println("Connessione stabilita");
		// Dopo ogni metodo è possibile siano sollevate delle eccezioni,
		// in caso di eccezione la scritta non viene stampata poiché l'esecuzione si interrompe prima, 
		// viceversa la scritta compare
		
		Statement st = conn.createStatement();
		
		/*
		 * 4. Formulare la query SQL
		 * Utilizzare gli operatori di Java e le stringhe per assemblare una stringa 
		 */
		
		int meseNascita = 1;
		String sql = "SELECT COGNOME, NOME, GIORNONASCITA, MESENASCITA, ANNONASCITA FROM Anagrafe WHERE MESENASCITA = " + meseNascita
				+ " ORDER BY COGNOME";
		
		// Stampare a schermo la QUERY per controllo 
		System.out.println("sql: " + sql + "\n");
		
		/*
		 * 5. Eseguire la query SQL
		 * Utilizzare lo statement precedentemente creato per richiedere l'esecuzione di 
		 * una certa query SQL
		 * Le operazioni di interrogazione SELECT si eseguono attraverso il metodo executeQuery e restituiscono un RESULTSET
		 * che incapsula l'insieme dei record corrispondenti alla query di input
		 * Tutte le altre operazioni che hanno come effetto quello di modificare struttura o contenuto di una base dati
		 * sono eseguibili attraverso il metodo executeUpdate e resituiscono tipicamente il numero di record interessati dalla 
		 * modifica (se ha senso tale restituzione)
		 */
		
		ResultSet rs = st.executeQuery(sql);
		
		/*
		 * 6. Attendere il risultato e processarlo 
		 *
		 * Per motivi di performance, quando si esegue una query di selezione su tabelle voluminose, JDBC non resituisce
		 * immediatamente l'intero recordset selezionato, ma solo una sua parte (100 records alla volta)
		 * Man mano che l'applicazione processa 100 records, attraverso un meccanismo automatico e trasparente vengono richiesti 
		 * al DBMS i successivi 100 records e così via sino a che non si esaurisce l'intero RESULTSET
		 * Ciò è mascherato nel meccanismo rs.next()
		 */
		
		while(rs.next()) {
			/*
			 * Tale ciclo WHILE fornisce ripetutamente accesso a records successivi, se presenti
			 * Il ciclo è ripetuto tante volte sino all'esaurimento dei records da consumare
			 * Metodi preimplementati in RESULTSET permettono la restituzione di oggetti di tipo desiderato
			 */
			
			System.out.println("Soggetto: " + rs.getString("COGNOME") + " " + rs.getString("NOME"));
			
			/*
			 * Nel fare il parsing degli argomenti è possibile utilizzare una sintassi posizionale (fornendo l'indice dell'attributo selezionato)
			 * oppure una sintassi nominale (fornendo il nome dell'attributo selezionato, come fatto sopra)
			 */
			System.out.println("Soggetto: " + rs.getString(1) + " " + rs.getString(2));
			System.out.println("Data nascita: " + rs.getInt("GIORNONASCITA") + "/" + rs.getInt("MESENASCITA") + "/" 
					+ rs.getInt("ANNONASCITA") + "\n"); 
		}
		
		/*
		 * Problemi con QUERY di maggiore complessità
		 * Update di un record esistente 
		 */
		int idSoggetto = 7;
		String nomeSoggetto = "Giulio";
		
		/*
		 * SQL richiede il nome tra apici
		 * Risoluzione concreta: inserimento di tre apici nella costruzione della stringa 
		 */
		sql = "update Anagrafe set NOME = '" + nomeSoggetto + "' WHERE ID = " + idSoggetto;
		
		// Stampa a schermo della query 
		System.out.println("Query: " + sql);
		int nRows = st.executeUpdate(sql);
		System.out.println("Records modificati: " + nRows);
		
		/*
		 * Inserimento di un nuovo record
		 * L'attributo ID è assegnato in automatico dal database, non va indicato manualmente
		 */
		String cognome = "Verdi";
		String nome = "Gianni";
		int giornoNascita = 2;
		 meseNascita = 1;
		int annoNascita = 1980;
		String sesso = "Maschio";
		
		/*
		 * Il meccanismo presentato è molto farragginoso ed aperto ad errori o problemi di sicurezza
		 * NON BISOGNEREBBE fornire all'utente la possibilità di formulare autonomamente la totalità delle query per 
		 * prevenire rischio di accesso a dati riservati
		 */
		sql = "INSERT INTO Anagrafe (COGNOME, NOME, GIORNONASCITA, MESENASCITA, ANNONASCITA, SESSO)"
				+ " VALUES ('" + cognome + "','" + nome + "'," + giornoNascita + "," + meseNascita + 
				"," + annoNascita + ",'" + sesso + "')";
		
		// Stampa a schermo della query
		System.out.println("Query: " + sql);
		nRows = st.executeUpdate(sql);
		System.out.println("Righe inserite: " + nRows);
		
		
		/*
		 * 7. Chiudere tutto 
		 * Chiudere la connessione una volta terminata la sessione di lavoro per liberare le risorse del DBMS
		 */
		
		conn.close();
		System.out.println("Connessione chiusa");

	}

}
