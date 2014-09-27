package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.api.Api;
import org.qinshuihepan.bbs.data.Request;
import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.model.Post;
import org.qinshuihepan.bbs.ui.adapter.MyPostsAdapter;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liurongchan on 14-9-23.
 */
public class ProfileActivity extends Activity{

    String jifen;
    String weiwang;
    String shuidi;
    String cunzaigan;
    String bingjing;
    @InjectView(R.id.jifen_text)
    TextView jifen_text;
    @InjectView(R.id.weiwang_text)
    TextView weiwang_text;
    @InjectView(R.id.shuidi_text)
    TextView shuidi_text;
    @InjectView(R.id.cunzaigan_text)
    TextView cunzaigan_text;
    @InjectView(R.id.bingjign_text)
    TextView bingjing_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ButterKnife.inject(this);
        final String uid = Athority.getSharedPreference().getString("uid","");
        TaskUtils.executeAsyncTask(new AsyncTask<String, Void, ArrayList<BasePost>>() {
            @Override
            protected ArrayList<BasePost> doInBackground(String... params) {
                ArrayList<BasePost> posts = new ArrayList<BasePost>();
                Document doc = null;
                Connection.Response response = Request.execute(String.format(Api.PROFILE, uid), Api.USER_AGENT, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                try {
                    doc = response.parse();
                    Log.e("doc", doc.text());
                    Elements user_boxs = doc.getElementsByClass("user_box");
                    String author = Athority.getSharedPreference().getString("username", "");
                    for (Element user_box : user_boxs) {
                        int i = 1;
                        for (Element span : user_box.getElementsByTag("span")) {
                            switch (i) {
                                case 1 :
                                    jifen = span.text();
                                    i++;
                                    break;
                                case 2 :
                                    weiwang = span.text();
                                    i++;
                                    break;
                                case 3 :
                                    shuidi = span.text();
                                    i++;
                                    break;
                                case 4 :
                                    cunzaigan = span.text();
                                    i++;
                                    break;
                                case 5 :
                                    bingjing = span.text();
                                    i++;
                                    break;
                            }
                        }
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return posts;
            }

            @Override
            protected void onPostExecute(ArrayList<BasePost> posts) {
                super.onPostExecute(posts);
                jifen_text.setText(jifen);
                weiwang_text.setText(weiwang);
                shuidi_text.setText(shuidi);
                cunzaigan_text.setText(cunzaigan);
                bingjing_text.setText(bingjing);
            }
        });
    }
}
