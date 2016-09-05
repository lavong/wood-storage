package com.jordylangen.woodstorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogStatement {

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyMMddHHmmss", Locale.ENGLISH);
    private static final String NULL = "null";

    private Date timeStamp;
    private String tag;
    private int priority;
    private String message;
    private String exception;

    public LogStatement(String tag, int priority, String message, Throwable throwable) {
        this(tag, priority, message, new Date(), throwable != null ? throwable.getMessage() : null);
    }

    private LogStatement(String tag, int priority, String message, Date timeStamp, String exception) {
        this.tag = tag;
        this.priority = priority;
        this.message = message;
        this.exception = exception;
        this.timeStamp = timeStamp;
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

    public String getException() {
        return exception;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    String serialize() {
        return String.format(Locale.getDefault(), "%s;%d;%s;%s;%s", tag, priority, message, DATE_TIME_FORMAT.format(timeStamp), exception);
    }

    static LogStatement deserialize(String line) {
        String[] values = line.split(";");

        String tag = values[0];
        int priority = Integer.parseInt(values[1]);
        String message = values[2];
        Date timeStamp;
        try {
            timeStamp = DATE_TIME_FORMAT.parse(values[3]);
        } catch (ParseException e) {
            timeStamp = new Date();
        }

        String exception = null;
        if (values.length == 5) {
            exception = values[4];
        }

        return new LogStatement(NULL.equals(tag) ? null : tag, priority, NULL.equals(message) ? null : message, timeStamp, NULL.equals(exception) ? null : exception);
    }
}
