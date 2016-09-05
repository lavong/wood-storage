package com.jordylangen.woodstorage;

import rx.Observable;

public interface Storage {

    void add(LogStatement logStatement);

    Observable<LogStatement> load();
}
