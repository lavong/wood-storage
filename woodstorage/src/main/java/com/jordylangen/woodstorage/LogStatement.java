package com.jordylangen.woodstorage;

public class LogStatement {

    private String tag;
    private int priority;
    private String message;
    private Throwable throwable;

    public LogStatement(String tag, int priority, String message, Throwable throwable) {
        this.tag = tag;
        this.priority = priority;
        this.message = message;
        this.throwable = throwable;
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
}
