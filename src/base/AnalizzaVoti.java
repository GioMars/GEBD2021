package base;
/* 
 * Applicazione per analizzare la carriera di un certo studente espressa
 * sotto forma dell'elenco dei voti che ha conseguito agli esami sostenuti.
 * calcola e mostra a schermo alcune semplici statistiche come:
 * - numero esami
 * - lista degli esami
 * - voto minimo e massimo
 * - media dei voti
 * - media ponderata dei voti (pesi: CFU)
 * - media dei voti (ad eccezione del voto più basso)
 * - media ponderata dei voti (ad eccezione del voto più basso, selezionando in caso di ambiguità il credito superiore)
 * E' inoltre richiesto che i voti siano inseriti in memoria per futuri utilizzi
 * Attenzione: non è previsto controllo degli errori per quanto concerne i valori inseriti
 */

import java.util.Scanner;

public class AnalizzaVoti {

	public static void main(String[] args) {
		int votoMin = 999;
		int votoMax = 0;
		int nrEsami;
		int voti[];
		int crediti[];
		int somma = 0;
		int credititot = 0;  // numero totale di crediti
		int sommapes = 0;    // somma dei voti pesati ciascuno per il numero assoluto di crediti
		int cfuMax = 0;
		Scanner sc = new Scanner(System.in);

		// Acquisire inizialmente il numero di voti da usare per creare l'array
		System.out.println("Inserisci il numero di esami da processare");
		nrEsami = sc.nextInt();
		

		voti = new int[nrEsami];       // Array che ospiterà i voti
		crediti = new int[nrEsami];

		for (int i = 0; i < nrEsami; i++) {
			System.out.println("Inserisci il prossimo voto");
			voti[i] = sc.nextInt();
			
			System.out.println("Inserisci il corrispettivo numero di crediti");
			crediti[i] = sc.nextInt();

			somma += voti[i];
			credititot += crediti[i];
			sommapes += voti[i] * crediti[i];

			if (voti[i] > votoMax) {
				votoMax = voti[i];
			} else if (voti[i] <= votoMin && crediti[i] > cfuMax) {
				votoMin = voti[i];
				cfuMax = crediti[i];
			}
		}

		if (nrEsami == 0) {
			System.out.println("Non è stato conseguito alcun esame");
		} else if (nrEsami == 1) {
			System.out.println("Voto medio: " + (float)somma);
			System.out.println("Voto medio ponderato: " + (float) sommapes / credititot);
			System.out.println(
					"Non è possibile calcolare il voto medio escludendo il voto minimo, in quanto è stato conseguito un solo esame");
			System.out.println("Numero esami: " + nrEsami);
			System.out.println("Voto minimo: " + votoMin);
			System.out.println("Voto massimo: " + votoMax);
		} else {
			System.out.println("Voto medio: " + (float)somma/nrEsami);
			System.out.println("Voto medio ponderato: " + (float) sommapes / credititot);
			System.out.println("Voto medio (escluso il voto minimo): " + ((float)somma - votoMin)/(nrEsami-1));
			System.out.println("Voto medio ponderato (escluso voto minimo con maggior numero di crediti): "
					+ ((float) sommapes - votoMin * cfuMax) / (credititot - cfuMax));
			System.out.println("Numero esami: " + nrEsami);
			System.out.println("Voto minimo: " + votoMin);
			System.out.println("Voto massimo: " + votoMax);
			
			/*
			 * Per default in Java la divisione tra interi restituisce un intero,
			 * per conoscere la parte decimale occorre che almeno uno dei due argomenti sia
			 * un FLOAT o un DOUBLE
			 * In un caso come il nostro, è possibile trasformare uno dei
			 * due argomenti in float attraverso una operazione di 'casting', facendo cioè
			 * precedere la variabile dall'indicazione del tipo desiderato, racchiuso tra parentesi
			 */
		}
	}

}
