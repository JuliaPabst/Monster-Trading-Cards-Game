package jules.pabst.server;

import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.util.HttpRequestParser;
import jules.pabst.server.util.HttpResponseFormatter;
import jules.pabst.server.util.HttpSocket;
import java.io.IOException;
import jules.pabst.server.Application;

import java.io.IOException;
import java.net.Socket;

public class RequestHandler {

    // [x] receive socket
    // [x] wrap socket in HttpSocket
    // [x] get HTTP request
    // [x] parse to request obj
    // give request to application
    // receive response
    // format response to HTTP response
    // send response to client

    private final Socket socket;
    private final Application application;

    public RequestHandler(
            Socket socket,
            Application application
    ) {
        this.socket = socket;
        this.application = application;
    }

    public void handle() {
        HttpRequestParser httpRequestParser = new HttpRequestParser();
        HttpResponseFormatter httpResponseFormatter = new HttpResponseFormatter();

        try (HttpSocket httpSocket = new HttpSocket(this.socket)) {
            String http = httpSocket.read();
            Request request = httpRequestParser.parse(http);
            System.out.println(request.getPath());

            Response response = this.application.handle(request);

            http = httpResponseFormatter.format(response);
            httpSocket.write(http);
        } catch (IOException e) {

            // send standard error response

            throw new RuntimeException(e);
        }

    }
}