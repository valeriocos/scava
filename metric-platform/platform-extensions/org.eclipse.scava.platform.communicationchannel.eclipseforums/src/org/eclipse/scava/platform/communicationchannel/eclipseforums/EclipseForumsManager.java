package org.eclipse.scava.platform.communicationchannel.eclipseforums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;//REMOVE only used for testing
import java.sql.Timestamp;

import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.delta.communicationchannel.CommuincationChannelForumPost;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.ICommunicationChannelManager;
import org.eclipse.scava.platform.communicationchannel.eclipseforums.utils.EclipseForumUtils;
import org.eclipse.scava.platform.communicationchannel.eclipseforums.utils.EclipseForumsForum;
import org.eclipse.scava.platform.communicationchannel.eclipseforums.utils.EclipseForumsPost;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EclipseForumsManager implements ICommunicationChannelManager<EclipseForum> {

	public EclipseForumsManager() {
		this.topicList = new ArrayList<>();
		this.postList = new ArrayList<>();

	}

	private static String host = "https://api.eclipse.org/";
	private final static String PAGE_SIZE = "100";
	private List<EclipseForumsPost> postList;
	private List<String> topicList;
	private OkHttpClient client = new OkHttpClient();

	// IMPLEMENTED METHODS START
	@Override
	public boolean appliesTo(CommunicationChannel eclipseForum) {
		return eclipseForum instanceof EclipseForum;
	}

	@Override
	public Date getFirstDate(DB db, EclipseForum eclipseForum) throws Exception {

		System.out.println("Getting First Date");
		
		//MODIFY TO GET FOURM INFORMATION AND THEN GET ALL TOPICS
		//	-get topic ID first post date and last postdate store in list this is  used to created temporal filters

		HttpUrl.Builder categoryBuilder = HttpUrl.parse(host).newBuilder();
		categoryBuilder.addEncodedPathSegment("forums");
		categoryBuilder.addEncodedPathSegment("forum");
		categoryBuilder.addPathSegment(eclipseForum.getForum_id());
		
		// urlBuilder.addEncodedQueryParameter("forum_id", eclipseForum.getForum_id());
		// urlBuilder.addEncodedQueryParameter("order_by", "DESC");
		// urlBuilder.addEncodedQueryParameter("pagesize", PAGE_SIZE);

		String requestCategoryUrl = categoryBuilder.build().toString();
		Request categoryRequest = new Request.Builder().url(requestCategoryUrl).build();
		Response categoryResponse = client.newCall(categoryRequest).execute();
		JsonNode jsonNode = new ObjectMapper().readTree(categoryResponse.body().string());
		Date firstPostDate = EclipseForumUtils.convertStringToDate((jsonNode.findValue("created_date").toString()));
		categoryResponse.close();
		
		HttpUrl.Builder forumBuilder = HttpUrl.parse(host).newBuilder();
		forumBuilder.addEncodedPathSegment("forums");
		forumBuilder.addEncodedPathSegment("topic");
		forumBuilder.addQueryParameter("forum_id", eclipseForum.getForum_id());
		forumBuilder.addQueryParameter("pagesize", PAGE_SIZE);
		
		String requestForumUrl = forumBuilder.build().toString();
	
		Request forumRequest = new Request.Builder().url(requestForumUrl).build();
		Response forumResponse = client.newCall(forumRequest).execute();
		
		jsonNode = new ObjectMapper().readTree(forumResponse.body().string()).get("result");;
		forumResponse.close();
		
		if (jsonNode.isArray()) {
			for (JsonNode node : jsonNode) {		
				topicList.add(node.findValue("id").toString().replaceAll("\"", ""));
			}
		}
	
		return firstPostDate;
	}

	@Override
	public CommunicationChannelDelta getDelta(DB db, EclipseForum forum, Date date) throws Exception{

		String since = Long.toString((date.toJavaDate().getTime()) /1000l);
		String until = Long.toString((date.addDays(1).toJavaDate().getTime()-1000) /1000l);
		
		CommunicationChannelDelta delta = new CommunicationChannelDelta();
		delta.setCommunicationChannel(forum);
		
		for (String topic_id : topicList) {
			
			HttpUrl.Builder forumBuilder = HttpUrl.parse(host).newBuilder();
			forumBuilder.addEncodedPathSegment("forums");
			forumBuilder.addEncodedPathSegment("post");
			forumBuilder.addQueryParameter("topic_id", topic_id);
			forumBuilder.addQueryParameter("since", since);
			forumBuilder.addQueryParameter("until", until);
			forumBuilder.addQueryParameter("pagesize", PAGE_SIZE);
			
			String requestUrl = forumBuilder.build().toString();
			Request request = new Request.Builder().url(requestUrl).build();
			Response response = client.newCall(request).execute();
			JsonNode  jsonNode = new ObjectMapper().readTree(response.body().string());
			
			if (!jsonNode.isNull()) {
				if(jsonNode.isArray()) {
					for(JsonNode node : jsonNode) {
						
						System.out.println(node.findValue("body"));
					
					}		
				}
			}
		}
		

		// OPTIMISATION |
		// Reads the last element in the list, which are descending order
		// (newest first). If a post is added to delta, remove from list.
		// Removing from the bottom of the list prevents shifting all elements
		// to the left ( or simply...current index of element - 1).
		// Also, if a new creation date is detected return delta, this minimises
		// the number of iterations through the list.

//		try {
//			Boolean flag;
//			do {
//				CommuincationChannelForumPost post = new CommuincationChannelForumPost();
//				
//				int i = postList.size() - 1;
//				flag = false;
//				if (DateUtils.isSameDay(postList.get(i).getDate(), date.toJavaDate())) {
//					flag = true;
//					post.setCommunicationChannel(forum);
//					post.setPostId(postList.get(i).getPostId());
//					post.setDate(postList.get(i).getDate());
//					post.setForumId((postList.get(i).getForumId()));
//					post.setSubject(postList.get(i).getSubject());
//					post.setText(postList.get(i).getText());
//					post.setUser(postList.get(i).getUser());
//					delta.getPosts().add(post);
//					System.err.println(date.toString() + ": post " + post.getPostId() + " added");
//					postList.remove(i);
//
//				} else {
//					return delta;
//				}
//
//			} while (flag = true);
//
//		} catch (ArrayIndexOutOfBoundsException e) {
//			System.err.println("delta is RETURNING");
//			return delta;
//		}

		// This should never be reached.
		return delta;
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
		ef.setForum_id("305"); // Can't run java program in Eclipse - javaw error
		// ef.setTopic_id("799069");// Eclipse Won't Start

		EclipseForumsManager efc = new EclipseForumsManager();

		Date today = new Date();
		Date date = new Date(efc.getFirstDate(null, ef).toString());
		
		do {
			System.out.println("\nDate : " + date + "----------------------------------");
			CommunicationChannelDelta delta = new CommunicationChannelDelta();
			delta = efc.getDelta(null, ef, date);
			//System.out.println("\t" + delta.getPosts().size()); 
			date = new Date(date.addDays(1).toString());
		} while (date.compareTo(today) < 1);

	}

}