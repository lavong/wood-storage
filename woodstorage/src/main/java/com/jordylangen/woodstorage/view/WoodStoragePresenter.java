package com.jordylangen.woodstorage.view;

import com.jordylangen.woodstorage.LogEntry;
import com.jordylangen.woodstorage.R;
import com.jordylangen.woodstorage.WoodStorageFactory;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

class WoodStoragePresenter implements WoodStorageContract.Presenter {

    private WoodStorageContract.View view;
    private Subscription subscription;
    private boolean isSortOrderAscending;

    WoodStoragePresenter () {
        isSortOrderAscending = true;
    }

    @Override
    public void setup(final WoodStorageContract.View view) {
        this.view = view;
        subscribe();
    }

    @Override
    public void teardown() {
        subscription.unsubscribe();
    }

    @Override
    public void onOptionsItemSelected(int itemId) {
        if (itemId == R.id.woodstorage_action_sort) {
            invertSortOrder();
        }

        if (subscription != null) {
            subscription.unsubscribe();
        }

        view.clear();
        subscribe();
    }

    private void subscribe() {
        Observable<LogEntry> observable = WoodStorageFactory.getWorker() != null
                ? WoodStorageFactory.getWorker().getStorage().load()
                : Observable.<LogEntry>empty();

        subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LogEntry>() {
                    @Override
                    public void call(LogEntry logEntry) {
                        if (isSortOrderAscending) {
                            view.add(logEntry);
                        } else {
                            view.addAt(logEntry, 0);
                        }
                    }
                });
    }

    private void invertSortOrder() {
        isSortOrderAscending = !isSortOrderAscending;
    }
}
