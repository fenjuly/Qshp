package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.api.Api;
import org.qinshuihepan.bbs.data.Request;
import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.model.Post;
import org.qinshuihepan.bbs.ui.adapter.MessageContentAdapter;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by liurongchan on 14-9-23.
 */
public class MyMessageActivity extends Activity {
    ListView mListView;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_myposts);
        mListView = (ListView) findViewById(R.id.listView);
        TaskUtils.executeAsyncTask(new AsyncTask<String, Void, ArrayList<BasePost>>() {
            @Override
            protected ArrayList<BasePost> doInBackground(String... params) {
                ArrayList<BasePost> posts = new ArrayList<BasePost>();
                Document doc = null;
                Connection.Response response = Request.execute(Api.MY_MESSAGES, Api.USER_AGENT, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                try {
                    doc = response.parse();
                    Log.e("doc",doc.text());
                    Elements bm_cs = doc.getElementsByClass("bm_c");
                    String str_tid = "";
                    String title = "";
                    String time = "";
                    String comment_count = "";
                    int haveimg = 0;
                    int tid = 0;
                    String author = "";
                    BasePost post;
                    for (Element bm_c : bm_cs) {
                        Elements as = bm_c.getElementsByTag("a");
                        for (Element a : as) {
                            String start = "home.php?mod=space&do=pm&subop=view&touid=";
                            String len = " mobile=1";
                            String url = a.attr("href");
                            str_tid = url.substring(start.length(), url.length() - len.length());
                            Log.e("tid", str_tid);
                            tid = Integer.valueOf(str_tid);
                            title = a.text();
                        }
                        int i = 1;
                        for (Element span : bm_c.getElementsByTag("span")) {
                            if (i == 1) {
                                i++;
                                author = span.text();
                            } else {
                                time = span.text();
                            }
                        }
                        post = new Post(0, tid, 0, title, "", time, haveimg, 0, author, null);
                        posts.add(post);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return posts;
            }

            @Override
            protected void onPostExecute(ArrayList<BasePost> posts) {
                super.onPostExecute(posts);
                MessageContentAdapter messageContentAdapter = new MessageContentAdapter(mContext, posts);
                mListView.setAdapter(messageContentAdapter);
            }
        });
    }
}
