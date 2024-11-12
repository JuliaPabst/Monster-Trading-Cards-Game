package jules.pabst.server.http;

import jules.pabst.server.http.Status;

import java.util.HashMap;

public class Response {
    private Status status;
    private final HashMap<String, String> header;
    private String body;


    public Response() {
        this.header = new HashMap<>();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(String name, String value) {
        this.header.put(name, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}