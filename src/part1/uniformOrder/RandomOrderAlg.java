package part1.uniformOrder;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import model.DoublyLinkedList;
import model.Vertex;
import util.FileProcessor;
import util.NodeProcessor;

public class RandomOrderAlg {
	
	static DoublyLinkedList visitedNodeList = new DoublyLinkedList();
	static List<Vertex> vertices;
	static Set<Integer> colorsUsed = new HashSet<Integer>();
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\input.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\input.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\complete_graph.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\cycle_graph.txt";
	//static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\uniform_graph.txt";
	static final String fileName = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\skewed_graph_1001.txt";
	static NodeProcessor nodeProc = new NodeProcessor();
	

	public static void main(String[] args) {
		Instant startTime = Instant.now();
		
		FileProcessor fileProc = new FileProcessor();
		List<Vertex> randomVertices = new ArrayList<Vertex>();
		
		//read input file
		List<Integer> fileContent = fileProc.readInput(fileName); // n		
		
		int vertexCount = fileContent.get(0);
		vertices = nodeProc.createVertex(vertexCount);// v
		
		List<Integer> vertextPosition = nodeProc.extractVerticesPosition(fileContent, vertexCount);// v
		nodeProc.createAdjacentList(fileContent, vertextPosition, vertices);// v
		
		//nodeProc.printInput(vertices); // ignore sysout
		
		//store vertices in random order
		randomVertices.addAll(vertices);
		Collections.shuffle(randomVertices, new Random());// v
		/*
		 * System.out.println("Random ordered vertices-->"); for(Vertex vtx :
		 * randomVertices) { System.out.println(vtx ); } System.out.println("\n");
		 */
		
		assignColor(randomVertices); // v [ (v^2)/6 + c/2 ]
		
		//printing vertex in visited order
		//nodeProc.printOutput(visitedNodeList, vertices, colorsUsed.size());	// ignore sysout
		System.out.println("Total No.of colors used : " + colorsUsed.size());
		
		Instant endTime = Instant.now();
		long executionTime = Duration.between(startTime, endTime).toMillis();
		System.out.println("\nExecution Time in Milli Seconds : Uniform Order ---->  "+executionTime);
	}


	private static void assignColor(List<Vertex> randomVertices) {// v [ (v^2/6) + c/2 ]
		//assign color 1 to first vertex in the list		
				Vertex vertex1 = randomVertices.get(0);
				vertex1.setColor(1);
				vertex1.setVisited(true);
				colorsUsed.add(1);
				
				visitedNodeList.addNode(vertex1.getId());
				
				//Assign color to remaining vertices
				int newColor;
				for(int index=1; index < randomVertices.size(); index++) {// v
					Vertex vertex = randomVertices.get(index);
					List<Integer> adjacentColors = new ArrayList<Integer>();
					
					//get colors used by adjacent nodes
					for(Integer adjacentVtxId : vertex.getAdjacentList()){// Avg case v/3                        log V
						Vertex adjacentVtx = nodeProc.getVertex(adjacentVtxId, vertices); // v/2               V V/2  1
						if(adjacentVtx.getColor() > 0) {
							adjacentColors.add(adjacentVtx.getColor());
						}
					}
					//filter out adjacent vertices colors from used colors & get smallest color from the remaining colors
					Optional<Integer> smallestColorOpt = colorsUsed.stream().filter(color -> !adjacentColors.contains(color))
							.sorted().findFirst();// Avg case - c/2                 C ,  C/2                
					
					if(smallestColorOpt.isPresent()) { //use existing smallest color
						newColor = smallestColorOpt.get();
					}else { // generate new color
						newColor = colorsUsed.size()+1;
						colorsUsed.add(newColor);
					}
					vertex.setColor(newColor);
					vertex.setVisited(true);
					visitedNodeList.addNode(vertex.getId());
				}
				
				randomVertices = null;		
	}

}
