package org.qinshuihepan.bbs.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.ui.MainActivity;
import org.qinshuihepan.bbs.ui.adapter.DrawerAdapter;
import org.qinshuihepan.bbs.util.Utils;

/**
 * Created by liurongchan on 14-4-25.
 */
public class DrawerFragment extends Fragment {

    private ListView mListView;

    private DrawerAdapter mAdapter;

    private MainActivity mActivity;

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        View contentView = inflater.inflate(R.layout.fragment_drawer, null);
        mListView = (ListView) contentView.findViewById(R.id.listView);
        mAdapter = new DrawerAdapter(mListView, mActivity);
        mListView.setAdapter(mAdapter);
        mListView.setItemChecked(0, true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListView.setItemChecked(position, true);
                mActivity.setCategory(Utils.FORUM_CATEGORY[position]);
            }
        });
        return contentView;
    }

}
