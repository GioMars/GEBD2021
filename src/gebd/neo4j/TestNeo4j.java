package gebd.neo4j;

import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

public class TestNeo4j {

	public static void main(String[] args) {
		/*
		 * La procedura da utilizzare per collegarsi a Neo4j da Java ricorda
		 * quelle già vista per JDBC (a livello concettuale).
		 * - si istanzia il driver
		 * - si crea una connessione
		 * - si crea una sessione
		 * - si definisce il contenuto di una query e la si esegue
		 * - si interpreta il risultato 
		 * - si chiude tutto
		 */
		
		/* 
		 * PRIMO PASSO 
		 * Si definisce la stringa di connessione a Neo4j, si utilizza il protocollo BOLT per collegarsi 
		 * nel nostro stesso computer sulla porta standard 7686
		 * Si forniscono poi le credenziali di accesso 
		 */
		String uri = "bolt://localhost:7687";
		AuthToken token = AuthTokens.basic("neo4j", "castellitto987");
		
		/*
		 * Si istanzia il driver e si crea la prima connessione
		 */
		Driver driver = GraphDatabase.driver(uri, token);
		
		/*
		 * Si apre una sessione di lavoro
		 */
		Session s = driver.session();
		System.out.println("Connessione stabilita");
		
		/*
		 *  FORMULAZIONE DI UNA QUERY 
		 *  Si desidera conoscere nome e cognome di alcuni studenti presenti nel database SEGRETERIA STUDENTI 
		 *  Si ricorda che le interrogazioni Java, non supportando la rapprentazione grafica di Neo4j, richiedono query 
		 *  aventi risposta in forma tabellare
		 */
		String query = "match(n:studente) return n.nome as nome, n.cognome as cognome limit 20";
		
		// Si ottiene un oggetto di tipo RESULT sfruttando la connessione
		Result result = s.run(query);
		
		/*
		 * Per poter consumare il contenuto di RESULT bisogna scorrerlo una riga alla volta 
		 * Gli oggetti contenenti i valori estrapolati dalla query sono amorfi,
		 * bisogna attribuirne un tipo e convertirli al volo utilizzando medodi della forma .asX (dove X è il tipo desiderato)
		 * Si presti attenzione alla sintassi utilizzata per prelevare gli attributi di interesse da ogni record 
		 */
		while(result.hasNext()) {
			Record r = result.next();
			String nome = r.get("nome").asString();
			String cognome = r.get("cognome").asString();
			System.out.println("Nome: " + nome + ", Cognome: " + cognome);
		}
		
		System.out.println("\n");
		
		/*
		 * FORMULAZIONE PARAMETRIZZATA DI UNA QUERY 
		 * Query che restituisce nome e voti degli esami sostenuti da uno studente (di cui se ne fornisce la matricola mediante parametro)
		 * La parametrizzazione è del tipo $matricola 
		 */
		query = "match(n:studente)-[e:esame]-(i:insegnamento) where n.matricola = $matricola return i.nome as nome, e.voto as voto";
		
		// Salvare uno studente (la matricola è definita come stringa nel database)
		String matricola = "1653873024";
		
		/*
		 * Binding chiave-valore
		 * Quando nella query si incotra la stringa 'matricola', essa è rimpiazzata dal segnaposto con il valore presente nella variabile 
		 */
		result = s.run(query, Values.parameters("matricola", matricola));
		System.out.println("Esami dello studente matricola " + matricola);
		while(result.hasNext()) {
			Record r = result.next();
			System.out.println("Esame: " + r.get("nome").asString());
			System.out.println("Voto: " + r.get("voto").asString());
		}
		
		s.close();

	}

}
