package com.anh.movie.request;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONObject;

import com.anh.movie.entities.Movie;
import com.anh.movie.entities.Rate;
import com.anh.movie.entities.Rate.pk;
import com.anh.movie.entities.User;
import com.anh.movie.utils.HibernateUtils;

@Path("/rate")
public class RateRequest {
	@Context
	SecurityContext securityContext;

	@POST
	@RolesAllowed({ "user", "admin" })
	@Produces("application/json")
	@ValidateOnExecution
	public Response rateMovie(@QueryParam("id") int idMovie, @QueryParam("score") int score) {
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
			sql = "Select movie from " + Movie.class.getName() + " movie where movie.idMovie = ?";
			@SuppressWarnings("unchecked")
			Query<Movie> query1 = session.createQuery(sql);
			query1.setParameter(0, idMovie);
			movie = query1.getSingleResult();
			movie.setmVoteAverage(
					(movie.getmVoteAverage() * movie.getmVoteCount() + score) / ((double) (movie.getmVoteCount() + 1)));
			movie.setmVoteCount(movie.getmVoteCount() + 1);
			List<Movie> movies = new ArrayList<Movie>();
			Rate rate = new Rate();
			rate.setUser(user);
			rate.setMovie(movie);
			rate.setId(new pk(user.getUsername(), movie.getIdMovie()));
			rate.setScore(score);
			user.getRates().add(rate);
			movie.getRates().add(rate);
			session.merge(rate);
			double R0 = 0;
			double W = 0;
			sql = "Select movie from " + Movie.class.getName() + " movie";
			@SuppressWarnings("unchecked")
			Query<Movie> query = session.createQuery(sql);
			movies = query.getResultList();
			sql = "Select AVG(mVoteAverage),SUM(mVoteCount) from " + Movie.class.getName() + " movie";
			@SuppressWarnings("unchecked")
			Query<Object[]> query4 = session.createQuery(sql);
			Object[] o = query4.getSingleResult();
			for (Movie movie1 : movies) {
				R0 = (double) o[0];
				W = movie1.getmVoteCount() / Double.valueOf((long) o[1]);
				double Ra = movie1.getmVoteAverage() * W + (1 - W) * R0;
				sql = "update Movie movie set movie.popularity = :popularity where movie.idMovie = :idMovie";
				Query<?> query3 = session.createQuery(sql);
				query3.setParameter("popularity", Ra);
				query3.setParameter("idMovie", movie1.getIdMovie());
				query3.executeUpdate();
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			JSONObject object = new JSONObject();
			object.put("message", "cant rate this movie");
			return Response.status(200).entity(object.toString()).build();
		}
		JSONObject object = new JSONObject();
		object.put("message", "success");
		return Response.status(200).entity(object.toString()).build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getRateMovie(@PathParam("id") int idMovie) {
		String username = securityContext.getUserPrincipal().getName();
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		Rate rate;
		User user = null;
		try {
			session.getTransaction().begin();
			String sql = "Select rate from " + Rate.class.getName()
					+ " rate where rate.id.username = :username and rate.id.idMovie =:idMovie";
			@SuppressWarnings("unchecked")
			Query<Rate> query3 = session.createQuery(sql);
			query3.setParameter("username", username);
			query3.setParameter("idMovie", idMovie);
			rate = query3.getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			JSONObject object = new JSONObject();
			object.put("score", "0");
			return Response.status(200).entity(object.toString()).build();
		}
		JSONObject object = new JSONObject();
		object.put("score", rate.getScore());
		return Response.status(200).entity(object.toString()).build();
	}
}
