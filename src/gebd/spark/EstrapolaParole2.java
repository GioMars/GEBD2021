package gebd.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;

import scala.Tuple2;

public class EstrapolaParole2 implements PairFlatMapFunction<String, String, Integer> {
	/*
	 * In input si considera una stringa ed in output si richiede una lista di coppie di stringhe e numeri
	 * dove la prima è il testo di ciascuna parola ed il secondo la sua frequenza (1)
	 */
	public java.util.Iterator<Tuple2<String,Integer>> call(String line){
		String[] words = line.split(" ");
		
		List<Tuple2<String, Integer>> result = new ArrayList<Tuple2<String, Integer>>();
		for (String word : words) {
			/*
			 * Sostituire con stringa vuota i simboli di punteggiatura e rendere tutto minuscolo 
			 */
			word = word.replaceAll("[,.;:?!-]", "").toLowerCase();
			if (word.length()>0) {
			result.add(new Tuple2<String, Integer>(word, 1));
			}
		}
		
		return result.iterator();
	};
}
