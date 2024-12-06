package jules.pabst.application.monsterTradingCards.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jules.pabst.application.monsterTradingCards.exception.InvalidBodyException;
import jules.pabst.application.monsterTradingCards.exception.JsonParserException;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

public abstract class Controller {

    private final ObjectMapper objectMapper;

    Controller() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public abstract Response handle(Request request);


    protected <T> T fromBody(String body, Class<T> type) {
        try {
            return objectMapper.readValue(body, type);
        } catch (JsonProcessingException e) {
            throw new InvalidBodyException(e);
        }
    }

    protected <T> T arrayFromBody(String body, TypeReference<T> typeReference) {
        try {
            System.out.println("Body: " + body);
            return objectMapper.readValue(body, typeReference);
        } catch (JsonProcessingException e) {
            throw new InvalidBodyException(e);
        }
    }

    protected String getAuthorizationHeader(Request request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            throw new RuntimeException("Authorization header is missing");
        }
        return authHeader;
    }

    protected Response json(Status status, Object object) {
        Response response = new Response();
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        try {
            response.setBody(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new JsonParserException(e);
        }

        return response;
    }
}
