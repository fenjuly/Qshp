package org.qinshuihepan.bbs.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.ui.MainActivity;
import org.qinshuihepan.bbs.ui.PostContentActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liurongchan on 14-4-25.
 */
public class PostsAdapter extends CursorAdapter {

    private static final int LEFT_STRIPS[] = {R.drawable.u47, R.drawable.u67, R.drawable.u87, R.drawable.u107, R.drawable.u127, R.drawable.u147};

    private LayoutInflater mLayoutInflater;

    private ListView mListView;


    public PostsAdapter(Context context, ListView listView) {
        super(context, null, false);
        mLayoutInflater = ((Activity) context).getLayoutInflater();
        mListView = listView;
    }

    @Override
    public BasePost getItem(int position) {
        mCursor.moveToPosition(position);
        return BasePost.fromCursor(mCursor, BasePost.POST);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflater.inflate(R.layout.listitem_post, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = getHolder(view);
        view.setEnabled(!mListView.isItemChecked(cursor.getPosition()
                + mListView.getHeaderViewsCount()));
        final BasePost post = BasePost.fromCursor(cursor, BasePost.POST);
        int resid = LEFT_STRIPS[(int) (Math.random() * 6)];

        holder.left_strip.setImageResource(resid);
        holder.title.setText(post.title);
        holder.content.setText(post.content);
        holder.time.setText(post.time);
        holder.comment_count.setText(String.valueOf(post.comment_count));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostContentActivity.class);
                intent.putExtra(PostContentActivity.FID, post.fid);
                intent.putExtra(PostContentActivity.TID, post.tid);
                mContext.startActivity(intent);
            }
        });

    }

    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }

    static class Holder {
        @InjectView(R.id.left_strip)
        ImageView left_strip;

        @InjectView(R.id.title)
        TextView title;

        @InjectView(R.id.content)
        TextView content;

        @InjectView(R.id.time)
        TextView time;

        @InjectView(R.id.comment_count)
        TextView comment_count;

        public Holder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
