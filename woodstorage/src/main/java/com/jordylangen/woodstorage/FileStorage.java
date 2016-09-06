package com.jordylangen.woodstorage;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

public class FileStorage implements Storage {

    private static final String TAG = "FileStorage";
    private static final int MAX_LOG_COUNT = 1028;
    private static final int DELETE_COUNT = 256;
    private static final int DELETE_OFFSET_BY_INDEX_AND_NEW_WRITE = 2;

    private File file;
    private StorageConfig storageConfig;
    private ReplaySubject<LogStatement> replaySubject;

    FileStorage(String pathToFile) {
        this(new StorageConfig(MAX_LOG_COUNT, DELETE_COUNT, pathToFile));
    }

    public FileStorage(StorageConfig storageConfig) {
        file = new File(storageConfig.getPathToFile());
        this.storageConfig = storageConfig;
    }

    @Override
    public synchronized void save(LogStatement logStatement) {
        int lineCount = getLineCount();
        ensureMaxLineCount(lineCount);
        write(logStatement);

        if (replaySubject != null) {
            replaySubject.onNext(logStatement);
        }
    }

    private synchronized int getLineCount() {
        try {
            FileReader fileReader = new FileReader(file);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
            while ((lineNumberReader.readLine()) != null) ;
            return lineNumberReader.getLineNumber();
        } catch (IOException exception) {
            Log.e(TAG, "could not get the line count for " + file.getAbsolutePath(), exception);
            return -1;
        }
    }

    private synchronized void ensureMaxLineCount(int currentLineCount) {
        if (currentLineCount < storageConfig.getMaxLogCount()) {
            return;
        }

        int startAtLine = currentLineCount - (storageConfig.getMaxLogCount() - storageConfig.getDeleteCount() - DELETE_OFFSET_BY_INDEX_AND_NEW_WRITE);
        StringBuilder stringBuffer = new StringBuilder();

        try {
            int lineCounter = 0;

            FileReader fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);

            String line;
            while ((line = in.readLine()) != null) {
                lineCounter++;

                if (lineCounter >= startAtLine) {
                    stringBuffer.append(line);
                    stringBuffer.append("\n");
                }
            }

            in.close();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fileWriter);

            // do we need to optimize this? write a set of chars?
            out.write(stringBuffer.toString());

            out.flush();
            out.close();

        } catch (IOException exception) {
            Log.e(TAG, "could not trim the file " + file.getAbsolutePath(), exception);
        }
    }

    private synchronized void write(LogStatement logStatement) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write(logStatement.serialize());
            out.write("\n");
            out.flush();
            out.close();
        } catch (IOException exception) {
            Log.e(TAG, "could not write to file at " + file.getAbsolutePath(), exception);
        }
    }

    @Override
    public Observable<LogStatement> load() {
        if (replaySubject == null) {
            replaySubject = ReplaySubject.create();

            Observable.fromCallable(new Callable<List<LogStatement>>() {
                @Override
                public List<LogStatement> call() throws Exception {
                    return loadLogsFromFile();
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(new Func1<List<LogStatement>, Observable<LogStatement>>() {
                        @Override
                        public Observable<LogStatement> call(List<LogStatement> logStatements) {
                            return Observable.from(logStatements);
                        }
                    })
                    .subscribe(new Action1<LogStatement>() {
                        @Override
                        public void call(LogStatement logStatement) {
                            replaySubject.onNext(logStatement);
                        }
                    });
        }

        return replaySubject.asObservable();
    }

    private List<LogStatement> loadLogsFromFile() {
        List<LogStatement> logs = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);

            String line;
            while ((line = in.readLine()) != null) {
                LogStatement logStatement = LogStatement.deserialize(line);
                logs.add(logStatement);
            }
        } catch (IOException exception) {
            Log.e(TAG, "could not write to file at " + file.getAbsolutePath(), exception);
            return new ArrayList<>();
        }

        return logs;
    }
}
