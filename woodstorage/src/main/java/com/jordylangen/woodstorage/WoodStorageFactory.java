package com.jordylangen.woodstorage;

import rx.subjects.PublishSubject;

public final class WoodStorageFactory {

    private static WoodStorageWorker WORKER;

    public static WoodStorageTree getInstance() {
        return getInstance(new StorageFactory());
    }

    public static WoodStorageTree getInstance(StorageFactory storageFactory) {
        PublishSubject<LogStatement> publishSubject = PublishSubject.create();
        WoodStorageTree tree = new WoodStorageTree(publishSubject);

        if (WORKER != null) {
            stop();
        }

        WORKER = new WoodStorageWorker(storageFactory.create(), publishSubject.asObservable());
        WORKER.start();

        return tree;
    }

    public static synchronized void stop() {
        WORKER.stop();
    }

    public static WoodStorageWorker getWorker() {
        return WORKER;
    }
}
