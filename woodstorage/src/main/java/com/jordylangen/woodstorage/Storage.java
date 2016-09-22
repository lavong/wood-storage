package com.jordylangen.woodstorage;

import io.reactivex.Flowable;

public interface Storage {

    void save(LogEntry logEntry);

    Flowable<LogEntry> load();

    void clear();
}
