package com.anh.movie.request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import com.anh.movie.entities.Actor;
import com.anh.movie.entities.Character;
import com.anh.movie.entities.Movie;
import com.anh.movie.utils.Constant;
import com.anh.movie.utils.HibernateUtils;

@Path("/actor")
public class ActorRequest {
	@GET
	@Path("/actors")
	@Produces("application/json")
	public Response getActors(@QueryParam("page") int page) {
		if (page <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Actor> actors = new ArrayList<Actor>();
		try {
			session.getTransaction().begin();
			String sql = "Select actor from " + Actor.class.getName() + " actor";
			@SuppressWarnings("unchecked")
			Query<Actor> query = session.createQuery(sql);
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			actors = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Actor actor : actors) {
			JSONObject object2 = new JSONObject();
			object2.put("id", actor.getIdActor());
			object2.put("name", actor.getName());
			object2.put("profile_path", actor.getIdActor());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getDetailActor(@PathParam("id") int id) {
		if (id <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page id be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		Actor actor = null;
		try {
			session.getTransaction().begin();
			String sql = "Select actor from " + Actor.class.getName() + " actor where actor.idActor = ?";
			@SuppressWarnings("unchecked")
			Query<Actor> query = session.createQuery(sql);
			query.setParameter(0, id);
			actor = query.getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		if (actor == null) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "The resource you requested could not be found");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		JSONObject object = new JSONObject();
		object.put("id", actor.getIdActor());
		object.put("imdb_id", actor.getIdImdb());
		object.put("name", actor.getName());
		object.put("also_known_as", actor.getAlsoKnownAs());
		object.put("gender", actor.getGender());
		object.put("birthday", actor.getBirthday() != null ? actor.getBirthday().toString() : null);
		object.put("deathday", actor.getDeathday() != null ? actor.getDeathday().toString() : null);
		object.put("biography", actor.getBiography());
		object.put("place_of_birth", actor.getPlaceOfBirth());
		object.put("profile_path", actor.getProfilePath());
		JSONArray array = new JSONArray();
		Hibernate.initialize(actor.getCharacters());
		for (Character character : actor.getCharacters()) {
			org.json.JSONObject object2 = new JSONObject();
			Hibernate.initialize(character.getMovie());
			object2.put("id", character.getMovie().getIdMovie());
			object2.put("title", character.getMovie().getmTitle());
			object2.put("poster_path", character.getMovie().getmPosterPath());
			object2.put("release_date",
					character.getMovie().getmReleaseDate() != null ? character.getMovie().getmReleaseDate().toString()
							: null);
			array.put(object2);
		}
		object.put("movies", array);
		return Response.status(200).entity(object.toString()).build();
	}
}
