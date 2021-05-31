package gebd.spark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;

import scala.Tuple2;

/*
 * Data in input una stringa nella forma p1, p2, p3, p4
 * restituisce in output tutte le possibile coppie pi, pj dove i<>j (i diverso da j) senza ripetizioni
 * ed accompagnate dalla frequenza 1
 */
public class EstraiCoppie implements PairFlatMapFunction<String, String, Integer> {
	// Input: <String> Output: <String, Integer>

	public Iterator<Tuple2<String, Integer>> call(String carrello) {
		
		// In output avrò una lista di stringhe da ogni stringa in input 
		List<Tuple2<String, Integer>> output  = new ArrayList<Tuple2<String, Integer>>();
		
		// Spezzetto ogni stringa e salvo in un array le componenti 
		String[] prodotti = carrello.split(",");
		
		// Selezione delle varie coppie mediante due cicli FOR 
		for(int i=1; i <prodotti.length-1; i++) {
			for(int j=i+1; j <prodotti.length; j++) {
				/*
				 * Normalizzare la struttura delle tuple in modo che al primo posto compaia sempre 
				 * il prodotto con ID minore
				 */
				String p1 = prodotti[i];
				String p2 = prodotti[j];
				if (p1.compareTo(p2) > 0) {
					String xx = p1;
					p1 = p2;
					p2 = xx;
				}
				
				output.add(new Tuple2<String, Integer>(p1+","+p2,1 ));
			}
		}
		
		return output.iterator();
	}
	
}
