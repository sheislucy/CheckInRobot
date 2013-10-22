package soho.chloe.checkinrobot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;

import soho.chloe.checkinrobot.bean.UserBean;

public class XiamiCheckInService extends BaseService {

	@Override
	protected List<BasicHeader> buildCommonHeaders() {
		return new ArrayList<BasicHeader>();
	}

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		List<BasicHeader> headers = new ArrayList<BasicHeader>();
		headers.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"));
		headers.add(new BasicHeader("Origin", "http://www.xiami.com"));
		headers.add(new BasicHeader("Referer",
				"http://www.xiami.com/?spm=0.0.0.0.uakRuE"));
		return headers;
	}

	public boolean checkIn(UserBean user) throws ClientProtocolException,
			IOException {
		setUrl("http://www.xiami.com/task/signin");
		setFormParamMap(new HashMap<String, String>());
		setCookieStore(buildCookieStore(user));
		HttpResponse response = doPost();
		StatusLine statusLine = response.getStatusLine();
		return null != statusLine && statusLine.getStatusCode() == 200;
	}

	protected CookieStore buildCookieStore(UserBean user) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		for (Entry<String, Object> cookieEntry : user.getCookieMap().entrySet()) {
			BasicClientCookie cookie = new BasicClientCookie(
					cookieEntry.getKey(), (String) cookieEntry.getValue());
			cookie.setDomain(".xiami.com");
			cookie.setPath("/");
			cookieStore.addCookie(cookie);
		}
		return cookieStore;
	}

}
