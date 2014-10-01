package org.qinshuihepan.bbs.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.qinshuihepan.bbs.util.Utils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.util.ArrayList;

/**
 * Created by liurongchan on 14-4-23.
 */
public class Post extends BasePost implements ListItem {

    public Post(int fid, int tid, int pid, String title, String content, String time, int haveimg, int comment_count, String author, ArrayList<Image> images) {
        super(fid, tid, pid, title, content, time, haveimg, comment_count, author, images);
    }

    public static void bindView(View view, Context context, Cursor cursor) {
        Holder holder = getHolder(view);

        BasePost post = BasePost.fromCursor(cursor, ITEM);

        holder.author.setText(post.author);
        holder.time.setText(post.time);
        String[] temp  = post.content.split("%%%%%");
        if (cursor.getPosition() == 0 ) {
            holder.title.setText(post.title);
            holder.title.setTextColor(Color.BLACK);
        } else {
            holder.title.setText(temp[0]);
            holder.title.setTextColor(Color.LTGRAY);
            holder.title.setTextSize(12);
        }
        holder.content.setText(temp[1]);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public BasePost getItem(int position) {
        return null;
    }

}
