package base;
public class TestArray {

	public static void main(String[] args) {

		/*
		 * Dichiarazione della variabile che punta all'ARRAY
		 * La variabile voti è predisposta a puntare ad un ARRAY di interi
		 * ed usa come identificatore la stringa 'voti'
		 * Attenzione, l'array non è stato ancora creato
		 */
		int[] voti;
		
		/*
		 * Creazione dell'array
		 * Assegnazione dell'array alla variabile puntatore
		 * Creo un nuovo array di taglia 10
		 * Creo un contenitore di 10 celle, ciascuna delle quali
		 * può contenere un numero intero
		 * La creazione predispone un oggetto a sé stante, per poterlo utilizzare
		 * ho bisogno di far riferimento ad esso attraverso una variabile,
		 * la variabile voti
		 */
		voti = new int[10];
		
		/*
		 * L'array è pronto per essere utilizzato. Tuttavia, il suo contenuto
		 * inizialmente è indefinito, per cui potrà essere solo aggiornato in scrittura
		 * (inizializzazione)
		 * 
		 * E' possibile accedere al contenuto di un array attraverso l'operatore []
		 * fornendo in input l'indice dell'elemento desiderato (attenzione: la numerazione
		 * parte da 0)
		 */
		voti[0] = 28;
		voti[1] = 26;
		voti[9] = 28;
		
		/*
		 * Inizializzazione dell'array contestuale alla sua creazione
		 */
		int[] voti2 = {21,24,23};
		
		System.out.println(voti[0]);
		/*
		 * Sto assegnando a 'voti2' il puntatore all'array puntato da 'voti'
		 * Come conseguenza, l'array precedentemente puntato da voti2 viene
		 * distrutto perché non c'è più nessuno che lo usa
		 */
		voti2 = voti;
		
		/*
		 * Voglio ottenere due array distinti con la stessa
		 * taglia e lo stesso contenuto, non voglio due variabili
		 * distinte che puntano allo stesso array
		 */
		int[] voticopia = new int[10];
		
		/*
		 * Per duplicare l'array, occorre eseguire per ogni sua posizione
		 * la duplicazione del contenuto corrispondente,
		 * problema risolvibile attraverso un ciclo FOR
		 * Nella sintassi del FOR è presente uno spazio da adoperare per 
		 * una istruzione di inizializzazione del ciclo, la prima istruzione ad essere
		 * eseguita quando si 'prende' il FOR
		 * ESEMPIO int i=0
		 * Il ciclo FOR consente di eseguire ripetutamente un blocco di
		 * istruzioni fino a quando una certa condizione è verificata (ciclo per vero)
		 * ESEMPIO i < voti.length
		 * Al termime di ciascuna iterazione, si esegue l'istruzione di 'aggiornamento' del FOR
		 * ESEMPIO i++
		 * 
		 * Nell'esempio, utilizziamo la proprietà .length degli array in Java,
		 * che mi restituisce la taglia dell'array cui la variabile punta
		 */
		for(int i = 0; i < voti.length; i++) {
			voticopia[i] = voti[i];
		}
		
		System.out.println(voticopia[0]);
		
		/*
		 * Quando occorre copiare in modo efficiente il contenuto di un array
		 * in un altro array, piuttosto che farlo 'a mano' utilizzando FOR,
		 * è preferibile utilizzare la funzione ARRAYCOPY
		 * Questa richiede l'indicazione dell'array che si intende copiare, la posizione di partenza,
		 * l'array  su cui si intende copiare, la posizione di partenza
		 * ed il numero di elementi da copiare
		 */
		System.arraycopy(voti, 0, voticopia,0,voti.length);
		
		/*
		 * Stampa dei due ARRAY
		 */
		System.out.println("\n");
		for(int i = 0; i< voti.length; i++) {
			System.out.println(voti[i]);
		}
		System.out.println("\n");
		for(int i = 0; i< voti2.length; i++) {
			System.out.println(voti2[i]);
		}	
		
		
	}

}
