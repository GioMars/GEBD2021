package gebd.spark;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class Main {

	public static void main(String[] args) {
		
		Logger.getLogger("org").setLevel(Level.ERROR);
		Logger.getLogger("akka").setLevel(Level.ERROR);
		
		/*
		 * Creare un nuovo JavaSparkContext
		 * Utilizzaremo file di espressione genomica di piante
		 */
		JavaSparkContext spark = new JavaSparkContext();
		
		/*
		 * Le linee del dataset prevedono la seguente struttura
		 * '>' +  'nome sequenza' + '\n'
		 * 'sequenza nucleotidica'
		 */
		
		// Argomento da fornire al metodo main per individuare la locazione dei dati 
		String cartella = args[0];
		
		/*
		 * Lettura dei file della cartella
		 */
		JavaRDD<String> sequenze = spark.textFile(cartella);
		
		/*
		 * Filtraggio delle sequenze togliendo i nomi, prendendo le righe che non iniziano con '>'
		 */
		JavaRDD<String> seqContent = sequenze.filter(x -> x.charAt(0) != '>');
		
		/*
		 * Estrarre le sottosequenze lunghe un certo K e contarle
		 */
		int k = 5;
		
		JavaRDD<String> subSeqsRdd = seqContent.flatMap(seq -> {
			ArrayList<String> subSeqs = new ArrayList<>();
			
			for (int i = 0; i < seq.length() - k + 1; i++)
				subSeqs.add(seq.substring(i, i+k));
			
			return subSeqs.iterator();
		});
		
		JavaPairRDD<Integer, String> subCounts = subSeqsRdd.
				mapToPair(subSeq -> new Tuple2<String, Integer>(subSeq, 1)).
				reduceByKey((c1, c2) -> c1+c2).
				mapToPair(t -> new Tuple2<>(t._2, t._1)). // Inversione di chiave con valore per il SORT 
				sortByKey(false); // Il booleano FALSE specifica ordinamento discendente
		
		System.out.println(subCounts.take(10));
	}
}
