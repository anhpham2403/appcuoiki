/**
 * Created by Philip A Senger on November 10, 2015
 */
package com.anh.movie;

import org.glassfish.jersey.server.ContainerRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.anh.movie.entities.User;
import com.anh.movie.utils.HibernateUtils;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.logging.Logger;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JWTSecurityFilter implements ContainerRequestFilter {

	final static Logger logger = Logger.getLogger(JWTSecurityFilter.class.getName());

	@Context
	Key key;

	@Inject
	javax.inject.Provider<UriInfo> uriInfo;

	public static String extractJwtTokenFromAuthorizationHeader(String auth) {
		// Replacing "Bearer Token" to "Token" directly
		return auth.replaceFirst("[B|b][E|e][A|a][R|r][E|e][R|r] ", "").replace(" ", "");
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String method = requestContext.getMethod().toLowerCase();
		String path = ((ContainerRequest) requestContext).getPath(true).toLowerCase();

		if (("get".equals(method) && ("application.wadl".equals(path) || "application.wadl/xsd0.xsd".equals(path)))
				|| ("post".equals(method) && "authentication".equals(path))) {
			// pass through the filter.
			requestContext.setSecurityContext(
					new SecurityContextAuthorizer(uriInfo, () -> "anonymous", "anonymous"));
			return;
		}

		String authorizationHeader = ((ContainerRequest) requestContext).getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null) {
			requestContext.setSecurityContext(
					new SecurityContextAuthorizer(uriInfo, () -> "anonymous", "anonymous"));
			return;
		}

		String strToken = extractJwtTokenFromAuthorizationHeader(authorizationHeader);
		if (TokenUtil.isValid(strToken, key)) {
			String name = TokenUtil.getName(strToken, key);
			String roles = TokenUtil.getRoles(strToken, key);
			if (name != null && !roles.equals("")) {
				User user = null;
				SessionFactory factory = HibernateUtils.getSessionFactory();
				Session session = factory.getCurrentSession();
				try {
					session.getTransaction().begin();
					String sql = "Select user from " + User.class.getName() + " user where user.username = ?";
					@SuppressWarnings("unchecked")
					Query<User> query = session.createQuery(sql);
					query.setParameter(0, name);
					user = query.getSingleResult();
					session.getTransaction().commit();
				} catch (Exception e) {
					e.printStackTrace();
					session.getTransaction().rollback();
				}
				if (user != null) {
					System.out.println(user.getRoles());
					System.out.println(roles.toString());
					if (user.getRoles() != null && Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles))) {
						requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, () -> name, roles));
						return;
					} else {
						logger.info("role did not match the token");
					}
				} else {
					logger.info("User not found");
				}
			} else {
				logger.info("name, roles or version missing from token");
			}
		} else {
			logger.info("token is invalid");
		}
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}
}
