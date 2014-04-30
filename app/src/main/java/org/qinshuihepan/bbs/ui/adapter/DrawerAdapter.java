package org.qinshuihepan.bbs.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.qinshuihepan.bbs.App;
import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.util.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liurongchan on 14-4-25.
 */
public class DrawerAdapter extends BaseAdapter {

    private ListView mListView;


    public DrawerAdapter(ListView listView, Context context) {
        mListView = listView;
    }

    @Override
    public int getCount() {
        return Utils.FORUM_CATEGORY.length;
    }

    @Override
    public Object getItem(int position) {
        return Utils.FORUM_CATEGORY[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.listitem_drawer, null);
        }
        Holder holder = getHolder(convertView);
        holder.category.setText(Utils.FORUM_CATEGORY[position]);
        holder.category.setSelected(mListView.isItemChecked(position));
        return convertView;
    }

    static class Holder {
        @InjectView(R.id.textView)
        TextView category;

        public Holder(View convertView) {
            ButterKnife.inject(this, convertView);
        }
    }

    private Holder getHolder(final View convertView) {
        Holder holder = (Holder) convertView.getTag();
        if (holder == null) {
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }
        return holder;
    }

}
