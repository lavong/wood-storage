package com.jordylangen.woodstorage;

import rx.Observable;

public class FileStorage implements Storage {

    @Override
    public void add(LogStatement logStatement) {

    }

    @Override
    public Observable<LogStatement> load() {
        return null;
    }
}
