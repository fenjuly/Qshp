package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends Activity {
    private TextView searchNumsTextView;
    private String searchNums;
    private ListView searchListView;
    private MyPostsAdapter searchResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchNumsTextView = (TextView) findViewById(R.id.test);
        searchListView = (ListView) findViewById(R.id.lv_search);

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showSearchResults(query);
        }
    }

    private void showSearchResults(final String query) {
        new AsyncTask<String, Void, String>() {
            Document doc;
            String formhash;

            @Override
            protected String doInBackground(String... params) {
                Map<String, String> map = new HashMap<String, String>();
                ArrayList<BasePost> posts = new ArrayList<BasePost>();
                try {
                    Connection.Response response = Request.execute(Api.MOBILE_SEARCH, Api.USER_AGENT, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                    doc = response.parse();
                    Elements elements = doc.select("[name=formhash]");
                    for (Element element : elements) {
                        formhash = element.attr("value");
                    }
                    Log.d("formhash", formhash);
                    map.put("srchtxt", query);
                    map.put("searchsubmit", "yes");
                    map.put("formhash", formhash);
                    Connection.Response result = Request.execute(Api.MOBILE_SEARCH, Api.USER_AGENT, map, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.POST);
                    doc = result.parse();

                    String title = "";
                    int tid = 0;
                    String author = Athority.getSharedPreference().getString("username", "");
                    BasePost post;
                    for (Element element : doc.getElementsByClass("thread_tit")) {
                        searchNums = element.text();
                    }
                    Log.d("searchnum", searchNums);
                    Elements threadlist = doc.select("div.threadlist > ul > li> a");
                    Log.d("threadlist", threadlist.toString());
                    for (Element href : threadlist) {
                        title = href.text();
                        String url = href.toString();
                        if(url.indexOf("tid=") + 4 > url.indexOf("&amp;highlight")) {

                        } else  {
                            tid = Integer.valueOf(url.substring(url.indexOf("tid=") + 4, url.indexOf("&amp;highlight")));
                            post = new Post(0, tid, 0, title, "", "", 0, 0, "", null);
                            posts.add(post);
                        }
                    }
                    searchResultsAdapter = new MyPostsAdapter(SearchActivity.this, posts, searchListView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return doc.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                searchNumsTextView.setText(searchNums);
                searchListView.setAdapter(searchResultsAdapter);
            }
        }.execute(query);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

}
