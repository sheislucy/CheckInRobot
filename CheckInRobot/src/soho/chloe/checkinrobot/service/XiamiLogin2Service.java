package soho.chloe.checkinrobot.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicHeader;

import soho.chloe.checkinrobot.bean.LoginFormBean;
import soho.chloe.checkinrobot.bean.UserBean;

public class XiamiLogin2Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public void login(UserBean user, LoginFormBean form)
			throws ClientProtocolException, IOException {
		setUrl("https://login.xiami.com/member/login");
		Map<String, String> loginParam = form.getFormMap();
		loginParam.put("email", user.getUserName());
		loginParam.put("password", user.getPassword());
		setFormParamMap(loginParam);

		HttpResponse response = doPost();
		StatusLine statusLine = response.getStatusLine();
		if (null != statusLine && statusLine.getStatusCode() == 302) {
			Header[] headers = response.getHeaders("Set-Cookie");
			user.getCookieMap().clear();
			for (Header header : headers) {
				for (String pairStr : cut(header.getValue())) {
					String[] pair = pairStr.split("=");
					if (pair != null && pair.length > 1) {
						if (pair[0].equalsIgnoreCase("member_auth")) {
							user.getCookieMap().put(pair[0], pair[1]);
						} else if (pair[0].equalsIgnoreCase("_xiamitoken")) {
							user.getCookieMap().put(pair[0], pair[1]);
						}
					}
				}
			}
		}
	}

	private String[] cut(String stringPiece) {
		return stringPiece.split(";");
	}
}
