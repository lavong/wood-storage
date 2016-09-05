package com.jordylangen.woodstorage;

import java.util.Date;

public class LogStatement {

    private Date timeStamp;
    private String tag;
    private int priority;
    private String message;
    private Throwable throwable;

    public LogStatement(String tag, int priority, String message, Throwable throwable) {
        this.tag = tag;
        this.priority = priority;
        this.message = message;
        this.throwable = throwable;
        this.timeStamp = new Date();
    }

    public String getTag() {
        return tag;
    }

    public int getPriority() {
        return priority;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
