package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.qinshuihepan.bbs.ui.adapter.MessageConversationAdapter;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by liurongchan on 14-9-28.
 */
public class MessageConversationActivity extends Activity {

    Context mContext;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_conversation);
        mContext = this;
        mListView = (ListView) findViewById(R.id.listView);
        final int touid = getIntent().getIntExtra("touid", 0);
        TaskUtils.executeAsyncTask(new AsyncTask<String, Void, ArrayList<BasePost>>() {
            @Override
            protected ArrayList<BasePost> doInBackground(String... params) {
                ArrayList<BasePost> posts = new ArrayList<BasePost>();
                Document doc = null;
                Connection.Response response = Request.execute(String.format(Api.MESSAGE_CONTENT, touid), Api.USER_AGENT, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                try {
                    doc = response.parse();
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
                        Elements as = bm_c.getElementsByClass("xi2");
                        Elements spans = bm_c.getElementsByClass("xg1");
                        author = as.text();
                        time = spans.text();
                        title = bm_c.getElementsByClass("xs1").text();
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
                if (posts.size() >= 1) {
                    posts.remove(posts.size() - 1);
                }
                MessageConversationAdapter messageConversationAdapter = new MessageConversationAdapter(mContext, posts);
                mListView.setAdapter(messageConversationAdapter);
            }
        });
    }
}
