package jules.pabst.server;

import jules.pabst.server.http.Response;
import jules.pabst.server.http.Request;

public interface Application {

    Response handle(Request request);
}
