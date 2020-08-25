package part1.welshPowell;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import model.DoublyLinkedList;
import model.Vertex;
import util.FileProcessor;
import util.NodeProcessor;

public class WelshPowellAlg {

	static DoublyLinkedList visitedNodeList = new DoublyLinkedList();
	static List<Vertex> vertices;
	static Set<Integer> colorsUsed = new HashSet<Integer>();
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\input.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\complete_graph.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\cycle_graph.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\uniform_graph.txt";
	static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\skewed_graph_1001.txt";
	static NodeProcessor nodeProc = new NodeProcessor();
	
	
	public static void main(String[] args) {
		Instant startTime = Instant.now();
		
		FileProcessor fileProc = new FileProcessor();
		List<Vertex> colorProcessList = new ArrayList<Vertex>();
		
		//read input file
		List<Integer> fileContent = fileProc.readInput(fileName); 	// n - no.of line in input file
		
		int vertexCount = fileContent.get(0);
		vertices = nodeProc.createVertex(vertexCount); // v - no.of vertices
		
		List<Integer> vertextPosition = nodeProc.extractVerticesPosition(fileContent, vertexCount); // v - no.of vertices
		nodeProc.createAdjacentList(fileContent, vertextPosition, vertices); // v - no.of vertices
		
		//nodeProc.printInput(vertices); //ignore sysouts
		
		colorProcessList.addAll(vertices);
		Collections.sort(colorProcessList);// v log v -> uses merge sort
		
		while(colorProcessList.size() != 0) {// v/2
			
			assignColor(colorProcessList);   // 2 (v/3) + (v^3/12)      v/3 + V/2(log V) + log V
		}
		colorProcessList = null;
		
		System.out.println("Total No.of colors used : " + colorsUsed.size());
		//printing vertex visit output
		//nodeProc.printOutput(visitedNodeList, vertices, colorsUsed.size());	// ignore sysout	
		
		Instant endTime = Instant.now();
		long executionTime = Duration.between(startTime, endTime).toMillis();
		System.out.println("\nExecution Time in Milli Seconds : Welsh Powell Order ---->  "+executionTime);
		
		
	}

	

	private static void assignColor(List<Vertex> colorProcessList) { //  v/3 + (log V * V/2) + log V
		List<Vertex> coloredVertices = new ArrayList<Vertex>();
		int newColor = colorsUsed.size()+1;
		List<Integer> adjacentColor = null;

		//assigning new color to the vertex which is at the top of the list
		Vertex visitingVertex = colorProcessList.get(0);
		visitingVertex.setColor(newColor);
		visitingVertex.setVisited(true);
		colorsUsed.add(newColor);
		coloredVertices.add(visitingVertex);

		// filtering vertices which are not adjacent to current visiting vertex
		List<Vertex> nonAdjacentVertices = colorProcessList.stream().filter(
				vtx -> !visitingVertex.getAdjacentList().contains(vtx.getId()) && vtx.getId() != visitingVertex.getId())
				.collect(Collectors.toList()); // - v
		
		if (!nonAdjacentVertices.isEmpty()) {
			for (Vertex nonAdjacentVtx : nonAdjacentVertices) {	//  v/2 avg case
				adjacentColor = new ArrayList<Integer>();
				// getting adjacent vertices for vertex which are not adjacent to current
				// visiting vertex
				for (int vtxId : nonAdjacentVtx.getAdjacentList()) { // log V - avg case
					Vertex nonAdjacentsAdjVtx = nodeProc.getVertex(vtxId, vertices); // v/2 - avg case
					if(nonAdjacentsAdjVtx.getColor() > 0) {
						adjacentColor.add(nonAdjacentsAdjVtx.getColor());
					}
				}
				 /* If the adjacent vertices of non-adjacent vertex not colored with the
				 * current/new color, assign the new color to the non-adjacent vertex
				 */
				if (!adjacentColor.contains(newColor)) {
					nonAdjacentVtx.setColor(newColor);
					nonAdjacentVtx.setVisited(true);
					coloredVertices.add(nonAdjacentVtx);
				}
			}
		}

		//Remove colored vertices from 'colorProcessList'		
		colorProcessList.removeAll(coloredVertices);//v^2
		//Add colored vertices to visited node list
		for(Vertex colorVtx : coloredVertices) { //  log V
			visitedNodeList.addNode(colorVtx.getId());
		}
		
	}

}
