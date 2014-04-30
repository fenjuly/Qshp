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
import android.widget.AdapterView;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qinshuihepan.bbs.App;
import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.api.Api;
import org.qinshuihepan.bbs.dao.ImagesDataHelper;
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
    private ImagesDataHelper miDataHelper;
    private PostsAdapter mAdapter;
    private int mPage = 1;

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
        miDataHelper = new ImagesDataHelper(getActivity());
        mAdapter = new PostsAdapter(getActivity(), mListView);
        View header = new View(getActivity());
        mListView.addHeaderView(header);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
        mListView.setLoadNextListener(new PageListView.OnLoadNextListener() {
            @Override
            public void onLoadNext() {
                loadNext();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        initActionBar();
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getLoaderManager().initLoader(0, null, this);
        loadFirst();
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
        if (!mSwipeLayout.isRefreshing() && (0 == next)) {
            mSwipeLayout.setRefreshing(true);
        }
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
                            Elements spans = by.getElementsByTag("span");
                            for (Element span : spans) {
                                time = span.getElementsByTag("span").text();
                            }
                            break;
                        }
                        Elements nums = tbody.getElementsByClass("num");
                        for (Element num : nums) {
                            comment_count = num.getElementsByTag("a").text();
                        }
                        Elements imgs = tbody.getElementsByTag("img");
                        if (imgs.size() > 1) {
                            haveimg = 1;
                            post = new PostWithPic(0, tid, 0, title, "", time, haveimg, Integer.valueOf(comment_count), null);
                        } else {
                            haveimg = 0;
                            post = new Post(0, tid, 0, title, "", time, haveimg, Integer.valueOf(comment_count), null);
                        }
                        System.out.println(haveimg);
                        posts.add(post);
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
                getLoaderManager().restartLoader(0, null, PostsFragment.this);
            }
        });
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
}
