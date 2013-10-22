package soho.chloe.checkinrobot.service;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.htmlparser.util.ParserException;

import soho.chloe.checkinrobot.bean.UserBean;

public class XiamiService {

	private XiamiLogin1Service login1 = new XiamiLogin1Service();
	private XiamiLogin2Service login2 = new XiamiLogin2Service();
	private XiamiNeedCheckInService need = new XiamiNeedCheckInService();
	private XiamiSignCountService signCount = new XiamiSignCountService();
	private XiamiCheckInService checkIn = new XiamiCheckInService();

	public String goXiami(UserBean user) throws ClientProtocolException,
			ParseException, ParserException, IOException {
		login2.login(user, login1.getLoginDomain());
		if (need.seeIfNeedCheckIn(user)) {
			signCount.getSignCount(user);
			if (checkIn.checkIn(user)) {
				need.seeIfNeedCheckIn(user);
				return "在虾米签到成功，" + user.getSignInMessage();
			}
			return "在虾米签到失败";
		}
		return "在虾米" + user.getSignInMessage();
	}

}
