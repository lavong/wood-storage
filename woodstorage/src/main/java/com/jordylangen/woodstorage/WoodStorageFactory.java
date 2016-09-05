package com.jordylangen.woodstorage;

import java.util.ArrayList;
import java.util.List;

import rx.subjects.PublishSubject;

public class WoodStorageFactory {

    private static final List<WoodStorageWorker> workers = new ArrayList<>();

    public static WoodStorageTree create() {
        PublishSubject<LogStatement> publishSubject = PublishSubject.create();
        WoodStorageTree tree = new WoodStorageTree(publishSubject);
        WoodStorageWorker worker = new WoodStorageWorker(publishSubject.asObservable());
        worker.start();

        workers.add(worker);

        return tree;
    }

    public static synchronized void stop() {
        for (WoodStorageWorker worker : workers) {
            worker.stop();
        }

        workers.clear();
    }
}
