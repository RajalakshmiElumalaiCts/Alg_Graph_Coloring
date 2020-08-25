package part2.skewed;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.util.CollectionUtils;

import part2.model.Edge;
import part2.model.GraphVertex;

public class SkewedGraph {
	
	static String SKEWED = "skewed";
	//vertices added in ascending order like vtx1, vtx2, vtx3 etc
	//static Set<GraphVertex> vertices = new TreeSet<GraphVertex>();
	
	static List<GraphVertex> vertices = new ArrayList<GraphVertex>();
	
	static List<Edge> skewedEdgesList = new ArrayList<Edge>();
	
	static Edge edgeToRemove;	
	
	static ExecutorService executorService = Executors.newFixedThreadPool(10); 
	
	
	 static Runnable threadTask = () -> {
		 try { skewedEdgesList.remove(edgeToRemove);
		 }catch (Exception e) { 
			 System.err.println("task interrupted"); 
			 } 
		};
	

	public static void main(String[] args) {
		Instant startTime = Instant.now();
		int noOfvertices;
		String graphType;
		int skewedEdgeCount;
		
		if(args != null && args.length == 3) {
			noOfvertices = Integer.parseInt(args[0]);
			skewedEdgeCount = Integer.parseInt(args[1]);
			graphType = args[2];
			
			
			if(SKEWED.equalsIgnoreCase(graphType)) {
				
				System.out.println("Graph Type : Skewed");
				System.out.println("Number of edges from input : "+ skewedEdgeCount);
				
				if(noOfvertices > 0) {					
					PrintWriter pw;
					try {
						pw = new PrintWriter(new FileWriter("C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\skewed_graph_1002.txt"));
						
						//writing total no.of vertices into file
						pw.write(String.valueOf(noOfvertices));
						pw.println();
						
						createVertex(noOfvertices);
						List<Edge> possibleEdges = createAllPossibleEdges(noOfvertices);
						List<Edge> skewedEdges = createSkewedEdges(possibleEdges, noOfvertices);
						
						skewedEdgesList.addAll(skewedEdges);
						Collections.shuffle(skewedEdgesList);
						
						addAdjacentVertex(skewedEdgeCount);
						
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
		executorService.shutdown();
		Instant endTime = Instant.now();
		long executionTime = Duration.between(startTime, endTime).toMillis();
		System.out.println("\nExecution Time in Milli Seconds ---->  "+executionTime);
		}
	
	private static void createVertex(int noOfvertices) {
		for(int vtxId = 1; vtxId <= noOfvertices ; vtxId++) {	
			GraphVertex vertex = new GraphVertex(vtxId);
			vertices.add(vertex);
		}
	}
	
	private static void addAdjacentVertex(int skewedEdgeCount) {
		Random random = new Random();
		System.out.println("skewedEdgesList-->"+skewedEdgesList.size());
		for(int count=1; count <=skewedEdgeCount; count++) {
			int randomEdgeIndex = random.nextInt(skewedEdgeCount-(count-1)) + 1;
			edgeToRemove = skewedEdgesList.get(randomEdgeIndex);
			//Thread thread = new Thread(threadTask);
			//thread.start();
			//once picked, remove the edge from random list,so that duplication will get reduced
			executorService.execute(threadTask);
			
			//if edge 23 is chosen, vertex 3 is added to vertex 2 as an adjacent vertex
			GraphVertex fromVertex = getVertex(edgeToRemove.getFromVertex());
			//to avoid duplication adjacent list created as set
			Set<Integer> adjacentList = fromVertex.getAdjacentList();
			if(adjacentList == null) {
				adjacentList = new HashSet<Integer>();
				fromVertex.setAdjacentList(adjacentList);
			}			
			boolean isEdgeAdded = adjacentList.add(edgeToRemove.getToVertex());
			
			if(isEdgeAdded){
				//'tovertex' adjacent list is updated, if and only if 'fromvertex' adjacent list is modified
				GraphVertex toVertex = getVertex(edgeToRemove.getToVertex());
				Set<Integer> adjList = toVertex.getAdjacentList();
				if(adjList == null) {
					adjList = new HashSet<Integer>();
					toVertex.setAdjacentList(adjList);
				}
				adjList.add(edgeToRemove.getFromVertex());	
			}
			/*
			 * try { thread.join(); } catch (InterruptedException e) { e.printStackTrace();
			 * }
			 */
		}
		
		
		
		checkForAdjacentVtx();
	}

	//if any of the vertex not having adjacent vertex add it
	private static void checkForAdjacentVtx() {
		
		List<GraphVertex> emptyVtx = getEmptyAdjVertices();
		
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
					vertex2 = getVertexNotWithId(vertex1.getId()); 
					//vertices.stream().filter(vtx -> vtx.getId() != vertex1.getId()).findAny().get();
				}
				
				vertex1.getAdjacentList().add(vertex2.getId());
				vertex2.getAdjacentList().add(vertex1.getId());
			}
		}
	}

	private static GraphVertex getVertexNotWithId(int id) {
		GraphVertex vtxToRtn = null;
		for(GraphVertex vtx : vertices) {
			if(id != vtx.getId()) {
				vtxToRtn = vtx;
				break;
			}
		}
		return vtxToRtn;
	}

	private static List<GraphVertex> getEmptyAdjVertices() {
		List<GraphVertex> emptyVtx = new ArrayList<GraphVertex>();
		//vertices.stream().filter(vtx -> CollectionUtils.isEmpty(vtx.getAdjacentList()))
		//.collect(Collectors.toList());
		for(GraphVertex vtx : vertices) {
			if(CollectionUtils.isEmpty(vtx.getAdjacentList())) {
				emptyVtx.add(vtx);
			}
		}
		return emptyVtx;
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
	
	private static List<Edge> createSkewedEdges(List<Edge> possibleEdges, int noOfvertices) {
		List<Edge> skewedEdges = new ArrayList<Edge>();
		List<Edge> subList;
		int skewedCount = 1;
		int fromIndex = 0;
		int skewedItrCount = 0;
		for(int index = 0; index < possibleEdges.size(); index = index + (noOfvertices-skewedCount)) {
			int toIndex = 0;
			if(fromIndex +(noOfvertices-skewedCount) >  possibleEdges.size()) {
				toIndex = possibleEdges.size()-1;
			}else {
				toIndex = fromIndex +(noOfvertices-skewedCount);
			}
			
			subList = possibleEdges.subList(fromIndex, toIndex);
			skewedItrCount = noOfvertices-skewedCount;
			if(skewedItrCount > Math.floorDiv(noOfvertices, 4)) {
				skewedItrCount = Math.floorDiv(noOfvertices, 4);				
			}
			skewedEdges.addAll(addSkewedEdges(subList, skewedItrCount));
			skewedCount++;
			fromIndex = toIndex;
			if(fromIndex == possibleEdges.size()) {
				break;
			}
						
		}		
		return skewedEdges;
	}

	
	
	private static List<Edge> addSkewedEdges(List<Edge> subList, int skewedCount) {
		//int limit = 0;
		//adding duplicated edges for skewed graph
		//if A,B,C are vertices, it will add edges AB,AB,AC,AC,BC
		List<Edge> skewedEdges = new ArrayList<Edge>();
		for(int index = 0; index < subList.size(); index++) {
			for(int count = 1; count <= skewedCount ;count++) {
				skewedEdges.add(subList.get(index));
			}			
		}
		return skewedEdges;
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
		GraphVertex vertex1 = getVertex(1);//vertices.stream().filter(vtx -> vtx.getId()== 1).findAny().get();
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
		GraphVertex vtxToRtn = null;
		for(GraphVertex vtx : vertices) {
			if(vtxId == vtx.getId()) {
				vtxToRtn = vtx;
				break;
			}
		}
		return vtxToRtn;
	}

}
