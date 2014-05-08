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
    public int pid;

    public Image(String url, int pid) {
        this.url = url;
        this.pid = pid;
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
        int pid = cursor.getInt(cursor.getColumnIndex(ImagesDataHelper.ImagesDBInfo.PID));
        image = new Image(url, pid);
        addToCache(image);
        return image;
    }

    public static ArrayList<Image> listfromCursor(Cursor cursor) {
        ArrayList<Image> images = new ArrayList<Image>();
        if (cursor.getCount() == 0) {
            return images;
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int pid = cursor.getInt(cursor.getColumnIndex(ImagesDataHelper.ImagesDBInfo.PID));
            String url = cursor.getString(cursor.getColumnIndex(ImagesDataHelper.ImagesDBInfo.URL));
            images.add(new Image(url, pid));
        }
        return images;
    }
}
