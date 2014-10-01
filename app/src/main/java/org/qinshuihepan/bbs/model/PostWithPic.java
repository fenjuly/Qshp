package org.qinshuihepan.bbs.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.qinshuihepan.bbs.R;
import org.qinshuihepan.bbs.api.Api;
import org.qinshuihepan.bbs.ui.ImageViewActivity;
import java.util.ArrayList;

/**
 * Created by liurongchan on 14-4-23.
 */
public class PostWithPic extends BasePost implements ListItem {

    public PostWithPic(int fid, int tid, int pid, String title, String content, String time, int haveimg, int comment_count, String author, ArrayList<Image> images) {
        super(fid, tid, pid, title, content, time, haveimg, comment_count, author, images);
    }

    public static void bindView(View view, final Context context, Cursor cursor) {
        LinearLayout layout;
        ArrayList<ImageView> imgviews = new ArrayList<ImageView>();
        ArrayList<Image> images;
        Holder holder = getHolder(view);
        layout = holder.layout;
        BasePost post = BasePost.fromCursor(cursor, ITEM);
        images = post.images;
        imgviews.clear();
        for (int i = 0; i < images.size(); i++) {
            ImageView img = new ImageView(context);
            final String imageurl;
            imgviews.add(img);
            layout.addView(img);
            if (images.get(i).url.startsWith("data")) {
                imageurl = Api.HOST + "/" + images.get(i).url;
                Picasso.with(context).load(imageurl).error(R.drawable.placeholder_fail).resize(150, 150)
                        .into(img);
            } else {
                imageurl = images.get(i).url;
                Picasso.with(context).load(images.get(i).url).error(R.drawable.placeholder_fail).resize(150, 150).into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra(ImageViewActivity.IMAGE_URL, imageurl);
                    context.startActivity(intent);
                }
            });
        }

        holder.time.setText(post.time);
        holder.author.setText(post.author);
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
