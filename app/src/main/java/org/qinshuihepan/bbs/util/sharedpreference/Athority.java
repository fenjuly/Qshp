package org.qinshuihepan.bbs.util.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import org.qinshuihepan.bbs.App;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by liurongchan on 14-4-25.
 */
public class Athority {

    public static final String ACCOUNT_INFORMATION = "accout_information";

    private static SharedPreferences mShared;


    public static SharedPreferences getSharedPreference() {
        if (mShared == null) {
            mShared = App.getContext().getSharedPreferences(ACCOUNT_INFORMATION, Context.MODE_PRIVATE);
        }
        return mShared;
    }

    public static void addCookies(Map<String, String> cookies) {
        if (mShared == null) {
            getSharedPreference();
        }
        SharedPreferences.Editor editor = mShared.edit();
        Iterator<String> iterator = cookies.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            editor.putString(key, cookies.get(key));
        }
        editor.apply();
    }

    public static void addOther(String key, String value) {
        if (mShared == null) {
            getSharedPreference();
        }
        SharedPreferences.Editor editor = mShared.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getInformation(String key) {
        String information = "";
        if (mShared == null) {
            getSharedPreference();
        }
        information = mShared.getString(key, "no");
        return information;
    }

}
