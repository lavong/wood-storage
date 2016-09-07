package com.jordylangen.woodstorage.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jordylangen.woodstorage.LogEntry;
import com.jordylangen.woodstorage.R;

public class WoodStorageView extends BaseView<WoodStorageContract.View, WoodStorageContract.Presenter> implements WoodStorageContract.View {

    private LogEntryAdapter adapter;

    public WoodStorageView(Context context) {
        this(context, null);
    }

    public WoodStorageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WoodStorageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setup() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.woodstorage_overview_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LogEntryAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected WoodStorageContract.Presenter newPresenter() {
        return new WoodStoragePresenter();
    }

    @Override
    public void add(LogEntry logEntry) {
        adapter.add(logEntry);
    }

    @Override
    public void addAt(LogEntry logEntry, int index) {
        adapter.add(logEntry, index);
    }

    @Override
    public void clear() {
        adapter.clear();
    }
}
