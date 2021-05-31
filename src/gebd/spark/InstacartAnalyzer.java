package gebd.spark;

import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import scala.Tuple2;

public class InstacartAnalyzer {

	public static void main(String[] args) {
		
		// Canonico preambolo 
		Logger.getLogger("org").setLevel(Level.ERROR);
		Logger.getLogger("akka").setLevel(Level.ERROR);
		SparkConf sc = new SparkConf();
		sc.setAppName("Instacart Analyzer");
		sc.setMaster("local[*]");
		JavaSparkContext jsc = new JavaSparkContext(sc);
		
		// Caricamento del file 
		JavaRDD<String> dOrders1 = jsc.textFile("data/orders.csv");
		System.out.println(dOrders1.take(5));
		
		// Scartare l'intestazione 
		JavaRDD<String> dOrders2 = dOrders1.filter(x -> !x.contains("cart_id,product_id,order,moved"));
		System.out.println(dOrders2.take(5));
		
		// Splittare le stringhe e scartare le colonne (order/moved) non desiderate 
		
		// Si ricorda la notazione valida per operare sulle stringhe 
		String xx = "1,49302,1,1";
		String cart_id = xx.split(",")[0];
		String product_id = xx.split(",")[1];
		System.out.println("IDCart: " + cart_id + ", IDProduct: " + product_id);
		
		// Di ogni riga si estrae il product ID e se ne accosta la frequenza 1
		JavaPairRDD<String, Integer> dProducts1 = dOrders2.
				mapToPair(x -> new Tuple2<String, Integer>(x.split(",")[1], 1));
		System.out.println(dProducts1.take(5));
		
		// Frequenza cumulate di acquisto dei prodotti per ID
		JavaPairRDD<String, Integer> dProducts2 = dProducts1.reduceByKey((x,y) -> x+y);
		System.out.println(dProducts2.take(10));
		
		/*
		 * Individuare il prodotto con la frequenza massima 
		 * Il metodo MAX richiede un COMPARATOR, un oggetto che stabilisca il metodo per confrontare 
		 * due tuple 
		 */ 
		System.out.println("Prodotto più venduto: " + dProducts2.max(new ProductComparator()));
		
		
		/*
		 * DETERMINARE LA COPPIA DI PRODOTTI PIU' SPESSO ACQUISTATI INSIEME 
		 * Una coppia di prodotti è acquistata assieme se compare nello stesso carrello (ordine)
		 * Si richiede una rappresentazione centrata sui carrelli 
		 * - Da dOrders2 (file di testo senza intestazione) costruisco una JavaPairRDD 
		 *   con in chiave l'orderID ed in valore il productID, tutte le 
		 *   altre informazioni sono da scartare --> trasformazione MAP TO PAIR 
		 * - Si desidera associare a stessa chiave tutti i prodotti che compongono medesimo carrello (K)
		 *   mediante trasformazione REDUCE 
		 * - Elimino l'informazione sulla chiave (inutile) mediante VALUES
		 * - Contare le volte che una certa coppia di prodotti viene acquistata assieme, devo annotare  
		 *   tutte le coppie di elementi distinti che si trovano nei carrelli, quindi attribuire frequenza 1:
		 *   trasformazione FLAT MAP TO PAIR (da ogni stringa in input ne sorgono genericamente n in output)
		 * - Aggregare e contare tutte le coppie uguali --> Reduce By Key 
		 * - Determinare il massimo 
		 */
		
		System.out.println("Punto di partenza: " + dOrders2.take(3));
		JavaPairRDD<String, String> dOrders3 = 
				dOrders2.mapToPair(x -> new Tuple2<String, String>(x.split(",")[0], x.split(",")[1]));
		System.out.println(dOrders3.take(10));
		
		
		JavaPairRDD<String, String> dOrders4 = dOrders3.reduceByKey((x,y) -> x+","+y);
		// x ed y sono due records associati a medesima chiave + concatenamento di stringhe 
		System.out.println(dOrders4.take(10));
		
		JavaRDD<String> dOrders5 = dOrders4.values();
		System.out.println(dOrders5.take(10));
		
		JavaPairRDD<String, Integer> dOrders6 = dOrders5.flatMapToPair(new EstraiCoppie());
		System.out.println(dOrders6.take(10));
		
		JavaPairRDD<String, Integer> dOrders7 = dOrders6.reduceByKey((x,y) -> x+y);
		System.out.println(dOrders7.take(10));
		
		System.out.println("Coppia di prodotti più spesso acquistati assieme: " + 
				dOrders7.max(new ProductComparator()));
		

		/*
		 * DETERMINARE PER OGNI PRODOTTO L'ALTRO PRODOTTO PIU' SPESSO ACQUISTATO INSIEME  
		 * - Per ogni coppia di prodotti è necessario produrre in output due coppie:
		 *   da (K[prodotto1, prodotto 2], V[frequenza]) --> punto di partenza: dOrders7
		 *   a (K[prodotto 1], V[prodotto 2, frequenza]) (K[prodotto 2], V[prodotto 1, frequenza])
		 *   Trasformazione FlatMapToPair
		 * - Assieme ad ogni prodotto, chiave, associare tutti i prodotti con esso comprati e le frequenze 
		 * 	 Trasformazione Reduce By Key 
		 * - Per ogni prodotto, scorrere le liste di valori [prodotto, frequenza] e trovare la frequenza più alta
		 *   L'output deve presentare K prodotto e V (prodotto consigliato, frequenza di acquisto) 
		 *   Trasformazione Map to Pair --> da ogni coppia KV in input si restituisce una coppia KV in output
		 */
		
		JavaPairRDD<String, String> dOrders8 = dOrders7.
				flatMapToPair(new DisaccoppiaProdotti());
		System.out.println(dOrders8.take(10));
		
		JavaPairRDD<String, String> dOrders9 = dOrders8.
				reduceByKey((x,y) -> x+","+y);
		System.out.println(dOrders9.take(10));
		
		JavaPairRDD<String, String> dOrders10 = dOrders9.
				mapToPair(new TrovaMassimo());
		System.out.println(dOrders10.take(10));
		
		/*
		 * Applicazione in pausa per la fase di profiling
		 */
		Scanner scan;
		System.out.println("Premi invio per concludere l'esecuzione");
		scan = new Scanner(System.in);
		scan.next();
		
	
		// CREAZIONE ARCHI 'RACCOMANDAZIONE' NEL DATABASE NEO4J
		
		/*
		String uri = "bolt://localhost:7687";
		AuthToken token = AuthTokens.basic("neo4j", "password");
		
		Driver driver = GraphDatabase.driver(uri, token);
		
		Session s = driver.session();
		System.out.println("Connessione stabilita");
		
		// Prendo i due prodotti per la raccomandazione 
		List<Tuple2<String, String>> recommends = dOrders10.collect();
		int count = 0;
		for(Tuple2<String, String> r: recommends) {
			String from = r._1;
			String to = r._2.split(",")[0];  // Non considero la frequenza 
			String cql = "match (n:Product {pid:'"+from+"'}),(m:Product {pid:'"+to+"'}) create (n)-[:RECOMMENDS]->(m)";
			s.run(cql);
			System.out.println(cql);
			count++;
		}
		
		s.close();
		System.out.println("Aggiunti " + count + " archi di tipo RACCOMANDAZIONE");
		*/
		
	}

}
