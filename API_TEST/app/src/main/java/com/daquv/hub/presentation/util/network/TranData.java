package com.daquv.hub.presentation.util.network;

public class TranData<T> {

    private String mTranId;
    protected T mTranData;

    protected void setId (String id) {
        mTranId = id;
    }

    public String getId () {
        return mTranId;
    }

    public void set(T t) {
        this.mTranData = t;
    }

    public T get() {
        return mTranData;
    }
}
