package com.graylabs.hasvi.Models;

import java.io.Serializable;

public class CardItem implements Serializable {
    private String mTextResource;
    private String mTitleResource;
    private int mImgResId;

    public CardItem(String title, String text, int imgResId) {
        mTitleResource = title;
        mTextResource = text;
        mImgResId = imgResId;
    }

    public String getText() {
        return mTextResource;
    }

    public String getTitle() {
        return mTitleResource;
    }

    public int getImgId(){
        return mImgResId;
    }
}
