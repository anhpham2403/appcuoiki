package com.anh.movie;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anh.movie.entities.Movie;

@Path("/movie")
public class MovieRequest {
	@SuppressWarnings("deprecation")
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
		List<Movie> movies = new ArrayList<>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie " + " order by movie.mVoteAverage desc";
			Query<Movie> query = session.createQuery(sql);
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object = new JSONObject(movie.toString());
			array.put(object);
		}
		return Response.status(200).entity(array.toString()).build();
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
		List<Movie> movies = new ArrayList<>();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie where movie.mReleaseDate >?"
					+ " order by movie.mReleaseDate desc";
			Query<Movie> query = session.createQuery(sql);
			query.setDate(0, Calendar.getInstance().getTime());
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			movies = query.getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONArray array = new JSONArray();
		for (Movie movie : movies) {
			JSONObject object = new JSONObject(movie.toString());
			array.put(object);
		}
		return Response.status(200).entity(array.toString()).build();
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
		Movie movie = new Movie();
		try {
			session.getTransaction().begin();
			String sql = "Select movie from " + Movie.class.getName() + " movie where movie.mId =?";
			Query<Movie> query = session.createQuery(sql);
			query.setInteger(0, id);
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
		return Response.status(200).entity(movie.toString()).build();
	}
}
