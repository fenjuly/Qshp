package org.qinshuihepan.bbs.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.ui.fragment.DrawerFragment;
import org.qinshuihepan.bbs.ui.fragment.PostsFragment;
import org.qinshuihepan.bbs.util.Utils;
import org.qinshuihepan.bbs.util.update.NetChecker;
import org.qinshuihepan.bbs.util.update.UpdateChecker;
import org.qinshuihepan.bbs.view.FoldingDrawerLayout;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity {

    @InjectView(R.id.drawer_layout)
    FoldingDrawerLayout mDrawerLayout;

    String mCategory;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mKeyBackClickCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initActionBar();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setCategory(Utils.FORUM_CATEGORY[0]);
        UpdateChecker.checkForNotification(MainActivity.this);

        replaceFragment(R.id.left_drawer, new DrawerFragment());
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.changeAcount:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            case R.id.myposts:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setCategory(String category) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (mCategory != null && mCategory.equals(category)) {
            return;
        }
        mCategory = category;
        setTitle(mCategory);
        if (NetChecker.checkNetworkConnection(this)) {
            PostsFragment mContentFragment = PostsFragment.newInstance(category);
            replaceFragment(R.id.content_frame, mContentFragment);
        } else {
            new AlertDialog.Builder(this).setTitle("网络未连接").setMessage("设置完成后请尝试手动刷新")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            Intent intentToNetwork = new Intent(
                                    Settings.ACTION_SETTINGS);
                            paramDialogInterface.cancel();
                            MainActivity.this.startActivity(intentToNetwork);
                        }
                    }).setNegativeButton("取消", null).show();
        }
    }

    // 双击返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (mKeyBackClickCount++) {
                case 0:
                    Toast.makeText(this, R.string.press_again_to_exit,
                            Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mKeyBackClickCount = 0;
                        }
                    }, 3000);
                    break;

                case 1:
                    finish();
                    break;

                default:
                    mKeyBackClickCount = 0;
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
