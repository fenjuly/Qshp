package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AbsListView;
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
import org.qinshuihepan.bbs.ui.adapter.MyPostsAdapter;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class MyPostsActivity extends Activity {

    ListView mListView;
    Context mContext;
    ArrayList<BasePost> posts = new ArrayList<BasePost>();
    MyPostsAdapter myPostsAdapter;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的帖子");
        mContext = this;
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_myposts);
        final String uid = Athority.getSharedPreference().getString("uid","");
        mListView = ( ListView) findViewById(R.id.listView);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
                            TaskUtils.executeAsyncTask(new AsyncTask<String, Void, ArrayList<BasePost>>() {
                                @Override
                                protected ArrayList<BasePost> doInBackground(String... params) {
                                    Document doc = null;
                                    String post_url = String.format(Api.MY_POSTS, uid) + "&page=" +String.valueOf(page);
                                    Connection.Response response = Request.execute(post_url, Api.USER_AGENT, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                                    try {
                                        doc = response.parse();
                                        Elements bm_cs = doc.getElementsByClass("bm_c");
                                        if (!bm_cs.text().equals("还没有相关的帖子")) {
                                            String str_tid = "";
                                            String title = "";
                                            String time = "";
                                            String comment_count = "";
                                            int haveimg = 0;
                                            int tid = 0;
                                            String author = Athority.getSharedPreference().getString("username","");
                                            BasePost post;
                                            for (Element bm_c : bm_cs) {
                                                Elements as = bm_c.getElementsByTag("a");
                                                for (Element a : as) {
                                                    String start = "forum.php?mod=viewthread  tid=";
                                                    String len = "  mobile=1";
                                                    String url = a.attr("href");
                                                    str_tid = url.substring(start.length() - 1 , url.length() - len.length() + 1);
                                                    tid = Integer.valueOf(str_tid);
                                                    title = a.text();
                                                }
                                                Elements xg1s = bm_c.getElementsByClass("xg1");
                                                comment_count = xg1s.text().substring("回".length());
                                                post = new Post(0, tid, 0, title, "", time, haveimg, Integer.valueOf(comment_count), author, null);
                                                posts.add(post);
                                            }
                                            page++;
                                        }
                                    } catch (IOException e ) {
                                        e.printStackTrace();
                                    }
                                    return posts;
                                }

                                @Override
                                protected void onPostExecute(ArrayList<BasePost> posts) {
                                    super.onPostExecute(posts);
                                    myPostsAdapter.refresh(posts);
                                }
                            });
                        }
                        // 判断滚动到顶部
                        if(mListView.getFirstVisiblePosition() == 0){
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

            }
        });

        TaskUtils.executeAsyncTask(new AsyncTask<String, Void, ArrayList<BasePost>>() {
            @Override
            protected ArrayList<BasePost> doInBackground(String... params) {
                ArrayList<BasePost> posts = new ArrayList<BasePost>();
                Document doc = null;
                Connection.Response response = Request.execute(String.format(Api.MY_POSTS, uid), Api.USER_AGENT, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                try {
                    doc = response.parse();
                    Elements bm_cs = doc.getElementsByClass("bm_c");
                    String str_tid = "";
                    String title = "";
                    String time = "";
                    String comment_count = "";
                    int haveimg = 0;
                    int tid = 0;
                    String author = Athority.getSharedPreference().getString("username","");
                    BasePost post;
                    for (Element bm_c : bm_cs) {
                        Elements as = bm_c.getElementsByTag("a");
                        for (Element a : as) {
                            String start = "forum.php?mod=viewthread  tid=";
                            String len = "  mobile=1";
                            String url = a.attr("href");
                            str_tid = url.substring(start.length() - 1 , url.length() - len.length() + 1);
                            tid = Integer.valueOf(str_tid);
                            title = a.text();
                        }
                        Elements xg1s = bm_c.getElementsByClass("xg1");
                        if(xg1s.text().length() != 0) {
                            comment_count = xg1s.text().substring("回".length());
                        } else {
                            comment_count = "0";
                        }
                        post = new Post(0, tid, 0, title, "", time, haveimg, Integer.valueOf(comment_count), author, null);
                        posts.add(post);
                    }
                } catch (IOException e ) {
                    e.printStackTrace();
                }
                return posts;
            }

            @Override
            protected void onPostExecute(ArrayList<BasePost> posts) {
                super.onPostExecute(posts);
                if(posts.size() == 1) {
                    posts.clear();
                }
                myPostsAdapter = new MyPostsAdapter(mContext, posts, mListView);
                mListView.setAdapter(myPostsAdapter);
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