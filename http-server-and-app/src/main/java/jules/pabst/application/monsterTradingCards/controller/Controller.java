package jules.pabst.application.monsterTradingCards.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.exception.InvalidBodyException;
import jules.pabst.application.monsterTradingCards.exception.JsonParserException;
import jules.pabst.application.monsterTradingCards.exception.MissingAuthorizationHeader;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.List;

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
            return objectMapper.readValue(body, typeReference);
        } catch (JsonProcessingException e) {
            throw new InvalidBodyException(e);
        }
    }

    protected String getAuthorizationToken(Request request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            throw new MissingAuthorizationHeader("Authorization header is missing");
        }

        String token = authHeader.split(" ")[1];
        String name = token.split("-")[0];

        System.out.println("Username: " + name);

        return token;
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

    protected Response returnPlain(Status status, List<Card> cards) {
        Response response = new Response();
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        StringBuilder body = new StringBuilder();
        for(Card card : cards){
            body.append("Card-Id: " + card.getId());
            body.append("\r\n");
            body.append("Card-Name: " + card.getName());
            body.append("\r\n");
            body.append("Card-Damage: " + card.getDamage());
            body.append("\r\n");
            body.append("Card-Owner-Uuid: " + card.getOwnerUuid());
            body.append("\r\n");
            body.append("Card-Deck-User-Uuid: " + card.getDeckUserId());
            body.append("\r\n");
            body.append("\r\n");
        }

        response.setBody(body.toString());
        return response;
    }
}
