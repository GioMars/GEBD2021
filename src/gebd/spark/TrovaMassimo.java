package gebd.spark;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
	/*
	 * Data in input una tupla nel formato 
	 * K=p
	 * V=p1, f1, ..., pn, fn
	 * restituisce in output la tupla 
	 * K=p
	 * V=pi, fi
	 * dove fi è la frequenza massima 
	 */
public class TrovaMassimo implements PairFunction<Tuple2<String, String>, String, String> {
		
		public Tuple2<String, String> call(Tuple2<String, String> t) {
			// Prendere la parte V della tupla in input 
			String V = t._2;
			
			/*
			 * Le posizioni con indice dispari all'interno di V sono occupate dalle frequenze 
			 * Le posizioni con indice pari all'interno di V sono occupare dai product ID 
			 */
			
			// Opero uno SPLIT e memorizzo le componenti di V in un array di stringhe 
			String[] prodotti = V.split(",");
			
			// Inizializzo il valore massimo della frequenza con il primo valore di frequenza che incontro
			int maxFreq = Integer.parseInt(prodotti[1]);
			String maxProductId = prodotti[0];
			
			for(int i = 3; i < prodotti.length; i+=2) {
				if (Integer.parseInt(prodotti[i]) > maxFreq) {
					maxFreq = Integer.parseInt(prodotti[i]);
					maxProductId = prodotti[i-1];
				}
			}
			
			/*
			 * Versione semplificata, se due prodotti hanno associata medesima frequenza di acquisto, verrà consigliato
			 * unicamente il primo incontrato
			 */ 
			
			return new Tuple2<String, String> (t._1, maxProductId +","+ maxFreq);
		}

}
