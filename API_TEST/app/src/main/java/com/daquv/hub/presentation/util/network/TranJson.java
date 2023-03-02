package com.daquv.hub.presentation.util.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TranJson extends TranData <HashMap<String, Object>> {

    public TranJson() {
        mTranData = new HashMap<>();
    }

    public TranJson(String id) {
        setId(id);
    }

    public TranJson (String id, Object obj) throws JSONException {
        setId(id);
        if (obj instanceof JSONObject) {
            if(obj.equals(JSONObject.NULL) || obj.equals("")) {
                mTranData = JSONHelper.toHashMap(new JSONObject());
            } else {
                mTranData = JSONHelper.toHashMap((JSONObject) obj);
            }
        } else {
            mTranData = (HashMap<String, Object>) obj;
        }
    }

    /**
     * HashMap 으로 구성된 송신 Data 리턴한다. <br>
     * @return HashMap<String, Object> 송신 Data
     */
    public HashMap<String, Object> getData() {
        return get();
    }

    public void put (String key, Object val) {
        mTranData.put(key, val);
    }

    public String getString (String key) {
        if (mTranData.containsKey(key))
            return (String) mTranData.get(key);
        return null;
    }

    public int getInt (String key) {
        if (mTranData.containsKey(key))
            return (int) mTranData.get(key);
        return 0;
    }

    public boolean getBoolean (String key) {
        if (mTranData.containsKey(key))
            return (boolean) mTranData.get(key);
        return false;
    }

    public HashMap<String, Object> getMap (String key) {
        if (mTranData.containsKey(key))
            return (HashMap<String, Object>) mTranData.get(key);
        return null;
    }

    public ArrayList <Object> getList (String key) {
        if (mTranData.containsKey(key))
            return (ArrayList <Object> ) mTranData.get(key);
        return null;
    }

    public JSONObject getJSONObject() throws JSONException {
        return (JSONObject) JSONHelper.toJSON(get());
    }

    public JsonObject getGsonObject() throws JSONException {
        JSONObject object = (JSONObject) JSONHelper.toJSON(get());
        JsonObject gsonObject = new Gson().fromJson(object.toString(), JsonObject.class);
        Log.d("TranJson", "getGsonObject: " + gsonObject.toString());
        return gsonObject;
    }
}
