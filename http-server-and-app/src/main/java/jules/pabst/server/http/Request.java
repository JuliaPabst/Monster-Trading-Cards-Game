package jules.pabst.server.http;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private Method method;

    private String path;

    private String body;

    private final HashMap<String, String> header;

    public Request() {
        this.header = new HashMap<>();
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeader(String name) {
        return this.header.get(name);
    }

    public void setHeader(String name, String value) {
        this.header.put(name, value);
    }
}
