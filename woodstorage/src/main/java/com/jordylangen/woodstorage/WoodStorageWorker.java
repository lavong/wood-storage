package com.jordylangen.woodstorage;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WoodStorageWorker {

    private Storage storage;
    private Observable<LogEntry> logObserver;
    private Disposable subscription;

    public WoodStorageWorker(Storage storage, Observable<LogEntry> logObserver) {
        this.storage = storage;
        this.logObserver = logObserver;
    }

    public void start() {
        subscription = logObserver.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new io.reactivex.functions.Consumer<LogEntry>() {
                    @Override
                    public void accept(LogEntry logEntry) throws Exception {
                        storage.save(logEntry);
                    }
                });
    }

    public void stop() {
        subscription.dispose();
    }

    public Storage getStorage() {
        return storage;
    }
}
