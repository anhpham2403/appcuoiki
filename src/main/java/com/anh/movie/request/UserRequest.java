package com.anh.movie.request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONObject;

import com.anh.movie.TokenUtil;
import com.anh.movie.entities.Actor;
import com.anh.movie.entities.Movie;
import com.anh.movie.entities.Token;
import com.anh.movie.entities.User;
import com.anh.movie.utils.Constant;
import com.anh.movie.utils.HibernateUtils;

@Path("/user")
public class UserRequest {
	private final static Logger logger = Logger.getLogger(UserRequest.class.getName());
	@Context
	Key key;

	@Context
	Request request;
	@Context
	ServletContext servletContext;
	@Context
	SecurityContext securityContext;

	@PermitAll
	@POST
	@Path("/authentication")
	@Produces("application/json")
	@Consumes("application/x-www-form-urlencoded")
	public Response getUser(@FormParam("username") String username, @FormParam("password") String password) {
		Date expiry = getExpiryDate(1);
		User user = authenticate(username, password);
		if (user == null) {
			JSONObject object = new JSONObject();
			object.put("error", "Invalid username or password");
			return Response.status(501).entity(object.toString()).build();
		}
		String jwtString = TokenUtil.getJWTString(username, user.getRoles(), expiry, key);
		Token token = new Token();
		token.setAuthToken(jwtString);
		JSONObject object = new JSONObject();
		object.put("username", user.getUsername());
		object.put("name", user.getName());
		object.put("email", user.getEmail());
		object.put("role", user.getRoles());
		object.put("token", token.getAuthToken());
		object.put("file_path", user.getFilePath());
		return Response.status(200).entity(object.toString()).build();
	}

	/**
	 * get Expire date in minutes.
	 *
	 * @param minutes
	 *            the minutes in the future.
	 * @return
	 */
	private Date getExpiryDate(int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}

	private User authenticate(String username, String password) throws NotAuthorizedException {
		// Validate the extracted credentials
		User user = null;
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			session.getTransaction().begin();
			String sql = "Select user from " + User.class.getName()
					+ " user where user.username = :username and user.password = :password";
			@SuppressWarnings("unchecked")
			Query<User> query = session.createQuery(sql);
			query.setParameter("username", username);
			query.setParameter("password", password);
			user = query.getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.info("Invalid username '" + username + "' ");
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return user;
	}

	@GET
	@RolesAllowed({ "user", "admin" })
	@Path("/{username}")
	@Produces("application/json")
	@ValidateOnExecution
	public Response getUser(@NotNull @PathParam("username") String username) {
		User user = null;
		if (!securityContext.getUserPrincipal().getName().equals(username)) {
			throw new NotAllowedException("Not allowed ");
		}
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			session.getTransaction().begin();
			String sql = "Select user from " + User.class.getName() + " user where user.username = ?";
			@SuppressWarnings("unchecked")
			Query<User> query = session.createQuery(sql);
			query.setParameter(0, username);
			user = query.getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		if (user == null) {
			JSONObject object = new JSONObject();
			object.put("error", "cant get user");
			return Response.status(501).entity(object.toString()).build();
		}
		JSONObject object = new JSONObject();
		object.put("username", user.getUsername());
		object.put("name", user.getName());
		object.put("email", user.getEmail());
		object.put("role", user.getRoles());
		object.put("file_path", user.getFilePath());
		return Response.status(200).entity(object.toString()).build();
	}

	@POST
	@Path("/register")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces("application/json")
	public Response registerUser(@NotNull @FormDataParam("username") String username,
			@NotNull @FormDataParam("name") String name, @NotNull @FormDataParam("email") String email,
			@FormDataParam("password") String password, @FormDataParam("image") InputStream image,
			@FormDataParam("image") FormDataContentDisposition contentDispositionHeader) {
		String actualPath = servletContext.getRealPath("/images");
		String uploadedFileLocation = actualPath + File.separator + contentDispositionHeader.getFileName();
		writeToFile(image, uploadedFileLocation);
		User user = new User();
		user.setUsername(username);
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setFilePath("/images/" + contentDispositionHeader.getFileName());
		logger.info("Invalid username " + username + " ");
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			JSONObject object = new JSONObject();
			object.put("error", "username has already been taken");
			return Response.status(501).entity(object.toString()).build();
		}
		JSONObject object = new JSONObject();
		object.put("username", user.getUsername());
		object.put("name", user.getName());
		object.put("email", user.getEmail());
		object.put("role", user.getRoles());
		object.put("file_path", user.getFilePath());
		return Response.status(200).entity(object.toString()).build();
	}

	@POST
	@RolesAllowed({ "user", "admin" })
	@Path("/update")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response updateUser(@NotNull @FormDataParam("username") String username,
			@NotNull @FormDataParam("name") String name, @NotNull @FormDataParam("email") String email,
			@FormDataParam("password_old") String passwordOld, @FormDataParam("password") String password,
			@FormDataParam("image") InputStream image,
			@FormDataParam("image") FormDataContentDisposition contentDispositionHeader) {
		if (image != null) {
			String actualPath = servletContext.getRealPath("/images");
			String uploadedFileLocation = actualPath + File.separator + contentDispositionHeader.getFileName();
			writeToFile(image, uploadedFileLocation);
		}
		if (!securityContext.getUserPrincipal().getName().equals(username)) {
			throw new NotAllowedException("Not allowed ");
		}
		User user = null;
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			session.getTransaction().begin();
			String sql = "Select user from " + User.class.getName() + " user where user.username = ?";
			@SuppressWarnings("unchecked")
			Query<User> query = session.createQuery(sql);
			query.setParameter(0, username);
			user = query.getSingleResult();
			if (!user.getPassword().equals(passwordOld)) {
				JSONObject object = new JSONObject();
				object.put("error", "error password");
				return Response.status(501).entity(object.toString()).build();
			}
			user.setUsername(username);
			user.setName(name);
			user.setEmail(email);
			user.setPassword(password);
			if (image != null) {
				user.setFilePath("/images/" + contentDispositionHeader.getFileName());
			}
			session.update(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			JSONObject object = new JSONObject();
			object.put("error", "cant update user");
			return Response.status(501).entity(object.toString()).build();
		}
		String jwtString = TokenUtil.getJWTString(username, user.getRoles(), null, key);
		Token token = new Token();
		token.setAuthToken(jwtString);
		JSONObject object = new JSONObject();
		object.put("username", user.getUsername());
		object.put("name", user.getName());
		object.put("email", user.getEmail());
		object.put("role", user.getRoles());
		object.put("token", token.getAuthToken());
		object.put("file_path", user.getFilePath());
		return Response.status(200).entity(object.toString()).build();
	}

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}