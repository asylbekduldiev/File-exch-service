package com.example.filesharing;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class UploadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        String contentType = exchange.getRequestHeaders().getFirst("Content-type");
        if (contentType == null || !contentType.contains("multipart/form-data")) {
            exchange.sendResponseHeaders(400, -1); // Bad Request
            return;
        }

        MultipartParser parser = new MultipartParser();
        boolean success = parser.handleUpload(exchange);

        String response = success ? "Upload successful" : "Upload failed";
        exchange.sendResponseHeaders(success ? 200 : 500, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}