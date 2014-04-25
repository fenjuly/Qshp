package org.qinshuihepan.bbs.model;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by liurongchan on 14-4-23.
 */
public  abstract class BasePost {


    public abstract View newView(Context context, Cursor cursor, ViewGroup viewGroup);

    public abstract BasePost getItem(int position);

    public abstract void bindView(View view, Context context, Cursor cursor);

    public abstract BasePost fromCursor(Cursor cursor);

    public int fid;
    public int tid;
    public int pid;
    public String title;
    public String content;
    public String time;
    public  String image;
}
