package com.example.filesharing;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MultipartParser {

    public boolean handleUpload(HttpExchange exchange) {
        try {
            File uploadsDir = new File("uploads");
            if (!uploadsDir.exists() && !uploadsDir.mkdirs()) {
                throw new IOException("Cannot create uploads directory");
            }

            String contentTypeHeader = exchange.getRequestHeaders().getFirst("Content-type");
            String boundary = null;
            if (contentTypeHeader != null) {
                String[] params = contentTypeHeader.split(";");
                for (String param : params) {
                    param = param.trim();
                    if (param.startsWith("boundary=")) {
                        boundary = param.substring("boundary=".length());
                        if (boundary.startsWith("\"") && boundary.endsWith("\"")) {
                            boundary = boundary.substring(1, boundary.length() - 1);
                        }
                    }
                }
            }
            if (boundary == null) {
                throw new IOException("Missing boundary in multipart/form-data");
            }

            byte[] body = exchange.getRequestBody().readAllBytes();
            String bodyStr = new String(body);

            String[] parts = bodyStr.split("--" + boundary);
            for (String part : parts) {
                if (part.contains("Content-Disposition") && part.contains("filename=\"")) {
                    String[] headersAndData = part.split("\r\n\r\n", 2);
                    if (headersAndData.length == 2) {
                        String headers = headersAndData[0];
                        String data = headersAndData[1];

                        String fileName = null;
                        for (String headerLine : headers.split("\r\n")) {
                            if (headerLine.contains("filename=\"")) {
                                int start = headerLine.indexOf("filename=\"") + 10;
                                int end = headerLine.indexOf("\"", start);
                                fileName = headerLine.substring(start, end);
                            }
                        }

                        if (fileName != null && !fileName.isEmpty()) {
                            File file = new File(uploadsDir, new File(fileName).getName()); // безопасность имени
                            if (data.endsWith("\r\n")) {
                                data = data.substring(0, data.length() - 2);
                            }
                            Files.write(file.toPath(), data.getBytes());
                            System.out.println("Saved file: " + file.getAbsolutePath());
                        }
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}