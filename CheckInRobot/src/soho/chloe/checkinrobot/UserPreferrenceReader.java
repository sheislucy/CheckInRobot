package soho.chloe.checkinrobot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soho.chloe.checkinrobot.bean.UserBean;

public class UserPreferrenceReader {
	private static Map<String, UserBean> USERS = new HashMap<>();

	static {
		File userPreference = new File("user.dat");
		if (userPreference.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(
					userPreference));) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] config = line.split("=");
					if (null != config && config.length > 1) {
						String[] siteAndTitle = config[0].split("\\.");
						CheckInWebsite site = CheckInWebsite
								.getSite(siteAndTitle[0]);
						if (site != CheckInWebsite.UNKNOWN) {
							UserBean user = USERS.get(siteAndTitle[0]);
							if (null == user) {
								user = new UserBean();
								user.setSite(site);
								if (config[0].contains("username")) {
									user.setUserName(config[1]);
								} else if (config[0].contains("password")) {
									user.setPassword(config[1]);
								}
								USERS.put(siteAndTitle[0], user);
							} else {
								if (config[0].contains("username")) {
									user.setUserName(config[1]);
								} else if (config[0].contains("password")) {
									user.setPassword(config[1]);
								}
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<UserBean> getUserList() {
		return new ArrayList<UserBean>(USERS.values());
	}
}
