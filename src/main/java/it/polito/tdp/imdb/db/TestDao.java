package it.polito.tdp.imdb.db;

import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;

public class TestDao {

	public static void main(String[] args) {
		TestDao testDao = new TestDao();
		testDao.run();
	}
	
	public void run() {
		ImdbDAO dao = new ImdbDAO();
		Map<Integer,Actor> map= new HashMap<>();
		dao.listAllActors(map);
		System.out.println(dao.getVertici(map, "Horror"));
	}

}
