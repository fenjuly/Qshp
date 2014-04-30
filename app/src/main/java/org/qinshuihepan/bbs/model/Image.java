package org.qinshuihepan.bbs.model;

import android.database.Cursor;

import org.qinshuihepan.bbs.dao.ImagesDataHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liurongchan on 14-4-25.
 */
public class Image {

    private static final HashMap<String, Image> CACHE = new HashMap<String, Image>();

    public String url;
    public int tid;

    public Image(String url, int tid) {
        this.url = url;
        this.tid = tid;
    }


    private static void addToCache(Image image) {
        CACHE.put(image.url, image);
    }

    private static Image getFromCache(String url) {
        return CACHE.get(url);
    }


    public static Image fromCursor(Cursor cursor) {
        String url = cursor.getString(cursor.getColumnIndex(ImagesDataHelper.ImagesDBInfo.URL));
        Image image = getFromCache(url);
        if (image != null) {
            return image;
        }
        int tid = cursor.getInt(cursor.getColumnIndex(ImagesDataHelper.ImagesDBInfo.TID));
        image = new Image(url, tid);
        addToCache(image);
        return image;
    }

    public static ArrayList<Image> listfromCursor(Cursor cursor) {
        ArrayList<Image> images = new ArrayList<Image>();
        if (cursor.getCount() == 0) {
            return images;
        }
        while (cursor.moveToNext()) {
            int tid = cursor.getInt(cursor.getColumnIndex(ImagesDataHelper.ImagesDBInfo.TID));
            String url = cursor.getString(cursor.getColumnIndex(ImagesDataHelper.ImagesDBInfo.URL));
            images.add(new Image(url, tid));
        }
        return images;
    }
}
