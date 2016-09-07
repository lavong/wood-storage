package com.jordylangen.woodstorage.view;

import android.content.Context;
import android.util.AttributeSet;

public class TagFilterView extends BaseView<TagFilterContract.View, TagFilterContract.Presenter> implements TagFilterContract.View {

    public TagFilterView(Context context) {
        this(context, null);
    }

    public TagFilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setup() {

    }

    @Override
    protected TagFilterContract.Presenter newPresenter() {
        return new TagFilterPresenter();
    }

    @Override
    public void add(TagFilterViewModel selectableTag) {

    }
}
