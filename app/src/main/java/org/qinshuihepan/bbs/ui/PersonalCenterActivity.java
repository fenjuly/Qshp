package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.ui.adapter.PersonalCenterAdapter;

/**
 * Created by liurongchan on 14-9-21.
 */
public class PersonalCenterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.activity_personal_header)
                .contentLayout(R.layout.activity_personal_listview);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        ListView listView = (ListView) findViewById(android.R.id.list);
        PersonalCenterAdapter adapter = new PersonalCenterAdapter(this);
        listView.setAdapter(adapter);
    }
}
