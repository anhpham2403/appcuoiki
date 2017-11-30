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
import org.json.JSONException;
import org.json.JSONObject;
import com.anh.movie.entities.Character;
import com.anh.movie.entities.Actor;
import com.anh.movie.entities.Genre;
import com.anh.movie.entities.Movie;
import com.anh.movie.utils.Constant;
import com.anh.movie.utils.HibernateUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/movie")
public class MovieRequest {
	@GET
	@Path("/top_rated")
	@Produces("application/json")
	public Response getTopRateMovie(@QueryParam("page") int page) throws JSONException {
		if (page <= 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(jsonObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie " + " order by movie.mVoteAverage desc";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JsonObject object = new JsonObject();
		object.addProperty("page", page);
		JsonArray array = new JsonArray();
		for (Movie movie : movies) {
			JsonObject object2 = new JsonObject();
			object2.addProperty("id", movie.getIdMovie());
			object2.addProperty("poster_path", movie.getmPosterPath());
			object2.addProperty("title", movie.getmTitle());
			object2.addProperty("release_date", movie.getmReleaseDate().getTime());
			array.add(object2);
		}
		object.add("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/upcoming")
	@Produces("application/json")
	public Response getUpcomingMovie(@QueryParam("page") int page) throws JSONException {
		if (page <= 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(jsonObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie where movie.mReleaseDate >?"
					+ " order by movie.mReleaseDate desc";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			query.setParameter(0, Calendar.getInstance().getTime());
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JsonObject object = new JsonObject();
		object.addProperty("page", page);
		JsonArray array = new JsonArray();
		for (Movie movie : movies) {
			JsonObject object2 = new JsonObject();
			object2.addProperty("id", movie.getIdMovie());
			object2.addProperty("poster_path", movie.getmPosterPath());
			object2.addProperty("title", movie.getmTitle());
			object2.addProperty("release_date", movie.getmReleaseDate().getTime());
			array.add(object2);
		}
		object.add("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getDetailMovie(@PathParam("id") int id) {
		if (id <= 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error", "id must be greater than 0");
			return Response.status(501).entity(jsonObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		Movie movie = null;
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie where movie.id =?";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			query.setParameter(0, id);
			if (query.uniqueResult() == null) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("error", "The resource you requested could not be found");
				return Response.status(501).entity(jsonObject.toString()).build();
			}
			movie = query.getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		if (movie == null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error", "The resource you requested could not be found");
			return Response.status(501).entity(jsonObject.toString()).build();
		}
		JsonObject object2 = new JsonObject();
		object2.addProperty("id", movie.getIdMovie());
		object2.addProperty("imdb_id", movie.getmImdbId());
		object2.addProperty("poster_path", movie.getmPosterPath());
		object2.addProperty("title", movie.getmTitle());
		object2.addProperty("original_title", movie.getmOriginalTitle());
		object2.addProperty("overview", movie.getmOverView());
		object2.addProperty("status", movie.getmStatus());
		object2.addProperty("release_date", movie.getmReleaseDate().getTime());
		object2.addProperty("vote_average", movie.getmVoteAverage());
		object2.addProperty("vote_count", movie.getmVoteCount());
		JsonArray jsonArray = new JsonArray();
		for (Genre genre : movie.getGenres()) {
			JsonObject genreJson = new JsonObject();
			genreJson.addProperty("id", genre.getId());
			genreJson.addProperty("name", genre.getName());
			jsonArray.add(genreJson);
		}
		object2.add("genres", jsonArray);
		JsonArray jsonArray2 = new JsonArray();
		for (Character character : movie.getCharacters()) {
			Actor actor = character.getActor();
			JsonObject actorJson = new JsonObject();
			actorJson.addProperty("id", actor.getIdActor());
			actorJson.addProperty("name", actor.getName());
			actorJson.addProperty("character", character.getCharacter());
			actorJson.addProperty("profile_path", character.getProfilePath());
			jsonArray2.add(actorJson);
		}
		object2.add("cast", jsonArray2);
		return Response.status(200).entity(object2.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/by_genre_id")
	@Produces("application/json")
	public Response getMoviesByGenreId(@QueryParam("id") int idGenre, @QueryParam("page") int page)
			throws JSONException {
		if (page <= 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(jsonObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName()
					+ " movie join fetch movie.genres c where c.id = ?";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			query.setParameter(0, idGenre);
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JsonObject object = new JsonObject();
		object.addProperty("page", page);
		JsonArray array = new JsonArray();
		for (Movie movie : movies) {
			JsonObject object2 = new JsonObject();
			object2.addProperty("id", movie.getIdMovie());
			object2.addProperty("poster_path", movie.getmPosterPath());
			object2.addProperty("title", movie.getmTitle());
			object2.addProperty("release_date", movie.getmReleaseDate().getTime());
			array.add(object2);
		}
		object.add("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/search")
	@Produces("application/json")
	public Response getMoviesByName(@QueryParam("name") String name, @QueryParam("page") int page)
			throws JSONException {
		if (page <= 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(jsonObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName()
					+ " movie where movie.mTitle like ? or movie.mOriginalTitle like ?";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			query.setParameter(0, "%" + name + "%");
			query.setParameter(1, "%" + name + "%");
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JsonObject object = new JsonObject();
		object.addProperty("page", page);
		JsonArray array = new JsonArray();
		for (Movie movie : movies) {
			JsonObject object2 = new JsonObject();
			object2.addProperty("id", movie.getIdMovie());
			object2.addProperty("poster_path", movie.getmPosterPath());
			object2.addProperty("title", movie.getmTitle());
			object2.addProperty("release_date", movie.getmReleaseDate().getTime());
			array.add(object2);
		}
		object.add("result", array);
		return Response.status(200).entity(object.toString()).build();
	}
}
