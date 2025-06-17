package com.example.filesharing;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.RequestContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HttpExchangeRequestContext implements RequestContext {

    private final HttpExchange exchange;

    public HttpExchangeRequestContext(HttpExchange exchange) {
        this.exchange = Objects.requireNonNull(exchange, "HttpExchange cannot be null");
    }

    @Override
    public String getCharacterEncoding() {
        String contentType = getContentType();
        if (contentType != null && contentType.contains("charset=")) {
            int charsetIndex = contentType.indexOf("charset=") + "charset=".length();
            return contentType.substring(charsetIndex).split(";")[0].trim();
        }
        return null;
    }

    @Override
    public String getContentType() {
        return exchange.getRequestHeaders().getFirst("Content-type");
    }

    @Override
    public int getContentLength() {
        String contentLengthStr = exchange.getRequestHeaders().getFirst("Content-Length");
        if (contentLengthStr != null) {
            try {
                return Integer.parseInt(contentLengthStr);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return exchange.getRequestBody();
    }
}