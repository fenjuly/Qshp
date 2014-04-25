package org.qinshuihepan.bbs.model;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liurongchan on 14-4-23.
 */
public class Post extends BasePost implements ListItem {
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public BasePost getItem(int position) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    private static void addToCache(BasePost post) {
        CACHE.put(post.tid, post);
    }

    private static BasePost getFromCache(int tid) {
        return CACHE.get(tid);
    }

    @Override
    public  BasePost fromCursor(Cursor cursor) {
        return null;
    }

}
