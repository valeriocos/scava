package org.eclipse.scava.platfrom.communicationchannel.eclipseforums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;//REMOVE only used for testing

import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.ICommunicationChannelManager;
import org.eclipse.scava.platfrom.communicationchannel.eclipseforums.api.EclipseForumComunicationChannelDelta;
import org.eclipse.scava.platfrom.communicationchannel.eclipseforums.api.EclipseForumUtils;
import org.eclipse.scava.platfrom.communicationchannel.eclipseforums.api.EclipseForumsPost;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForums;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;

import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class EclipseForumsManager implements ICommunicationChannelManager<EclipseForums> {

	public EclipseForumsManager(EclipseForums ef) throws Exception {
		this.postList = new ArrayList<>();

	}

	private static String host = "https://api.eclipse.org/";
	private final static String PAGE_SIZE = "100";
	private List<EclipseForumsPost> postList;

	// IMPLEMENTED METHODS START
	@Override
	public boolean appliesTo(CommunicationChannel eclipseForum) {
		return eclipseForum instanceof EclipseForums;
	}

	@Override
	public Date getFirstDate(DB db, EclipseForums eclipseForums) throws Exception {

		Date firstPostDate;
		OkHttpClient client = new OkHttpClient();

		HttpUrl.Builder urlBuilder = HttpUrl.parse(host).newBuilder();
		urlBuilder.addEncodedPathSegment("forums");
		urlBuilder.addEncodedPathSegment("post");
		urlBuilder.addEncodedQueryParameter("topic_id", eclipseForums.getTopic_id());
		urlBuilder.addEncodedQueryParameter("order_by", "DESC");
		urlBuilder.addEncodedQueryParameter("pagesize", PAGE_SIZE);

		String requestUrl = urlBuilder.build().toString();

		Request request = new Request.Builder().url(requestUrl).build();

		Response response = client.newCall(request).execute();

		System.out.println(response.headers());

		final JsonNode arrNode = new ObjectMapper().readTree(response.body().string()).get("result");

		response.close();

		if (arrNode.isArray()) {
			for (JsonNode objNode : arrNode) {

				EclipseForumsPost post = new EclipseForumsPost();
				// post.setNewsgroup(EclipseForums); // Need to see how we set the communication
				// channel
				post.setArticleId(EclipseForumUtils.fixString(objNode.findValue("id").toString()));
				post.setSubject(EclipseForumUtils.fixString(objNode.findValue("subject").toString()));
				post.setTopicId(EclipseForumUtils.fixString(objNode.findValue("topic_id").toString()));
				post.setPosterId(EclipseForumUtils.fixString(objNode.findValue("poster_id").toString()));
				post.setCreatedDate(
						EclipseForumUtils.convertStringToDate(objNode.findValue("created_date").toString()));
				post.setForumId(EclipseForumUtils.fixString(objNode.findValue("forum_id").asText()));
				post.setBody(EclipseForumUtils.fixString(objNode.findValue("body").toString()));
				post.setHtmlUrl(EclipseForumUtils.fixString(objNode.findValue("html_url").toString()));
				post.setTopicId(EclipseForumUtils.fixString(objNode.findValue("topic_url").toString()));
				post.setForumId(EclipseForumUtils.fixString(objNode.findValue("forum_url").toString()));
				post.setUser(EclipseForumUtils.fixString(objNode.findValue("user_url").toString()));

				postList.add(post);
			}
		}

		firstPostDate = (postList.get(postList.size() - 1).getCreatedDate());
		// TODO remove print out and pause
		System.out.println("FORUM SUBJECT   : " + postList.get(0).getSubject());
		System.out.println("FIRST DATE      : " + firstPostDate);
		System.out.println("NUMBER OF POSTS : " + postList.size());
		TimeUnit.MILLISECONDS.sleep(5);

		return firstPostDate;
	}

	@Override
	public CommunicationChannelDelta getDelta(DB db, EclipseForums communicationChannel, Date date) {

		EclipseForumComunicationChannelDelta delta = new EclipseForumComunicationChannelDelta();
		delta.setNewsgroup(communicationChannel);

		// TODO remove
		System.err.println("\nDate: " + date);

		// OPTIMISATION |
		// Reads the last element in the list, which are descending order
		// (newest first). If a post is added to delta, remove from list.
		// Removing from the bottom of the list prevents shifting all elements
		// to the left ( or simply...current index of element - 1).
		// Also, if a new creation date is detected return delta, this minimises
		// the number of iterations through the list.

		try {
			Boolean flag;
			do {
				int i = postList.size() - 1;
				flag = false;
				if (DateUtils.isSameDay(postList.get(i).getCreatedDate().toJavaDate(), date.toJavaDate())) {
					flag = true;
					// TODO Remove print out
					System.out.println(
							"\t [POST] " + postList.get(i).getArticleId() + " made by " + postList.get(i).getUser());
					delta.getArticles().add(postList.get(i));
					postList.remove(i);

				} else {

					return delta;

				}

			} while (flag = true);

		} catch (ArrayIndexOutOfBoundsException e) {

			return delta;
		}

		// This should never be reached.
		return null;
	}

	@Override
	public String getContents(DB db, EclipseForums communicationChannel, CommunicationChannelArticle article)
			throws Exception {
		// TODO Auto-generated method stub
		// NOT USED
		return null;
	}

	// IMPLEMENTED METHODS END

	// TODO Remove once complete
	public static void main(String[] args) throws Exception {

		EclipseForums ef = new EclipseForums();

		// EXAMPLE FORUMS
		ef.setTopic_id("1092788"); // Can't run java program in Eclipse - javaw error
		// ef.setTopic_id("799069");// Eclipse Won't Start

		EclipseForumsManager efc = new EclipseForumsManager(ef);

		Date today = new Date();
		Date date = new Date(efc.getFirstDate(null, ef).toString());

		do {

			efc.getDelta(null, ef, date);
			// System.out.println(efc.getDelta(null, ef, date).getArticles()); // This is
			// for viewing the contents of the deltas
			date = new Date(date.addDays(1).toString());

		} while (date.compareTo(today) < 1);

	}

}