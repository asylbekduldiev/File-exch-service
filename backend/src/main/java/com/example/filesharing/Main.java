package com.example.filesharing;

import com.example.filesharing.Filter.AuthFilter;
import com.example.filesharing.Filter.CORSFilter;
import com.example.filesharing.resource.FileCleaner;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
        ResourceConfig config = new ResourceConfig()
                .packages("com.example.filesharing.resource")
                .register(MultiPartFeature.class)
                .register(CORSFilter.class)
                .register(AuthFilter.class);

        FileCleaner.start();

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println("Jersey app started at " + BASE_URI);
        System.out.println("Press ENTER to stop...");
        System.in.read();
        server.shutdownNow();
    }
}