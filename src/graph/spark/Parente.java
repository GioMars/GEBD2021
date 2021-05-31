package graph.spark;

public class Parente {
	private String id, name, genere;
	
	// Costruttore
	public Parente(String id, String name, String genere) {
		this.id = id;
		this.name = name;
		this.genere = genere;
	}
	
	// Costruttore vuoto
	public Parente() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	
}
