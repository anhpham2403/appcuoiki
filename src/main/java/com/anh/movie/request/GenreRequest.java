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

import com.anh.movie.utils.HibernateUtils;
import com.anh.movie.entities.Genre;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
		JsonObject object = new JsonObject();
		JsonArray array = new JsonArray();
		for (Genre genre : genres) {
			JsonObject object2 = new JsonObject();
			object2.addProperty("id", genre.getId());
			object2.addProperty("name", genre.getName());
			array.add(object2);
		}
		object.add("genres", array);
		return Response.status(200).entity(object.toString()).build();
	}
}
