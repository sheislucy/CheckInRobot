package soho.chloe.checkinrobot.bean;

import java.util.HashMap;
import java.util.Map;

import soho.chloe.checkinrobot.CheckInWebsite;

public class UserBean {
	private String userName;
	private String password;
	private CheckInWebsite site;
	private Map<String, Object> cookieMap = new HashMap<String, Object>();
	private String signInMessage;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, Object> getCookieMap() {
		return cookieMap;
	}

	public void setCookieMap(Map<String, Object> cookieMap) {
		this.cookieMap = cookieMap;
	}

	public CheckInWebsite getSite() {
		return site;
	}

	public void setSite(CheckInWebsite site) {
		this.site = site;
	}

	public String getSignInMessage() {
		return signInMessage;
	}

	public void setSignInMessage(String signInMessage) {
		this.signInMessage = signInMessage;
	}

}
