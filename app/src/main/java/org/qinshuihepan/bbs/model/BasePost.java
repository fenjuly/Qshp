package org.qinshuihepan.bbs.model;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import org.qinshuihepan.bbs.App;
import org.qinshuihepan.bbs.dao.ImagesDataHelper;
import org.qinshuihepan.bbs.dao.PostsDataHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liurongchan on 14-4-23.
 */
public class BasePost {
    public static final HashMap<Integer, BasePost> CACHE = new HashMap<Integer, BasePost>();

    public static final int NOIMG = 0;
    public static final int YESIMG = 1;

    private static ImagesDataHelper miDataHelper = new ImagesDataHelper(App.getContext());

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    public BasePost getItem(int position) {
        return null;
    }

    public void bindView(View view, Context context, Cursor cursor) {
    }

    public static void addToCache(BasePost post) {
        CACHE.put(post.tid, post);
    }

    public static BasePost getFromCache(int tid) {
        return CACHE.get(tid);
    }

    public static BasePost fromCursor(Cursor cursor) {
        int tid = cursor.getInt(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.TID));
        BasePost post = getFromCache(tid);
        if (post != null) {
            return post;
        }

        int fid = cursor.getInt(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.FID));
        int pid = cursor.getInt(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.PID));
        String title = cursor.getString(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.TITLE));
        String content = cursor.getString(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.CONTENT));
        String time = cursor.getString(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.TIME));
        int haveimg = cursor.getInt(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.HAVEIMG));
        int comment_count = cursor.getInt(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.COMMENT_COUNT));
        String author = cursor.getString(cursor.getColumnIndex(PostsDataHelper.PostsDBInfo.AUTHOR));

        ArrayList<Image> images = null;
        if (haveimg == NOIMG) {
            post = new Post(fid, tid, pid, title, content, time, haveimg, comment_count, author, images);
        } else {

            images = miDataHelper.queryImages(tid);
            post = new PostWithPic(fid, tid, pid, title, content, time, haveimg, comment_count, author, images);
        }
        addToCache(post);
        return post;
    }

    public BasePost(int fid, int tid, int pid, String title, String content, String time, int haveimg, int comment_count, String author, ArrayList<Image> images) {
        this.fid = fid;
        this.tid = tid;
        this.pid = pid;
        this.title = title;
        this.content = content;
        this.time = time;
        this.haveimg = haveimg;
        this.comment_count = comment_count;
        this.author = author;
        this.images = images;
    }

    public int fid;
    public int tid;
    public int pid;
    public String title;
    public String content;
    public String time;
    public int haveimg;
    public int comment_count;
    public String author;
    public ArrayList<Image> images;
}
