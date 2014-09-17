package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.api.Api;
import org.qinshuihepan.bbs.data.Request;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liurongchan on 14-4-29.
 * Modified on 14-9-15
 */
public class LoginActivity extends Activity {

    @InjectView(R.id.username)
    public EditText usernameText;
    @InjectView(R.id.password)
    public EditText passwordText;
    @InjectView(R.id.confirm)
    public CircularProgressButton confirm;
    private Context mContext;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        mContext = this;

        confirm.setIndeterminateProgressMode(true);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(mContext, "用户名不能为空！", Toast.LENGTH_SHORT)
                            .show();
                } else if (password.equals("")) {
                    Toast.makeText(mContext, "密码不能为空！", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    confirm.setProgress(50);
                    TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            return isLoginSuccess();
                        }

                        @Override
                        protected void onPostExecute(Boolean success) {
                            super.onPostExecute(success);
                            if (!success) {
                                confirm.setIdleText("认证失败,请重试！");
                                confirm.setProgress(0);
                                passwordText.setText("");
                                usernameText.setText("");
                                usernameText.requestFocus();
                            } else {
                                confirm.setProgress(100);
                                Intent intent = new Intent(mContext, MainActivity.class);
                                mContext.startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isLoginSuccess() {
        Map<String, String> datas = new HashMap<String, String>();
        try {
            Connection.Response before_login = Request.execute(Api.MOBILE_COOKIE_LOGIN, Api.USER_AGENT, Connection.Method.GET);
            Document doc = before_login.parse();
            String formhash = doc.getElementById("formhash").attr("value");
            String loginaction = doc.getElementsByTag("form").attr("action");
            Log.d("loginaction", loginaction);

            datas.put("formhash", formhash);
            datas.put("referer", Api.HOST);
            datas.put("fastloginfield", "username");
            datas.put("username", username);
            datas.put("password", password);
            datas.put("questionid", "0");
            datas.put("submit", "登陆");
            datas.put("answer", "");

            Map<String, String> cookies = before_login.cookies();
            Connection.Response logining = Request.execute(Api.HOST + loginaction, Api.USER_AGENT, datas, cookies, Connection.Method.POST);
            if (logining.cookie("v3hW_2132_auth") == null) {
                return false;
            } else {
                cookies = logining.cookies();
                cookies.put("v3hW_2132_saltkey", before_login.cookie("v3hW_2132_saltkey"));
                cookies.put(Athority.PREF_HAS_LOGINED, "yes");
                Athority.addCookies(cookies);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
