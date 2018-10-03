package com.anh.movie;

import io.jsonwebtoken.impl.crypto.MacProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Key;
import java.util.Date;

@ApplicationPath("/")
public class Application extends ResourceConfig { 

	private static Key key;
	private static DatabaseReference  mDatabase;
	@Context 
	ServletContext servletContext;
	
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
		String path = null;
		System.out.println("file path1:" + path);
		try {
			path = servletContext.getResource("/WEB-INF/currencyserver240395-bd7933f24eae.json").getPath();
		} catch (MalformedURLException e) {
			System.out.println("da vao day");

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		System.out.println("file path2:" + path);
		syncData(path);

	}

	public static Key getKey() {
		return key;
	}

	public static void setKey(Key key) {
		Application.key = key;
	}

	public static void syncData(String path) {
		System.out.println("file path4:" + path);
		FileInputStream serviceAccount = null;
		try {
			serviceAccount = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("file path3:" + path);
		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://currencyserver240395.firebaseio.com/").build();
		} catch (IOException e) {
			System.out.println("da vao day2");

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		FirebaseApp.initializeApp(options);
		mDatabase = FirebaseDatabase.getInstance().getReference("currency");
		mDatabase.setValueAsync("©2016 androidhive. All rights Reserved");

	}
}
