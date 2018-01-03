package com.anh.movie;

import io.jsonwebtoken.impl.crypto.MacProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;
import java.security.Key;
import java.util.Date;

@ApplicationPath("/")
public class Application extends ResourceConfig { 

	private static Key key;

	public Application() {
		this(MacProvider.generateKey());
	}

	public Application(final Key key) {
		Application.setKey(key);
		register(LoggingFilter.class);
		register(RolesAllowedDynamicFeature.class);
		register(JWTSecurityFilter.class);
		register(JacksonFeature.class);
		register(MultiPartFeature.class);
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
