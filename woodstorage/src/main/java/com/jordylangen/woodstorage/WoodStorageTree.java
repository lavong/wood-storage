package com.jordylangen.woodstorage;

import rx.subjects.PublishSubject;
import timber.log.Timber;

public class WoodStorageTree extends Timber.DebugTree {

    private PublishSubject<LogStatement> logStatementPublishSubject;

    WoodStorageTree(PublishSubject<LogStatement> logStatementPublishSubject) {
        this.logStatementPublishSubject = logStatementPublishSubject;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        LogStatement logStatement = new LogStatement(tag, priority, message, t);
        logStatementPublishSubject.onNext(logStatement);
    }
}
