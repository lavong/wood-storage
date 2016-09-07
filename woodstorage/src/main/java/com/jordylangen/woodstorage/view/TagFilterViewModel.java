package com.jordylangen.woodstorage.view;

public class TagFilterViewModel {

    private String tag;
    private boolean isSelected;

    public TagFilterViewModel(String tag, boolean isSelected) {
        this.tag = tag;
        this.isSelected = isSelected;
    }

    public String getTag() {
        return tag;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
