package com.anh.movie.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import com.anh.movie.entities.Movie;
import com.anh.movie.entities.Review;
import com.anh.movie.entities.User;
import com.anh.movie.utils.Constant;
import com.anh.movie.utils.HibernateUtils;

@Path("/review")
public class ReviewRequest {
	private final static Logger logger = Logger.getLogger(UserRequest.class.getName());
	@Context
	SecurityContext securityContext;

	@POST
	@RolesAllowed({ "user", "admin" })
	@Produces("application/json")
	@ValidateOnExecution
	public Response ReviewMovie(@QueryParam("id") int idMovie, @QueryParam("content") String content,
			@QueryParam("time") long time) {
		String username = securityContext.getUserPrincipal().getName();
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		User user = null;
		Movie movie = null;
		try {
			session.getTransaction().begin();
			String sql = "Select user from " + User.class.getName() + " user where user.username = ?";
			@SuppressWarnings("unchecked")
			Query<User> query = session.createQuery(sql);
			query.setParameter(0, username);
			user = query.getSingleResult();
			sql = "Select movie from " + Movie.class.getName() + " movie where movie.idMovie = ?";
			@SuppressWarnings("unchecked")
			Query<Movie> query1 = session.createQuery(sql);
			query1.setParameter(0, idMovie);
			movie = query1.getSingleResult();
			Review review = new Review();
			review.setUser(user);
			review.setMovie(movie);
			review.setContent(content);
			review.setTime(new Date(time));
			user.getReviews().add(review);
			movie.getReviews().add(review);
			session.save(review);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			JSONObject object = new JSONObject();
			object.put("error", "cant review this movie");
			return Response.status(501).entity(object.toString()).build();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "success");
		return Response.status(200).entity(jsonObject.toString()).build();
	}

	@GET
	@Path("/movie/{id}")
	@Produces("application/json")
	public Response getReviewsOfMovie(@PathParam("id") int id, @QueryParam("page") int page) {
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		List<Review> reviews = new ArrayList<>();
		try {
			session.getTransaction().begin();
			String sql = "Select review from " + Review.class.getName()
					+ " review JOIN review.movie c where c.idMovie = ?";
			@SuppressWarnings("unchecked")
			Query<Review> query = session.createQuery(sql);
			query.setParameter(0, id);
			reviews = query.getResultList();
			query.setFirstResult((page - 1) * Constant.ITEM_PER_PAGE);
			query.setMaxResults(Constant.ITEM_PER_PAGE);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		for (Review review : reviews) {
			JSONObject object2 = new JSONObject();
			JSONObject object3 = new JSONObject();
			Hibernate.initialize(review.getUser());
			object3.put("username", review.getUser().getUsername());
			object3.put("file_path", review.getUser().getFilePath());
			object3.put("name", review.getUser().getName());
			object2.put("user", object3);
			object2.put("content", review.getContent());
			object2.put("time", review.getTime() != null ? review.getTime().toString() : null);
			array.put(object2);
		}
		object.put("result", array);
		object.put("page", page);
		return Response.status(200).entity(object.toString()).build();
	}
}
