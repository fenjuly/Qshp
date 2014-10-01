package org.qinshuihepan.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.ui.PostContentActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liurongchan on 14-9-22.
 */
public class MyPostsAdapter extends BaseAdapter{

    private static final int LEFT_STRIPS[] = {R.drawable.u47, R.drawable.u67, R.drawable.u87, R.drawable.u107, R.drawable.u127, R.drawable.u147};

    private ListView mListView;
    private Context mContext;

    private ArrayList<BasePost> posts;

    public MyPostsAdapter(Context context, ArrayList<BasePost> posts, ListView mListView) {
        this.posts = posts;
        this.mContext = context;
        this.mListView = mListView;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    public void refresh(ArrayList<BasePost> mPosts) {
        this.posts = mPosts;
        notifyDataSetChanged();
    }

    @Override
    public BasePost getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem_post, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        int resid = LEFT_STRIPS[(int) (Math.random() * 6)];

        holder.left_strip.setImageResource(resid);
        holder.title.setText(posts.get(position).title);
        holder.content.setText(posts.get(position).content);
        holder.time.setText(posts.get(position).time);
        holder.comment_count.setText(String.valueOf(posts.get(position).comment_count));
        view.setEnabled(!mListView.isItemChecked(position
                + mListView.getHeaderViewsCount()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostContentActivity.class);
                intent.putExtra(PostContentActivity.TID, posts.get(position).tid);
                intent.putExtra(PostContentActivity.TITLE, posts.get(position).title);
                mContext.startActivity(intent);
            }
        });
        return view;
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
