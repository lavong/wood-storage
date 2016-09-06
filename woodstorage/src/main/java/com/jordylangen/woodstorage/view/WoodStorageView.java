package com.jordylangen.woodstorage.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.jordylangen.woodstorage.LogStatement;

import java.util.HashMap;
import java.util.Map;

public class WoodStorageView extends FrameLayout implements WoodStorageContract.View {

    private WoodStorageContract.Presenter presenter;

    private LogStatementAdapter adapter;

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
    protected void onFinishInflate() {
        super.onFinishInflate();

        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LogStatementAdapter();
        recyclerView.setAdapter(adapter);

        addView(recyclerView);

        presenter = (WoodStorageContract.Presenter) PresenterCache.get(getId());
        if (presenter == null) {
            presenter = new WoodStoragePresenter();
            PresenterCache.put(getId(), presenter);
        }

        presenter.setup(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.teardown();
        super.onDetachedFromWindow();
    }

    @Override
    public void show(LogStatement logStatement) {
        adapter.add(logStatement);
    }
}
