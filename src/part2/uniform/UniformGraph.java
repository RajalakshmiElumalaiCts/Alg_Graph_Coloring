package part2.uniform;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import part2.model.Edge;
import part2.model.GraphVertex;

public class UniformGraph {

	static String UNIFORM = "uniform";
	//vertices added in ascending order like vtx1, vtx2, vtx3 etc
	static Set<GraphVertex> vertices = new TreeSet<GraphVertex>();
	
	public static void main(String[] args) {
		int noOfvertices;
		String graphType;
		int uniformEdgeCount;
		
		if(args != null && args.length == 3) {
			noOfvertices = Integer.parseInt(args[0]);
			uniformEdgeCount = Integer.parseInt(args[1]);
			graphType = args[2];
			
			if(UNIFORM.equalsIgnoreCase(graphType)) {
				/*
				 * int completeGraphEdges = Math.floorDiv(Math.multiplyExact(noOfvertices,
				 * (noOfvertices-1)), 2); Random random = new Random(); //generating random
				 * number of edges int uniformEdgeCount = random.nextInt(completeGraphEdges -
				 * (completeGraphEdges/2) + 1) + (completeGraphEdges/6);
				 */
				System.out.println("Graph Type : Uniform");
				System.out.println("Number of edges expected to generate : "+ uniformEdgeCount);
				
				if(noOfvertices > 0) {					
					PrintWriter pw;
					try {
						
						String path = "C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\uniform_graph.txt";
						pw = new PrintWriter(new FileWriter(path));
						
						//writing total no.of vertices into file
						pw.write(String.valueOf(noOfvertices));
						pw.println();
						
						createVertex(noOfvertices);
						List<Edge> possibleEdges = createAllPossibleEdges(noOfvertices);
						Collections.shuffle(possibleEdges);
						
						addAdjacentVertex(uniformEdgeCount, possibleEdges);
						
						//writing starting position for each vertex into file
						writeVertexPosition(pw, noOfvertices);
						
						//writing adjacent vertices for each vertex into file
						int edgeCount = writeAdjacentVertices(pw, noOfvertices);
						System.out.println("Number of edges generated for graph : "+edgeCount);
					pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}					 
					
					}		
				}				
			}
		}
	
	private static void createVertex(int noOfvertices) {
		for(int vtxId = 1; vtxId <= noOfvertices ; vtxId++) {	
			GraphVertex vertex = new GraphVertex(vtxId);
			vertices.add(vertex);
		}
	}
	
	private static void addAdjacentVertex(int uniformEdgeCount, List<Edge> possibleEdges) {
		Random random = new Random();
		for(int count=1; count <=uniformEdgeCount; count++) {
			int randomEdgeIndex = random.nextInt(uniformEdgeCount-(count-1)) + 1;
			Edge edge = possibleEdges.get(randomEdgeIndex);
			
			//if edge 23 is chosen, vertex 3 is added to vertex 2 as an adjacent edge
			GraphVertex fromVertex = vertices.stream().filter(vtx -> vtx.getId()== edge.getFromVertex()).findAny().get();
			Set<Integer> adjacentList = fromVertex.getAdjacentList();
			if(adjacentList == null) {
				adjacentList = new HashSet<Integer>();
				fromVertex.setAdjacentList(adjacentList);
			}
			adjacentList.add(edge.getToVertex());
			
			GraphVertex toVertex = vertices.stream().filter(vtx -> vtx.getId()== edge.getToVertex()).findAny().get();
			Set<Integer> adjList = toVertex.getAdjacentList();
			if(adjList == null) {
				adjList = new HashSet<Integer>();
				toVertex.setAdjacentList(adjList);
			}
			adjList.add(edge.getFromVertex());
			
			//once added remove the edge from random list,so that duplication is avoided
			possibleEdges.remove(edge);
		}
		checkForAdjacentVtx();
		//System.out.println("vertices after adding adjacent vertex :" + vertices);
	}

	//if any of the vertex not having adjacent vertex add it
	private static void checkForAdjacentVtx() {
		List<GraphVertex> emptyVtx = vertices.stream().filter(vtx -> CollectionUtils.isEmpty(vtx.getAdjacentList()))
				.collect(Collectors.toList());
		if(!emptyVtx.isEmpty()) {
			for(int index=0; index < emptyVtx.size(); index++) {
				GraphVertex vertex1 = emptyVtx.get(index);
				vertex1.setAdjacentList(new HashSet<Integer>());
				GraphVertex vertex2;
				if(index+1 < emptyVtx.size()) {
					vertex2 = emptyVtx.get(index+1);
					vertex2.setAdjacentList(new HashSet<Integer>());
				}else {
					//randomly choose a vertex
					vertex2 = vertices.stream().filter(vtx -> vtx.getId() != vertex1.getId()).findAny().get();
				}
				
				vertex1.getAdjacentList().add(vertex2.getId());
				vertex2.getAdjacentList().add(vertex1.getId());
			}
		}
	}

	private static List<Edge> createAllPossibleEdges(int noOfvertices) {
		List<Edge> possibleEdges = new ArrayList<Edge>();
		for(int id = 1; id <= noOfvertices; id++) {
			for(int adjId= id+1; adjId <= noOfvertices; adjId++) {
				Edge edge = new Edge(id, adjId);
				possibleEdges.add(edge);
			}
		}		 
		return possibleEdges;
	}

	private static int writeAdjacentVertices(PrintWriter pw, int noOfvertices) {
		int edgeDuplicateCount = 0;
		
		Iterator<GraphVertex> itr = vertices.iterator();		
		while(itr.hasNext()) {
			GraphVertex vertex = itr.next();
			//for each vertex, writing adjacent vertices IDs
			Iterator<Integer> adjItr = vertex.getAdjacentList().iterator();
			while(adjItr.hasNext()) {
					edgeDuplicateCount++;
					pw.write(String.valueOf(adjItr.next()));
					pw.println();
			}
		}
		int edgeCount = edgeDuplicateCount/2;
		return edgeCount;
	}

	private static void writeVertexPosition(PrintWriter pw, int noOfvertices) {
		
		GraphVertex vertex1 = vertices.stream().filter(vtx -> vtx.getId()== 1).findAny().get();
		int vtxOneStartPos = 0 + noOfvertices + 1;
		pw.write(String.valueOf(vtxOneStartPos));
		pw.println();
		
		int prevVtxPos = vtxOneStartPos;
		int prevVtxAdjCount = vertex1.getAdjacentList().size();
		
		for (int vtxId = 2; vtxId <= vertices.size(); vtxId++) {
			GraphVertex vertex = getVertex(vtxId);
			int vtxPos = prevVtxPos + prevVtxAdjCount;
			pw.write(String.valueOf(vtxPos));
			pw.println();
			prevVtxPos = vtxPos;
			prevVtxAdjCount = vertex.getAdjacentList().size();
		}
		
	}

	private static GraphVertex getVertex(int vtxId) {		
		return vertices.stream().filter(vtx -> vtx.getId()== vtxId).findAny().get();
	}		

}
