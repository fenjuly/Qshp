package org.qinshuihepan.bbs.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.api.Api;
import org.qinshuihepan.bbs.dao.ImagesDataHelper;
import org.qinshuihepan.bbs.dao.ItemsDataHelper;
import org.qinshuihepan.bbs.data.Request;
import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.model.Image;
import org.qinshuihepan.bbs.model.Post;
import org.qinshuihepan.bbs.model.PostWithPic;
import org.qinshuihepan.bbs.ui.adapter.CardsAnimationAdapter;
import org.qinshuihepan.bbs.ui.adapter.PostContentAdapter;
import org.qinshuihepan.bbs.util.ActionBarUtils;
import org.qinshuihepan.bbs.util.ListViewUtils;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.Utils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;
import org.qinshuihepan.bbs.view.LoadingFooter;
import org.qinshuihepan.bbs.view.PageListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liurongchan on 14-5-2.
 */
public class PostContentActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    public static final String TID = "tid";
    public static final String FID = "fid";
    public static final String TITLE = "title";

    SwipeRefreshLayout mSwipeLayout;

    PageListView mListView;
    ArrayList<BasePost> posts = new ArrayList<BasePost>();
    String formhash = "";
    String url = "";
    private int tid;
    private int fid;
    private String title;
    private ItemsDataHelper mtDataHelper;
    private ImagesDataHelper miDataHelper;
    private PostContentAdapter mAdapter;
    private int mPage = 1;
    private int maxPage;
    private boolean isloadmaxpage = false;
    private ProgressDialog progressDialog;
    private HttpResponseCache httpResponseCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.view_post);
        tid = getIntent().getExtras().getInt(TID);
        fid = getIntent().getExtras().getInt(FID);
        title = getIntent().getExtras().getString(TITLE);
        setContentView(R.layout.post_layout);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mListView = (PageListView) findViewById(R.id.listView);

        mtDataHelper = new ItemsDataHelper(this);
        miDataHelper = new ImagesDataHelper(this);
        mAdapter = new PostContentAdapter(this);
        View header = new View(this);
        mListView.addHeaderView(header);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
        mListView.setLoadNextListener(new PageListView.OnLoadNextListener() {
            @Override
            public void onLoadNext() {
                if (mPage >= maxPage) {
                    Toast.makeText(getApplicationContext(), "已经滑到底啦！", Toast.LENGTH_SHORT).show();
                    mListView.setState(LoadingFooter.State.TheEnd);
                } else {
                    loadNext();
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int postion, long id) {
                final EditText editText = new EditText(PostContentActivity.this);
                editText.setHeight(PostContentActivity.this.getResources().getDimensionPixelSize(
                        R.dimen.comment_dialog_height));
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                        250)});
                editText.setGravity(Gravity.LEFT | Gravity.TOP);

                AlertDialog.Builder commentDialog = new AlertDialog.Builder(
                        PostContentActivity.this).setTitle("回复").setView(editText)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", null);
                final AlertDialog dialog = commentDialog.create();
                dialog.show();

                if (postion == 1) {
                    url = String.format(Api.REPLY, fid, tid);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    String message = editText.getText().toString();
                                    byte[] m = message.getBytes();
                                    if (m.length < 6) {
                                        Toast.makeText(PostContentActivity.this, "输入字节数不能小于6!",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        dialog.dismiss();
                                        final String[] params = {url, message};
                                        final Comment comment = new Comment();
                                        progressDialog = ProgressDialog.show(PostContentActivity.this,
                                                null, "请稍后");
                                        if (Build.VERSION.SDK_INT >= 11) {
                                            comment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
                                        } else {
                                            comment.execute(params);
                                        }
                                    }
                                }
                            }
                    );
                } else {
                    url = String.format(Api.REPLY_SINGLE, fid, tid);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    String message = editText.getText().toString();
                                    byte[] m = message.getBytes();
                                    if (m.length < 6) {
                                        Toast.makeText(PostContentActivity.this, "输入字节数不能小于6!",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        dialog.dismiss();
                                        final String[] params = {url, message, String.valueOf(postion)};
                                        final Comment_Single comment_single = new Comment_Single();
                                        progressDialog = ProgressDialog.show(PostContentActivity.this,
                                                null, "请稍后");
                                        if (Build.VERSION.SDK_INT >= 11) {
                                            comment_single.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
                                        } else {
                                            comment_single.execute(params);
                                        }
                                    }
                                }
                            }
                    );
                }

                return true;
            }
        });

        initActionBar();
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        getSupportLoaderManager().initLoader(1, null, this);
        loadFirst();

        try {
            File httpCacheDir = new File(PostContentActivity.this.getExternalCacheDir(), "http");
            long httpCacheSize = 20 * 1024 * 1024; // 10 MiB
            httpResponseCache = HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initActionBar() {
        View actionBarContainer = ActionBarUtils.findActionBarContainer(this);
        actionBarContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewUtils.smoothScrollListViewToTop(mListView);
            }
        });
    }

    private void loadData(final int next) {
        if (!mSwipeLayout.isRefreshing() && (1 == next)) {
            mSwipeLayout.setRefreshing(true);
        }
        TaskUtils.executeAsyncTask(new AsyncTask<Void, Object, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... objects) {
                boolean isRefreshFromTop = (1 == mPage);
                try {
                    ArrayList<Image> images = new ArrayList<Image>();

                    Document doc;
                    int pid;
                    String time;
                    String content;
                    String author;
                    String temp = "";

                    if (isRefreshFromTop) {
                        mtDataHelper.deleteAll();
                        miDataHelper.deleteAll();
                    }
                    Connection.Response res = Request.execute(String.format(Api.POST_CONTENT, tid, next), "Mozilla", (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);

                    Athority.addCookies(res.cookies());
                    doc = res.parse();
                    Elements inputs = doc.getElementsByTag("input");
                    for (Element input : inputs) {
                        if (input.attr("name").equals("formhash")) {
                            formhash = input.attr("value");
                        }
                    }

                    Elements plhins = doc.getElementsByClass("plhin");
                    posts.clear();
                    for (Element plhin : plhins) {
                        pid = Integer.valueOf(plhin.id().substring("pid".length()));
                        Elements xw1s = plhin.getElementsByClass("xw1");
                        author = xw1s.text();
                        Elements t_fs = plhin.getElementsByClass("t_f");
                        for (Element t_f : t_fs) {
                            temp = t_f.getElementsByTag("blockquote").text();
                            break;
                        }
                        content = t_fs.text();
                        content = content.substring(temp.length());
                        content = temp + "%%%%%" + content;
                        time = plhin.getElementById("authorposton" + pid).text();
                        Elements e_pics = plhin.getElementsByClass("zoom");
                        images.clear();
                        if (e_pics != null) {
                            for (Element e_pic : e_pics) {
                                if (e_pic.attr("zoomfile").equals("")) {
                                    if (e_pic.attr("file").equals("")) {
                                        continue;
                                    } else {
                                        images.add(new Image(e_pic.attr("file"), pid));
                                    }
                                } else {
                                    images.add(new Image(e_pic.attr("zoomfile"), pid));
                                }
                            }
                        }
                        if (images.size() == 0) {
                            posts.add(new Post(fid, tid, pid, title,
                                    content, time, 0, 0, author, null));
                        } else {
                            posts.add(new PostWithPic(fid, tid, pid, title,
                                    content, time, 1, 0, author, null));
                            miDataHelper.bulkInsert(images);
                        }

                    }

                    if (!isloadmaxpage) {
                        Element pgt = doc.getElementById("pgt");
                        if (pgt == null) {
                            maxPage = 1;
                            mPage = 1;
                            isloadmaxpage = true;
                            return false;
                        } else {
                            if (pgt.getElementsByTag("strong").text().equals("")) {
                                maxPage = 1;
                                mPage = 1;
                            } else {
                                maxPage = Integer.valueOf(pgt.getElementsByTag(
                                        "strong").text());
                                mPage = maxPage;
                                Elements page_numbers = pgt.getElementsByTag("a");
                                int now_number = 1;
                                String str_now_number;
                                for (Element page_number : page_numbers) {
                                    str_now_number = page_number.text();
                                    if (str_now_number.startsWith("... ")) {
                                        now_number = Integer.valueOf(str_now_number
                                                .substring("... ".length()));
                                    } else if (str_now_number.equals("下一页")) {
                                        continue;
                                    } else if (str_now_number.equals("返回列表")) {
                                        continue;
                                    } else if (str_now_number.equals("")) {
                                        continue;
                                    } else {
                                        now_number = Integer
                                                .valueOf(str_now_number);
                                    }
                                    if (now_number > maxPage) {
                                        maxPage = now_number;
                                    }
                                }
                                isloadmaxpage = true;
                            }
                        }
                    }

                    mtDataHelper.bulkInsert(posts);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return isRefreshFromTop;
            }

            @Override
            protected void onPostExecute(Boolean isRefreshFromTop) {
                super.onPostExecute(isRefreshFromTop);
                if (isRefreshFromTop) {
                    mSwipeLayout.setRefreshing(false);
                } else {

                    mListView.setState(LoadingFooter.State.Idle, 3000);
                }
                getSupportLoaderManager().restartLoader(1, null, PostContentActivity.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            httpResponseCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFirst() {
        mPage = 1;
        loadData(mPage);
    }

    private void loadNext() {
        mPage++;
        loadData(mPage);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mtDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        mAdapter.changeCursor(data);
//        if (data != null && data.getCount() == 0) {
//            loadFirst();
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onRefresh() {
        loadFirst();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.changeCursor(null);
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(1, null, PostContentActivity.this);
    }

    private class Comment extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int statusCode = 0;

            HashMap<String, String> datas = new HashMap<String, String>();
            datas.put("message", params[1]);
            datas.put("formhash", formhash);
            datas.put("useig", "1");
            datas.put("subject", "");
            datas.put("posttime", "");
            datas.put("handlekey", "reply");
            datas.put("replysubmit", "true");
            Connection.Response res = Request.execute(params[0], "Mozilla", datas, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.POST);

            statusCode = res.statusCode();

            return statusCode;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result == 200) {
                Toast.makeText(PostContentActivity.this, "回复成功!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PostContentActivity.this, "回复失败!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class Comment_Single extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int statusCode = 0;
            String noticetrimstr;
            String content = "";
            if (posts.get(Integer.valueOf(params[2]) - 1).content.startsWith("%%%%%")) {
                content = posts.get(Integer.valueOf(params[2]) - 1).content.substring("%%%%%".length());
            } else {
                String[] temp = posts.get(Integer.valueOf(params[2]) - 1).content.split("%%%%%");
                content = temp[1];
            }
            noticetrimstr = "[quote][size=2][url=forum.php?mod=redirect&goto=findpost&pid="
                    + posts.get(Integer.valueOf(params[2]) - 1).pid
                    + "&tid="
                    + posts.get(Integer.valueOf(params[2]) - 1).tid
                    + "][color=#999999]"
                    + posts.get(Integer.valueOf(params[2]) - 1).author
                    + posts.get(Integer.valueOf(params[2]) - 1).time
                    + "[/color][/url][/size] "
                    + content
                    + "[/quote]";

            HashMap<String, String> datas = new HashMap<String, String>();
            datas.put("message", params[1]);
            datas.put("formhash", formhash);
            datas.put("useig", "1");
            datas.put("subject", "");
            datas.put("posttime", "");
            datas.put("handlekey", "reply");
            datas.put("replysubmit", "true");
            datas.put("noticetrimstr", noticetrimstr);
            Connection.Response res = Request.execute(params[0], "Mozilla", datas, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.POST);

            statusCode = res.statusCode();

            return statusCode;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result == 200) {
                Toast.makeText(PostContentActivity.this, "回复成功!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PostContentActivity.this, "回复失败!", Toast.LENGTH_SHORT).show();
            }
        }

    }

}


