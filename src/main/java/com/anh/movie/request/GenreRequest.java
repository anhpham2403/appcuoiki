package com.anh.movie.request;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import com.anh.movie.entities.Genre;
import com.anh.movie.utils.HibernateUtils;


@Path("genre")
public class GenreRequest {
	@GET
	@Path("/genres")
	@Produces("application/json")
	public Response getGenres() {
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Genre> genres = new ArrayList<Genre>();
		try {
			session.getTransaction().begin();
			String sql = "Select genre from " + Genre.class.getName() + " genre ";
			@SuppressWarnings("unchecked")
			Query<Genre> query = session.createQuery(sql);
			genres = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		for (Genre genre : genres) {
			JSONObject object2 = new JSONObject();
			object2.put("id", genre.getId());
			object2.put("name", genre.getName());
			array.put(object2);
		}
		object.put("genres", array);
		return Response.status(200).entity(object.toString()).build();
	}
}
