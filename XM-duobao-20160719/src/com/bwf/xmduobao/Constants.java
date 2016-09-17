package com.bwf.xmduobao;

public interface Constants {
//	String HOST = "http://172.22.200.221:8080";
//	String HOST = "http://192.168.31.100:8080";
	String HOST = "http://123.56.145.151:8080";
//	String HOST = "http://192.168.191.1:8080";
	String URL_HOME_CHANNEL = HOST + "/Duobao-XM/channel";
	String URL_HOME_GOODS_BY_HOT = HOST + "/Duobao-XM/home?pageNum={0}&pageSize={1}";
	String URL_HOME_GOODS_BY_TIMES_ASC = HOST + "/Duobao-XM/home?pageNum={0}&pageSize={1}&orderBy=totalTimes";
	String URL_HOME_GOODS_BY_TIMES_DESC = HOST + "/Duobao-XM/home?pageNum={0}&pageSize={1}&orderBy=totalTimes&order=desc";
	String URL_CATEGORY = HOST + "/Duobao-XM/category";
	String URL_CATEGORY_GOODS_LIST = HOST + "/Duobao-XM/list?category={0}";
	String URL_QUESTION = HOST + "/Duobao-XM/questions";
	String URL_PERIOD_DETAIL = HOST + "/Duobao-XM/period-detail?period={0}";
	String URL_GOODS_DETAIL = HOST + "/Duobao-XM/goods-detail?goodsId={0}";
	String URL_DUOBAO_RECORD = HOST + "/Duobao-XM/record-list?period={0}&pageNum={1}&pageSize=20&since={2}";
	String URL_WIN_HISTORY = HOST + "/Duobao-XM/winhistory?gid={0}";
	String URL_COMPUTE_DETAIL = HOST + "/Duobao-XM/period_compute_detail?period={0}";
	String URL_LATEST_PERIOD = HOST + "/Duobao-XM/latest-period?gid={0}";
	String URL_REVEALED_LASTEST = HOST + "/Duobao-XM/revealed-lastest?pageNum={0}&pageSize={1}";
	String URL_UPDATE_PERIOD_INFO = HOST + "/Duobao-XM/period-info?period={0}";
	String URL_JOIN = HOST + "/Duobao-XM/duobao?times={0}&period={1}&token={2}&IP={3}&IPAddress={4}";
	String URL_LOGIN_OTHER_PLATFORM = HOST+"/Duobao-XM/platform-login";
	String URL_GET_IP_ADDRESS = "http://api.ip138.com/query/?token=a5d252dce07de076872b602c1c6bd1b6";
	int TYPE_PHONE = 0;
	int TYPE_WEIXIN = 1;
	int TYPE_QQ = 2;
	int TYPE_SINA = 3;
	/**
	 * periods=[119923694,119923892]
	 * times=[100,200]
	 * token=12dbb705540b22c752ab1b494a6ef764
	 */
	String URL_ADD_TO_LIST = HOST + "/Duobao-XM/add_to_list";
	String URL_GET_DUOBAO_LIST = HOST + "/Duobao-XM/unpay_list?uid={0}";
	/**
	 * periods=[119923694,119923892]
	 * times=[100,200]
	 * token=12dbb705540b22c752ab1b494a6ef764
	 * IP
	 * IPAddress
	 */
	String URL_DUOBAO = HOST + "/Duobao-XM/duobao2";
	/**
	 * token
	 * periods
	 * times
	 */
	String URL_UPDATE_DUOBAO_LIST = HOST + "/Duobao-XM/update_list";
}
