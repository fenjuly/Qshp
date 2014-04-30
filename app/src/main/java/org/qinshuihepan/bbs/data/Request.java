package org.qinshuihepan.bbs.data;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liurongchan on 14-4-24.
 */
public class Request {


    public static Connection.Response execute(String url, String agent, Map<String, String> datas, Map<String, String> cookies, Connection.Method method) {
        Connection.Response response = null;
        try {
            response =
                    Jsoup.connect(url).userAgent(agent).timeout(10*1000).data(datas).cookies(cookies).method(method).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Connection.Response execute(String url, String agent, Connection.Method method) {
        Connection.Response response = null;
        try {
            response =
                    Jsoup.connect(url).userAgent(agent).timeout(10*1000).method(method).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Connection.Response execute(String url, String agent, Map<String, String> cookies, Connection.Method method) {
        Connection.Response response = null;
        try {
            response =
                    Jsoup.connect(url).userAgent(agent).timeout(10*1000).cookies(cookies).method(method).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
