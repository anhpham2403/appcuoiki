package com.anh.movie.request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import com.anh.movie.entities.Character;
import com.anh.movie.entities.Crew;
import com.anh.movie.entities.Actor;
import com.anh.movie.entities.Genre;
import com.anh.movie.entities.Movie;
import com.anh.movie.utils.Constant;
import com.anh.movie.utils.HibernateUtils;

@Path("/movie")
public class MovieRequest {
	@GET
	@Path("/top_rate")
	@Produces("application/json")
	public Response getTopRateMovie(@QueryParam("page") int page) {
		if (page <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
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
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object2 = new JSONObject();
			object2.put("id", movie.getIdMovie());
			object2.put("poster_path", movie.getmPosterPath());
			object2.put("title", movie.getmTitle());
			object2.put("release_date", movie.getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/upcoming")
	@Produces("application/json")
	public Response getUpcomingMovie(@QueryParam("page") int page) {
		if (page <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie where movie.mReleaseDate >?"
					+ " order by movie.mReleaseDate desc";
			Calendar timeNow = Calendar.getInstance();
			timeNow.set(Calendar.DATE, timeNow.get(Calendar.DATE) + 7);
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			query.setParameter(0, timeNow.getTime());
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object2 = new JSONObject();
			object2.put("id", movie.getIdMovie());
			object2.put("poster_path", movie.getmPosterPath());
			object2.put("title", movie.getmTitle());
			object2.put("release_date", movie.getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/now_playing")
	@Produces("application/json")
	public Response getNowPlayingMovie(@QueryParam("page") int page) {
		if (page <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName()
					+ " movie where movie.mReleaseDate between :date1 and :date2" + " order by movie.mReleaseDate desc";
			@SuppressWarnings("unchecked")
			Calendar timeNow = Calendar.getInstance();
			Calendar time1 = Calendar.getInstance();
			Calendar time2 = Calendar.getInstance();
			time1.set(Calendar.DATE, timeNow.get(Calendar.DATE) - 7);
			time2.set(Calendar.DATE, timeNow.get(Calendar.DATE) + 7);
			Query<Movie> query = session.createQuery(sql);
			query.setParameter("date1", time1.getTime());
			query.setParameter("date2", time2.getTime());
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object2 = new JSONObject();
			object2.put("id", movie.getIdMovie());
			object2.put("poster_path", movie.getmPosterPath());
			object2.put("title", movie.getmTitle());
			object2.put("release_date", movie.getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/popular")
	@Produces("application/json")
	public Response getPopularMovie(@QueryParam("page") int page) {
		if (page <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie order by movie.popularity desc";
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
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object2 = new JSONObject();
			object2.put("id", movie.getIdMovie());
			object2.put("poster_path", movie.getmPosterPath());
			object2.put("title", movie.getmTitle());
			object2.put("release_date", movie.getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getDetailMovie(@PathParam("id") int id) {
		if (id <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "id must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
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
				JSONObject JSONObject = new JSONObject();
				JSONObject.put("error", "The resource you requested could not be found");
				return Response.status(501).entity(JSONObject.toString()).build();
			}
			movie = query.getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		if (movie == null) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "The resource you requested could not be found");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		JSONObject object2 = new JSONObject();
		object2.put("id", movie.getIdMovie());
		object2.put("imdb_id", movie.getmImdbId());
		object2.put("poster_path", movie.getmPosterPath());
		object2.put("title", movie.getmTitle());
		object2.put("original_title", movie.getmOriginalTitle());
		object2.put("overview", movie.getmOverView());
		object2.put("status", movie.getmStatus());
		object2.put("release_date", movie.getmReleaseDate().toString());
		object2.put("vote_average", movie.getmVoteAverage());
		object2.put("vote_count", movie.getmVoteCount());
		JSONArray JSONArray = new JSONArray();
		for (Genre genre : movie.getGenres()) {
			JSONObject genreJson = new JSONObject();
			genreJson.put("id", genre.getId());
			genreJson.put("name", genre.getName());
			JSONArray.put(genreJson);
		}
		object2.put("genres", JSONArray);
		JSONArray JSONArray2 = new JSONArray();
		for (Character character : movie.getCharacters()) {
			Actor actor = character.getActor();
			JSONObject characterJson = new JSONObject();
			JSONObject actorJson = new JSONObject();
			actorJson.put("id", actor.getIdActor());
			actorJson.put("name", actor.getName());
			characterJson.put("actor", actorJson);
			characterJson.put("character", character.getCharacter());
			characterJson.put("profile_path", character.getProfilePath());
			JSONArray2.put(characterJson);
		}
		object2.put("cast", JSONArray2);
		return Response.status(200).entity(object2.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/by_genre_id")
	@Produces("application/json")
	public Response getMoviesByGenreId(@QueryParam("id") int idGenre, @QueryParam("page") int page) {
		if (page <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
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
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object2 = new JSONObject();
			object2.put("id", movie.getIdMovie());
			object2.put("poster_path", movie.getmPosterPath());
			object2.put("title", movie.getmTitle());
			object2.put("release_date", movie.getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	
	@SuppressWarnings("deprecation")
	@GET
	@Path("/search")
	@Produces("application/json")
	public Response getMoviesByName(@QueryParam("name") String name, @QueryParam("page") int page) {
		if (page <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
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
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object2 = new JSONObject();
			object2.put("id", movie.getIdMovie());
			object2.put("poster_path", movie.getmPosterPath());
			object2.put("title", movie.getmTitle());
			object2.put("release_date", movie.getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("/by_actor_id")
	@Produces("application/json")
	public Response getMoviesByActorId(@QueryParam("id") int idActor, @QueryParam("page") int page) {
		if (page <= 0) {
			JSONObject JSONObject = new JSONObject();
			JSONObject.put("error", "page must be greater than 0");
			return Response.status(501).entity(JSONObject.toString()).build();
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName()
					+ " movie join fetch movie.characters c where c.actor.idActor = ?";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			query.setParameter(0, idActor);
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object2 = new JSONObject();
			object2.put("id", movie.getIdMovie());
			object2.put("poster_path", movie.getmPosterPath());
			object2.put("title", movie.getmTitle());
			object2.put("release_date", movie.getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@GET
	@Path("/caculate")
	@Produces("application/json")
	public Response cacuclateView() {
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			List<Movie> movies = new ArrayList<Movie>();
			double R0 = 0;
			double W = 0;
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			movies = query.getResultList();
			sql = "Select AVG(mVoteAverage),SUM(mVoteCount) from " + Movie.class.getName() + " movie";
			@SuppressWarnings("unchecked")
			Query<Object[]> query2 = session.createQuery(sql);
			Object[] o = query2.getSingleResult();
			for (Movie movie : movies) {
				R0 = (double) o[0];
				W = movie.getmVoteCount() / Double.valueOf((long) o[1]);
				double Ra = movie.getmVoteAverage() * W + (1 - W) * R0;
				sql = "update Movie movie set movie.popularity = :popularity where movie.idMovie = :idMovie";
				Query<?> query3 = session.createQuery(sql);
				query3.setParameter("popularity", Ra);
				query3.setParameter("idMovie", movie.getIdMovie());
				int result = query3.executeUpdate();
				System.out.println(result + " result ");
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return Response.ok().build();
	}

	@GET
	@Path("/image/{id}")
	@Produces("application/json")
	public Response getImageOfMovie(@PathParam("id") int id) {
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		List<String> imagePath = new ArrayList<>();
		String sql = "Select image_path from image_movie where id_movie = ?";
		@SuppressWarnings("unchecked")
		Query<String> query = session.createSQLQuery(sql);
		query.setParameter(0, id);
		imagePath = query.getResultList();
		JSONArray array = new JSONArray();
		for (String i : imagePath) {
			array.put(i);
		}
		session.close();
		return Response.status(200).entity(array.toString()).build();
	}
	@GET
	@Path("/movie_of_crew")
	@Produces("application/json")
	public Response getCrewsOfMovie(@QueryParam("page") int page,@QueryParam("id") int idCrew) {
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Movie> movies = new ArrayList<Movie>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName()
					+ " movie JOIN movie.crews c where c.idCrew = ?";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			query.setParameter(0, idCrew);
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONObject object = new JSONObject();
		object.put("page", page);
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object2 = new JSONObject();
			object2.put("id", movie.getIdMovie());
			object2.put("poster_path", movie.getmPosterPath());
			object2.put("title", movie.getmTitle());
			object2.put("release_date", movie.getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}
}
