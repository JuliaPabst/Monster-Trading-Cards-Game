package jules.pabst.server.util;

public class NoHttpStatusException extends RuntimeException {
    public NoHttpStatusException(String message) {
        super(message);
    }
}