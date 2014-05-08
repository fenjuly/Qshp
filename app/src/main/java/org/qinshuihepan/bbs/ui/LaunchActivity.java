package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

/**
 * Created by liurongchan on 14-5-7.
 */
public class LaunchActivity extends Activity {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getActionBar().hide();
        setContentView(R.layout.launch);
        final SharedPreferences mShared = mContext.getSharedPreferences(
                Athority.ACCOUNT_INFORMATION, Context.MODE_PRIVATE);
        if (mShared.getString("login", "no").equals("yes")) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(mContext,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(mContext,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }

    }

}
