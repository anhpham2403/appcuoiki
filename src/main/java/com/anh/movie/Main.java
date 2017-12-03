/**
 * Created by Philip A Senger on November 10, 2015
 */
package com.anh.movie;


import io.jsonwebtoken.impl.crypto.MacProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.security.Key;
import java.util.logging.Logger;

public class Main {
    public static final String BASE_URI = "http://theanh.kilatiron.com";
    final static Logger logger = Logger.getLogger(Main.class.getName());

    public static HttpServer startServer() {

        Key key = MacProvider.generateKey();

        final ResourceConfig rc = new Application(key);
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        logger.info(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}

