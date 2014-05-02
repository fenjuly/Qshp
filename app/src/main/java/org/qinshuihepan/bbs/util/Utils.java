package org.qinshuihepan.bbs.util;


import org.qinshuihepan.bbs.App;
import org.qinshuihepan.bbs.R;

import java.util.HashMap;

/**
 * Created by liurongchan on 14-4-25.
 */
public class Utils {

    public static final String FORUM_CATEGORY[] = App.getContext().getResources().getStringArray(R.array.forumcategory);

    public static final HashMap<String, Integer> FORUM_CATEGORY_ID = new HashMap<String, Integer>() {
        /**
         *
         */
        private static final long serialVersionUID = 1410556396714226136L;

        {
            put("最新回复", R.string.最新回复);
            put("最新发表", R.string.最新发表);
            put("今日热门", R.string.今日热门);

            put("保研考研", R.string.保研考研);
            put("LaTeX技术交流", R.string.LaTeX技术交流);
            put("聚焦两会", R.string.聚焦两会);
            put("镜头下的成电", R.string.镜头下的成电);
            put("一周锐评", R.string.一周锐评);
            put("名师博文", R.string.名师博文);
            put("大学生热点", R.string.大学生热点);
            put("成电UED", R.string.成电UED);
            put("前端之美", R.string.前端之美);
            put("互联网资讯", R.string.互联网资讯);
            put("数字前端", R.string.数字前端);
            put("数学之美", R.string.数学之美);
            put("电脑FAQ", R.string.电脑FAQ);
            put("硬件数码", R.string.硬件数码);
            put("Unix_Linux", R.string.Unix_Linux);
            put("程序员", R.string.程序员);
            put("电子设计", R.string.电子设计);
            put("就业创业", R.string.就业创业);
            put("手机之家", R.string.手机之家);
            put("出国留学", R.string.出国留学);
            put("相约回家", R.string.相约回家);
            put("学习交流", R.string.学习交流);
            put("成电轨迹", R.string.成电轨迹);
            put("情感专区", R.string.情感专区);
            put("生活百科", R.string.生活百科);
            put("老乡会", R.string.老乡会);
            put("成电骑迹", R.string.成电骑迹);
            put("摄影艺术", R.string.摄影艺术);
            put("旅游专版", R.string.旅游专版);
            put("动漫时代", R.string.动漫时代);
            put("会心一笑", R.string.会心一笑);
            put("影视天地", R.string.影视天地);
            put("游戏世界", R.string.游戏世界);
            put("经典图吧", R.string.经典图吧);
            put("娱乐花边", R.string.娱乐花边);
            put("体坛风云", R.string.体坛风云);
            put("音乐空间", R.string.音乐空间);
            put("情系舞缘", R.string.情系舞缘);
            put("校园新闻", R.string.校园新闻);
            put("时政要闻", R.string.时政要闻);
            put("社会百态", R.string.社会百态);
            put("科技教育", R.string.科技教育);
            put("军事国防", R.string.军事国防);
            put("经济相关", R.string.经济相关);
            put("二手专区", R.string.二手专区);
            put("店铺专区", R.string.店铺专区);
            put("房屋租赁", R.string.房屋租赁);
            put("水手之家", R.string.水手之家);
            put("历史_文化_人物", R.string.历史_文化_人物);
            put("文人墨客", R.string.文人墨客);
            put("我的大学生活", R.string.我的大学生活);
            put("毕业感言", R.string.毕业感言);
            put("母校发展_我来献策", R.string.母校发展_我来献策);
            put("站务公告", R.string.站务公告);
            put("站务综合", R.string.站务综合);
            put("影视资源", R.string.影视资源);
            put("体育资源", R.string.体育资源);
            put("动漫资源", R.string.动漫资源);
            put("软件资源", R.string.软件资源);
            put("音乐资源", R.string.音乐资源);
        }
    };
}
