package gebd.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class AnalizzaIncidentiStradali2 {
	
	public static void main(String[] args) throws AnalysisException {
			
			// Codice tratto da incidenti stradali versione BASE 
			Logger.getLogger("org").setLevel(Level.ERROR);
			Logger.getLogger("akka").setLevel(Level.ERROR);
			SparkConf sc = new SparkConf();
			sc.setAppName("Analizza Incidenti Stradali");
			sc.setMaster("local[*]");
			JavaSparkContext jsc = new JavaSparkContext(sc);
				
			JavaRDD<String> dLines = jsc.textFile("data/incidenti_roma_2020.csv");
			
			dLines = dLines.filter(x -> x.indexOf("Protocollo") == -1);
			
			JavaRDD<Incidente> dIncidente = dLines.map(x -> new Incidente(Integer.parseInt(x.split(";")[1]),
					x.split(";")[4],
					x.split(";")[6],
					Integer.parseInt(x.split(";")[0]),
					Integer.parseInt(x.split(";")[23])));
			
			JavaPairRDD<String, Integer> dIncidente1 = dIncidente.
					mapToPair(i -> new Tuple2<String, Integer>(i.getStrada1(),1));
			JavaPairRDD<String, Integer> dIncidente2 = dIncidente1.reduceByKey((z,y) -> z+y);
			
			
			/*
			 * La più elementare modalità di utilizzo di Spark è di basso livello e si basa sull'uso delle RDD
			 * L'utente deve esprimere in modo esplicito tutte le trasformazioni da operare sulle RDD; ciò 
			 * rappresenta una fonte di difficoltà
			 * 
			 * Molte delle operazioni eseguite su Spark sono assimilabili a delle query nel linguaggio SQL
			 * Spark mette a disposizione una modalità di funzionamento alternativa
			 * che consente di interrogare le RDD usando una variante di SQL
			 * Tale modalità si basa sull'utilizzo di una differente struttura dati di nome DATASET/DATAFRAME
			 * Tale nuova struttura dati è ricca di funzioni ed è di più alto livello rispetto le RDD
			 * Un DATASET consente di ospitare un set di variabili tipizzate, abbandonado l'impostazione KV,
			 * e risulta assimilabile ad una tabella 
			 * 
			 * Di seguito si introducono brevemente le istruzioni necessarie per fare uso di tale modalità
			 */
			
			/*
			 * Come primo passo occorre inizializzare Spark da capo 
			 * Creare una nuova istanza di una Spark Session riciclando la configurazione preesistente
			 * La Spark Session supporta l'abilità di formulare query in un linguaggio affine ad SQL
			 */
			SparkSession spark = SparkSession.builder().appName("Esempio uso Dataframe").config(jsc.getConf()).getOrCreate();
			
			/*
			 * Ho creato una nuova istanza di Spark da utilizzare per la manipolazione di Dataframe
			 * Per creare un dataframe da zero posso
			 * - convertire una RDD in un dataframe
			 * - caricare in contenuto di un file .CSV in memoria come dataframe 
			 */
			
			/*
			 * Convertire il contenuto delle RDD di incidente nel DATAFRAME/DATASET DFIncidente
			 * Nel fare la conversione devo indicare a Spark la classe con cui sono fatti gli elementi 
			 * della RDD dIncidente
			 */
			
			Dataset<Row> DFIncidente = spark.createDataFrame(dIncidente, Incidente.class);
			DFIncidente.show();
			// ricorda spark --> SparkSession 
			
			// Materializzazione in memoria (temporaneamente) come se fosse una tabella SQL
			DFIncidente.createTempView("incidenti"); 
			
			String sql = "SELECT * FROM incidenti WHERE idGruppo = 5";
			Dataset<Row> result = spark.sql(sql);
			result.show();
			// Scrittura su disco
			// result.write().format("csv").csv("incidenti5");
			
			
			/*
			 * Carico in memoria il contenuto di un file .CSV direttamente sotto forma di DATASET/DATAFRAME
			 */
			Dataset<Row> DFIncidente2 = spark.read().option("header", "true").option("inferSchema", "true").
				option("delimiter", ";").csv("data/incidenti_roma_2020.csv");
			DFIncidente2.show();
			
			
	}
}
