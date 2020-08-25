package part1.greedy;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.DoublyLinkedList;
import model.Vertex;
import util.FileProcessor;
import util.NodeProcessor;

public class GreedyAlg {
	
	static DoublyLinkedList visitedNodeList = new DoublyLinkedList();
	static List<Vertex> vertices;
	static List<Integer> colorsUsed = new ArrayList<Integer>();
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\input.txt";
	static NodeProcessor nodeProc = new NodeProcessor();
	
		//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\complete_graph.txt";
		//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\cycle_graph.txt";
		//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\uniform_graph.txt";
		static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\skewed_graph_1002.txt";

	public static void main(String[] args) {
		Instant startTime = Instant.now();

		FileProcessor fileProc = new FileProcessor();
		
		//read input file
		List<Integer> fileContent = fileProc.readInput(fileName);// n	
		
		int vertexCount = fileContent.get(0);
		vertices = nodeProc.createVertex(vertexCount);//v
		
		List<Integer> vtxAdjNodePos = nodeProc.extractVerticesPosition(fileContent, vertexCount); // v
		nodeProc.createAdjacentList(fileContent, vtxAdjNodePos, vertices);// v
		
		//nodeProc.printInput(vertices);
		
		//processing vertices in the order how they appear in the input file
		assignColor(); // v [ v/2 log v]
		
		//printing vertex in visited order
		//nodeProc.printOutput(visitedNodeList, vertices, colorsUsed.size());	
		System.out.println("Total No.of colors used : " + colorsUsed.size());
		
		Instant endTime = Instant.now();
		long executionTime = Duration.between(startTime, endTime).toMillis();
		System.out.println("\nExecution Time in Milli Seconds : Greedy Order ---->  "+executionTime);
		
	}

	private static void assignColor() { // v [ v/2 log v]
		// assign color 1 to first vertex in the list
		Vertex vertex1 = vertices.get(0);
		vertex1.setColor(1);
		vertex1.setVisited(true);
		colorsUsed.add(1);

		visitedNodeList.addNode(vertex1.getId());

		// Assign color to remaining vertices
		int newColor;
		for (int index = 1; index < vertices.size(); index++) {// v
			Vertex vertex = vertices.get(index);
			List<Integer> adjacentColors = new ArrayList<Integer>();

			// get colors used by adjacent nodes
			for (Integer adjacentVtxId : vertex.getAdjacentList()) { // avg case v/3    log v
				Vertex adjacentVtx = nodeProc.getVertex(adjacentVtxId, vertices);// v/2
				if (adjacentVtx.getColor() > 0) {
					adjacentColors.add(adjacentVtx.getColor());
				}
			}
			// filter out adjacent vertices colors from used colors & get smallest color
			// from the remaining colors
			Optional<Integer> smallestColorOpt = colorsUsed.stream().filter(color -> !adjacentColors.contains(color)) 
					.sorted().findFirst(); // Avg case - c/2

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

}
