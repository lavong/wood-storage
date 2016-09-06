package com.jordylangen.woodstorage;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class InMemoryStorage implements Storage {

    static final int MAX_ITEMS = 128;

    private List<LogStatement> logStatements;

    public InMemoryStorage() {
        logStatements = new ArrayList<>();
    }

    @Override
    public void add(LogStatement logStatement) {
        logStatements.add(logStatement);

        if (logStatements.size() > MAX_ITEMS) {
            logStatements = logStatements.subList(logStatements.size() - MAX_ITEMS, logStatements.size());
        }
    }

    @Override
    public Observable<LogStatement> load() {
        return Observable.from(logStatements);
    }
}
