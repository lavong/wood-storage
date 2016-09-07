package com.jordylangen.woodstorage.view;

import com.jordylangen.woodstorage.LogStatement;

public interface WoodStorageContract {

    interface View extends Contract.View {

        void show(LogStatement logStatement);
    }

    interface Presenter extends Contract.Presenter<View> {

        void onOptionsItemSelected(int itemId);
    }
}
