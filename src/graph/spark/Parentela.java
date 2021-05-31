package graph.spark;

public class Parentela {
	private String src, dst, relationship;
	
	public Parentela (String src, String dst, String relationship) {
		this.src = src;
		this.dst = dst;
		this.relationship = relationship;
	}
	
	public Parentela () {}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
}
