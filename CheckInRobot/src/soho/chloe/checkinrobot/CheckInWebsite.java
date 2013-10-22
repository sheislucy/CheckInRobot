package soho.chloe.checkinrobot;

public enum CheckInWebsite {
	XIAMI, YINYUETAI,

	UNKNOWN;

	public static CheckInWebsite getSite(String siteName) {
		if (null != siteName) {
			for (CheckInWebsite site : CheckInWebsite.values()) {
				if (siteName.contains(site.name())) {
					return site;
				}
			}
		}
		return UNKNOWN;
	}
}
