package jules.pabst.server.util;

import jules.pabst.server.http.Response;

public class HttpResponseFormatter {

    public String format(Response response) {
        StringBuilder result = new StringBuilder();

        // Append status line
        result.append("HTTP/1.1")
                .append(" ");

        if (response.getStatus() == null || response.getStatus().getMessage() == null) {
            throw new NoHttpStatusException("Invalid status code or message");
        } else {
            result.append(response.getStatus().getCode())
                    .append(" ")
                    .append(response.getStatus().getMessage())
                    .append("\r\n");
        }


        // Append headers (if present)
        if (response.getHeader() != null && !response.getHeader().isEmpty()) {
            response.getHeader().forEach((key, value) ->
                    result.append(key).append(": ").append(value).append("\r\n")
            );
        }

        if (response.getBody() != null) {
            result.append("Content-Length").append(": ").append(response.getBody().length()).append("\r\n");
        }

        // extra line break to separate headers from body
        result.append("\r\n");

        // Append body (if present)
        if (response.getBody() != null) {
            result.append(response.getBody());
        }

        return result.toString();
    }
}
