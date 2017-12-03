/**
 * Created by Philip A Senger on November 10, 2015
 */
package com.anh.movie;

import io.jsonwebtoken.impl.crypto.MacProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;
import java.security.Key;
import java.util.Date;

@ApplicationPath("/")
public class Application extends ResourceConfig { // implements ContextResolver<ObjectMapper> {

	private static Key key;

	public Application() {
		this(MacProvider.generateKey());
	}

	public Application(final Key key) {
		this.setKey(key);
		register(LoggingFilter.class);
		// roles security
		register(RolesAllowedDynamicFeature.class);
		// jwt filter
		register(JWTSecurityFilter.class);
		// turn on Jackson, Moxy isn't that good of a solution.
		register(JacksonFeature.class);

		packages("com.anh.movie");
		register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(getKey()).to(Key.class);
            }
        });
		property("jersey.config.beanValidation.enableOutputValidationErrorEntity.server", "true");
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

	}

	public static Key getKey() {
		return key;
	}

	public static void setKey(Key key) {
		Application.key = key;
	}

}
