package com.example.filesharing;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileServer server = new FileServer();
        server.start(8080);
    }
}