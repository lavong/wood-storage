package com.jordylangen.woodstorage.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.jordylangen.woodstorage.LogEntry;
import com.jordylangen.woodstorage.R;
import com.jordylangen.woodstorage.WoodStorageFactory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

class WoodStoragePresenter implements WoodStorageContract.Presenter {

    private WoodStorageContract.View view;
    private Subscription logEntriesSubscription;
    private Subscription selectedTagsSubscription;
    private boolean isSortOrderAscending;
    private List<String> selectedTags;

    WoodStoragePresenter() {
        isSortOrderAscending = true;
        selectedTags = new ArrayList<>();
    }

    @Override
    public void setup(final WoodStorageContract.View view) {
        this.view = view;
        subscribe();
    }

    @Override
    public void teardown() {
        unsubscribe();

        if (selectedTagsSubscription != null) {
            selectedTagsSubscription.unsubscribe();
        }
    }

    @Override
    public void onOptionsItemSelected(int itemId) {
        if (itemId == R.id.woodstorage_action_filter) {
            showTagFilterDialog();
            return;
        }

        unsubscribe();
        if (itemId == R.id.woodstorage_action_sort) {
            invertSortOrder();
        } else if (itemId == R.id.woodstorage_action_clear && WoodStorageFactory.getWorker() != null) {
            WoodStorageFactory.getWorker().getStorage().clear();
        }

        view.clear();
        subscribe();
    }

    private void unsubscribe() {
        if (logEntriesSubscription != null) {
            logEntriesSubscription.unsubscribe();
        }
    }

    private void subscribe() {
        Observable<LogEntry> observable = WoodStorageFactory.getWorker() != null
                ? WoodStorageFactory.getWorker().getStorage().load()
                : Observable.<LogEntry>empty();

        Observable<LogEntry> logEntryObservable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        if (selectedTags != null && !selectedTags.isEmpty()) {
            logEntryObservable = logEntryObservable.filter(new Func1<LogEntry, Boolean>() {
                @Override
                public Boolean call(LogEntry logEntry) {
                    return selectedTags.contains(logEntry.getTag());
                }
            });
        }

        logEntriesSubscription = logEntryObservable.subscribe(new Action1<LogEntry>() {
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

    private void showTagFilterDialog() {
        Context context = view.getContext();
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(LayoutInflater.from(context).inflate(R.layout.view_tag_filter, null))
                .create();

        dialog.show();

        TagFilterContract.Presenter tagFilterPresenter = PresenterCache.get(R.id.dialog_tag_filter);
        if (tagFilterPresenter == null) {
            return;
        }

        selectedTagsSubscription = tagFilterPresenter.observeSelectedTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<SelectableTag>, Observable<List<String>>>() {
                    @Override
                    public Observable<List<String>> call(List<SelectableTag> selectableTags) {
                        return Observable.from(selectableTags)
                                .filter(new Func1<SelectableTag, Boolean>() {
                                    @Override
                                    public Boolean call(SelectableTag selectableTag) {
                                        return selectableTag.isSelected();
                                    }
                                })
                                .map(new Func1<SelectableTag, String>() {
                                    @Override
                                    public String call(SelectableTag selectableTag) {
                                        return selectableTag.getTag();
                                    }
                                })
                                .toList();
                    }
                })
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> tags) {
                        unsubscribe();
                        selectedTags = tags;
                        view.clear();
                        subscribe();
                    }
                });
    }
}
