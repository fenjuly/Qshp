package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
        setTheme(R.style.AppTheme2);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.activity_personal_header)
                .contentLayout(R.layout.activity_personal_listview);
        View view = helper.createView(this);
        setContentView(view);
        helper.initActionBar(this);
        ListView listView = (ListView) findViewById(android.R.id.list);
        PersonalCenterAdapter adapter = new PersonalCenterAdapter(this);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
