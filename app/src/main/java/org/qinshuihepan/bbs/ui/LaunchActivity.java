package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

/**
 * Created by liurongchan on 14-5-7.
 * Modified on 14-9-15
 */
public class LaunchActivity extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mContext = this;
        final SharedPreferences mShared = mContext.getSharedPreferences(
                Athority.ACCOUNT_INFORMATION, Context.MODE_PRIVATE);
        if (mShared.getString(Athority.PREF_HAS_LOGINED, "no").equals("yes")) {
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

    //屏蔽所有按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
