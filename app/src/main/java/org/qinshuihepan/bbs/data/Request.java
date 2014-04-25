package org.qinshuihepan.bbs.data;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liurongchan on 14-4-24.
 */
public class Request {

    private String url;
    private String agent;
    private Map<String, String> datas;
    private Map<String, String> cookies;
    private Connection.Method method;

    public Request(String url, String agent, Map<String, String> datas, Map<String, String> cookies, Connection.Method method) {
        this.url = url;
        this.agent = agent;
        this.datas = datas;
        this.cookies = cookies;
        this.method = method;
    }


    public Connection.Response execute() {
        Connection.Response response = null;
        try {
            response =
                    Jsoup.connect(url).data(datas).cookies(cookies).method(method).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
