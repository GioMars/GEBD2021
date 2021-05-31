package gestioneStudentiBase;

public class StudenteSG extends Studente {

	public StudenteSG (String nome, String cognome) {
		super(nome, cognome, "SG");
	}
	
	/*
	 * METODO CALCOLA MEDIA 
	 */
	public double calcolaMedia() {
		if (numeroEsami == 0) {
			return 0.0;
		}

		if (numeroEsami == 1) {
			return (double) esami[0].getVoto();
		}

		int somma = 0;
		for (int i = 0; i < numeroEsami; i++) {
			somma += esami[i].getVoto();
		}

		return (double) somma / numeroEsami;

		}
	
	public void mostraInformazioni() {
		System.out.println("Nome: " + this.nome);
		System.out.println("Cognome: " + this.cognome);
		System.out.println("Iscritto a SG");
		mostraVoti();
	}
}
