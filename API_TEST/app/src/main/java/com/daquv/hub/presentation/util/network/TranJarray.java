package com.daquv.hub.presentation.util.network;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TranJarray  extends TranData <List<Object>> {

    protected int mIdx = 0;

    public TranJarray(String id) {
        setId(id);
        mTranData = new ArrayList<Object>();
    }

    public TranJarray(String id, Object obj) throws JSONException {
        setId(id);
        if (obj instanceof JSONArray) {
            mTranData = JSONHelper.toList((JSONArray) obj);
        } else {
            mTranData = (ArrayList<Object>) obj;
        }
    }

    public List<Object> getData() {
        return get();
    }

    public void put (Object val) {
        mTranData.add(val);
    }

    /**
     * 현재 인덱스 리턴 <br>
     * @return 현재 Index (mRecvIdx)
     */
    public int getIndex() {
        return mIdx;
    }

    /**
     * 수신 data index (mRecvIdx) 에러 여부 리턴
     * <br><br>
     * - EOR (End Of Record) 여부 리턴
     *
     * @return true (EOR) / false (Not EOR)
     * @throws JSONException
     */
    public boolean isEOR() throws JSONException {
        if(mIdx == mTranData.size())
            return true;
        return false;
    }

    /**
     * 수신 Data 처음 Index 로 이동
     */
    public void moveFirst() {
        mIdx = 0;
    }

    /**
     * 수신 Data 다음 Index 로 이동
     */
    public void moveNext() {
        mIdx++;
    }

    /**
     * 수신 Data 마지막 Index 로 이동
     * @throws JSONException
     */
    public void moveLast() throws JSONException {
        mIdx = mTranData.size() - 1;
    }

    /**
     * 수신 Data 이전 Index 로 이동
     */
    public void movePrev() {
        mIdx--;
    }

    /**
     * 수신 Data 특정 위치로 이동 <br>
     * @param pos 이동할 Index
     */
    public void move(int pos) {
        mIdx = pos;
    }

    public String getString () {
        return (String) mTranData.get(getIndex());
    }

    public String getString (String key) {
        HashMap<String, Object> map = getMap();
        if (map != null) {
            return (String) map.get(key);
        }
        return null;
    }

    public int getInt () {
        return (int) mTranData.get(getIndex());
    }

    public int getInt (String key) {
        HashMap<String, Object> map = getMap();
        if (map != null) {
            return (int) map.get(key);
        }
        return 0;
    }

    public boolean getBoolean () {
        return (boolean) mTranData.get(getIndex());
    }

    public boolean getBoolean (String key) {
        HashMap<String, Object> map = getMap();
        if (map != null) {
            return (boolean) map.get(key);
        }
        return false;
    }

    public HashMap <String, Object> getMap () {
        return (HashMap <String, Object>) mTranData.get(getIndex());
    }

    public HashMap<String, Object> getMap (String key) {
        HashMap<String, Object> map = getMap();
        if (map != null) {
            return (HashMap<String, Object>) map.get(key);
        }
        return null;
    }

    public ArrayList <Object> getList () {
        return (ArrayList <Object>) mTranData.get(getIndex());
    }


    public ArrayList <Object> getList (String key) {
        HashMap<String, Object> map = getMap();
        if (map != null) {
            return (ArrayList <Object>) map.get(key);
        }
        return null;
    }

    public JSONArray getJSONArray() throws JSONException {
        return (JSONArray) JSONHelper.toJSON(get());
    }
}
