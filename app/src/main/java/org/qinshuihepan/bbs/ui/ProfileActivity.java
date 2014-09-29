package org.qinshuihepan.bbs.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.api.Api;
import org.qinshuihepan.bbs.data.Request;
import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.util.TaskUtils;
import org.qinshuihepan.bbs.util.sharedpreference.Athority;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liurongchan on 14-9-23.
 */
public class ProfileActivity extends Activity {

    String jifen;
    String weiwang;
    String shuidi;
    String cunzaigan;
    String bingjing;
    String name;
    String url;

    @InjectView(R.id.jifen_text)
    TextView jifen_text;
    @InjectView(R.id.weiwang_text)
    TextView weiwang_text;
    @InjectView(R.id.shuidi_text)
    TextView shuidi_text;
    @InjectView(R.id.cunzaigan_text)
    TextView cunzaigan_text;
    @InjectView(R.id.bingjign_text)
    TextView bingjing_text;
    @InjectView(R.id.name)
    TextView name_text;
    @InjectView(R.id.avatar)
    ImageView avatar;

    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.inject(this);
        final String uid = Athority.getSharedPreference().getString("uid", "");
        TaskUtils.executeAsyncTask(new AsyncTask<String, Void, ArrayList<BasePost>>() {
            @Override
            protected ArrayList<BasePost> doInBackground(String... params) {
                ArrayList<BasePost> posts = new ArrayList<BasePost>();
                Document doc = null;
                Connection.Response response = Request.execute(String.format(Api.PROFILE, uid), Api.USER_AGENT, (Map<String, String>) Athority.getSharedPreference().getAll(), Connection.Method.GET);
                try {
                    doc = response.parse();
                    Elements user_boxs = doc.getElementsByClass("user_box");
                    Elements avatar_ms = doc.getElementsByClass("avatar_m");
                    for (Element avatar_m : avatar_ms) {
                        Elements imgs = avatar_m.getElementsByTag("img");
                        url = imgs.attr("src");
                    }
                    name = Athority.getSharedPreference().getString("username", "");
                    for (Element user_box : user_boxs) {
                        int i = 1;
                        for (Element span : user_box.getElementsByTag("span")) {
                            switch (i) {
                                case 1:
                                    jifen = span.text();
                                    i++;
                                    break;
                                case 2:
                                    weiwang = span.text();
                                    i++;
                                    break;
                                case 3:
                                    shuidi = span.text();
                                    i++;
                                    break;
                                case 4:
                                    cunzaigan = span.text();
                                    i++;
                                    break;
                                case 5:
                                    bingjing = span.text();
                                    i++;
                                    break;
                            }
                        }
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
               url = "http://bbs.stuhome.net/uc_server/data/avatar/000/" + uid.substring(0, 2) + "/" + uid.substring(2, 4) + "/" + uid.substring(4) + "_avatar_small.jpg";
                Log.e("url", url);
                bitmap = getBitmap(url);
                return posts;
            }

            @Override
            protected void onPostExecute(ArrayList<BasePost> posts) {
                super.onPostExecute(posts);
                jifen_text.setText(jifen);
                weiwang_text.setText(weiwang);
                shuidi_text.setText(shuidi);
                cunzaigan_text.setText(cunzaigan);
                bingjing_text.setText(bingjing);
                name_text.setText(name);
                if (bitmap != null) {
                    avatar.setImageBitmap(bitmap);
                }
            }
        });
    }

    public Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            HttpClient client = new DefaultHttpClient();
            URI uri = URI.create(url);
            HttpGet get = new HttpGet(uri);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            long length = entity.getContentLength();
            Log.i("czb", " " + length);
            InputStream in = entity.getContent();
            if (in != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap = BitmapFactory.decodeStream(in);
                in.close();
                baos.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

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
