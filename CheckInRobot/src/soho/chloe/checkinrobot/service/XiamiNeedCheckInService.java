package soho.chloe.checkinrobot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import soho.chloe.checkinrobot.CheckInBuggieConstant;
import soho.chloe.checkinrobot.bean.UserBean;

public class XiamiNeedCheckInService extends BaseService {

	@Override
	protected List<BasicHeader> buildCommonHeaders() {
		return new ArrayList<BasicHeader>();
	}

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return new ArrayList<BasicHeader>();
	}

	public boolean seeIfNeedCheckIn(UserBean user)
			throws ClientProtocolException, IOException, ParserException {
		setUrl("http://www.xiami.com/home/indexright");
		setCookieStore(buildCookieStore(user));
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity(),
				CheckInBuggieConstant.ENCODING_UTF8);
		Parser parser = new Parser(responseBody);
		NodeList divNodes = parser.extractAllNodesThatMatch(new TagNameFilter(
				"div"));
		for (int j = 0; j < divNodes.size(); j++) {
			Div div = (Div) divNodes.elementAt(j);
			if (null != div.getAttribute("id")
					&& div.getAttribute("id").equals("rank")) {
				NodeList linkNodes = div.getChildren()
						.extractAllNodesThatMatch(new TagNameFilter("div"),
								true);
				if (null != linkNodes) {
					for (int i = 0; i < linkNodes.size(); i++) {
						Div bTag = (Div) linkNodes.elementAt(i);
						if (null != bTag.getAttribute("class")
								&& bTag.getAttribute("class")
										.contains("action")) {
							String text = bTag.getStringText();
							if (-1 != text.indexOf("已签到")) {
								text = text.substring(text.indexOf("已签到"),
										text.indexOf("</b>"));
								user.setSignInMessage(text);
								return false;
							} else {
								return true;
							}
						}

					}
				}
			}
		}
		user.getCookieMap().put("__XIAMI_SESSID",
				UUID.randomUUID().toString().replace("-", ""));
		return true;
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
