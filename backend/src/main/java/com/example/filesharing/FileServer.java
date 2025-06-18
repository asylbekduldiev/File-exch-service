package com.example.filesharing;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FileServer {

    private HttpServer server;

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/upload", new UploadHandler());
        server.createContext("/download", new DownloadHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + port + "/upload");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}
