package gebd.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class TestSpark {
	
	/*
	 * Applicazione di prova di Spark
	 * Text processing
	 */
	
	// METODO MAIN
	public static void main(String[] args) {
		
		/*
		 * Per default, le esecuzioni di applicazioni SPARK possono generare numerosi messaggi a schermo,
		 * solitamente tali messaggi descrivono il buon andamento o meno dell'esecuzione
		 * Per rendere leggibile l'output dell'applicazione si decide di sopprimere tali messaggi tranne che 
		 * nel caso di errori 
		 * Tutte le stampe a schermo che si originano in classi che cominciano per ORG o per AKKA saranno da mostrarsi solo
		 * in caso di errore
		 */
		Logger.getLogger("org").setLevel(Level.ERROR);
		Logger.getLogger("akka").setLevel(Level.ERROR);
		
		/*
		 * Per poter iniziare a lavorare con SPARK bisogna dapprima definirne la configurazione attraverso un oggetto 
		 * di tipo SPARKCONF (contenitore che ospita le preferences)
		 */
		SparkConf sc = new SparkConf();
		
		/*
		 * Definire all'interno dello SparkConf il nome dell'applicazione mediante metodo SET APP NAME
		 */
		sc.setAppName("La mia prima applicazione");
		
		/*
		 * Scegliere il contesto di esecuzione dell'applicazione attraverso il metodo SET MASTER,
		 * questi può assumere tre diversi valori 
		 * - local (un computer personale)
		 * - cluster (sistema distribuito)
		 * - yarn (cluster HADOOP)
		 * Quando si lavora su un cluster, si utilizzano in automatico tutti i processori disponibili
		 * Quando si sceglie di lavorare in local, si specifica il numero di processori da utilizzare indicandoli tra parentesi quadra 
		 * (asterisco '*' implica l'utilizzo di tutti i processori disponibili)
		 */
		sc.setMaster("local[*]");
		
		/*
		 * Avviare SPARK attraverso l'oggetto JAVA SPARK CONTEXT
		 * si sfruttano le preferences impostate all'interno dello SparkConf
		 */
		JavaSparkContext jsc = new JavaSparkContext(sc);
		
		/*
		 * Di base, le applicazioni SPARK creano e processano delle RDD,
		 * queste possono essere viste come dei DATAFRAMES distribuiti 
		 * Esistono diversi modi per creare una RDD, il più semplice è attraverso il metodo PARALLELIZE
		 */
		
		// Creazione di una lista di numeri interi 
		List<Integer> lista = new ArrayList<Integer>(); 
		lista.add(5);
		lista.add(12);
		lista.add(4);
		
		/*
		 * Attraverso il metodo PARALLELIZE è possibile trasformare una collection in una RDD
		 * La variabile JavaRDD che si origina non punta ad una struttura dati in memoria,
		 * essa è una sorta di stub contenente al suo interno dei puntatori ai nodi del sistema distribuito 
		 * 
		 * PARALLELIZE spacchetta la collection e la invia, in blocchi, sui nodi del sistema distribuito
		 * 
		 * Da questo momento in poi, ogni operazione eseguita su una RDD non sarà eseguita localmente all'applicazione Driver ma 
		 * sarà richiesta a tutti i nodi che detengono una porzione della struttura dati originaria
		 * 
		 * E' possibile interagire con una RDD per mezzo di
		 * - azioni: tipicamente processano parte o tutta una RDD e restituiscono uno o più valori scalari
		 * - trasformazioni: tipicamente processano parte / tutta / più RDD e restituiscono altre RDD
		 * 
		 * L'esecuzione di un metodo su sistema distribuito prevede
		 * - richiamare i blocchi distribuiti sui nodi
		 * - richiedere l'esecuzione dell'operazione su tutti i blocchi
		 * - richiedere le risposte 
		 * - restituire la soluzione unificata
		 */
		JavaRDD<Integer> dLista = jsc.parallelize(lista);
		// d- sta per 'distribuito'
		
		/*
		 * Alcuni esempi di azioni 
		 */
		System.out.println("Dimensione della lista: " + dLista.count());
		System.out.println("Primo elemento della lista: " + dLista.first());
		System.out.println("Primi due elementi della lista: " + dLista.take(2));
		
		/*
		 * E' possibile trasferire il contenuto di una RDD sul DRIVER attraverso 
		 * l'azione COLLECT. Attenzione a non spostare in memoria del DRIVER collezioni di dati enormi 
		 */
		List<Integer> lista2 = dLista.collect();
		
		
		/*
		 * Risolvere per tappe il problema del wordcount introducendo per l'occasione 
		 * alcune delle trasformazioni suppportate da Spark
		 */
		
		// Una lista di stringhe
		List<String> lines = new ArrayList<String>();
		lines.add("sopra la panca");
		lines.add("la capra campa");
		lines.add("sotto la panca");
		lines.add("la capra crepa");
		
		JavaRDD<String> dLines = jsc.parallelize(lines);
		
		/*
		 * Per iniziare, risolviamo una versione semplificata del problema nella quale si desidera 
		 * misurare la taglia complessiva di tali stringhe 
		 * 
		 * Da risolvere in due steps:
		 * - map step: contare la taglia di ogni riga
		 * - reduce step: sommare i conteggi
		 *
		 * Eseguire un'istruzione di MAP su tutte le stringhe presenti in dLines
		 * Con tale istruzione si richiede di valutare e restituire la taglia di ciascuna di queste stringhe
		 * In output si avrà una JavaRDD con l'elenco delle taglie
		 * Come argomento del metodo MAP si deve fornire la descrizione dell'istruzione che si desidera ciascun executor
		 * vada ad eseguire sulla porzione di dati in proprio possesso
		 * Esistono due diversi modi di procedere con Java/SPARK
		 * - le funzioni lambda
		 * - l'implementazione di interfacce
		 * 
		 * Le funzioni lambda sono strumenti presenti in Java per descrivere in che modo debbano essere processati tutti 
		 * gli elementi di una data collezione
		 * Introdurrre una variabile che descrive il generico elemento della collezione, seguita dalla sequenza -> e poi 
		 * dal tipo di eleborazione cui si desidera sottoporlo 
		 * 
		 * La trasformazione MAP prende in input una collection di stringhe e ne restituisce la taglia;
		 * ad ogni stringa in input corrisponde un valore in output
		 */
		JavaRDD<Integer> dSize = dLines.map(x -> x.length());
		System.out.println(dSize.first());
		System.out.println(dSize.take(2));
		System.out.println(dSize.collect());
		
		/*
		 * Reduce Step: sommare la taglia di tutte le stringhe attraverso una funzione di REDUCE
		 * Presi in input un insieme di n interi, si restituisce un solo valore totale 
		 */
		Integer size = dSize.reduce((x,y) -> x+y);    // 3 numeri <-> 2 operazioni
		System.out.println("Il numero di caratteri complessivo è " + size);
		
		/*
		 * Risolvere ora la versione originale del problema in due STEP:
		 * per ogni parola distinta ricorrente nel testo si desidera conoscere la frequenza di ricorrenza
		 * I due steps
		 * - map step: estrarre tutte le parole presenti in una linea associandovi la frequenza 1
		 * - reduce step: aggregare tutte le occorrenze di una stessa parola, sommandone le frequenze
		 */
		
		/*
		 * Nel primo passo si processa ciascuna stringa estrapolando tutte le parole presenti al suo interno
		 * In casi come questo, potrebbe essere difficile descrivere il tipo di elaborazione che si ha in mente attraverso una
		 * funzione lambda, pertanto si ricorre ad una strada alternativa --> le interfacce
		 * Dal momento che per ogni stringa in input l'output potrebbe contenere 0,1 o n stringhe, non si utilizza più la trasformazione
		 * MAP ma la trasformazione FLAT MAP 
		 * Si conosce l'esistenza di tale trasformazione dalla documentazione 
		 * Nella codumentazione c'è scritto cosa fa ogni trasformazione ed anche come sia possibile specificarne il comportamento 
		 * 
		 * FLAT MAP restituisce una RDD applicando una funzione ad ogni elemento 
		 */
		
		JavaRDD<String> dWords = dLines.flatMap(new EstrapolaParole());
		System.out.println(dWords.collect());
		
		/*
		 * Ad ogni elemento di dLines voglio associare la frequenza 1
		 *  
		 * Utilizzo una trasformazione MAP TO PAIR per convertire la JavaRDD<String>
		 * in JavaPairRDD <String,Integer>, accostando a ciascuna parola (chiave) un intero (1, valore)
		 * 
		 * Per ogni elemento della RDD di input si restituisce una tupla di due elementi (nella forma desiderata),
		 * basta sfruttare l'espressione lambda
		 * Per confezionare la coppia (chiave, valore) si ricorre alla classe Tuple2
		 */
		JavaPairRDD<String, Integer> dWords2 = dWords.mapToPair(x -> new Tuple2(x,1));
		System.out.println(dWords2.collect());
		
		/*
		 * Aggregare tutte le coppie che presentano medesima chiave
		 * Reduce by Key dalla documentazione JavaPairRDD --> aggrega gli elementi con stessa chiave 
		 * applicando la funzione di somma (implementata mediante lambda) ai valori 
		 */
		JavaPairRDD<String, Integer> dWords3 = dWords2.reduceByKey((x,y)->x+y);
		System.out.println(dWords3.collect());
		
		
		/*
		 * Seconda soluzione del medesimo problema che riduce il numero di trasformazioni 
		 */
		JavaPairRDD<String, Integer> dWords4 = dLines.flatMapToPair(new EstrapolaParole2());
		JavaPairRDD<String, Integer> dWords5 = dWords4.reduceByKey((x,y)->x+y);
		System.out.println(dWords5.collect());
		
		// o ancora, una soluzione aggregata
		JavaPairRDD<String, Integer> dWordsAggr = dLines.flatMapToPair(new EstrapolaParole2()).
				reduceByKey((x,y)->x+y);
		System.out.println(dWordsAggr.collect());
		
		/*
		 * Nella realtà i dati si trovano su disco, database remoto o su Web
		 * 
		 * Caso dati su disco contenuti nella directory del progetto 
		 * Negli scenari d'uso di SPARK, sovente, i files di input sono molto voluminosi
		 * In questi casi si adoperano sistemi concepiti per big data come HADOOP/HDFS
		 * In casi più semplici è possibile assumere che un file di input si trovi sul nostro PC sotto forma di un 
		 * file di testo 
		 */
		
		/*
		 * Il metodo TextFile di JavaSparkContext carica il contenuto di un file di testo direttamente in 
		 * una JavaRDD senza ricorrere a Parallelize 
		 */
		JavaRDD<String> dLines1 = jsc.textFile("data/testo.txt");
		System.out.println(dLines1.take(2));
		
		JavaPairRDD<String, Integer> dWords6 = dLines1.flatMapToPair(new EstrapolaParole2());
		JavaPairRDD<String, Integer> dWords7 = dWords6.reduceByKey((x,y)->x+y);
		System.out.println(dWords7.collect());
		
		/*
		 * Una volta conclusa l'elaborazione occorre fissare il risultato su disco o su database (si esplora di seguito
		 * la prima possibilità)
		 * Nella realtà si utilizzano tecnologie specializzate per BigData, ma è comunque possibile salvare un 
		 * risultato sotto forma di file di testo 
		 * Il metodo SaveAsTextFile consente di salvare su disco il contenuto di una RDD 
		 * Su un input così piccolo Spark ha organizzato la RDD in due pezzi, secondo l'ottica distribuita, salvati 
		 * per semplicità di esempio sul disco locale 
		 */
		
		//dWords7.saveAsTextFile("frequenze");
		
		/*
		 * Complicazioni del programma
		 * Introdurre delle trasformazioni che consentano di
		 * - contare le occorrenze delle sole parole che contengono una certa sottostringa sviluppando due varianti:
		 * 	 nella prima il filtering avviene prima della Reduce by Key, nella seconda dopo (guarda documentazione)
		 * - dati due files di testo input TESTO 1 e TESTO 2, contare le frequenze cumulate delle parole per i due file
		 * - determinare l'elenco delle parole presenti in almeno uno dei due files
		 * - determinare l'elenco delle parole presenti in entrambi i files (intersezione)
		 * 
		 * Suggerimento: data una JavaPairRDD è possibile estrapolare la sola parte chiave o la sola parte valore sotto forma di 
		 * JavaRDD utilizzando rispettivamente i metodi keys() e values()
		 */
		
		/*
		 * 1) Contare le occorrenze delle sole parole che contengono una certa sottostringa sviluppando due varianti: 
		 * 	  - nella prima il filtering avviene prima della Reduce by Key
		 *    - nella seconda il filtering avviene dopo la Reduce by Key
		 */
		
		// Definire una sottostringa di interesse 
		String pattern = "lav";
		
		JavaPairRDD<String, Integer> dWordsFiltered = dLines1.
				flatMapToPair(new EstrapolaParole2()).
				filter(x -> x._1.contains(pattern)).
				reduceByKey((x,y)->x+y);
		
		System.out.println(dWordsFiltered.collect());
		
		
		JavaPairRDD<String, Integer> dWordsFiltered2 = dLines1.
				flatMapToPair(new EstrapolaParole2()).
				reduceByKey((x,y)->x+y).
				filter(x -> x._1.contains("a"));
		
		System.out.println(dWordsFiltered.collect());
		
		/*
		 * Covenienza dell'operazione? Prima o dopo? A priori non è possibile scegliere una delle due 
		 * soluzioni, può risultare altrettanto conveniente: 
		 * - filtrare dapprima tutti gli elementi per poi proseguire lungo la pipe-line su un numero minore di records
		 * - posticipare il filtraggio su pochi elementi (il filtraggio può essere impegnativo)
		 */
		
		//  2) Dati due files di testo input TESTO 1 e TESTO 2, contare le frequenze cumulate delle parole per i due file
		JavaPairRDD<String, Integer> dWordsCumulated = jsc.textFile("data/testo1.txt,data/testo2.txt").
				flatMapToPair(new EstrapolaParole2()).
				reduceByKey((x,y)->x+y);
		
		System.out.println(dWordsCumulated.collect());
		
		// 3) Determinare l'elenco delle parole presenti in almeno uno dei due files
		JavaRDD<String> dWordsList = jsc.textFile("data/testo1.txt,data/testo2.txt").
				flatMapToPair(new EstrapolaParole2()).
				reduceByKey((x,y)->1).
				keys();
		
		System.out.println(dWordsList.collect());
		
		// 4) Determinare l'elenco delle parole presenti in entrambi i files (intersezione)
		JavaPairRDD<String, Integer> dWordsText1 = jsc.textFile("data/testo1.txt").
				flatMapToPair(new EstrapolaParole2()).
				reduceByKey((x,y)->1);
		JavaPairRDD<String, Integer> dWordsText2 = jsc.textFile("data/testo2.txt").
				flatMapToPair(new EstrapolaParole2()).
				reduceByKey((x,y)->1);
		JavaRDD<String> dWordsTextUnion = dWordsText1.union(dWordsText2).
				reduceByKey((x,y)-> x+y).
				filter(x -> x._2 == 2).
				keys();
		System.out.println(dWordsTextUnion.collect());
		
		// Soluzione del professore sintetica ed efficiente mediante JOIN
		dWordsTextUnion = dWordsText1.join(dWordsText2).keys();
		System.out.println(dWordsTextUnion.collect());
		}
	
	/*
	 * Approfondimento
	 * MAP TO PAIR -> relazione 1:1 da input una coppia (chiave K, valore V)
	 * FLAT MAP TO PAIR -> relazione 1:Generico da input 0,1,n coppia/e (chiave K, valore V)
	 * REDUCE BY KEY: operazione reduce sui valori che condividono stessa chiave 
	 */


}
