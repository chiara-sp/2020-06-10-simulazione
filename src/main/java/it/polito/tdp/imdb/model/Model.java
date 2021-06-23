package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	ImdbDAO dao;
	Map<Integer,Actor> idMapActors;
	SimpleWeightedGraph<Actor,DefaultWeightedEdge> grafo;
	 
    Simulatore sim;
	
	public Model() {
		dao= new ImdbDAO();
		idMapActors= new HashMap<>();
		dao.listAllActors(idMapActors);
		sim= new Simulatore();
	}
	
	public List<String> getAllGenres(){
		return dao.getAllGenres();
	}
	public void creaGrafo(String genre) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta vertici
		Graphs.addAllVertices(grafo, dao.getVertici(idMapActors, genre));
		
		//aggiunta archi
		for(Adiacenza a: dao.getAdiacenze(idMapActors, genre)) {
			Graphs.addEdge(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	public int numVertici() {
		return grafo.vertexSet().size();
	}
	public int numArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Actor> visitaAmpiezza(Actor source) {
		List<Actor> visita = new ArrayList<>();
		
		GraphIterator<Actor, DefaultWeightedEdge> bfv = new BreadthFirstIterator<>(this.grafo, source);
		while(bfv.hasNext()) {
			visita.add( bfv.next() ) ;
		}
		
		Collections.sort(visita);
		return visita ;
		
	}
	public List<Actor> getVertici(){
		if(grafo== null)
			return null;
		List<Actor> lista = new LinkedList<>(grafo.vertexSet());
		Collections.sort(lista);
		return lista;
	}
	public boolean grafoCreato() {
		if (this.grafo!=null)
			return true;
		return false;
	}
	public void simula(int giorni) {
		if(grafo!=null) {
			sim.init(grafo, giorni);
			sim.run();
		}
	}
	public List<Actor> intervistati(){

		List<Actor> lista = new LinkedList<>(sim.intervistati.values());
		return lista;
	}
	public int numPause() {
		return sim.pause;
	}
	
}
