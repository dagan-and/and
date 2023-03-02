package com.daquv.hub.presentation.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.daquv.hub.App;
import com.daquv.hub.presentation.util.network.JSONHelper;
import com.daquv.hub.presentation.util.secure.AESUtils;
import com.daquv.hub.presentation.util.secure.HexUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPref {

    /** 데이터 저장 타입 구분 접두사 (코드) - JSONObject */
    private static final String PFIX_TP_CD_JOBJ     = "_JOBJ_";
    /** 데이터 저장 타입 구분 접두사 (코드) - JSONArray */
    private static final String PFIX_TP_CD_JARR     = "_JARR_";
    /** 데이터 저장 타입 구분 접두사 (코드) - List */
    private static final String PFIX_TP_CD_LIST     = "_LIST_";
    /** 데이터 저장 타입 구분 접두사 (코드) - Map */
    private static final String PFIX_TP_CD_MAP      = "_MAP_";
    /** 데이터 저장 타입 구분 접두사 (코드) - Byte Array */
    private static final String PFIX_TP_CD_BYTES    = "_BYTES_";

    /** UTF-8 Charset */
    private static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

    /** SharedPreference Instance */
    private static SharedPref mInstance;
    /** 암호화키 */
    private static String mCipherKey;
    /** SharedPreferences */
    private static SharedPreferences mPref;

    /**
     * 초기화
     * @param context Context
     */
    public static void initialize (Context context) {
        initialize(context, null);
    }

    /**
     * 초기화
     * @param context Context
     * @param cryptKey 암호화
     */
    public static void initialize (Context context, String cryptKey) {
        Logger.info("Context isNull :: " + (context == null) + " / " + "cryptKey :: " + cryptKey);
        mCipherKey = cryptKey;
    }

    /**
     * SharedPref Instance 생성, 반환 <br>
     * @return SharedPref Single Instance
     */
    public static SharedPref getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPref();
        }
        if(mPref == null) {
            mPref = App.Companion.context().getSharedPreferences("DAQUV", Context.MODE_PRIVATE);
        }
        return mInstance;
    }

    /**
     * 데이터 암호화 관리 여부 반환
     * @return 데이터 암호화 관리 여부 (true : 암호화하여 관리 / false : 평문으로 관리)
     */
    public boolean isCipherMode () {
        return !TextUtils.isEmpty(mCipherKey);
    }

    /**
     * 데이터 암호화
     * @param val 평문 데이터
     * @return 암호화 데이터 Hex String
     */
    private String encVal(String val) {
        String encVal = "";
        try {
            if (!TextUtils.isEmpty(val)) {
                encVal = AESUtils.encryptHex(mCipherKey, val);
            }
        } catch (Exception e) {
            Logger.error(e);
        }

        Logger.dev("Enc Val :: " + encVal);
        return encVal;
    }

    /**
     * 데이터 복호화
     * @param valEncHex 암호화 데이터 Hex String
     * @return 복호화 데이터
     */
    private String decVal (String valEncHex) {
        String decVal = "";
        try {
            if (!TextUtils.isEmpty(valEncHex)) {
                decVal = new String(AESUtils.decryptHex(mCipherKey, valEncHex));
            }
        } catch (Exception e) {
            Logger.error(e);
        }

        Logger.dev("Dec Val :: " + decVal);
        return decVal;
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     */
    public void put ( String key, String val) {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        if (isCipherMode()) {
            mPref.edit().putString(key, encVal(val)).commit();
        } else {
            mPref.edit().putString(key, val).commit();
        }
    }

    /**
     * String 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 "" 반환)
     */
    public String getString( String key) {
        Logger.info("Key :: " + key);

        if (isCipherMode()) {
            return decVal(mPref.getString(key, ""));
        } else {
            return mPref.getString(key, "");
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     */
    public void put ( String key, int val) {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        if (isCipherMode()) {
            mPref.edit().putString(key, encVal(String.valueOf(val))).commit();
        } else {
            mPref.edit().putInt(key, val).commit();
        }
    }

    /**
     * int 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 0 반환)
     */
    public int getInt( String key) {
        Logger.info("Key :: " + key);

        if (isCipherMode()) {
            return Integer.valueOf(decVal(mPref.getString(key, "0")));
        } else {
            return mPref.getInt(key, 0);
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     */
    public void put ( String key, long val) {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        if (isCipherMode()) {
            mPref.edit().putString(key, encVal(String.valueOf(val))).commit();
        } else {
            mPref.edit().putLong(key, val).commit();
        }
    }

    /**
     * long 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 0 반환)
     */
    public long getLong( String key) {
        Logger.info("Key :: " + key);

        if (isCipherMode()) {
            return Long.valueOf(decVal(mPref.getString(key, "0")));
        } else {
            return mPref.getLong(key, 0);
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     */
    public void put ( String key, float val) {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        if (isCipherMode()) {
            mPref.edit().putString(key, encVal(String.valueOf(val))).commit();
        } else {
            mPref.edit().putFloat(key, val).commit();
        }
    }

    /**
     * float 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 0 반환)
     */
    public float getFloat( String key) {
        Logger.info("Key :: " + key);

        if (isCipherMode()) {
            return Float.valueOf(decVal(mPref.getString(key, "0")));
        } else {
            return mPref.getFloat(key, 0);
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     */
    public void put ( String key, boolean val) {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        if (isCipherMode()) {
            mPref.edit().putString(key, encVal(String.valueOf(val))).commit();
        } else {
            mPref.edit().putBoolean(key, val).commit();
        }
    }

    /**
     * boolean 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 false 반환)
     */
    public boolean getBoolean( String key) {
        Logger.info("Key :: " + key);

        if (isCipherMode()) {
            return Boolean.valueOf(decVal(mPref.getString(key, "false")));
        } else {
            return mPref.getBoolean(key, false);
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     */
    public void put ( String key, byte [] val) {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        String setVal;
        if (isCipherMode()) {
            setVal = PFIX_TP_CD_BYTES + encVal(new String(val, CHARSET_UTF_8));
        } else {
            setVal = PFIX_TP_CD_BYTES + new String(val, CHARSET_UTF_8);
        }
        mPref.edit().putString(key, setVal).commit();
    }

    /**
     * byte [] 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 null 반환)
     */
    public byte [] getByte ( String key) {
        Logger.info("Key :: " + key);

        String saveVal = mPref.getString(key, "");
        if (saveVal.startsWith(PFIX_TP_CD_BYTES)) {
            String valNoPrefix = saveVal.replaceFirst(PFIX_TP_CD_BYTES, "");
            if (isCipherMode()) {
                return decVal(valNoPrefix).getBytes(CHARSET_UTF_8);
            } else {
                return valNoPrefix.getBytes(CHARSET_UTF_8);
            }
        } else {
            return null;
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     * @throws Exception
     */
    public void put ( String key, List<?> val) throws Exception {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        List<String> list = new ArrayList<>();
        for (Object obj : val) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            byte[] bytes = bos.toByteArray();
            String hexStr = HexUtils.toHex(bytes);
            list.add(hexStr);
        }
        String setVal;
        if (isCipherMode()) {
            setVal = PFIX_TP_CD_LIST + encVal(JSONHelper.toJSON(list).toString());
        } else {
            setVal = PFIX_TP_CD_LIST + JSONHelper.toJSON(list).toString();
        }
        mPref.edit().putString(key, setVal).commit();
    }

    /**
     * List 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 null 반환)
     */
    public List<?> getList ( String key) throws Exception {
        Logger.info("Key :: " + key);

        String saveVal = mPref.getString(key, "");
        if (saveVal.startsWith(PFIX_TP_CD_LIST)) {
            String valNoPrefix = saveVal.replaceFirst(PFIX_TP_CD_LIST, "");
            String strVal = "";
            if (isCipherMode()) {
                strVal = decVal(valNoPrefix);
            } else {
                strVal = valNoPrefix;
            }

            JSONArray jarrVal = new JSONArray(strVal);
            ArrayList<Object> list = new ArrayList<>();
            for (int i = 0; i < jarrVal.length(); i++) {
                String strHex = jarrVal.getString(i);
                ByteArrayInputStream bis = new ByteArrayInputStream(HexUtils.toByte(strHex));
                ObjectInput in = new ObjectInputStream(bis);
                Object valObj = in.readObject();
                list.add(valObj);
            }

            return list;
        } else {
            return null;
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     * @throws Exception
     */
    public void put ( String key, Map<String, ?> val) throws Exception {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        Set<String> keySet = val.keySet();
        Map<String, String> map = new HashMap<>();
        for (String dataKey : keySet) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(val.get(dataKey));
            byte[] bytes = bos.toByteArray();
            String hexStr = HexUtils.toHex(bytes);
            map.put(dataKey, hexStr);
        }

        String setVal;
        if (isCipherMode()) {
            setVal = PFIX_TP_CD_MAP + encVal(JSONHelper.toJSON(map).toString());
        } else {
            setVal = PFIX_TP_CD_MAP + JSONHelper.toJSON(map).toString();
        }
        mPref.edit().putString(key, setVal).commit();
    }

    /**
     * Map 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 null 반환)
     */
    public Map<String, ?> getMap ( String key) throws Exception {
        Logger.info("Key :: " + key);

        String saveVal = mPref.getString(key, "");
        if (saveVal.startsWith(PFIX_TP_CD_MAP)) {
            String valNoPrefix = saveVal.replaceFirst(PFIX_TP_CD_MAP, "");
            String strVal = "";
            if (isCipherMode()) {
                strVal = decVal(valNoPrefix);
            } else {
                strVal = valNoPrefix;
            }

            JSONObject jobjVal = new JSONObject(strVal);
            HashMap<String, Object> map = new HashMap<>();
            Iterator<String> keys = jobjVal.keys();
            while (keys.hasNext()) {
                String dataKey = keys.next();
                String strHex = jobjVal.getString(dataKey);
                ByteArrayInputStream bis = new ByteArrayInputStream(HexUtils.toByte(strHex));
                ObjectInput in = new ObjectInputStream(bis);
                Object data = in.readObject();
                map.put(dataKey, data);
            }

            return map;
        } else {
            return null;
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     */
    public void put ( String key, JSONObject val) {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        String setVal;
        if (isCipherMode()) {
            setVal = PFIX_TP_CD_JOBJ + encVal(val.toString());
        } else {
            setVal = PFIX_TP_CD_JOBJ + val.toString();
        }
        mPref.edit().putString(key, setVal).commit();
    }

    /**
     * JSONObject 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 null 반환)
     * @throws JSONException
     */
    public JSONObject getJSONObject ( String key) throws JSONException {
        Logger.info("Key :: " + key);

        String saveVal = mPref.getString(key, "");
        if (saveVal.startsWith(PFIX_TP_CD_JOBJ)) {
            String valNoPrefix = saveVal.replaceFirst(PFIX_TP_CD_JOBJ, "");
            if (isCipherMode()) {
                return new JSONObject(decVal(valNoPrefix));
            } else {
                return new JSONObject(valNoPrefix);
            }
        } else {
            return null;
        }
    }

    /**
     * 데이터 저장
     * @param key 데이터 Key
     * @param val 데이터
     */
    public void put ( String key, JSONArray val) {
        Logger.info("Key :: " + key + " / " + "Val :: " + val);

        String setVal;
        if (isCipherMode()) {
            setVal = PFIX_TP_CD_JARR + encVal(val.toString());
        } else {
            setVal = PFIX_TP_CD_JARR + val.toString();
        }
        mPref.edit().putString(key, setVal).commit();
    }

    /**
     * JSONArray 데이터 반환
     * @param key 데이터 Key
     * @return 데이터 (Key 에 해당하는 데이터가 없는 경우 null 반환)
     * @throws JSONException
     */
    public JSONArray getJSONArray ( String key) throws JSONException {
        Logger.info("Key :: " + key);

        String saveVal = mPref.getString(key, "");
        if (saveVal.startsWith(PFIX_TP_CD_JARR)) {
            String valNoPrefix = saveVal.replaceFirst(PFIX_TP_CD_JARR, "");
            if (isCipherMode()) {
                return new JSONArray(decVal(valNoPrefix));
            } else {
                return new JSONArray(valNoPrefix);
            }
        } else {
            return null;
        }
    }

    /**
     * 데이터 Key 존재 여부 반환
     * @param key 데이터 Key
     * @return 데이터 Key 존재 여부 (true : 데이터 Key 존재 / false : 데이터 Key 미존재)
     */
    public boolean contains( String key) {
        return mPref.contains(key);
    }

    /**
     * Preference 해당 KEY-Value 삭제 <br>
     */
    @SuppressLint("CommitPrefEdits")
    public void remove( String key) {
        mPref.edit().remove(key).commit();
    }

    /**
     * Preference Data 전체 삭제 <br>
     */
    @SuppressLint("CommitPrefEdits")
    public void clear() {
        mPref.edit().clear().commit();
    }

}
