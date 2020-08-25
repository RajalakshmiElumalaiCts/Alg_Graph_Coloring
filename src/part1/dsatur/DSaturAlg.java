package part1.dsatur;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import model.DoublyLinkedList;
import model.Vertex;
import util.FileProcessor;
import util.NodeProcessor;

public class DSaturAlg {
	
	/**
	 * map of linked list which holds same degree vertices in a list and sorts map by degree of vertex in reverse order
	 */	
	static Map<Integer, LinkedList<Integer>> sameDegreeVertices = new TreeMap<Integer, LinkedList<Integer>>(
			Comparator.reverseOrder());	
	/**
	 * Map which holds maximal cliques in the graph and sorts clique degree in reverse order
	 */
	static Map<Integer, Set<Integer>> maximalCliques = new HashMap<Integer, Set<Integer>>();
	
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
		vertices = nodeProc.createVertex(vertexCount); // v
		
		List<Integer> vtxAdjNodePos = nodeProc.extractVerticesPosition(fileContent, vertexCount); // v
		nodeProc.createAdjacentList(fileContent, vtxAdjNodePos, vertices); //v
		
		//nodeProc.printInput(vertices); // ignore sysout
		createSameDegreeList();	// v
		//System.out.println(sameDegreeVertices);
		Iterator<Entry<Integer, LinkedList<Integer>>> mapItr = sameDegreeVertices.entrySet().iterator();
		Entry<Integer, LinkedList<Integer>> mapEntry;
		// once a maximal / largest clique is find, stop iteration.
		//Running time - considering (v-1)/2 possible degrees.[A graph can have 'v-1' no.of vertices.]
		while(mapItr.hasNext() && maximalCliques.isEmpty()) {  // Avg case- (v-1)/4             log V or v/8 or v/4  
			mapEntry = mapItr.next();
			//System.out.println("Initial passing degree"+mapEntry.getKey());
			findMaximalClique(mapEntry.getKey(), mapEntry.getValue());// [ v/8 * v/2 ] + v/4 + v/8 [v/4 * log v ]
		}
		
		
		//System.out.println("Maximal cliques : "+maximalCliques);
		//System.out.println();
		
		colorMaximalClique(); // v/2 * log v
		
		colorRemainingVertex(); // (v - v/4) [v/4 + v/4] + v               v + 3v/4 [ v/2* logv ]
		 
		//printing vertex in visited order
		//nodeProc.printOutput(visitedNodeList, vertices, colorsUsed.size());	
		
		System.out.println("Total No.of colors used : " + colorsUsed.size());
		
		Instant endTime = Instant.now();
		long executionTime = Duration.between(startTime, endTime).toMillis();
		System.out.println("\nExecution Time in Milli Seconds : DSatur Algorithm ---->  "+executionTime);
	}

	private static void colorRemainingVertex() { // (v - v/4) [v/4 + v/4] + v              v + 3v/4 [ v/2* logv ]
		
		List<Vertex> remainingVertices  = null;
		if(!maximalCliques.isEmpty()) {
			Integer maxCliqueDegree = maximalCliques.keySet().stream().sorted(Comparator.reverseOrder()).findFirst().get();
			Set<Integer> maxCliqueMem = maximalCliques.get(maxCliqueDegree);
			
			//filtering out colored clique members
			remainingVertices = vertices.stream().filter(vtx -> !maxCliqueMem.contains(vtx.getId()))
					.collect(Collectors.toList());// 	v
			assignColor(remainingVertices); //(v - v/4) [v/4 + v/4]                   3v/4 [v/2*logv]
		}else {
			assignColor(vertices); //(v - v/4) [v/4 + v/4]                   3v/4 [v/2*logv]
		}
		
		
		
	}

	private static void assignColor(List<Vertex> remainingVertices) { // (v - v/4) [v/4 + v/4]       3v/4 [v/2*logv]
		int newColor;
		
		for(Vertex vertex : remainingVertices) { // v - v/4                                    3v/4
			List<Integer> adjacentColors = new ArrayList<Integer>();

			// get colors used by adjacent nodes
			for (Integer adjacentVtxId : vertex.getAdjacentList()) { // v/4 - avg case                    log v
				Vertex adjacentVtx = nodeProc.getVertex(adjacentVtxId, vertices);                   //v/2               
				if (adjacentVtx.getColor() > 0) {
					adjacentColors.add(adjacentVtx.getColor());
				}
			}
			// filter out adjacent vertices colors from used colors & get smallest color
			// from the remaining colors
			Optional<Integer> smallestColorOpt = colorsUsed.stream().filter(color -> !adjacentColors.contains(color))
					.sorted().findFirst(); // v/4 no.of colors for v/4 vertices

			if (smallestColorOpt.isPresent()) { // use existing smallest color
				newColor = smallestColorOpt.get();
			} else { // generate new color
				newColor = colorsUsed.size() + 1;
				colorsUsed.add(newColor);
			}
			vertex.setColor(newColor);
			vertex.setVisited(true);
			visitedNodeList.addNode(vertex.getId());
		}
	}

	private static void colorMaximalClique() { // v/2 * v/4                    v/2 * log v
		if(!maximalCliques.isEmpty()) {
		Integer maxCliqueDegree = maximalCliques.keySet().stream().sorted(Comparator.reverseOrder()).findFirst().get();
		Set<Integer> maxCliqueMem = maximalCliques.get(maxCliqueDegree);
		Iterator<Integer> setItr = maxCliqueMem.iterator();
		int vertexId;
		Vertex vertex;
		int newColor;
		while(setItr.hasNext()) { // iterate for count of members in the clique. v/2 to v/4. so, v/4                log v
			vertexId = setItr.next();
			vertex = nodeProc.getVertex(vertexId, vertices); // v/2                                                 v/2                                  
			newColor = colorsUsed.size()+1;
			vertex.setColor(newColor);
			vertex.setVisited(true);
			
			colorsUsed.add(newColor);
			visitedNodeList.addNode(vertex.getId());
		}
		}		
	}

	private static void findMaximalClique(Integer degree, LinkedList<Integer> sameDegVtx) { // [ v/8 * v/2 ] + v/4 + v/8 [v/4 * log v ]
		
		Set<Integer> sameOrHighDegVtx = new HashSet<Integer>();
		List<Integer> highDegrees;
		//adding vertices of current degree
		sameOrHighDegVtx.addAll(sameDegVtx);
		
		//getting higher degrees
		highDegrees = getHighDegree(degree); // (v-1)/4                       ignore
		//adding current degree. because if some other vertex with current degree exists, get them too
		highDegrees.add(degree);
		
		//System.out.println("highDegrees -->"+highDegrees);
		if (highDegrees != null && !highDegrees.isEmpty()) {
			for (int highDeg : highDegrees) { // (v-1)/4
				// getting vertices of high degree than the current degree
				sameOrHighDegVtx.addAll(sameDegreeVertices.get(highDeg));
			}
		}
		//System.out.println("Same high order vertices-->"+sameOrHighDegVtx);
		findClique(degree, sameOrHighDegVtx);//  v^2/4  + v/2 + v/4 [log (v/4)]	+ 2              [ v/8 * v/2 ] + v/4 + v/8 [v/4 * log v ]
	}
		
		
	private static void findClique(Integer degree, Set<Integer> sameOrHighDegVtx) { // v^2/4  + v/2 + v/4 [log (v/4)]	+ 2
		// [ v/8 * v/2 ] + v/4 + v/8 [v/4 * log v ]
		if(sameOrHighDegVtx.size()+1 > degree) {
			Set<Integer> adjVtx = new HashSet<Integer>();
			
			for(int vtxId : sameOrHighDegVtx) { // Avg case,v/2                                  v/8
				Vertex vertex = nodeProc.getVertex(vtxId, vertices); //Average case v/2                        v/2
				adjVtx.addAll(vertex.getAdjacentList());
			}
			//System.out.println("adjVtx of high degree vertex"+adjVtx);
			//get intersection of 'sameOrHighDegVtx' and 'adjVtx' to get the common vertices
			adjVtx.retainAll(sameOrHighDegVtx);// retainAll - O(n) [adjVtx size is n]. here it is v/2                    v/4  or   v/8
			
			//System.out.println("common vertices-->"+adjVtx);
			checkAdjacency(adjVtx); // v/4 [log (v/4)]	+ 2               v/8 [v/4 * log v ]
			
		}
		
	}



	private static void checkAdjacency(Set<Integer> commonVetices) {	// v/4 [log (v/4)]	+ 2                        v/8 [v/4 * log v ]
		List<Integer> adjVtxList = new ArrayList<Integer>(commonVetices); 
		int vertexId;
		Vertex vertex;
		boolean cliqueExists = true;
		for(int index = 0; index < adjVtxList.size(); index++) {// v/4 - in worst case. has to iterate whole list               log v  or v/8
			vertexId = adjVtxList.get(index);
			//checks whether the current vertex is adjacent to all other vertices
			for(int nxtIndex = index+1; nxtIndex < adjVtxList.size(); nxtIndex++) { // log (v/4)                         log v
				vertex = nodeProc.getVertex(adjVtxList.get(nxtIndex), vertices);                                    // v/4
				if(vertex.getAdjacentList().contains(vertexId)) {
					continue;
				}else {
					cliqueExists = false;
					break;
				}
			}
			if(!cliqueExists) {
				break;
			}			
		}
		
		/*
		 * System.out.println("cliqueExists--->"+cliqueExists);
		 * System.out.println("final commonVetices--->"+commonVetices);
		 */
		//if clique exists add them into map with corresponding degree
		if(cliqueExists) {
			addToMaxClique(commonVetices); // 2
		}
		
	}

	private static void addToMaxClique(Set<Integer> adjVtx) { // 2

		int maxCliqueDegree = adjVtx.size()-1;
		/*
		 * System.out.println("adjVtx --->"+adjVtx);
		 * System.out.println("maxCliqueDegree -->"+maxCliqueDegree);
		 */
		
		boolean maxCliqueExists = false;
		if(!maximalCliques.isEmpty()) {
			Iterator<Entry<Integer, Set<Integer>>> mapItr = maximalCliques.entrySet().iterator();
			Entry<Integer, Set<Integer>> entry;
			
			//checks whether maxClique map is already having current set of vertices as clique with high degree,
			//if exists, it will not add current clique as Max one
			while(mapItr.hasNext()) {// Average case 2 iteration. because looking for highest degree clique only. 
				entry = mapItr.next();
				/*
				 * System.out.println("entry.getValue() -->"+entry.getValue());
				 * System.out.println("entry.getkey() -->"+entry.getKey());
				 */
				if(entry.getValue().containsAll(adjVtx) && entry.getKey() >= maxCliqueDegree) {
					//System.out.println("setting maxCliqueExists = true");
					maxCliqueExists = true;
					break;
				}
			}
		}
		if(!maxCliqueExists) {
			maximalCliques.put(adjVtx.size()-1, adjVtx);
		}
			
	}

	//Running time - considering (v-1)/2 possible degrees.[A graph can have 'v-1' no.of vertices.]
	private static List<Integer> getHighDegree(int degreeParam) { // Avg case, v-1/4
		List<Integer> highDegree = new LinkedList<Integer>();
			Iterator<Integer> keyItr = sameDegreeVertices.keySet().iterator();
			Integer nextDegree;
			
			while(keyItr.hasNext()) {// Avg case - v-1/4
				nextDegree = keyItr.next();
				if(nextDegree > degreeParam) {
					highDegree.add(nextDegree);
				}else {//breaking while loop if nextDeg is not greater, as the map is sorted in reverse order
					break;
				}				
			}
		return highDegree;
	}

	private static void createSameDegreeList() { // v
		for(Vertex vertex : vertices) { // v
			int degree = vertex.getInitialDegree();
			LinkedList<Integer> linkedList = sameDegreeVertices.get(degree);
			if(linkedList == null) {
				linkedList = new LinkedList<Integer>();
				sameDegreeVertices.put(degree, linkedList);
			}
			linkedList.add(vertex.getId());	
		}
	}

	

}
