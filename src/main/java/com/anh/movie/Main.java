package com.anh.movie;

import io.jsonwebtoken.impl.crypto.MacProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.security.Key;
import java.util.logging.Logger;

public class Main {
	public static final String BASE_URI = "https://currencyserver240395.herokuapp.com";
	final static Logger logger = Logger.getLogger(Main.class.getName());
	private static DatabaseReference  mDatabase;

	public static HttpServer startServer() {
		Key key = MacProvider.generateKey();
		final ResourceConfig rc = new Application(key);
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		syncData();
		final HttpServer server = startServer();
		logger.info(String.format(
				"Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...",
				BASE_URI));
		System.in.read();
		server.stop();
		System.out.print("da vao day");
	}

	public static void syncData() {
		FileInputStream serviceAccount = null;
		try {
			serviceAccount = new FileInputStream("currencyserver240395-bd7933f24eae.json");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://currencyserver240395.firebaseio.com/").build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FirebaseApp.initializeApp(options);
		mDatabase = FirebaseDatabase.getInstance().getReference("currency");
		mDatabase.setValueAsync("©2016 androidhive. All rights Reserved");

	}
}
