package gestioneStudenti;

/*
 * Le interfacce sono un costrutto della programmazione ad oggetti in Java
 * che consente di definire un insieme di firme di metodi standard 
 * Tutte le classi che aderiscono ad una certa interfaccia sono
 * obbligate ad implementarne i metodi
 */ 

public interface Autodescrivente {
	/*
	 * Si desidera che talune classa riuscissero a definirsi autonomamente
	 * Nelle interfacce sono presenti le sole firme dei metodi e non la loro 
	 * implementazione
	 */

	public String getDescrizione();
}
