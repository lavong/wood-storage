package com.jordylangen.woodstorage.view;

public interface TagFilterContract extends Contract {

    interface View extends Contract.View {

        void add(TagFilterViewModel selectableTag);
    }

    interface Presenter extends Contract.Presenter<View> {

    }
}
