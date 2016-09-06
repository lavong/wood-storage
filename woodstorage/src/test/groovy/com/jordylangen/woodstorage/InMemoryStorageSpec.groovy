package com.jordylangen.woodstorage

import android.util.Log
import rx.functions.Action1

class InMemoryStorageSpec extends RxSpecification {

    InMemoryStorage storage;

    def setup() {
        storage = new InMemoryStorage()
    }

    def "should not contain more then max amount of items"() {
        when:
        for (def i = 0; i < InMemoryStorage.MAX_ITEMS * 2; i++) {
            storage.add(new LogStatement("test", Log.DEBUG, Integer.toString(i), null))
        }

        List<LogStatement> logs = [];
        storage.load()
                .subscribe(new Action1<LogStatement>() {
                    @Override
                    void call(LogStatement logStatement) {
                        logs.add(logStatement)
                    }
                })

        then:
        !logs.isEmpty()
        logs.size() == InMemoryStorage.MAX_ITEMS
        logs[0].message == Integer.toString(InMemoryStorage.MAX_ITEMS)
    }
}