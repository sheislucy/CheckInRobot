package soho.chloe.checkinrobot.bean;

import java.util.HashMap;
import java.util.Map;

public class LoginFormBean {
	private Map<String, String> formMap = new HashMap<>();

	public Map<String, String> getFormMap() {
		return formMap;
	}

	public void setFormMap(Map<String, String> formMap) {
		this.formMap = formMap;
	}

}
