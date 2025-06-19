package com.example.filesharing;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class FileServer {
    private HttpServer server;

    public void start(int port) throws IOException {
        URI baseUri = URI.create("http://localhost:" + port + "/");
        ResourceConfig config = new ResourceConfig().packages("com.example.filesharing");
        server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        System.out.println("Server started at " + baseUri);
    }

    public void stop() {
        if (server != null) {
            server.shutdownNow();
        }
    }
}