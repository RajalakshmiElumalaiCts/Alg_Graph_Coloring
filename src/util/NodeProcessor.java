package util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.DoublyLinkedList;
import model.Vertex;
import model.DoublyLinkedList.Node;

public class NodeProcessor {
	
	public List<Vertex> createVertex(Integer vertexCount) {
		List<Vertex> vertices = new ArrayList<Vertex>();
		for(int index = 1; index <= vertexCount; index++) {  // O(|V|)
			Vertex vertex = new Vertex(index);
			vertices.add(vertex);
		}
		return vertices;
	}
	
	public List<Integer> extractVerticesPosition(List<Integer> fileContent, int vertexCount) {
		List<Integer> vtxAdjNodePos = new ArrayList<Integer>();
		for(int index=1; index <= vertexCount; index++) {  // O(|V|)
			vtxAdjNodePos.add(fileContent.get(index));
		}
		return vtxAdjNodePos;
	}
	
	public void createAdjacentList(List<Integer> fileContent, List<Integer> vertextPosition,
			List<Vertex> vertices) {
		
		for(int index = 0; index < vertextPosition.size(); index++) { // O(|V|)
			int startPos = vertextPosition.get(index);
			int endPos = 0;
			if(index == vertextPosition.size()-1) {
				endPos = fileContent.size();
				
			}else {
				endPos = vertextPosition.get(index+1);
			}
			LinkedList<Integer> adjacentList = new LinkedList<Integer>(fileContent.subList(startPos, endPos));
			vertices.get(index).setAdjacentList(adjacentList);
			vertices.get(index).setInitialDegree(adjacentList.size());				
		}
	}
	
	public Vertex getVertex(int nodeId, List<Vertex> vertices) {
		Vertex visitingNode = null;
		for(Vertex vertex : vertices) { // O(|V|) , big omega(1) for best case, Average case O(|v|/2)
			if(vertex.getId() == nodeId) {
				visitingNode = vertex;
				break;
			}
		}
		return visitingNode;
	}

	public void printInput(List<Vertex> vertices) {
		System.out.println("Provided Input data :");
		for(Vertex vertex:vertices) {
			System.out.println("Vertex ID : " + vertex.getId());
			System.out.println("Adjacent vertex IDs : " + vertex.getAdjacentList());
			System.out.println("Original Degree : " + vertex.getInitialDegree());
			System.out.println();
			
		}		
	}

	public void printOutput(DoublyLinkedList visitedNodeList, List<Vertex> vertices, int totalColors) {
		System.out.println("Printing vertices as they are colored...");
		Node current = visitedNodeList.getHead();
		Vertex vertex = null;
		int sumOfOriginalDegree = 0;
		while (current != null) {
			vertex = getVertex(current.getData(), vertices);
			System.out.println("Vertex ID : " + vertex.getId());
			System.out.println("Color assigned: " + vertex.getColor());
			System.out.println("Original Degree : " + vertex.getInitialDegree());
			System.out.println();

			sumOfOriginalDegree = sumOfOriginalDegree + vertex.getInitialDegree();

			current = current.getNext();
		}

		System.out.println("Total No.of colors used : " + totalColors);
		System.out.println("Average of Original Degree : " + (sumOfOriginalDegree / vertices.size()));

	}
}
