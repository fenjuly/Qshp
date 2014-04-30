package org.qinshuihepan.bbs.model;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by liurongchan on 14-4-23.
 */
public class Post extends BasePost implements ListItem {

    public Post(int fid, int tid, int pid, String title, String content, String time, int haveimg, int comment_count, ArrayList<Image> images) {
        super(fid, tid, pid, title, content, time, haveimg, comment_count, images);
    }

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


}
