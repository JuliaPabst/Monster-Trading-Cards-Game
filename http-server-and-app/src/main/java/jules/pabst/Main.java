package jules.pabst;

import jules.pabst.server.util.HttpRequestParser;

public class Main {
    public static void main(String[] args) {
        // Start server
        // server.start();

        HttpRequestParser parser = new HttpRequestParser();
        parser.parse("""
            GET /home HTTP/1.1
            Host: localhost:10001
            Authentication: Bearer example-token
            
            {body}
            """);

    }
}