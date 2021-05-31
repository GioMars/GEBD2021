package graph.spark;

public class Relazione {
	
	// Attributi di interesse 
	private String src, dst, relationship;
	
	/*
	 * E' necessario indicare, in una classe che va a modellare gli archi di un grafo, 
	 * l'ID del vertice di partenza SRC, l'ID del vertice di destinazione DST
	 * e le proprietà (tipo di relazione RELATIONSHIP)
	 * Attenzione nell'indicare gli ID al tipo di ID utilizzato per identificare 
	 * univocamente i vertici del grafo 
	 */
	
	// Costruttore con le informazioni richieste
	public Relazione (String src, String dst, String relationship) {
		this.src = src;
		this.dst = dst;
		this.relationship = relationship;
	}
	
	// Costruttore vuoto 
	public Relazione () {}
	
	// Metodi Get/Set
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDst() {
		return dst;
	}

	public void setDest(String dst) {
		this.dst = dst;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	
}
