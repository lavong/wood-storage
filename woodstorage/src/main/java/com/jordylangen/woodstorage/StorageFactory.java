package com.jordylangen.woodstorage;

public class StorageFactory {

    public Storage create() {
        return new InMemoryStorage();
    }
}
