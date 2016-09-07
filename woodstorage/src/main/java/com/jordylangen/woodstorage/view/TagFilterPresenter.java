package com.jordylangen.woodstorage.view;

import com.jordylangen.woodstorage.LogEntry;
import com.jordylangen.woodstorage.WoodStorageFactory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TagFilterPresenter implements TagFilterContract.Presenter {

    private TagFilterContract.View view;
    private Subscription subscription;
    private List<TagFilterViewModel> selectedTags;

    TagFilterPresenter() {
        selectedTags = new ArrayList<>();
    }

    @Override
    public void setup(TagFilterContract.View view) {
        this.view = view;
        subscribe();
    }

    @Override
    public void teardown() {
        unsubscribe();
    }

    private void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void subscribe() {
        Observable<LogEntry> observable = WoodStorageFactory.getWorker() != null
                ? WoodStorageFactory.getWorker().getStorage().load()
                : Observable.<LogEntry>empty();

        subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<LogEntry, Boolean>() {
                    @Override
                    public Boolean call(LogEntry logEntry) {
                        if (logEntry.getTag() == null) {
                            return false;
                        }

                        for (TagFilterViewModel selectableTag : selectedTags) {
                            if (selectableTag.getTag().equals(logEntry.getTag())) {
                                return false;
                            }
                        }

                        return true;
                    }
                })
                .subscribe(new Action1<LogEntry>() {
                    @Override
                    public void call(LogEntry logEntry) {
                        TagFilterViewModel selectableTag = new TagFilterViewModel(logEntry.getTag(), true);
                        selectedTags.add(selectableTag);
                        view.add(selectableTag);
                    }
                });
    }
}
