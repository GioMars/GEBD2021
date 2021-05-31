package graph.spark;

public class Persona {
	
	// Attributi di interesse 
	private String id, name;
	private int age;
	
	// Costruttore con gli attributi richiesti 
	public Persona(String id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}
	
	// Costruttore vuoto
	public Persona() {}
	
	// Metodi Get/Set
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	
}
