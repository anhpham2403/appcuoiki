package com.anh.movie.request;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import com.anh.movie.entities.Favorite;
import com.anh.movie.entities.Favorite.pk;
import com.anh.movie.entities.Movie;
import com.anh.movie.entities.User;
import com.anh.movie.utils.HibernateUtils;

@Path("/favorite")
public class FavoriteRequest {
	@Context
	SecurityContext securityContext;

	@SuppressWarnings("deprecation")
	@GET
	@RolesAllowed({ "user", "admin" })
	@Produces("application/json")
	@ValidateOnExecution
	public Response getFavorives() {
		String username = securityContext.getUserPrincipal().getName();
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Favorite> favorites = new ArrayList<>();
		try {
			session.getTransaction().begin();
			String sql = "Select favorite from " + Favorite.class.getName()
					+ " favorite join favorite.user c where c.username = ?";
			@SuppressWarnings("unchecked")
			Query<Favorite> query = session.createQuery(sql);
			query.setParameter(0, username);
			favorites = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		for (Favorite favorite : favorites) {
			Hibernate.initialize(favorite.getMovie());
			JSONObject object2 = new JSONObject();
			object2.put("id", favorite.getMovie().getIdMovie());
			object2.put("poster_path", favorite.getMovie().getmPosterPath());
			object2.put("title", favorite.getMovie().getmTitle());
			object2.put("release_date", favorite.getMovie().getmReleaseDate().toString());
			array.put(object2);
		}
		object.put("result", array);
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@POST
	@Path("/{id}")
	@RolesAllowed({ "user", "admin" })
	@Produces("application/json")
	@ValidateOnExecution
	public Response addFavorite(@PathParam("id") int idMovie) {
		String username = securityContext.getUserPrincipal().getName();
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			session.getTransaction().begin();
			User user = null;
			Movie movie = null;
			String sql = "Select user from " + User.class.getName() + " user where user.username = :username";
			@SuppressWarnings("unchecked")
			Query<User> query2 = session.createQuery(sql);
			query2.setParameter("username", username);
			user = query2.getSingleResult();
			sql = "Select movie from " + Movie.class.getName() + " movie where movie.idMovie = :idMovie";
			@SuppressWarnings("unchecked")
			Query<Movie> query1 = session.createQuery(sql);
			query1.setParameter("idMovie", idMovie);
			movie = query1.getSingleResult();
			Favorite favorite = new Favorite();
			pk key = new pk(username, idMovie);
			favorite.setId(key);
			favorite.setUser(user);
			user.getFavorites().add(favorite);
			favorite.setMovie(movie);
			movie.getFavorites().add(favorite);
			session.save(favorite);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			JSONObject object = new JSONObject();
			object.put("message", "This film has been added to favorites");
			return Response.status(200).entity(object.toString()).build();
		}
		JSONObject object = new JSONObject();
		object.put("message", "success");
		return Response.status(200).entity(object.toString()).build();
	}

	@SuppressWarnings("deprecation")
	@DELETE
	@Path("/{id}")
	@RolesAllowed({ "user", "admin" })
	@Produces("application/json")
	@ValidateOnExecution
	public Response deleteFavorite(@PathParam("id") int idMovie) {
		String username = securityContext.getUserPrincipal().getName();
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			session.getTransaction().begin();
			User user = null;
			Movie movie = null;
			String sql = "Select user from " + User.class.getName() + " user where user.username = :username";
			@SuppressWarnings("unchecked")
			Query<User> query2 = session.createQuery(sql);
			query2.setParameter("username", username);
			user = query2.getSingleResult();
			sql = "Select movie from " + Movie.class.getName() + " user where movie.idMovie = :idMovie";
			@SuppressWarnings("unchecked")
			Query<Movie> query1 = session.createQuery(sql);
			query1.setParameter("idMovie", idMovie);
			movie = query1.getSingleResult();
			Favorite favorite = new Favorite();
			pk key = new pk(username, idMovie);
			favorite.setId(key);
			favorite.setUser(user);
			user.getFavorites().add(favorite);
			favorite.setMovie(movie);
			movie.getFavorites().add(favorite);
			session.delete(favorite);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			JSONObject object = new JSONObject();
			object.put("message", "Failure");
			return Response.status(200).entity(object.toString()).build();
		}
		JSONObject object = new JSONObject();
		object.put("message", "success");
		return Response.status(200).entity(object.toString()).build();
	}
}
