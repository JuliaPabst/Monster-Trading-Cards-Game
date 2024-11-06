package org.example.application;
import org.example.application.Payload;

public class Response {
    public enum Connection {
        KEEP_ALIVE, CLOSE
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    private String httpVersion;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    private int statusCode;

    public java.time.ZonedDateTime getDate() {
        return date;
    }

    public void setDate(java.time.ZonedDateTime date) {
        this.date = date;
    }

    private java.time.ZonedDateTime date;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    private String server;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection connection;

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    private String cacheControl;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private String contentType;

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    private String contentLength;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    private Payload payload;
}