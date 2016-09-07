package com.jordylangen.woodstorage.view;

import com.jordylangen.woodstorage.LogEntry;

public interface WoodStorageContract {

    interface View extends Contract.View {

        void add(LogEntry logEntry);

        void addAt(LogEntry logEntry, int index);

        void clear();
    }

    interface Presenter extends Contract.Presenter<View> {

        void onOptionsItemSelected(int itemId);
    }
}
