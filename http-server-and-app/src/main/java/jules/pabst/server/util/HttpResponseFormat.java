package jules.pabst.server.util;

import jules.pabst.server.http.Response;
import java.util.Map;

public class HttpResponseFormat {

    public String format(Response response) {
        StringBuilder result = new StringBuilder();

        // Validate the status line components
        if (response.getHttpVersion() == null || response.getStatus() == null) {
            throw new IllegalArgumentException("HTTP version and status cannot be null");
        }

        if (response.getStatus().getCode() == 0 || response.getStatus().getMessage() == null) {
            throw new IllegalArgumentException("Invalid status code or message");
        }

        // Append status line
        result.append(response.getHttpVersion())
                .append(" ")
                .append(response.getStatus().getCode())
                .append(" ")
                .append(response.getStatus().getMessage())
                .append("\r\n");

        // Append headers (if present)
        if (response.getHeader() != null && !response.getHeader().isEmpty()) {
            response.getHeader().forEach((key, value) ->
                    result.append(key).append(": ").append(value).append("\r\n")
            );
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
