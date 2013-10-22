package soho.chloe.checkinrobot.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.FormTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import soho.chloe.checkinrobot.CheckInBuggieConstant;
import soho.chloe.checkinrobot.bean.LoginFormBean;

public class XiamiLogin1Service extends BaseService {

	@Override
	protected List<BasicHeader> extendRequestHeader() {
		return null;
	}

	public LoginFormBean getLoginDomain() throws ParseException, IOException,
			ParserException {
		setUrl("http://www.xiami.com/");
		HttpResponse response = doGet();
		String responseBody = EntityUtils.toString(response.getEntity(),
				CheckInBuggieConstant.ENCODING_UTF8);
		Parser parser = new Parser(responseBody);
		NodeFilter filter = new TagNameFilter("form");
		NodeList linkNodes = parser.extractAllNodesThatMatch(filter);
		if (null != linkNodes) {
			for (int i = 0; i < linkNodes.size(); i++) {
				FormTag formTag = (FormTag) linkNodes.elementAt(i);
				if (null != formTag.getAttribute("action")
						&& formTag.getAttribute("action").equals(
								"https://login.xiami.com/member/login")) {
					LoginFormBean loginFormBean = new LoginFormBean();
					NodeList formChildren = formTag.getChildren();
					NodeList inputNodes = formChildren
							.extractAllNodesThatMatch(
									new TagNameFilter("input"), true);
					for (int j = 0; j < inputNodes.size(); j++) {
						TagNode inputTag = (TagNode) inputNodes.elementAt(j);
						if (("_xiamitoken").equalsIgnoreCase(inputTag
								.getAttribute("name"))) {
							loginFormBean.getFormMap().put("_xiamitoken",
									inputTag.getAttribute("value"));
						} else if (("done").equalsIgnoreCase(inputTag
								.getAttribute("name"))) {
							loginFormBean.getFormMap().put("done",
									inputTag.getAttribute("value"));
						} else if (("submit").equalsIgnoreCase(inputTag
								.getAttribute("name"))) {
							loginFormBean
									.getFormMap()
									.put("submit",
											URLEncoder.encode(
													inputTag.getAttribute("value"),
													CheckInBuggieConstant.ENCODING_GBK));
						}
					}
					return loginFormBean;
				}
			}
		}
		return null;
	}
}
