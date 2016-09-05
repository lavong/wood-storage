package com.jordylangen.woodstorage.view;

import android.util.SparseArray;

class PresenterCache {

    private final static SparseArray<Contract.Presenter> PRESENTERS = new SparseArray<>();

    static void put(int id, Contract.Presenter presenter) {
        PRESENTERS.put(id, presenter);
    }

    static Contract.Presenter get(int id) {
        if (PRESENTERS.indexOfKey(id) >= 0) {
            return PRESENTERS.get(id);
        }

        return null;
    }
}
