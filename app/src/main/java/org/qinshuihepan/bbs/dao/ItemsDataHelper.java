package org.qinshuihepan.bbs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;
import android.widget.BaseAdapter;

import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.util.database.Column;
import org.qinshuihepan.bbs.util.database.SQLiteTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liurongchan on 14-5-2.
 */
public class ItemsDataHelper extends BaseDataHelper {

    public ItemsDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.ITEMS_CONTENT_URI;
    }

    private ContentValues getContentValues(BasePost post) {
        ContentValues values = new ContentValues();
        values.put(ItemsDBInfo.FID, post.fid);
        values.put(ItemsDBInfo.PID, post.pid);
        values.put(ItemsDBInfo.TID, post.tid);
        values.put(ItemsDBInfo.TITLE, post.title);
        values.put(ItemsDBInfo.CONTENT, post.content);
        values.put(ItemsDBInfo.TIME, post.time);
        values.put(ItemsDBInfo.HAVEIMG, post.haveimg);
        values.put(ItemsDBInfo.COMMENT_COUNT, post.comment_count);
        values.put(ItemsDBInfo.AUTHOR, post.author);
        return values;
    }

    public BasePost query(long tid) {
        BasePost post = null;
        Cursor cursor = query(null, ItemsDBInfo.TID + "=?",
                new String[]{
                        String.valueOf(tid)
                }, null
        );
        if (cursor.moveToFirst()) {
            post = post.fromCursor(cursor, BasePost.ITEM);
        }
        cursor.close();
        return post;
    }

    public void bulkInsert(List<BasePost> posts) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for (BasePost post : posts) {
            ContentValues values = getContentValues(post);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    public int deleteAll() {
        synchronized (DataProvider.DBLock) {
            DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int row = db.delete(ItemsDBInfo.TABLE_NAME, null, null);
            return row;
        }
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null, null, ItemsDBInfo._ID + " ASC");
    }

    public static final class ItemsDBInfo implements BaseColumns {
        private ItemsDBInfo() {
        }

        public static final String TABLE_NAME = "items";

        public static final String FID = "fid";

        public static final String TID = "tid";

        public static final String PID = "pid";

        public static final String TITLE = "title";

        public static final String CONTENT = "content";

        public static final String TIME = "time";

        public static final String HAVEIMG = "haveimg";

        public static final String COMMENT_COUNT = "comment_count";

        public static final String AUTHOR = "author";


        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(FID, Column.DataType.INTEGER)
                .addColumn(TID, Column.DataType.INTEGER)
                .addColumn(PID, Column.DataType.INTEGER)
                .addColumn(TITLE, Column.DataType.TEXT)
                .addColumn(CONTENT, Column.DataType.TEXT)
                .addColumn(TIME, Column.DataType.TEXT)
                .addColumn(HAVEIMG, Column.DataType.INTEGER)
                .addColumn(COMMENT_COUNT, Column.DataType.INTEGER)
                .addColumn(AUTHOR, Column.DataType.TEXT);
    }

}
