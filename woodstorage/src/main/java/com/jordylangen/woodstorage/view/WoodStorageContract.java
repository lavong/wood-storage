package com.jordylangen.woodstorage.view;

import com.jordylangen.woodstorage.LogStatement;

interface WoodStorageContract {

    interface View extends Contract.View {

        void show(LogStatement logStatement);
    }

    interface Presenter extends Contract.Presenter<View> {

    }
}
