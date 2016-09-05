package com.jordylangen.woodstorage;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class InMemoryStorage implements Storage {

    private static final int MAX_ITEMS = 128;

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
        return Observable.from(logStatements)
                .toSortedList(new Func2<LogStatement, LogStatement, Integer>() {
                    @Override
                    public Integer call(LogStatement left, LogStatement right) {
                        return right.getTimeStamp().compareTo(left.getTimeStamp());
                    }
                })
                .flatMapIterable(new Func1<List<LogStatement>, Iterable<LogStatement>>() {
                    @Override
                    public Iterable<LogStatement> call(List<LogStatement> logStatements) {
                        return logStatements;
                    }
                });
    }
}
