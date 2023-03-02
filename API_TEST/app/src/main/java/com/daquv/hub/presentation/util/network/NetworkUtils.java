package com.daquv.hub.presentation.util.network;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.daquv.api.test.R;
import com.daquv.hub.presentation.util.Logger;
import com.daquv.hub.presentation.util.network.constant.ComTranKey;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
* 통신 처리 유틸리티
**/
public class NetworkUtils {


    public static JSONObject makeTranData(TranJson reqJobj) throws JSONException {
        Logger.dev("makeTranData >> tranJobj :: " + reqJobj.getJSONObject());
        JSONObject tranJobj = new JSONObject();
        tranJobj.put(ComTranKey.REQ.CNTS_CRTS_KEY_CODE, ComTranKey.REQ.API_REQ_AUTH_KEY);
        tranJobj.put(ComTranKey.REQ.DEVICE_INST_ID, "");
        tranJobj.put(ComTranKey.REQ.TRAN_NO, reqJobj.getId());
        // 암호화 하지 않음
        tranJobj.put(ComTranKey.REQ.ENC_YN, "N");
        tranJobj.put(ComTranKey.REQ.REQ_DATA, reqJobj.getJSONObject());
        return tranJobj;
    }

    public static JsonObject getGsonObject(JSONObject jsonObject) {
        try {
            Object object = JSONHelper.toJSON(jsonObject);
            return new Gson().fromJson(object.toString(), JsonObject.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Toast {
        public static void LoginErrorToast(Context context, String message) {
            if (message != null) {
                if (message.contains("500")) {
                    android.widget.Toast.makeText(context, context.getString(R.string.fail_login_info), android.widget.Toast.LENGTH_SHORT).show();
                } else if (message.contains("404")) {
                    android.widget.Toast.makeText(context, context.getString(R.string.network_error), android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * WebView Cookie Clear
     * @param context Context
     * @param domain 도메인 URL
     */
    public static void clearCookie (Context context, String domain) {
        Logger.info("context isNull :: " + (context == null));
        Logger.info("context domain :: " + domain);

        if (context == null || TextUtils.isEmpty(domain))
            return;


        Logger.dev("SDK_INT :: " + Build.VERSION.SDK_INT);
        String strCookie = getCookie(domain);
        Logger.dev("Cookie String :: " + strCookie);

        if (!TextUtils.isEmpty(strCookie)) {
            String [] cookies = strCookie.split(";");
            for (String cookie : cookies) {
                Logger.dev("Cookie :: " + cookie);
                String [] cookieParts = cookie.split("=");

                if (cookieParts.length > 0) {
                    String cookieKey = "";
                    String cookieValue = "";
                    if (cookieParts.length == 1) {
                        cookieKey = cookieParts[0];
                    }
                    else if (cookieParts.length == 2) {
                        cookieKey = cookieParts[0];
                        cookieValue = cookieParts[1];
                    }
                    Logger.dev("Cookie Key :: " + cookieKey);
                    Logger.dev("Cookie Value :: " + cookieValue);
                }
            }

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setCookie(domain, "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeSessionCookies(null);
                cookieManager.flush();
            } else {
                cookieManager.removeSessionCookie();
                CookieSyncManager cookieSyncManager;
                try {
                    cookieSyncManager = CookieSyncManager.getInstance();
                } catch (Exception e) {
                    cookieSyncManager = CookieSyncManager.createInstance(context);
                }
                cookieSyncManager.stopSync();
                cookieSyncManager.sync();
            }

            // TODO :: TEST
            strCookie = getCookie(domain);
            Logger.dev("After Cookie String :: " + strCookie);
        }
    }

    /**
     * 웹뷰 쿠키 반환
     * @param domain 도메인
     * @return 웹뷰 쿠키
     */
    public static String getCookie (String domain) {
        Logger.info("domain :: " + domain);

        String cookie = null;
        Logger.dev("CookieManager.getInstance() isNull :: " +
                (CookieManager.getInstance() == null));
        if (!TextUtils.isEmpty(domain) && CookieManager.getInstance() != null)
            cookie = CookieManager.getInstance().getCookie(domain);

        Logger.dev("cookie :: " + cookie);

        return cookie;
    }
}
