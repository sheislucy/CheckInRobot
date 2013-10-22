package soho.chloe.checkinrobot.service;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;

import soho.chloe.checkinrobot.bean.UserBean;

public class XiamiSignCountService extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public void getSignCount(UserBean user) throws ClientProtocolException,
			IOException {
		setUrl("http://www.xiami.com/home/indexright");
		setCookieStore(buildCookieStore(user));
		HttpResponse response = doGet();
		Header[] headers = response.getHeaders("Set-Cookie");
		for (Header header : headers) {
			for (String pairStr : cut(header.getValue())) {
				String[] pair = pairStr.split("=");
				if (pair != null && pair.length > 1) {
					if (pair[0].equalsIgnoreCase("t_sign_auth")) {
						user.getCookieMap().put(pair[0], pair[1]);
					}
				}
			}
		}
	}

	private String[] cut(String stringPiece) {
		return stringPiece.split(";");
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
