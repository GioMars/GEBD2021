package base;
/*
 * Importiamo la classe Scanner perché ne avremo bisogno
 * per dialogare con l'utente (ammettendo possibilità di inserimenti da tastiera)
 */
import java.util.Scanner;

/*
 * In questa applicazione introduciamo alcuni dei principali
 * costrutti del linguaggio Java
 */
public class Applicazione {

	public static void main(String[] args) {
		/*
		 * Tipicamente, realizziamo applicazioni perché siamo interessati ad elaborare
		 * dei dati; i dati potrebbero trovarsi su disco, su una base dati, su
		 * Internet o altrove.
		 * 
		 * Disponiamo di una fase preliminare durante la quale acquisiamo i dati su cui
		 * lavorare, dopodiché parte la fase di elaborazione vera e propria.
		 * 
		 * E' possibile conservare in memoria dei dati e far riferimento ad essi
		 * mediante delle 'variabili'. Una variabile rappresenta un puntatore ad un dato
		 * di un certo tipo. Le variabili sono contraddistinte dalla presenza di un
		 * 'identificatore', attraverso il quale possiamo usarle.
		 * 
		 * Java mette a disposizione numerosi tipo di dato, li possiamo caratterizzare
		 * in base al dominio su cui assumono valore ed in base alla loro 'larghezza'
		 * (in termini di numero di bytes)
		 */

		/*
		 * Tipi numerici, per la rappresentazione esatta
		 */

		byte a;    // 1 byte: [2^-7:2^7-1]
		short b;   // 2 bytes [2^-15:2^15-1]
		int c;     // 4 bytes [2^-31:2^31-1]
		long d;    // 8 bytes [2^-63:2^63-1]

		/*
		 * Tipi numerici con rappresentazione a virgola mobile
		 */
		float e;   // 4 bytes
		double f;  // 8 bytes

		/*
		 * Tipo per la rappresentazione di caratteri di testo
		 */
		char g;

		/*
		 * Tipo per la rappresentazione di valori booleani (TRUE & FALSE)
		 */
		boolean h;

		/*
		 * Nell'esempio soprastante, abbiamo dichiarato delle variabili definendo per
		 * ciascuna di esse un tipo ed un identificatore. Nel ciclo di vita di una
		 * variabile esistono diverse fasi: 
		 * - dichiarazione della variabile (tipo ed identificatore) 
		 * - inizializzazione (per poter usare una variabile, occorre attribuirle un valore iniziale) 
		 * - utilizzo (in lettura o in scrittura)
		 */

		// inizializzazione della variabile 'a' (di tipo INT)
		a = 5;
		
		// aggiornamento del valore intero cui punta la variabile 'a'
		a = 7;
		
		// imporre alla variabile 'b' di puntare all'intero puntato dalla variabile 'a'
		b = a;

		// dichiarazione ed inizializzazione assieme
		int i = 2;

		/*
		 * Per i decimali, utilizziamo il carattere '.' come separatore 
		 * Di default i valori decimapli hanno tipo DOUBLE 
		 */
		f = 5.0;
		
		/*
		 * Se vogliamo forzare l'uso dei float dovremo usare un
		 * casting
		 */
		e = (float) 5.0;

		/*
		 * I caratteri si indicano circondandoli con apici ''
		 */
		g = 'f';
		
		// Valori Booleani
		h = true;
		h = false;

		/*
		 * Operatori aritmetici: è possibile combinare valori numerici per mezzo di
		 * operatori aritmetici
		 */

		int x;
		int y;
		int tot;

		x = 5;
		y = 3;

		tot = x + y;
		tot = x - y;
		tot = x * y;
		tot = x / y;
		tot = x % y; // modulo

		/*
		 * Nel caso l'esito dell'operazione sia poi usato per aggiornare il valore di
		 * una delle variabili di partenza, posso abbreviare l'espressione nel seguente
		 * modo
		 */
		x = x + y;
		x += y;
		x -= y;
		x *= y;
		x /= y;

		// Operatore di post-incremento, il valore di x viene incrementato di 1
		x++;
		
		/*
		 * Operatore di pre-incremento, il valore di x viene incrementato di 1
		 * la differenza tra i due operatori sta nel fatto che nel primo caso
		 * l'incremento viene effettuato una volta conclusa la valutazione
		 * dell'intera espressione in cui quella operazione si trova.
		 * Nel secondo caso, l'incremento viene effettuato *prima* di valutare
		 * l'intera espressione in cui quella operazione si trova
		 */
		++x;

		/*
		 * E' possibile assemblare espressioni complesse combinando espressioni
		 * elementari 
		 * Potrebbe sorgere ambiguità circa l'ordine di
		 * valutazione delle espressioni 
		 * Per evitare ambiguità, il linguaggio definisce un ordine di
		 * valutazione degli operatori. Dapprima si risolvono le operazioni di prodotto,
		 * divisione e modulo. Dopodiché, si valutano le operazioni di somma e
		 * differenza. Nel caso non vogliate rimanere nel dubbio o vogliate imporre un
		 * criterio di valutazione diverso, è possibile usare le parentesi tonde
		 */
		System.out.println(4 * (3 + 2) * 5);
		System.out.println("\n");
		
		// Pre-incremento del valore puntato dalla variabile x
		System.out.println("Pre-incremento");
		x = 5;
		System.out.println(++x * 10);
		System.out.println("x= " + x);
		System.out.println("\n");
		
		// Post-incremento del valore puntato dalla variabile x
		System.out.println("Post-incremento");
		x = 5;
		System.out.println(x++ * 10);
		System.out.println("x= " + x);
		System.out.println("\n");

		/*
		 * Due o più valori numerici possono essere confrontati utilizzando degli
		 * operatori di confronto 
		 * Questi restituiscono un risultato sotto forma di un valore booleano
		 */

		x = 7;
		y = 3;

		/*
		 * Di seguito, gli operatori di confronto supportati da Java
		 */
		System.out.println(x > y);
		System.out.println(x >= y);
		System.out.println(x < y);
		System.out.println(x <= y);
		System.out.println(x == y);
		System.out.println(x != y);

		/*
		 * Il linguaggio fornisce poi degli operatori che ci consentono di lavorare su
		 * valori booleani
		 * 
		 * La relazione AND && restituisce valore TRUE solamente se tutti i valori coinvolti sono TRUE
		 * altrimenti restituisce FALSE (almeno un valore coinvolto FALSE)
		 * 
		 * La relazione OR || restuituisce valore TRUE se almeno uno dei valori coinvolti è TRUE
		 * altrimenti restituisce FALSE (tutti i valori coinvolti sono FALSE)
		 */

		System.out.println(true && true);
		System.out.println(false && true);
		System.out.println(true && false);
		System.out.println(false && false);

		System.out.println(true || true);
		System.out.println(false || true);
		System.out.println(true || false);
		System.out.println(false || false);
		
		// NOT 
		System.out.println(!(true));
		System.out.println(!(false));
		System.out.println("\n");

		/*
		 * Il costrutto WHILE consente di reiterare l'esecuzione
		 * di un blocco di istruzioni sino a che una certa espressione
		 * booleana risulta vera (ciclo per vero)
		 */
		while (true) {
			int risultato = -1;

			// Acquisizione input

			// Creo un oggetto che mi consentirà di leggere
			// ciò che l'utente digita alla tastiera
			Scanner sc = new Scanner(System.in);
			System.out.println("Inserisci il primo numero (0 per uscire)");
			x = sc.nextInt();
			if (x == 0) {
				/*
				 * L'istruzione break mi consente di interrompere
				 * l'esecuzione di una particolare iterazione e
				 * riprendere dalla prima istruzione successiva al ciclo while
				 */
				break;
			}

			System.out.println("Inserisci il secondo numero");
			y = sc.nextInt();

			/*
			 * Elaborazione
			 * Vogliamo fornire la possibilità all'utente di decidere quale operazione
			 * eseguire. Occorre presentare una selezione di possibili operazioni e sentire
			 * il suo parere
			 */
			System.out.println("Scegli operazione: 1-somma 2-differenza 3-prodotto 4-rapporto");
			int op = sc.nextInt();

			if (op == 3) {
				System.out.println("Prodotto");
				risultato = x * y;
			} else if (op == 2) {
				System.out.println("Differenza");
				risultato = x - y;
			} else if (op == 1) {
				System.out.println("Somma");
				risultato = x + y;
			} else if (op == 4) {
				if (y != 0) {
					System.out.println("Divisione Intera");
					risultato = x / y;
			}else {
					System.out.println("Errore: divisione per zero");
					/*
					 * L'istruzione CONTINUE consente di interrompere
					 * l'esecuzione di una particolare iterazione e
					 * riprendere dalla testa del ciclo WHILE
					 */
					continue;
				}
			}

			// Produzione output
			System.out.println("Il risultato è: " + risultato);
		
		};
		System.out.println("Sono fuori dal while ...");

	}

}
