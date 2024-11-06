package org.example.application;
import org.example.application.Error;

import java.time.Instant;

public class Payload {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public java.time.ZonedDateTime getData() {
        return data;
    }

    public void setData(java.time.ZonedDateTime data) {
        this.data = data;
    }

    private java.time.ZonedDateTime data;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    private Error error;

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    private java.time.Instant timeStamp;
}
