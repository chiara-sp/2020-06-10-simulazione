package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Simulatore {

	
	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	
	//private PriorityQueue<Actor> queue;
	
	private int giorni;
	
	Map<Integer,Actor> intervistati;
	int pause;
	List<Actor> attoriDisponibili;
	
	public void init(SimpleWeightedGraph<Actor,DefaultWeightedEdge> grafo, int giorni) {
		this.grafo=grafo;
		this.giorni=giorni;
		
		this.attoriDisponibili= new LinkedList<>(this.grafo.vertexSet());
		
		//imposto lo stato iniziale
		pause=0;
		intervistati= new HashMap<>();
		Actor a=this.getMathRandomList(attoriDisponibili);
		intervistati.put(1,a);
		attoriDisponibili.remove(a);
		
	
		
	}
	public void run() {
		
		for(int i=2; i<=giorni; i++) {
			
			double num= Math.random();
			if(num<=0.6) {
				Actor a= getMathRandomList(attoriDisponibili);
				intervistati.put(i, a);
				this.attoriDisponibili.remove(a);
			}
			else {
				Actor last= intervistati.get(intervistati.size());
				Actor recommended= getRecommended(last);
				if(recommended==null) {
					Actor a= getMathRandomList(attoriDisponibili);
					intervistati.put(i, a);
					this.attoriDisponibili.remove(a);
				}else {
					Actor a= recommended;
					intervistati.put(i, a);
					this.attoriDisponibili.remove(a);
				}
			}
		}
	}
	
	private Actor getRecommended(Actor lastInterviewed) {
		Actor recommended = null;
		int weight = 0;
		/*if(!grafo.vertexSet().contains(lastInterviewed)) {
			System.out.println("errore");
			return null;
		}*/
			
		for(Actor neighbor : Graphs.neighborListOf(this.grafo, lastInterviewed)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(lastInterviewed, neighbor)) > weight) {
				recommended = neighbor;
				weight = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(lastInterviewed, neighbor));
			}
		}
		
		return recommended;
	}
	public Actor getMathRandomList(List<Actor> list) {
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }
}
