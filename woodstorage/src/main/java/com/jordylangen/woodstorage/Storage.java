package com.jordylangen.woodstorage;

import rx.Observable;

public interface Storage {

    void save(LogStatement logStatement);

    Observable<LogStatement> load();
}
