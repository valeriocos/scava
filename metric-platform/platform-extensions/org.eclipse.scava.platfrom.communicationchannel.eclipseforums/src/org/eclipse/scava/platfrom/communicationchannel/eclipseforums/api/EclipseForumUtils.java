package org.eclipse.scava.platfrom.communicationchannel.eclipseforums.api;

import org.eclipse.scava.platform.Date;

public class EclipseForumUtils {

	public static Date convertStringToDate(String timestamp) {

		String timestampFix = EclipseForumUtils.fixString(timestamp);
		Long unixTimestamp = Long.parseLong(timestampFix);
		Date platformDate = new Date(unixTimestamp * 1000L);

		return platformDate;
	}

	/**
	 * 
	 * Removes " (quotation marks from the first and last index of the string)
	 * 
	 * @param string
	 * @return fixedString
	 */
	public static String fixString(String string) {

		String fixedString = string.substring(1, string.length() - 1);

		return fixedString;
	}
}
