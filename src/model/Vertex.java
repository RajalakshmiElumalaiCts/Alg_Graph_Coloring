package model;

import java.util.LinkedList;

import model.DoublyLinkedList.Node;

public class Vertex implements Comparable<Vertex>{
	int id;
	LinkedList<Integer> adjacentList;
	int initialDegree;
	Integer updatedDegree;
	//non-negative value represents color assigned to the vertex
	int color = -1;
	Node sameDegreeListPtr;
	Node deletedOrderListPtr;
	boolean isVisited;
	
	public Vertex(int id) {
		super();
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInitialDegree() {
		return initialDegree;
	}
	public void setInitialDegree(int initialDegree) {
		this.initialDegree = initialDegree;
	}
	public Integer getUpdatedDegree() {
		return updatedDegree;
	}
	public void setUpdatedDegree(Integer updatedDegree) {
		this.updatedDegree = updatedDegree;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public Node getSameDegreeListPtr() {
		return sameDegreeListPtr;
	}
	public void setSameDegreeListPtr(Node sameDegreeListPtr) {
		this.sameDegreeListPtr = sameDegreeListPtr;
	}
	
	public Node getDeletedOrderListPtr() {
		return deletedOrderListPtr;
	}

	public void setDeletedOrderListPtr(Node deletedOrderListPtr) {
		this.deletedOrderListPtr = deletedOrderListPtr;
	}

	public boolean isVisited() {
		return isVisited;
	}
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	public LinkedList<Integer> getAdjacentList() {
		return adjacentList;
	}
	public void setAdjacentList(LinkedList<Integer> adjacentList) {
		this.adjacentList = adjacentList;
	}
	
	@Override
	public String toString() {
		return "Vertex [id=" + id + ", adjacentList=" + adjacentList + ", initialDegree=" + initialDegree
				/*+ ", updatedDegree=" + updatedDegree + ", color=" + color + 
					 * ", sameDegreeListPtr=" + sameDegreeListPtr + ", updatedSameDegreeListPtr=" +
					 * updatedSameDegreeListPtr +
					 ", isVisited=" + isVisited  */ + "]";
	}

	@Override
	public int compareTo(Vertex vertex) {
		// sorts in descending order
		return vertex.initialDegree - this.initialDegree;
	}
	
}
