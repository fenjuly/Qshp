package org.qinshuihepan.bbs.api;

/**
 * Created by liurongchan on 14-4-23.
 */
public class Api {

    public static final String HOST = "http://bbs.stuhome.net";

    public static final String COOKIE_LOGIN = HOST + "/member.php?mod=logging&action=login";

    public static final String LOGIN = HOST + "/%1$s" + "&inajax=1"; //paramter: loginaction

    public static final String POSTS = HOST + "/forum.php?mod=forumdisplay&fid=" + "%1$s" + "&page=" + "%2$s"; //paramter: id, page_number

    public static final String POST_CONTENT = HOST + "/forum.php?mod=viewthread&tid=" + "%1$s" + "&extra=page&page=" + "%2$s"; //paramter: tid

    public static final String REPLY = HOST + "/forum.php?mod=post&infloat=yes&action=reply&fid=" + "%1$s" + "&extra=page&tid=" + "%2$s" + "&replysubmit=yes&handlekey=fastpost&inajax=1"; //paramter: fid, tid

    public static final String REPLY_SINGLE = HOST + "/forum.php?mod=post&infloat=yes&action=reply&fid=" + "%1$s" + "&extra=page&tid=" + "%2$s" + "&replysubmit=yes&inajax=1";
}
