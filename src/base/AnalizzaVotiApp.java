package base;
import java.util.Scanner;

/*
 * Occorre scrivere una applicazione per la gestione dei vari aspetti della
 * carriera di uno studente. Possiamo individuare a monte alcune operazioni
 * tipiche che si potranno ripetere nel tempo. Piuttosto che codificarle ogni
 * volta che ce ne sarà bisogno, vorremmo poterle riscrivere una volta sola
 * e poi richiamarle all'occorrenza. Questo approccio ha numerosi vantaggi:
 * - la riusabilità del codice: non devo tornare a fare un lavoro già fatto in passato
 * - la manutebilità del codice: piuttosto che gestire ed aggiornare un unico grande
 * listato, ho da gestire tanti piccoli codici, ciascuno specializzato in un particolare task
 * - l'aggiornabilità del codice: diviene possibile aggiornare le funzionalità dell'applicazione
 * sostituendo o aggiornando singoli moduli, senza modificare il resto
 * 
 * I task che abbiano in mente saranno realizzati per mezzo di metodi.
 * Questi possono essere visti come gruppi di istruzioni nel linguaggio Java
 * specializzati nella risoluzione di un particolare problema. Tuttavia, il metodo
 * non consiste solo nella sua implementazione bensì introduce anche una interfaccia di utilizzo.
 * Quest'ultima, che prende anche il nome di *contratto*, stabilisce le modalità
 * ed i requisiti per l'esecuzione di quel metodo, nonché la forma con cui quel metodo
 * restituisce un risultato.
 *
 * E' ora necessario distinguere lo sviluppo di una applicazione in due fasi.
 * Nella prima fase, anche in base all'analisi dei requisiti che avremo fatto, realizzeremo
 * dei metodi Java che andranno a risolvere tutti i task di cui avremo bisogno per un
 * certo problema.
 * Nella seconda fase utilizzeremo i metodi precedentemente implementati per realizzare
 * l'applicazione che abbiamo in mente. L'applicazione sarà implementata per mezzo
 * del metodo MAIN.
 */

public class AnalizzaVotiApp{
	
	/*
	 * Faremo uso di un metodo statico specializzato
	 * nell'acquisizione da tastiera della serie di voti
	 * conseguiti da un certo studente
	 * Definiamo un metodo statico e pubblico 
	 * con nome "leggiVoti", tale metodo non prende in input alcun argomento in input e restituisce,
	 * come risultato, una variabile di tipo int[]. Il metodo è in grado in completa
	 * autonomia di acquisire da input una serie di valori interi corrispondenti
	 * ai voti conseguiti da un certo studente. Una volta che il risultato sarò
	 * pronto, verrà restituito a chi ha eseguito questo metodo per mezzo dell'istruzione
	 * *return*. Notate che nella dichiarazione del metodo viene indicato il tipo
	 * di oggetto restituito dal metodo stesso (int[]). Questo tipo deve coincidere con
	 * il tipo della variabile restituita attraverso return
	 */
	
	public static int[] leggiVoti() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Inserisci il numero di esami da processare");
		int nrEsami = sc.nextInt();

		int[] voti = new int[nrEsami];
		
		for(int i=0; i < nrEsami; i++) {
			System.out.println("Inserisci il voto:");
			voti[i] = sc.nextInt();;
		}
		
		return voti;
	}
	
	/*
	 * Implementiamo un metodo che, data in input una serie di voti, 
	 * codificata sotto forma di array di interi, ne mostra a schermo 
	 * il contenuto, senza restituire formalmente alcun oggetto
	 * 
	 *  Nel definire il contratto del metodo, devo specificare per ciascun
	 *  argomento il tipo ed un identificatore che userò per manipolare
	 *  quel valore all'interno del metodo
	 */
	public static void mostraVoti(int[] voti) {		
		for(int i=0; i < voti.length; i++) {
			System.out.println(voti[i]);
		}
	}
	
	public static void main(String[] args) {
		
		int[] voti;
		
		/*
		 * Per poter richiamare un metodo statico occorre indicare il nome
		 * della classe su cui è definito, seguito dal carattere '.' e dal nome
		 * del metodo in questione. Gli eventuali argomenti vanno indicati racchiusi
		 * tra parentesi tonde
		 */
		voti = AnalizzaVotiApp.leggiVoti();
		AnalizzaVotiApp.mostraVoti(voti);
	}
	
	
	

	
	

}
