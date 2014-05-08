package org.qinshuihepan.bbs.ui.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qinshuihepan.bbs.App;
import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.api.Api;
import org.qinshuihepan.bbs.dao.PostsDataHelper;
import org.qinshuihepan.bbs.data.Request;
import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.model.Post;
import org.qinshuihepan.bbs.model.PostWithPic;
import org.qinshuihepan.bbs.ui.adapter.CardsAnimationAdapter;
import org.qinshuihepan.bbs.ui.adapter.PostsAdapter;
import org.qinshuihepan.bbs.util.ActionBarUtils;
import org.qinshuihepan.bbs.util.ListViewUtils;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.Utils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;
import org.qinshuihepan.bbs.view.LoadingFooter;
import org.qinshuihepan.bbs.view.PageListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liurongchan on 14-4-25.
 */
public class PostsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_CATEGORY = "extra_category";

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    @InjectView(R.id.listView)
    PageListView mListView;

    private String mCategory;
    private PostsDataHelper mpDataHelper;
    private PostsAdapter mAdapter;
    private int mPage = 1;
    private int maxPage = 1;
    private boolean isloadmaxpage = false;



    public static PostsFragment newInstance(String category) {
        PostsFragment fragment = new PostsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.inject(this, contentView);
        parseArgument();
        mpDataHelper = new PostsDataHelper(getActivity());
        mAdapter = new PostsAdapter(getActivity(), mListView);
        View header = new View(getActivity());
        mListView.addHeaderView(header);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
        mListView.setLoadNextListener(new PageListView.OnLoadNextListener() {
            @Override
            public void onLoadNext() {
                if (mPage >= maxPage) {
                    Toast.makeText(getActivity(), "已经滑到底啦！", Toast.LENGTH_SHORT).show();
                    mListView.setState(LoadingFooter.State.TheEnd);
                } else {
                    loadNext();
                }
            }
        });

        initActionBar();
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getLoaderManager().initLoader(0, null, this);
        mpDataHelper.deleteAll();
        return contentView;
    }

    private void parseArgument() {
        Bundle bundle = getArguments();
        mCategory = bundle.getString(EXTRA_CATEGORY);
    }

    private void initActionBar() {
        View actionBarContainer = ActionBarUtils.findActionBarContainer(getActivity());
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
        final Integer tempCategoryID = Integer.valueOf(App.getContext().getResources().getString(Integer.valueOf(Utils.FORUM_CATEGORY_ID.get(mCategory))));
        if (tempCategoryID.equals(10001) || tempCategoryID.equals(10002) || tempCategoryID.equals(10003)) {
            TaskUtils.executeAsyncTask(new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    ArrayList<BasePost> posts = new ArrayList<BasePost>();
                    Document doc = null;
                    mpDataHelper.deleteAll();
                    try {
                        Connection.Response response = Request.execute(Api.HOST, "Mozilla", (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                        doc = response.parse();
                        Element portalBlockContents = null;
                        String title = "";
                        String time = "";
                        String comment_count = "";
                        int haveimg = 0;
                        int tid = 0;
                        String author = "";
                        BasePost post;
                        switch (tempCategoryID) {
                            case 10002:
                                portalBlockContents = doc
                                        .getElementById("portal_block_21_content");
                                break;
                            case 10001:
                                portalBlockContents = doc
                                        .getElementById("portal_block_20_content");
                                break;
                            case 10003:
                                portalBlockContents = doc
                                        .getElementById("portal_block_22_content");
                                break;
                        }
                        for (Element portalBlockContent : portalBlockContents
                                .getElementsByTag("li")) {
                            tid = Integer.valueOf(portalBlockContent.select("a[title]").attr("href").substring(52));
                            title = portalBlockContent.select("a[title]").attr("title");
                            post = new Post(tempCategoryID, tid, 0, title, "", "", haveimg, 0, author, null);
                            posts.add(post);
                        }
                        mpDataHelper.bulkInsert(posts);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean isRefreshFromTop) {
                    super.onPostExecute(isRefreshFromTop);
                    if (isRefreshFromTop) {
                        mSwipeLayout.setRefreshing(false);
                    } else {
                        mListView.setState(LoadingFooter.State.Idle, 3000);
                    }
//                    getLoaderManager().restartLoader(0, null, PostsFragment.this);
                }
            });
        } else {
            TaskUtils.executeAsyncTask(new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    boolean isRefreshFromTop = (1 == mPage);
                    ArrayList<BasePost> posts = new ArrayList<BasePost>();
                    Document doc = null;
                    if (isRefreshFromTop) {
                        mpDataHelper.deleteAll();
                    }
                    try {
                        Connection.Response response = Request.execute(String.format(Api.POSTS, App.getContext().getResources().getString(Integer.valueOf(Utils.FORUM_CATEGORY_ID.get(mCategory))), next), "Mozilla", (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                        System.out.println(String.format(Api.POSTS, App.getContext().getResources().getString(Integer.valueOf(Utils.FORUM_CATEGORY_ID.get(mCategory))), next));
                        doc = response.parse();
                        Elements tbodies = doc.getElementsByTag("tbody");
                        String str_tid = "";
                        String title = "";
                        String time = "";
                        String comment_count = "";
                        int haveimg = 0;
                        int tid = 0;
                        String author = "";
                        BasePost post;
                        for (Element tbody : tbodies) {

                            str_tid = tbody.id();

                            if (str_tid.equals("")) {
                                continue;
                            } else if (str_tid.startsWith("stickthread_")) {
                                tid = Integer.valueOf(str_tid.substring("stickthread_"
                                        .length()));
                            } else if (str_tid.startsWith("normalthread_")) {
                                tid = Integer.valueOf(str_tid.substring("normalthread_"
                                        .length()));
                            } else if (str_tid.equals("separatorline")) {
                                continue;
                            }
                            Elements titles = tbody.select("a.s.xst");
                            title = titles.text();
                            Elements bys = tbody.getElementsByClass("by");
                            for (Element by : bys) {
                                author = by.getElementsByTag("a").text();
                                Elements spans = by.getElementsByTag("span");
                                for (Element span : spans) {
                                    time = span.getElementsByTag("span").text();
                                }
                                break;
                            }
                            Elements nums = tbody.getElementsByClass("num");
                            for (Element num : nums) {
                                comment_count = num.getElementsByTag("a").text();
                                if (!isNumeric(comment_count)) {
                                    comment_count = "0";
                                }
                            }

                            post = new Post(0, tid, 0, title, "", time, haveimg, Integer.valueOf(comment_count), author, null);
                            posts.add(post);
                        }

                        if (!isloadmaxpage) {
                            Element fd_page_bottom = doc.getElementById("fd_page_top");
                            if (fd_page_bottom.getElementsByTag("strong").text()
                                    .equals("")) {
                                maxPage = 1;
                                mPage = 1;
                            } else {
                                maxPage = Integer.valueOf(fd_page_bottom
                                        .getElementsByTag("strong").text());
                                mPage = maxPage;
                                Elements page_numbers = fd_page_bottom
                                        .getElementsByTag("a");
                                int now_number = 1;
                                String str_now_number = "";
                                for (Element page_number : page_numbers) {
                                    str_now_number = page_number.text();
                                    if (str_now_number.startsWith("... ")) {
                                        now_number = Integer.valueOf(str_now_number
                                                .substring("... ".length()));
                                    } else if (str_now_number.equals("下一页")) {
                                        continue;
                                    } else {
                                        now_number = Integer.valueOf(str_now_number);
                                    }
                                    if (now_number > maxPage) {
                                        maxPage = now_number;
                                    }
                                }
                                isloadmaxpage = true;
                            }
                        }


                        mpDataHelper.bulkInsert(posts);
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
//                    getLoaderManager().restartLoader(0, null, PostsFragment.this);
                }
            });
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

    public void loadFirstAndScrollToTop() {
        ListViewUtils.smoothScrollListViewToTop(mListView);
        loadFirst();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mpDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        if (data != null && data.getCount() == 0) {
            loadFirst();
        }
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
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    private static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
