package com.jordylangen.woodstorage;

import rx.Observable;

public interface Storage {

    void save(LogEntry logEntry);

    Observable<LogEntry> load();

    void clear();
}
