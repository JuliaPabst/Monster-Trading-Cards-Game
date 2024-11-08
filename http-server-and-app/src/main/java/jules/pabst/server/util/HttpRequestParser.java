package jules.pabst.server.util;

import jules.pabst.server.http.Request;

import jules.pabst.server.http.Method;

import java.util.HashMap;

public class HttpRequestParser {

    public Request parse(String http) {
        Request request = new Request();

        String[] lines = http.split("\r\n");
        String[] requestLineParts = lines[0].split(" ");
        String[] body = http.split("\r\n\r\n");

        request.setMethod(Method.valueOf(requestLineParts[0]));
        request.setPath(requestLineParts[1]);

        HashMap<String, String> headers = new HashMap<>();

        // parse headers
        for(int i = 1; i < lines.length; i++) {
            if(lines[i].isEmpty()) {
                break;
            }

            String[] headerParts = lines[i].split(":", 2);
            request.setHeader(headerParts[0], headerParts[1].trim());
        }

        // parse body
        if(body.length > 1) {
            request.setBody(body[1]);
            System.out.println(body[1]);
        }

        return request;
    }
}
