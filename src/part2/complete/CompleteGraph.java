package part2.complete;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CompleteGraph {

	static String COMPLETE = "Complete";	
	
	public static void main(String[] args) {
		int noOfvertices, noOfEdges;
		String graphType;
		
		if(args != null && args.length == 3) {
			noOfvertices = Integer.parseInt(args[0]);
			graphType = args[2];
			
			if(COMPLETE.equalsIgnoreCase(graphType)) {
				noOfEdges = Math.floorDiv(Math.multiplyExact(noOfvertices, (noOfvertices-1)), 2);
				System.out.println("Graph Type : Complete");
				System.out.println("Number of edges by formula [V(V-1)/2] : "+noOfEdges);
				
				if(noOfvertices > 0) {
					
					PrintWriter pw;
					try {
						pw = new PrintWriter(new FileWriter("C:\\Users\\laksh\\git\\Alg_Graph_Coloring\\complete_graph.txt"));
						
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
			for(int adjVtxId = 1; adjVtxId <= noOfvertices ; adjVtxId++) {
				if(adjVtxId != vtxId) {
					edgeDuplicateCount++;
					pw.write(String.valueOf(adjVtxId));
					pw.println();
				}
			}			
		}
		int edgeCount = edgeDuplicateCount/2;
		return edgeCount;
	}

	private static void writeVertexPosition(PrintWriter pw, int noOfvertices) {
		int adjVertices = noOfvertices-1;
		int verOneStartPos = 0 + noOfvertices + 1;
		pw.write(String.valueOf(verOneStartPos));
		pw.println();
		int prevVtxPos = verOneStartPos;
		
		for(int index = 1; index < noOfvertices ; index++) {
			int vtxPos = prevVtxPos + adjVertices;
			pw.write(String.valueOf(vtxPos));
			pw.println();
			prevVtxPos = vtxPos;
		}
		
	}			
}

