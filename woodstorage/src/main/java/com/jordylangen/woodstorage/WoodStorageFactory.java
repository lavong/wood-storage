package com.jordylangen.woodstorage;

import android.content.Context;

import io.reactivex.subjects.PublishSubject;

public final class WoodStorageFactory {

    private static WoodStorageWorker WORKER;

    public static WoodStorageTree getInstance(Context context) {
        return getInstance(context, new StorageFactory());
    }

    public static WoodStorageTree getInstance(Context context, StorageFactory storageFactory) {
        PublishSubject<LogEntry> publishSubject = PublishSubject.create();
        WoodStorageTree tree = new WoodStorageTree(publishSubject);

        if (WORKER != null) {
            stop();
        }

        WORKER = new WoodStorageWorker(storageFactory.create(context), publishSubject);
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
