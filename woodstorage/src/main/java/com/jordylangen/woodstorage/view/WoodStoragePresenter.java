package com.jordylangen.woodstorage.view;

import com.jordylangen.woodstorage.LogStatement;
import com.jordylangen.woodstorage.WoodStorageFactory;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

class WoodStoragePresenter implements WoodStorageContract.Presenter {

    private Subscription subscription;

    @Override
    public void setup(final WoodStorageContract.View view) {
        Observable<LogStatement> observable = WoodStorageFactory.getWorker() != null
                ? WoodStorageFactory.getWorker().getStorage().load()
                : Observable.<LogStatement>empty();

        subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LogStatement>() {
                    @Override
                    public void call(LogStatement logStatement) {
                        view.show(logStatement);
                    }
                });
    }

    @Override
    public void teardown() {
        subscription.unsubscribe();
    }
}
