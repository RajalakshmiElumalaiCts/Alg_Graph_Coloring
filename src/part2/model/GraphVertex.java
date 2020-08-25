package part2.model;

import java.util.Set;

public class GraphVertex implements Comparable<GraphVertex>{
	
	private int id;
	
	private Set<Integer> adjacentList;

	public GraphVertex(int id) {
		super();
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public Set<Integer> getAdjacentList() {
		return adjacentList;
	}

	public void setAdjacentList(Set<Integer> adjacentList) {
		this.adjacentList = adjacentList;
	}

	@Override
	public String toString() {
		return "GraphVertex [id=" + id + ", adjacentList=" + adjacentList + "]";
	}

	@Override
	public int compareTo(GraphVertex vertex) {
		return Integer.compare(this.id, vertex.getId());
	}
	
	

}
