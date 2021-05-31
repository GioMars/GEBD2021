package gestioneStudenti;

public final class StudenteSG extends Studente {

	public StudenteSG (String nome, String cognome) {
		super(nome, cognome, "SG");
	}
	
	/*
	 * METODO CALCOLA MEDIA 
	 */
	public double calcolaMedia() {
		if (esami.size() == 0) {
			return 0.0;
		}

		if (esami.size() == 1) {
			return (double) esami.get(0).getVoto();
		}

		int somma = 0;
		for (Esame e : esami) {
			somma += e.getVoto();
		}

		return (double) somma / esami.size();

	}
	
}
