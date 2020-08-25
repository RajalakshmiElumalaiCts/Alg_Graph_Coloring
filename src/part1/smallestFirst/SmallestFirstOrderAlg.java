package part1.smallestFirst;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import model.DoublyLinkedList;
import model.DoublyLinkedList.Node;
import model.Vertex;
import util.FileProcessor;
import util.NodeProcessor;

public class SmallestFirstOrderAlg {
	/**
	 * map of doubly linked list which holds same degree vertices in a list and sorts map by degree of vertex in reverse order
	 */
	static Map<Integer, DoublyLinkedList> sameDegreeVertices = new TreeMap<Integer, DoublyLinkedList>(Comparator.reverseOrder());
	static DoublyLinkedList visitedNodeList = new DoublyLinkedList();
	static List<Vertex> vertices;
	static List<Integer> colorsUsed = new ArrayList<Integer>();
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\input.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\complete_graph.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\cycle_graph.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\uniform_graph.txt";
	static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\skewed_graph_1002.txt";
	static NodeProcessor nodeProc = new NodeProcessor();

	public static void main(String[] args) {
		Instant startTime = Instant.now();
		FileProcessor fileProc = new FileProcessor();

		//read input file
		List<Integer> fileContent = fileProc.readInput(fileName);	// n	
		
		int vertexCount = fileContent.get(0);
		vertices = nodeProc.createVertex(vertexCount);// v
		
		List<Integer> vertextPosition = nodeProc.extractVerticesPosition(fileContent, vertexCount); // v
		nodeProc.createAdjacentList(fileContent, vertextPosition, vertices);// v
		
		//nodeProc.printInput(vertices); // ignore
		
		createSameDegreeList();	// v
		//System.out.println(sameDegreeVertices);
		
		while(sameDegreeVertices.size() != 0) { // v
			visitHighDegreeVertex();	// v/2 + v/2 log v					
		}
		//visitedNodeList.printNode();
		//System.out.println();
		assignColor(); //  v[ v/2 + [ log v * v/2 ] + v/4 ] 
		//printing vertex visit output
		//printOutput();	// ignore	
		
		System.out.println("Total No.of colors used : " + colorsUsed.size());
		
		Instant endTime = Instant.now();
		long executionTime = Duration.between(startTime, endTime).toMillis();
		System.out.println("\nExecution Time in Milli Seconds : Smallest First Order ---->  "+executionTime);
	}
	
	private static void printOutput() {
		System.out.println("Printing vertices as they are colored...");
		Node current = visitedNodeList.getTail();
		Vertex vertex = null;
		int sumOfOriginalDegree = 0;
		int maxVisitDegree = -1;
		while(current != null){
			vertex = nodeProc.getVertex(current.getData(), vertices);
			System.out.println("Vertex ID : "+vertex.getId());
			System.out.println("Color assigned: "+ vertex.getColor());
			System.out.println("Original Degree : "+ vertex.getInitialDegree());
			System.out.println("Degree while deleting : "+ vertex.getUpdatedDegree()+"\n");
			
			sumOfOriginalDegree = sumOfOriginalDegree + vertex.getInitialDegree();
			if(maxVisitDegree < current.getVisitingDegree()) {
				maxVisitDegree = current.getVisitingDegree();
			}			
			
			current = current.getPrevious();			
		}
		
		System.out.println("Total No.of colors used : "+ colorsUsed.size());
		System.out.println("Average of Original Degree : "+ (sumOfOriginalDegree/vertices.size()));
		System.out.println("Maximum Degree at deletion : "+maxVisitDegree);
		
	}
	
	private static void assignColor() {// v[ v/2 + [ log v * v/2 ] + v/4 ]

		//assign color from tail node as it is last visited
		Node current = visitedNodeList.getTail();
		Vertex vertex = null;
		Vertex adjacentVtx = null;
		while(current != null){ // v
			vertex = nodeProc.getVertex(current.getData(), vertices);// v/2
			if(vertex.getColor() < 0) {
				if(colorsUsed.isEmpty()) {
					vertex.setColor(1);
					colorsUsed.add(1);
				}else {
					List<Integer> adjacentColor = new ArrayList<Integer>();
					for(Integer vtxId : vertex.getAdjacentList()) { // log v					
						adjacentVtx = nodeProc.getVertex(vtxId, vertices); // v/2
						if(adjacentVtx.getColor() > 0) {
							adjacentColor.add(adjacentVtx.getColor());
						}					
					}
					//check is there colors used which are not used for current node's adjacent vetices
					Optional<Integer> filterOption = colorsUsed.stream().filter(color ->
					!adjacentColor.contains(color)).findAny(); // v/4
					//color exists which are not used by adjacent vertices
					if(filterOption.isPresent()) {
						vertex.setColor(filterOption.get());						
					}else {//all colors used by used by adjacent vertices. So generate new color
						vertex.setColor(colorsUsed.size()+1);
						colorsUsed.add(colorsUsed.size()+1);
					}
				}
			}
			
			current = current.getPrevious();
        }
	}

	private static void visitHighDegreeVertex() {// v/2 + v/2 log v
		
		//get the first linked list of nodes, as it has highest degree
		Entry<Integer, DoublyLinkedList> smallestDegreeEntry = sameDegreeVertices.entrySet().iterator().next();
		//System.out.println("Visiting vertex with degree "+smallestDegreeEntry.getKey());
		
		DoublyLinkedList smallestDegreeList = smallestDegreeEntry.getValue();
		Node nodeToVisit = smallestDegreeList.getTail();
		smallestDegreeList.removeLast();
		/*after removing the visiting node from same degree list, 
		 * if the list not having any other node, remove the list
		 */
		if(smallestDegreeList.size() == 0) {
			smallestDegreeList = null;
			sameDegreeVertices.remove(smallestDegreeEntry.getKey());
		}
		//Adding the visiting node with visiting degree in the visited node list
		visitedNodeList.addNode(nodeToVisit.getData(), smallestDegreeEntry.getKey());
		Vertex vertex = nodeProc.getVertex(nodeToVisit.getData(), vertices); // v/2
		vertex.setVisited(true);
		vertex.setSameDegreeListPtr(visitedNodeList.getHead());
		
		//for the first visiting degree, the updated degree will be null
		if(vertex.getUpdatedDegree() == null) {
			vertex.setUpdatedDegree(smallestDegreeEntry.getKey());
		}
		updateAdjacentVertex(vertex); // v/2 log v
		
	}
	
	private static void updateAdjacentVertex(Vertex vertex) {// v/2 log v 

		for(Integer adjacentvtxId : vertex.getAdjacentList()) { // Avg case - log v
			Vertex adjacentVertex  = nodeProc.getVertex(adjacentvtxId, vertices); // v/2
			int previousDegree;
			if(!adjacentVertex.isVisited()) {
				//updating degree for the adjacent vertices
				if(adjacentVertex.getUpdatedDegree() == null) {
					previousDegree = adjacentVertex.getInitialDegree();
					adjacentVertex.setUpdatedDegree(adjacentVertex.getInitialDegree() - 1);
				}else {
					previousDegree = adjacentVertex.getUpdatedDegree();
					adjacentVertex.setUpdatedDegree(adjacentVertex.getUpdatedDegree() - 1);
				}
				//updating same degree doubly linked list for the adjacent vertices
				DoublyLinkedList updatedDegreeList = sameDegreeVertices.get(adjacentVertex.getUpdatedDegree());
				if(updatedDegreeList == null) {
					updatedDegreeList = new DoublyLinkedList();
					sameDegreeVertices.put(adjacentVertex.getUpdatedDegree(), updatedDegreeList);
				}
				updatedDegreeList.addNode(adjacentvtxId);	
				//Remove adjacent vertex from previous same degree list
				DoublyLinkedList prevSameDegreeList = sameDegreeVertices.get(previousDegree);
				prevSameDegreeList.removeNode(adjacentvtxId);
				/*after removing the adjacent node from same degree list, 
				 * if the list not having any other node, remove the list
				 */
				if(prevSameDegreeList.size() == 0) {
					prevSameDegreeList = null;
					sameDegreeVertices.remove(previousDegree);
				}
			}
			}
	
	}

	private static void createSameDegreeList() {
		for(Vertex vertex : vertices) { // v
			int degree = vertex.getInitialDegree();
			DoublyLinkedList doubleLinkList = sameDegreeVertices.get(degree);
			if(doubleLinkList == null) {
				doubleLinkList = new DoublyLinkedList();
				sameDegreeVertices.put(degree, doubleLinkList);
			}
			doubleLinkList.addNode(vertex.getId());	
			vertex.setSameDegreeListPtr(doubleLinkList.getHead());
		}
	}

}
