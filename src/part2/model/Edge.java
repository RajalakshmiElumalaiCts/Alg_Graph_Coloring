package part2.model;

public class Edge {

	private int fromVertex;
	private int toVertex;
	
	public Edge(int fromVertex, int toVertex) {
		super();
		this.fromVertex = fromVertex;
		this.toVertex = toVertex;
	}

	
	public int getFromVertex() {
		return fromVertex;
	}


	public void setFromVertex(int fromVertex) {
		this.fromVertex = fromVertex;
	}


	public int getToVertex() {
		return toVertex;
	}


	public void setToVertex(int toVertex) {
		this.toVertex = toVertex;
	}


	@Override
	public String toString() {
		return "Edge [fromVertex=" + fromVertex + ", toVertex=" + toVertex + "]";
	}

	
}
