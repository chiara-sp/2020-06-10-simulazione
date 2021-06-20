package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer,Actor> idMapActors){
		String sql = "SELECT * FROM actors";
		//List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				if(!idMapActors.containsKey(actor.getId()))
				idMapActors.put(actor.getId(), actor);
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllGenres(){
		String sql="select distinct genre "
				+ "from movies_genres "
				+ "order by genre";
		List<String> result = new LinkedList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Actor> getVertici(Map<Integer,Actor> idMapActors, String genre){
		String sql="select distinct r.`actor_id` "
				+ "from roles r, movies_genres m "
				+ "where r.`movie_id`=m.`movie_id` and genre=?";
		List<Actor> result= new LinkedList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			ResultSet res = st.executeQuery();
			while (res.next()) {
             Actor a =idMapActors.get(res.getInt("actor_id"));
				if(a!=null) {
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public List<Adiacenza> getAdiacenze(Map<Integer,Actor> idMapActors, String genre){
		String sql="select r1.`actor_id` as a1, r2.`actor_id` as a2, count(distinct r1.`movie_id`) as peso "
				+ "from roles r1, roles r2, movies_genres mg "
				+ "where r1.`actor_id`<r2.`actor_id` and mg.`genre`=? and r1.`movie_id`=r2.`movie_id` and mg.`movie_id`=r1.`movie_id` "
				+ "group by a1, a2";
		
		List<Adiacenza> result= new LinkedList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			ResultSet res = st.executeQuery();
			while (res.next()) {
             Actor a1 =idMapActors.get(res.getInt("a1"));
             Actor a2= idMapActors.get(res.getInt("a2"));
             
				if(a1!= null && a2!=null) {
					result.add(new Adiacenza(a1,a2,res.getInt("peso")));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
