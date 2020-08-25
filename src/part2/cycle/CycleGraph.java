package part2.cycle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CycleGraph {

	static String CYCLE = "Cycle";
	
	public static void main(String[] args) {
		int noOfvertices, noOfEdges;
		String graphType;
		
		if(args != null && args.length == 3) {
			noOfvertices = Integer.parseInt(args[0]);
			graphType = args[2];
			
			if(CYCLE.equalsIgnoreCase(graphType)) {
				System.out.println("Graph Type : Cycle");
				noOfEdges = noOfvertices;
				System.out.println("Number of edges by formula E = V : "+ noOfEdges);
				
				if(noOfvertices > 0) {					
					PrintWriter pw;
					try {
						pw = new PrintWriter(new FileWriter("C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\cycle_graph.txt"));
						//writing total no.of vertices into file
						pw.write(String.valueOf(noOfvertices));
						pw.println();
						
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
	
	private static int writeAdjacentVertices(PrintWriter pw, int noOfvertices) {
		int edgeDuplicateCount = 0;
		for(int vtxId = 1; vtxId <= noOfvertices ; vtxId++) {			
			//for each vertex, writing adjacent vertices IDs
			int adjacent1Id = vtxId-1;
			
			//vertex 1 has last vertex has its adjacent vertex in cycle graph
			if(adjacent1Id == 0) {
				adjacent1Id = noOfvertices;
			}
			
			int adjacent2Id = vtxId+1;
			//last vertex has vertex 1 has its adjacent
			if(adjacent2Id > noOfvertices) {
				adjacent2Id = 1;
			}
			
			pw.write(String.valueOf(adjacent1Id));
			pw.println();
			edgeDuplicateCount++;
			
			pw.write(String.valueOf(adjacent2Id));
			pw.println();
			edgeDuplicateCount++;
			
		}
		int edgeCount = edgeDuplicateCount/2;
		return edgeCount;
	}

	private static void writeVertexPosition(PrintWriter pw, int noOfvertices) {
		//each vertex has 2 adjacent vertices
		int adjVertices = 2;
		int verOneStartPos = 0 + noOfvertices + 1;
		
		//writing vertex1's start position
		pw.write(String.valueOf(verOneStartPos));
		pw.println();
		int prevVtxPos = verOneStartPos;
		
		//writing remaining vertices start position
		for(int index = 1; index < noOfvertices ; index++) {
			int vtxPos = prevVtxPos + adjVertices;
			pw.write(String.valueOf(vtxPos));
			pw.println();
			prevVtxPos = vtxPos;
		}
		
	}

}
