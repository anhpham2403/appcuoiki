package com.anh.currency;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class Application extends ResourceConfig { 

	public Application() {
		register(LoggingFilter.class);
		register(JacksonFeature.class);
		register(MultiPartFeature.class);
		register(MainApplicationListener.class);
		packages("com.anh.currency");
	}
}
