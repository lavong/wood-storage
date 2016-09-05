package com.jordylangen.woodstorage;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WoodStorageWorker implements Action1<LogStatement> {

    private Storage storage;
    private Observable<LogStatement> logObserver;
    private Subscription subscription;

    public WoodStorageWorker(Storage storage, Observable<LogStatement> logObserver) {
        this.storage = storage;
        this.logObserver = logObserver;
    }

    public void start() {
        subscription = logObserver.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this);
    }

    public void stop() {
        subscription.unsubscribe();
    }

    @Override
    public void call(LogStatement logStatement) {
        storage.add(logStatement);
    }

    public Storage getStorage() {
        return storage;
    }
}
