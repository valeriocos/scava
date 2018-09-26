package org.eclipse.scava.platform.communicationchannel.eclipseforums;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;//REMOVE only used for testing

import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.delta.communicationchannel.CommuincationChannelForumPost;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.ICommunicationChannelManager;
import org.eclipse.scava.platform.communicationchannel.eclipseforums.utils.EclipseForumUtils;
import org.eclipse.scava.platform.communicationchannel.eclipseforums.utils.EclipseForumsPost;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EclipseForumsManager implements ICommunicationChannelManager<EclipseForum> {

	public EclipseForumsManager() {
		this.postList = new ArrayList<>();

	}

	private static String host = "https://api.eclipse.org/";
	private final static String PAGE_SIZE = "100";
	private List<EclipseForumsPost> postList;

	// IMPLEMENTED METHODS START
	@Override
	public boolean appliesTo(CommunicationChannel eclipseForum) {
		return eclipseForum instanceof EclipseForum;
	}

	@Override
	public Date getFirstDate(DB db, EclipseForum eclipseForum) throws Exception {

		System.out.println("Getting First Date");
		
		OkHttpClient client = new OkHttpClient();

		HttpUrl.Builder urlBuilder = HttpUrl.parse(host).newBuilder();
		urlBuilder.addEncodedPathSegment("forums");
		urlBuilder.addEncodedPathSegment("post");
		urlBuilder.addEncodedQueryParameter("topic_id", eclipseForum.getTopic_id());
		urlBuilder.addEncodedQueryParameter("order_by", "DESC");
		urlBuilder.addEncodedQueryParameter("pagesize", PAGE_SIZE);

		String requestUrl = urlBuilder.build().toString();

		Request request = new Request.Builder().url(requestUrl).build();

		Response response = client.newCall(request).execute();

		final JsonNode arrNode = new ObjectMapper().readTree(response.body().string()).get("result");

		response.close();

		if (arrNode.isArray()) {
			for (JsonNode objNode : arrNode) {

				EclipseForumsPost post = new EclipseForumsPost();
				
				post.setCommunicationChannel(eclipseForum);
				post.setPostId(EclipseForumUtils.fixString(objNode.findValue("id").toString()));
				post.setSubject(EclipseForumUtils.fixString(objNode.findValue("subject").toString()));
				post.setForumId(EclipseForumUtils.fixString(objNode.findValue("topic_id").toString()));
				post.setUser(EclipseForumUtils.fixString(objNode.findValue("poster_id").toString()));
				post.setDate(EclipseForumUtils.convertStringToDate(objNode.findValue("created_date").toString()).toJavaDate());
				post.setText(EclipseForumUtils.fixString(objNode.findValue("body").toString()));
				post.setHtmlUrl(EclipseForumUtils.fixString(objNode.findValue("html_url").toString()));
				post.setTopicUrl(EclipseForumUtils.fixString(objNode.findValue("topic_url").toString()));
				post.setForumUrl(EclipseForumUtils.fixString(objNode.findValue("forum_url").toString()));
				post.setUser(EclipseForumUtils.fixString(objNode.findValue("user_url").toString()));

				postList.add(post);
			}
		}
		
		Date firstPostDate = new Date(postList.get(postList.size() - 1).getDate());
		
		
		//firstPostDate = Date d = new Date().parseDate(x);
				
		// TODO remove print out and pause
		System.err.println("FORUM SUBJECT   : " + postList.get(0).getSubject());
		System.err.println("FIRST DATE      : " + firstPostDate);
		System.err.println("NUMBER OF POSTS : " + postList.size());
		TimeUnit.MILLISECONDS.sleep(5);

		return firstPostDate;
	}

	@Override
	public CommunicationChannelDelta getDelta(DB db, EclipseForum forum, Date date) {

		System.err.println("get delta is starting");
		
		CommunicationChannelDelta delta = new CommunicationChannelDelta();
		delta.setCommunicationChannel(forum);


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
				CommuincationChannelForumPost post = new CommuincationChannelForumPost();
				
				int i = postList.size() - 1;
				flag = false;
				if (DateUtils.isSameDay(postList.get(i).getDate(), date.toJavaDate())) {
					flag = true;
					post.setCommunicationChannel(forum);
					

					post.setPostId(postList.get(i).getPostId());
					post.setDate(postList.get(i).getDate());
					post.setForumId((postList.get(i).getForumId()));
					post.setSubject(postList.get(i).getSubject());
					post.setText(postList.get(i).getText());
					post.setUser(postList.get(i).getUser());
		
					delta.getPosts().add(post);
					
					System.err.println(date.toString() + ": post " + post.getPostId() + " added");
					postList.remove(i);

				} else {

					return delta;

				}

			} while (flag = true);

		} catch (ArrayIndexOutOfBoundsException e) {

			System.err.println("delta is RETURNING");
			return delta;
		}

		// This should never be reached.
		return null;
	}

	@Override
	public String getContents(DB db, EclipseForum communicationChannel, CommunicationChannelArticle article)
			throws Exception {
		// TODO Auto-generated method stub
		// NOT USED
		return null;
	}

	// IMPLEMENTED METHODS END

	// TODO Remove once complete
	public static void main(String[] args) throws Exception {

		EclipseForum ef = new EclipseForum();

		// EXAMPLE FORUMS
		ef.setTopic_id("1092788"); // Can't run java program in Eclipse - javaw error
		// ef.setTopic_id("799069");// Eclipse Won't Start

		EclipseForumsManager efc = new EclipseForumsManager();

		Date today = new Date();
		Date date = new Date(efc.getFirstDate(null, ef).toString());
		
		do {
			System.out.println("\nDate : " + date + "----------------------------------");
			CommunicationChannelDelta delta = new CommunicationChannelDelta();
			delta = 	efc.getDelta(null, ef, date);
			System.out.println("\t" + delta.getPosts().size()); 
			date = new Date(date.addDays(1).toString());
		} while (date.compareTo(today) < 1);

	}

}