package org.qinshuihepan.bbs.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by liurongchan on 14-4-23.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "uestc.db";

    private static final int VERSION = 1;

    public  DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PostsDataHelper.PostsDBInfo.TABLE.create(db);
        ImagesDataHelper.ImagesDBInfo.TABLE.create(db);
        ItemsDataHelper.ItemsDBInfo.TABLE.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
