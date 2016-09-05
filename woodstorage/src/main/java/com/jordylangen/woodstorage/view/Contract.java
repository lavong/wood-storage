package com.jordylangen.woodstorage.view;

interface Contract {

    interface View {

    }

    interface Presenter<V extends View> {

        void setup(V view);

        void teardown();
    }
}
