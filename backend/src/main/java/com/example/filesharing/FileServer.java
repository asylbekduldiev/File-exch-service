package com.example.filesharing;

import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class FileServer {
    private HttpServer server;

    public void start(int port) throws IOException {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();

        ResourceConfig config = new ResourceConfig()
                .packages("com.example.filesharing.resources")
                .register(MultiPartFeature.class)
                .register(CORSFilter.class);

        server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        System.out.println("Server started at " + baseUri);
    }

    public void stop() {
        if (server != null) {
            server.shutdownNow();
            System.out.println("Server stopped.");
        }
    }

    public static void main(String[] args) throws IOException {
        FileServer fileServer = new FileServer();
        fileServer.start(8080);
        System.out.println("Press ENTER to stop the server...");
        System.in.read();
        fileServer.stop();
    }
}