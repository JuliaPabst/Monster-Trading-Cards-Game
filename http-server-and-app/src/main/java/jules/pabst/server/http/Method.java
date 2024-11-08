package jules.pabst.server.http;

public enum Method {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH");

    private final String name;

    Method(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
