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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.anh.movie.entities.Actor;
import com.anh.movie.entities.Character;
import com.anh.movie.entities.Movie;
import com.anh.movie.utils.Constant;
import com.anh.movie.utils.HibernateUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/actor")
public class ActorRequest {
	@GET
	@Path("/actors")
	@Produces("application/json")
	public Response getActors(@QueryParam("page") int page) {
		if (page <= 0) {
			JsonObject JsonObject = new JsonObject();
			JsonObject.addProperty("error", "page must be greater than 0");
			return Response.status(501).entity(JsonObject.toString()).build();
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
		JsonObject object = new JsonObject();
		object.addProperty("page", page);
		JsonArray array = new JsonArray();
		for (Actor actor : actors) {
			JsonObject object2 = new JsonObject();
			object2.addProperty("id", actor.getIdActor());
			object2.addProperty("name", actor.getName());
			object2.addProperty("profile_path", actor.getIdActor());
			array.add(object2);
		}
		object.add("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getDetailActor(@PathParam("id") int id) {
		if (id <= 0) {
			JsonObject JsonObject = new JsonObject();
			JsonObject.addProperty("error", "page id be greater than 0");
			return Response.status(501).entity(JsonObject.toString()).build();
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
			JsonObject JsonObject = new JsonObject();
			JsonObject.addProperty("error", "The resource you requested could not be found");
			return Response.status(501).entity(JsonObject.toString()).build();
		}
		JsonObject object = new JsonObject();
		object.addProperty("id", actor.getIdActor());
		object.addProperty("imdb_id", actor.getIdImdb());
		object.addProperty("name", actor.getName());
		object.addProperty("also_known_as", actor.getAlsoKnownAs());
		object.addProperty("gender", actor.getGender());
		object.addProperty("birthday", actor.getBirthday() != null ? actor.getBirthday().getTime() : null);
		object.addProperty("deathday", actor.getDeathday() != null ? actor.getDeathday().getTime() : null);
		object.addProperty("biography", actor.getBiography());
		object.addProperty("place_of_birth", actor.getPlaceOfBirth());
		object.addProperty("profile_path", actor.getProfilePath());
		JsonArray array = new JsonArray();
		for (Character character : actor.getCharacters()) {
			JsonObject object2 = new JsonObject();
			object2.addProperty("id", character.getMovie().getIdMovie());
			object2.addProperty("title", character.getMovie().getmTitle());
			object2.addProperty("poster_path", character.getMovie().getmPosterPath());
			object2.addProperty("release_date",
					character.getMovie().getmReleaseDate() != null ? character.getMovie().getmReleaseDate().toString()
							: null);
			array.add(object2);
		}
		object.add("movies", array);
		return Response.status(200).entity(object.toString()).build();
	}
}
