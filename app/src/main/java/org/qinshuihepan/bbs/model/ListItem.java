package org.qinshuihepan.bbs.model;

import android.database.Cursor;

import java.util.HashMap;

/**
 * Created by liurongchan on 14-4-23.
 */
public interface ListItem {
    /**
     * this interface is used to display posts in listview
     */

    public static final HashMap<Integer, BasePost> CACHE = new HashMap<Integer, BasePost>();

}
