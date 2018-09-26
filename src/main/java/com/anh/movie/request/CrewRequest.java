	package com.anh.movie.request;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import com.anh.movie.entities.Crew;
import com.anh.movie.utils.HibernateUtils;

@Path("/crew")
public class CrewRequest {
	@GET
	@Path("/by_movie_id")
	@Produces("application/json")
	public Response getCrews(@QueryParam("id") int id) {
		if (id <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "id must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Crew> crews = new ArrayList<Crew>();
		try {
			session.getTransaction().begin();
			String sql = "Select crew from " + Crew.class.getName() + " crew JOIN crew.movies v  where v.idMovie = ?";
			@SuppressWarnings("unchecked")
			Query<Crew> query = session.createQuery(sql);
			query.setParameter(0, id);
			crews = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONArray array = new JSONArray();
		for (Crew crew : crews) {
			JSONObject object2 = new JSONObject();
			object2.put("id", crew.getIdCrew());
			object2.put("name", crew.getName());
			object2.put("job", crew.getJob());
			object2.put("profile_path", crew.getProfilePath());
			array.put(object2);
		}
		
		return Response.status(200).entity(array.toString()).build();
	}
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getDetailCrew(@PathParam("id") int id) {
		if (id <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page id be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		Crew crew = null;
		try {
			session.getTransaction().begin();
			String sql = "Select crew from " + Crew.class.getName() + " crew where crew.idCrew = ?";
			@SuppressWarnings("unchecked")
			Query<Crew> query = session.createQuery(sql);
			query.setParameter(0, id);
			crew = query.getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		if (crew == null) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "The resource you requested could not be found");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		JSONObject object = new JSONObject();
		object.put("id", crew.getIdCrew());
		object.put("job", crew.getJob());
		object.put("name", crew.getName());
		object.put("also_known_as", crew.getAlsoKnownAs());
		object.put("gender", crew.getGender());
		object.put("birthday", crew.getBirthday() != null ? crew.getBirthday().toString() : null);
		object.put("deathday", crew.getDeathday() != null ? crew.getDeathday().toString() : null);
		object.put("biography", crew.getBiography());
		object.put("place_of_birth", crew.getPlaceOfBirth());
		object.put("profile_path", crew.getProfilePath());
		return Response.status(200).entity(object.toString()).build();
	}
}
