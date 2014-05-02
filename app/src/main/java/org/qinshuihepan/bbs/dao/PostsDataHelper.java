package org.qinshuihepan.bbs.dao;

/**
 * Created by liurongchan on 14-4-23.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;

import org.qinshuihepan.bbs.model.BasePost;
import org.qinshuihepan.bbs.util.database.Column;
import org.qinshuihepan.bbs.util.database.SQLiteTable;

import java.util.ArrayList;
import java.util.List;

public class PostsDataHelper extends BaseDataHelper {

    public PostsDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.POSTS_CONTENT_URI;
    }

    private ContentValues getContentValues(BasePost post) {
        ContentValues values = new ContentValues();
        values.put(PostsDBInfo.FID, post.fid);
        values.put(PostsDBInfo.PID, post.pid);
        values.put(PostsDBInfo.TID, post.tid);
        values.put(PostsDBInfo.TITLE, post.title);
        values.put(PostsDBInfo.CONTENT, post.content);
        values.put(PostsDBInfo.TIME, post.time);
        values.put(PostsDBInfo.HAVEIMG, post.haveimg);
        values.put(PostsDBInfo.COMMENT_COUNT, post.comment_count);
        values.put(PostsDBInfo.AUTHOR, post.author);
        return values;
    }

    public BasePost query(long tid) {
        BasePost post = null;
        Cursor cursor = query(null, PostsDBInfo.TID + "=?",
                new String[]{
                        String.valueOf(tid)
                }, null
        );
        if (cursor.moveToFirst()) {
            post = post.fromCursor(cursor);
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
            int row = db.delete(PostsDBInfo.TABLE_NAME, null, null);
            return row;
        }
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null, null, PostsDBInfo._ID + " ASC");
    }

    public static final class PostsDBInfo implements BaseColumns {
        private PostsDBInfo() {
        }

        public static final String TABLE_NAME = "posts";

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

