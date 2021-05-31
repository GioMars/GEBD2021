package gebd.spark;

import java.util.Comparator;

import scala.Serializable;
import scala.Tuple2;

/*
 * Le classi che implementano un COMPARATOR in SPARK devono necessariamente 
 * implementare anche l'interfaccia SERIALIZABLE
 */

public class ProductComparator implements Serializable, Comparator<Tuple2<String, Integer>> {
	/*
	 * Date due tuple di Stringhe e Interi, qual è più grande di quale? 
	 * Se ne chiarisca il metodo!
	 * Confrontare coppie di tuple che descrivono la frequenza con cui sono stati 
	 * acquistati i prodotti forniti da input 
	 * Nel confrontare una coppia di tuple, riteniamo più grande la tupla con la frequenza 
	 * più alta 
	 * Il metodo COMPARE restituisce +1 se la prima tupla è maggiore della seconda, 0 se sono uguali,
	 * -1 altrimenti 
	 */
	
	public int compare(Tuple2<String, Integer> o1, Tuple2<String, Integer> o2) {
		if (o1._2 > o2._2) {
		return 1;
	} else if (o1._2 == o2._2) {
		return 0;
	} else {
		return -1;
	}
	}

}
