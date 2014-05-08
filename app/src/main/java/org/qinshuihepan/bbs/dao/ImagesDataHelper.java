package org.qinshuihepan.bbs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;

import org.qinshuihepan.bbs.model.Image;
import org.qinshuihepan.bbs.util.database.Column;
import org.qinshuihepan.bbs.util.database.SQLiteTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liurongchan on 14-4-25.
 */
public class ImagesDataHelper extends BaseDataHelper {
    public ImagesDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.IMAGES_CONTENT_URI;
    }

    private ContentValues getContentValues(Image image) {
        ContentValues values = new ContentValues();
        values.put(ImagesDBInfo.PID, image.pid);
        values.put(ImagesDBInfo.URL, image.url);
        return values;
    }

    public Image query(long pid) {
        Image image = null;
        Cursor cursor = query(null, ImagesDBInfo.PID + "=?",
                new String[]{
                        String.valueOf(pid)
                }, null
        );
        if (cursor.moveToFirst()) {
            image = Image.fromCursor(cursor);
        }
        cursor.close();
        return image;
    }


    public ArrayList<Image> queryImages(long pid) {
        ArrayList<Image> images = null;
        Cursor cursor = query(null, ImagesDBInfo.PID + "=?",
                new String[]{
                        String.valueOf(pid)
                }, null
        );
        if (cursor.moveToFirst()) {
            images = Image.listfromCursor(cursor);
        }
        cursor.close();
        return images;
    }

    public void bulkInsert(List<Image> images) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for (Image image : images) {
            ContentValues values = getContentValues(image);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    public int deleteAll() {
        synchronized (DataProvider.DBLock) {
            DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int row = db.delete(ImagesDBInfo.TABLE_NAME, null, null);
            return row;
        }
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null, null, ImagesDBInfo._ID + " ASC");
    }

    public static final class ImagesDBInfo implements BaseColumns {
        private ImagesDBInfo() {
        }

        public static final String TABLE_NAME = "images";

        public static final String PID = "pid";

        public static final String URL = "url";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(PID, Column.DataType.INTEGER)
                .addColumn(URL, Column.DataType.TEXT);
    }

}
