package com.jordylangen.woodstorage.view;

import android.util.SparseArray;

public class PresenterCache {

    private final static SparseArray<Contract.Presenter> PRESENTERS = new SparseArray<>();

    static void put(int id, Contract.Presenter presenter) {
        PRESENTERS.put(id, presenter);
    }

    public static <P extends Contract.Presenter> P get(int id) {
        if (PRESENTERS.indexOfKey(id) >= 0) {
            return (P) PRESENTERS.get(id);
        }

        return null;
    }
}
