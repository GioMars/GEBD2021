package gebd.spark;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

/*
 * Spark consente l'elaborazione distribuita di dati complessi, non solamente di dati di tipo primitivo
 * Si desidera costruire una rappresentazione distribuita di un dataset complesso nel suo insieme, 
 * non di una variabile singolarmente
 * L'obiettivo è di costruire una RDD in grado di mantenere numerose variabili allo stesso tempo, 
 * per raggiungerlo è necessario creare una classe WRAPPER.

 * Caricare il contenuto di un file .CSV contenente il report degli incidenti stradali a Roma
 * sotto forma di RDD ed applicare alcune operazioni
 */

public class AnalizzaIncidentiStradali {
	
	public static void main(String[] args){
			// Canonico preambolo 
			Logger.getLogger("org").setLevel(Level.ERROR);
			Logger.getLogger("akka").setLevel(Level.ERROR);
			SparkConf sc = new SparkConf();
			sc.setAppName("Analizza Incidenti Stradali");
			sc.setMaster("local[*]");
			JavaSparkContext jsc = new JavaSparkContext(sc);
				
			// Importo i records del dataset sotto forma di stringhe (separatore tra gli attributi ';')
			JavaRDD<String> dLines = jsc.textFile("data/incidenti_roma_2020.csv");
			
			/*
			 * Da ogni elemento in input ne voglio uno in output --> un oggetto incidente che incapsula
			 * tutti i valori 
			 * Trasformazione MAP 
			 */
			
			// Intestazione
			System.out.println(dLines.take(1));
			
			// Selezionare le sole righe dove la stringa di intestazione è assente
			dLines = dLines.filter(x -> x.indexOf("Protocollo") == -1);
			System.out.println(dLines.take(1));
			
			// Attenzione a modificare stringhe ed interi come richiesto dalla classe Incidente
			JavaRDD<Incidente> dIncidente = dLines.map(x -> new Incidente(Integer.
					parseInt(x.split(";")[1]),   				// idGruppo
					x.split(";")[4],             				// Strada 1
					x.split(";")[6],							// Strada 2
					Integer.parseInt(x.split(";")[0]),			// No. Protocollo
					Integer.parseInt(x.split(";")[23])));		// No. illesi 
			List<Incidente> x = dIncidente.take(2);
			for (Incidente i : x) {
				System.out.println(i.getStrada1());
				System.out.println(i.getStrada2());
			}
			
			/*
			 * Per ciascun incidente voglio tupla con strada in chiave e valore 1
			 * Trasformazione MAP TO PAIR 
			 */
			JavaPairRDD<String, Integer> dIncidente1 = dIncidente.
					mapToPair(i -> new Tuple2<String, Integer>(i.getStrada1(),1));
			
			/*
			 * Aggrego incidenti su stessa strada
			 * Trasformazione REDUCE BY KEY 
			 */
			JavaPairRDD<String, Integer> dIncidente2 = dIncidente1.reduceByKey((z,y) -> z+y);
			System.out.println(dIncidente2.collect());
			
	}
}
