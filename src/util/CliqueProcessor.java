package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.Clique;
import model.Vertex;

public class CliqueProcessor {
	NodeProcessor nodeProc = new NodeProcessor();

	public boolean isCompleteGraph(Set<Integer> commonVetices, List<Vertex> vertices){// v/4 * log(v/4)
			List<Integer> adjVtxList = new ArrayList<Integer>(commonVetices); 
			int vertexId;
			Vertex vertex;
			boolean isCompleteGraph = true;
			for(int index = 0; index < adjVtxList.size(); index++) {// v/4 - in worst case. has to iterate whole list
				vertexId = adjVtxList.get(index);
				//checks whether the current vertex is adjacent to all other vertices
				for(int nxtIndex = index+1; nxtIndex < adjVtxList.size(); nxtIndex++) { // log (v/4)
					vertex = nodeProc.getVertex(adjVtxList.get(nxtIndex), vertices);
					if(vertex.getAdjacentList().contains(vertexId)) {
						continue;
					}else {
						isCompleteGraph = false;
						break;
					}
				}
				if(!isCompleteGraph) {
					break;
				}			
			}
			return isCompleteGraph;
		}
	
	
	public boolean checkCliqueExists(Set<Integer> adjVtx, List<Clique> cliques) { // 2

		int maxCliqueDegree = adjVtx.size()-1;
		/*
		 * System.out.println("adjVtx --->"+adjVtx);
		 * System.out.println("maxCliqueDegree -->"+maxCliqueDegree);		 */
		
		boolean maxCliqueExists = false;
		if(!cliques.isEmpty()) {
			Iterator<Clique> listItr = cliques.iterator();
			
			//checks whether maxClique map is already having current set of vertices as clique with high degree,
			//if exists, it will not add current clique as Max one
			while(listItr.hasNext()) {// Average case 2 iteration. because looking for highest degree clique only. 
				Clique clique = listItr.next();
				/*
				 * System.out.println("entry.getValue() -->"+entry.getValue());
				 * System.out.println("entry.getkey() -->"+entry.getKey());
				 */
				if(clique.getMember().containsAll(adjVtx) && clique.getDegree() >= maxCliqueDegree) {
					//System.out.println("setting maxCliqueExists = true");
					maxCliqueExists = true;
					break;
				}
			}
		}
		return maxCliqueExists;
	}
}
