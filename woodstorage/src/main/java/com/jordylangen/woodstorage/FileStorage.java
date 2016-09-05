package com.jordylangen.woodstorage;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class FileStorage implements Storage {

    private static final String TAG = "FileStorage";

    private File file;

    public FileStorage(String pathToFile) {
        file = new File(pathToFile);
    }

    @Override
    public synchronized void add(LogStatement logStatement) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.newLine();
            out.write(logStatement.serialize());
            out.flush();
            out.close();
        } catch (IOException exception) {
            Log.e(TAG, "could not write to file at " + file.getAbsolutePath(), exception);
        }
    }

    @Override
    public Observable<LogStatement> load() {
        List<LogStatement> logs = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader in  = new BufferedReader(fileReader);

            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("")) {
                    continue;
                }

                LogStatement logStatement = LogStatement.deserialize(line);
                logs.add(logStatement);
            }
        } catch (IOException exception) {
            Log.e(TAG, "could not write to file at " + file.getAbsolutePath(), exception);
            return Observable.empty();
        }

        return Observable.from(logs);
    }
}
