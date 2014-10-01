package org.qinshuihepan.bbs.api;

/**
 * Created by liurongchan on 14-4-23.
 */
public class Api {

    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";

    public static final String HOST = "http://bbs.stuhome.net/";

    public static final String COOKIE_LOGIN = HOST + "member.php?mod=logging&action=login";

    public static final String LOGIN = HOST + "%1$s" + "&inajax=1"; //paramter: loginaction

    public static final String POSTS = HOST + "forum.php?mod=forumdisplay&fid=" + "%1$s" + "&page=" + "%2$s"; //paramter: id, page_number

    public static final String POST_CONTENT = HOST + "forum.php?mod=viewthread&tid=" + "%1$s" + "&extra=page&page=" + "%2$s"; //paramter: tid

    public static final String REPLY = HOST + "forum.php?mod=post&infloat=yes&action=reply&fid=" + "%1$s" + "&extra=page&tid=" + "%2$s" + "&replysubmit=yes&handlekey=fastpost&inajax=1"; //paramter: fid, tid

    public static final String REPLY_SINGLE = HOST + "forum.php?mod=post&infloat=yes&action=reply&fid=" + "%1$s" + "&extra=page&tid=" + "%2$s" + "&replysubmit=yes&inajax=1";

    public static final String LEAVE_MESSAGE = HOST + "home.php?mod=space&uid=" + "%1$s" + "&do=wall";

    //Mobile API
    public static final String MOBILE_COOKIE_LOGIN = HOST + "member.php?mod=logging&action=login&mobile=1";

    public static final String MOBILE_SEARCH = HOST + "search.php?mod=forum";

    public static final String MY_POSTS = HOST + "home.php?mod=space&uid=" + "%1$s" + "&do=thread&view=me&mobile=1"; //paramter: uid

    public static final String MY_COLLECTIONS = HOST + "home.php?mod=space&uid=" + "%1$s" + "&do=favorite&view=me&type=thread&mobile=2";  //paramter: uid

    public static final String MY_MESSAGES = HOST + "home.php?mod=space&do=pm&mobile=1";

    public static final String MESSAGE_CONTENT = HOST + "home.php?mod=space&do=pm&subop=view&touid=" + "%1$s" +"&mobile=1";

    public static final String PROFILE = HOST + "home.php?mod=space&uid=" + "%1$s" + "&do=profile&mobile=2";
}
