package part1.smallestLast;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import model.Clique;
import model.DoublyLinkedList;
import model.DoublyLinkedList.Node;
import model.Vertex;
import util.CliqueProcessor;
import util.FileProcessor;
import util.NodeProcessor;

public class SmallestLastOrderAlg {
	
	/**
	 * map of doubly linked list which holds same degree vertices in a list and sorts map by degree of vertex
	 */
	static Map<Integer, DoublyLinkedList> sameDegreeVertices = new TreeMap<Integer, DoublyLinkedList>();
	static DoublyLinkedList visitedNodeList = new DoublyLinkedList();
	static List<Vertex> vertices;
	static List<Integer> colorsUsed = new ArrayList<Integer>();
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\outuniform.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\complete_graph.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\cycle_graph.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\uniform_graph.txt";
	static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\skewed_graph_1002.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\input.txt";
	static NodeProcessor nodeProc = new NodeProcessor();
	static CliqueProcessor cliqueProc = new CliqueProcessor();
	static List<Clique> terminalCliques = new ArrayList<Clique>();
	
	public static void main(String[] args) {
		
		Instant startTime = Instant.now();
		
		FileProcessor fileProc = new FileProcessor();
		
		//read input file
		List<Integer> fileContent = fileProc.readInput(fileName);	//O(n)
		
		int vertexCount = fileContent.get(0);
		vertices = nodeProc.createVertex(vertexCount);  // O(|V|)
		
		List<Integer> vertextPosition = nodeProc.extractVerticesPosition(fileContent, vertexCount); // O(|V|)
		nodeProc.createAdjacentList(fileContent, vertextPosition, vertices); // O(|V|)
		
		//nodeProc.printInput(vertices); //ignore sysout
		
		createSameDegreeList();	 // O(|V|)
		//System.out.println(sameDegreeVertices);
		
		//loops till all the vertex are removed by visit
		while(sameDegreeVertices.size() != 0) {  // O(|V|)
			visitSmallestDegreeVertex();	// v/4 + [ v/4 * log(v/4) ] + v/2 +  v/3 * V/2					
		}
		//visitedNodeList.printNode();
		//System.out.println();
		assignColor();// v [ v/2 + [ v/3) * v/2) ] + v/2 ]	
		
		//System.out.println("Terminal Clique...");
		//System.out.println(terminalCliques + "\n");
		
		//printing vertex visit output
		printOutput();		//ignore sysout
		
		Instant endTime = Instant.now();
		long executionTime = Duration.between(startTime, endTime).toMillis();
		System.out.println("\nExecution Time in Milli Seconds : Smallest Last Order ---->  "+executionTime);
		
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
			
			//updateTerminalClique(current.getData());
			//System.out.println("Updated Terminal clique : ");
			//System.out.println(terminalCliques +"\n");
			
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

	private static void updateTerminalClique(int vtxId) {
		if(!terminalCliques.isEmpty()) {
			Iterator<Clique> listItr = terminalCliques.iterator();
			while(listItr.hasNext()) {
				Clique clique = listItr.next();
				if(clique.getMember().contains(vtxId)) {
					clique.getMember().remove(vtxId);
					if(clique.getMember().isEmpty()) {
						listItr.remove();						
					}else {
					clique.setDegree(clique.getDegree()-1);
					clique.setSize(clique.getSize()-1);
					}
				}
			}
			
		}
		
	}

	private static void assignColor() { // v [ v/2 + [ v/3) * v/2) ] + v/2 ]
		//assign color from tail node as it is last visited
		Node current = visitedNodeList.getTail();
		Vertex vertex = null;
		Vertex adjacentVtx = null;
		while(current != null){//  O(|V|)
			vertex = nodeProc.getVertex(current.getData(), vertices); // O(|v|/2),              v or 1 or v/2
			if(vertex.getColor() < 0) {
				if(colorsUsed.isEmpty()) {
					vertex.setColor(1);
					colorsUsed.add(1);
				}else {
					List<Integer> adjacentColor = new ArrayList<Integer>();
					for(Integer vtxId : vertex.getAdjacentList()) {	// O(|v|/3)	- average case,                log v(base 2)			
						adjacentVtx = nodeProc.getVertex(vtxId, vertices); //O(|v|/2)                       v or 1 or v/2
						if(adjacentVtx.getColor() > 0) {
							adjacentColor.add(adjacentVtx.getColor());
						}					
					}
					//check is there colors used which are not used for current node's adjacent vertices
					Optional<Integer> filterOption = colorsUsed.stream().filter(color ->
					!adjacentColor.contains(color)).findAny();  // v/2 - avg case                         log v(base 2)
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

	private static void visitSmallestDegreeVertex() {
		//v/4 + [ v/4 * log(v/4) ] + v/2 +  v/3 * V/2
		
		Entry<Integer, DoublyLinkedList> smallestDegreeEntry = sameDegreeVertices.entrySet().iterator().next();
		//System.out.println("Visiting vertex with degree "+smallestDegreeEntry.getKey());
		
		//checkTerminalCliqueExists(smallestDegreeEntry); //  v/4 + v/4 * log(v/4)
				
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
		//Adding the visiting node in the visited node list
		visitedNodeList.addNode(nodeToVisit.getData(), smallestDegreeEntry.getKey());
		Vertex vertex = nodeProc.getVertex(nodeToVisit.getData(), vertices); // Average case O(|v|/2)          V or 1 or V/2
		vertex.setVisited(true);
		vertex.setDeletedOrderListPtr(visitedNodeList.getHead());
		if(vertex.getUpdatedDegree() == null) {
			vertex.setUpdatedDegree(smallestDegreeEntry.getKey());
		}
		updateAdjacentVertex(vertex); //  v/3 * V/2
		
	}
 
	private static boolean checkTerminalCliqueExists(Entry<Integer, DoublyLinkedList> smallestDegreeEntry) {// v/4 + v/4 * log(v/4)
		
		boolean cliqueAdded = false;
		DoublyLinkedList vtxList = smallestDegreeEntry.getValue();
		int degree = smallestDegreeEntry.getKey();
		Set<Integer> nodeIds = new HashSet<Integer>();
		
		Node current = vtxList.getHead();
		while(current != null) {// avg case - v/4
			nodeIds.add(current.getData());
			current = current.getNext();
		}
		
		// for complete graph, no.of vertices in sameDegreelist should be greater than degree
		if(nodeIds.size() >= (degree + 1) ) {			
			boolean isCompleteGraph = cliqueProc.isCompleteGraph(nodeIds, vertices); // v/4 * log(v/4)
			if(isCompleteGraph) {
				cliqueAdded = addToClique(nodeIds); // 2
			}			
		}
		return cliqueAdded;
		
	}

	private static boolean addToClique(Set<Integer> nodeIds) {// 2
		boolean cliqueExists = cliqueProc.checkCliqueExists(nodeIds, terminalCliques);// 2
		if(!cliqueExists) {
			Clique clique = new Clique(nodeIds.size()-1, nodeIds);
			terminalCliques.add(clique);
			return true;
		}
		return false;
	}

	private static void updateAdjacentVertex(Vertex vertex) { //  v/3 * V/2
		for(Integer adjacentvtxId : vertex.getAdjacentList()) {    //  worst case- O(v-1), best case O(1), Average case O(v/3)                  log V
			Vertex adjacentVertex  = nodeProc.getVertex(adjacentvtxId, vertices);  // O(|v|/2)                                         V or 1 or V/2
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
				//Remove adjacent vertex from previous degree list
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
		for(Vertex vertex : vertices) { // // O(|V|)
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
