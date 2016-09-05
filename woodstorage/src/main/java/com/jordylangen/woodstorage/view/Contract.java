package com.jordylangen.woodstorage.view;

public interface Contract {

    public interface View {

    }

    public interface Presenter<V extends View> {

        void setup(V view);

        void teardown();
    }
}
