/*
 * Classe di prova per sperimentare l'aggiornamento congiunto di 
 * ArrayList e Dizionari
 */

package base;
import java.util.*;

public class TestArrayAndDictionary {
	private ArrayList<String> prova;
	private HashMap<Integer, String> idToProva;
	private int id = 0;
	
	public TestArrayAndDictionary() {
	prova = new ArrayList<String>();
	idToProva = new HashMap<Integer, String>(); 
	}
	
	public int carica(String stringa) {
		prova.add(stringa);
		idToProva.put(id,stringa);
		return id++;
	}
	
	public void elimina(String stringa) {
		for(String i : prova) {
			if (i.equals(stringa)) {
				prova.remove(stringa);
			}
		}
	}
	
	public void mostra() {
		System.out.println("Mostrare elementi nell'ArrayList");
		for(String i : prova) {
			System.out.println(i);
		}
		
		System.out.println("\n");
		System.out.println("Mostrare elementi nel dizionario");
		for(Integer key : idToProva.keySet()){
			String s = idToProva.get(key);
			System.out.println(s);
		}
		System.out.println("\n");
		
	}
	
	public static void main(String[] args) {
		TestArrayAndDictionary pr = new TestArrayAndDictionary();
		pr.carica("Massimo");		// zero 
		pr.carica("Diego");			// uno
		pr.carica("Luna");			// due
		pr.mostra();
		pr.elimina("Diego");
		pr.mostra();
		
		
		
	}
		
}
	


