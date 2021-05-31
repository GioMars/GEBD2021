package graph.spark;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.graphframes.GraphFrame;

public class Main {
	
	public static void main(String[] args) {
		// Voglio in log solo gli errori
				Logger.getLogger("org").setLevel(Level.ERROR);
				Logger.getLogger("akka").setLevel(Level.ERROR);
				
		/*
		 * Modellare un database distribuito a grafo, mediante Spark, circa relazioni
		 * di AMICIZIA e FOLLOW tra diverse persone 
		 * - Creare una classe che rappresenti le persone in una rete di relazioni, 
		 *   la classe creata ha nome PERSONA (i vertici del grafo)
		 * - Creare una classe che rappresenti le relazioni (gli archi), 
		 *   la classe creata ha nome RELAZIONE 
		 *   
		 * Il grafo è un insieme di vertici ed archi, da costruire separatamente per poi 
		 * assemlare assieme 
		 */
		
		/*
		 * Primo passo alla costruzione del grafo 
		 * Prevedere una lista di vertici 
		 */
		ArrayList<Persona> persone = new ArrayList<>();
		persone.add(new Persona("a", "Alice", 34));
		persone.add(new Persona("b", "Bob", 36));
		persone.add(new Persona("c", "Charlie", 30));
		persone.add(new Persona("d", "David", 29));
		persone.add(new Persona("e", "Esther", 32));
		persone.add(new Persona("f", "Fanny", 36));
		
		// Istanziare la Spark Session
		SparkSession spark = SparkSession.builder().
				master("local[*]").
				appName("Test grafi").
				getOrCreate();
		
		// Settare una cartella di chekpointing (funzionale ad alcuni algoritmi operazionali sul grafo)
		spark.sparkContext().setCheckpointDir("checkpoint_grafi");
		
		
		// Creazione del dataframe di vertici
		Dataset<Row> vertici = spark.createDataFrame(persone, Persona.class);
		
		/*
		 * Passo secondo
		 * Prevedere una lista di archi
		 */
		ArrayList<Relazione> relazioni = new ArrayList<>();
		relazioni.add(new Relazione("a","e","friend"));
		relazioni.add(new Relazione("f","b","follow"));
		relazioni.add(new Relazione("c","e","friend"));
		relazioni.add(new Relazione("a","b","friend"));
		relazioni.add(new Relazione("b","c","follow"));
		relazioni.add(new Relazione("c","b","follow"));
		relazioni.add(new Relazione("f","c","follow"));
		relazioni.add(new Relazione("e","f","follow"));
		relazioni.add(new Relazione("e","d","friend"));
		relazioni.add(new Relazione("d","a","friend"));
		
		// Creazione del dataframe di relazioni
		Dataset<Row> archi = spark.createDataFrame(relazioni, Relazione.class);
		
		/*
		 * Parte terza
		 * Assemblare un grafo come coppie di vertici ed archi 
		 * La creazione del grafo richiede nel costruttore un dataset di vertici 
		 * ed un dataset di archi
		 */
		GraphFrame grafo = new GraphFrame(vertici, archi);
		
		/*
		 * Fissato un vertice v di un grafo semplice si dice valenza o grado di v 
		 * il numero dei lati aventi v come estremo
		 * Per conteggiare il grado di un vertice si utilizza l'algoritmo DEGREES
		 * Il metodo Degrees restituisce in output un DataFrame
		 */
		grafo.degrees().show();
		
		/*
		 * L'algoritmo CONNECTED COMPONENTS fornisce un attributo che indica l'ID 
		 * automatico della componente del grafo cui ogni vertice appartiene
		 * Se due vertici sono in qualche modo collegati da un arco o da
		 * un percorso in serie di archi, allora apparterranno alla medesima componente
		 * L'algoritmo connettedComponents, lanciato con un RUN, resituisce come output 
		 * finale un DataFrame
		 */
		grafo.connectedComponents().run().show();
		
		/*
		 * Filtraggio di archi e vertici mediante una query di stampo SQL
		 * Si desidera unicamente considerare nel grafo gli archi caratterizzati
		 * dall'attributo in modalità AMICIZIA
		 * Gli archi con attributo in modalità FOLLOW sono scartati 
		 * Il filtraggio sugli archi (così come quello sui vertici) restituisce un nuovo 
		 * GraphFrame
		 */
		GraphFrame amicizie = grafo.filterEdges("relationship = 'friend'");
		
		// Richiedo DataFrame di archi
		amicizie.edges().show();
		
		/*
		 * Ora f non è più una componente connessa, ho tolto l'arco di follow ed f non è amica di nessuno,
		 * f appartiene ad un'altra componente 
		 */
		amicizie.connectedComponents().run().show();
		
		/*
		 * Voglio solo vertici ed archi in cui ci sono relazioni 'bidirezionali',
		 * l'algoritmo FIND restituisce un DataFrame 
		 */
		grafo.find("(src)-[e]->(dst); (dst)-[e2]-> (src)").show();
		
		// https://graphframes.github.io/graphframes/docs/_site/user-guide.html
		
		/*
		 * Eseguire una ricerca da un punto A ad un punto B utilizzando il GraphFrame in maniera intuitiva
		 * mediante BFS (un algoritmo che ritorna un oggetto di tipo BFS, il passaggio di RUN permette la 
		 * restituzione come output finale di un DataFrame)
		 */
		grafo.bfs().maxPathLength(100).fromExpr("age > 30").toExpr("age < 31").run().show();
		
		/*
		 * Specificare in una lista di vertici, tali vertici sono singolrmente il punto di arrivo per la 
		 * determinazione dello shortest path a partire da tutti gli altri vertici del grafo 
		 */ 
		ArrayList<Object> id_landmarks = new ArrayList<>();
		id_landmarks.add("f");
		id_landmarks.add("c");
		grafo.shortestPaths().landmarks(id_landmarks).run().show();
		
		
		// Un nuovo grafo 
		
		// Lista di vertici
		ArrayList<Parente> parenti = new ArrayList<>();
		parenti.add(new Parente("giu", "Giuseppe", "M"));
		parenti.add(new Parente("a", "Anna", "F"));
		parenti.add(new Parente("l", "Luca", "M"));
		parenti.add(new Parente("gio", "Giovanni", "M"));
		parenti.add(new Parente("sa", "Sara", "F"));
		parenti.add(new Parente("si", "Simona", "F"));
		
		Dataset<Row> vertici2 = spark.createDataFrame(parenti, Parente.class);
		
		// Lista di archi
		ArrayList<Parentela> parentele = new ArrayList<>();
		parentele.add(new Parentela("a","giu","genitore di"));
		parentele.add(new Parentela("l","giu","genitore di"));
		parentele.add(new Parentela("gio","sa","genitore di"));
		parentele.add(new Parentela("si","gio","genitore di"));
		parentele.add(new Parentela("si","a","genitore di"));
		
		Dataset<Row> archi2 = spark.createDataFrame(parentele, Parentela.class);
		
		// Grafo, richiede nel costruttore un dataset di vertici ed un dataset di archi
		GraphFrame grafo2 = new GraphFrame(vertici2, archi2);
		grafo2.degrees().show();
		
		
	}
}
