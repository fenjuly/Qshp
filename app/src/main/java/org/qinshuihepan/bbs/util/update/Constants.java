package org.qinshuihepan.bbs.util.update;

public class Constants {

    //服务器
	protected static final String APP_UPDATE_SERVER_URL = "http://222.197.183.133/development/index.html";
	
	// json {"url":"http://192.168.1.115:8080/xxx.apk","versionCode":2,"updateMessage":"版本更新信息"}
	//服务器返回的json数据格式，根据实际情况修改参数
	public static final String APK_DOWNLOAD_URL = "url";
	public static final String APK_UPDATE_CONTENT = "updateMessage";
	public static final String APK_VERSION_CODE = "versionCode";

}
