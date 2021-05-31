package gebd.spark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import scala.Tuple2;

public class EstrapolaParole implements FlatMapFunction<String, String> {
	/*
	 * Data una stringa in input, come ricavare in output una lista di stringhe 
	 * Nella documentazione T sta per tipo di input e U tipo di output
	 * La documentazione specifica quali metodi sono previsti dall'interfaccia, in tal caso il metodo 
	 * CALL prende in input una stringa e restituisce in output un iterator (una lista di stringhe nel concreto)
	 */
	
	public java.util.Iterator<String> call(String line){
		// Dividere le parole in ogni riga allo spazio mediante il metodo SPLIT di Java 
		String[] words = line.split(" ");
	
		
		/*
		 * Creare una lista di stringhe in cui copiare una ad una tutte le parole precedentemente 
		 * individuate 
		 */
		List<String> result = new ArrayList<String>();
		for (String word : words) {
			word = word.replaceAll("[,.;:?!-]", "").toLowerCase();
			if (word.length()>0) {
			result.add(word);
			}
		}
		
		/*
		 * Restituire il contenuto della lista sotto forma di iteratore 
		 * L'iteratore è richiesto per lavorare su dati di grandi dimensioni, si occupa della gestione 
		 */
		return result.iterator();
	}
	
}

