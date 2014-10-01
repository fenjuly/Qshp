package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
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
import org.qinshuihepan.bbs.ui.adapter.LeaveMessageAdapter;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by liurongchan on 14-9-28.
 */
public class LeaveMessageActivity extends Activity {

    Context mContext;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的留言");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_message_conversation);
        mContext = this;
        mListView = (ListView) findViewById(R.id.listView);
        final String uid = Athority.getSharedPreference().getString("uid","");
        TaskUtils.executeAsyncTask(new AsyncTask<String, Void, ArrayList<BasePost>>() {
            @Override
            protected ArrayList<BasePost> doInBackground(String... params) {
                ArrayList<BasePost> posts = new ArrayList<BasePost>();
                Document doc = null;
                Connection.Response response = Request.execute(String.format(Api.LEAVE_MESSAGE, uid), "Mozilla", (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                try {
                    doc = response.parse();
                    Elements dls = doc.getElementsByTag("dl");
                    String str_tid = "";
                    String title = "";
                    String time = "";
                    String comment_count = "";
                    int haveimg = 0;
                    int tid = 0;
                    String author = "";
                    BasePost post;
                    for (Element dl : dls) {
                        int i = 1;
                        int j = 1;
                        Elements as = dl.getElementsByTag("a");
                        Elements spans = dl.getElementsByTag("span");
                        Elements dds = dl.getElementsByTag("dd");
                        title = dds.text();
                        for (Element a : as) {
                            if(i == 5) {
                                author = a.text();
                                break;
                            }
                            i++;
                        }
                        for (Element span : spans) {
                            if (j == 3) {
                                time = span.text();
                                break;
                            }
                            j++;
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
                LeaveMessageAdapter leaveMessageAdapter = new LeaveMessageAdapter(mContext, posts);
                mListView.setAdapter(leaveMessageAdapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
