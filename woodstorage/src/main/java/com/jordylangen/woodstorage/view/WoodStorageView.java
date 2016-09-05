package com.jordylangen.woodstorage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class WoodStorageView extends FrameLayout implements WoodStorageContract.View {

    private WoodStorageContract.Presenter presenter;

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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
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
}
