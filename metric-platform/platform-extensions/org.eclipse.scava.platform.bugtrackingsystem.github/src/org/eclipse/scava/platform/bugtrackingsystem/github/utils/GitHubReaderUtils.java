/*******************************************************************************
 * Copyright Disclaimer
 * 
 * Contributors:
 * Daniel Campbell - Implementation.
 *******************************************************************************/
package org.eclipse.scava.platform.bugtrackingsystem.github.utils;

import java.util.Date;

import org.eclipse.scava.platform.bugtrackingsystem.github.GitHubComment;
import org.eclipse.scava.platform.bugtrackingsystem.github.GitHubIssue;
import org.eclipse.scava.platform.bugtrackingsystem.github.GitHubPullRequest;
import org.eclipse.scava.workflow.restmule.generated.client.github.model.Issues;
import org.eclipse.scava.workflow.restmule.generated.client.github.model.IssuesComments;
import org.eclipse.scava.workflow.restmule.generated.client.github.model.PullRequest;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class GitHubReaderUtils {
/*
 * This Class provides numerous common utilities for the GitHub Reader 
 */
	
	/**
	 * Convert Issues object to a GitHubIssue object that is compatible with the BugTrackingSystem
	 * 
	 * @param issue
	 * @return gitHubIssue
	 * 
	 **/
	@SuppressWarnings("unused")
	public static GitHubIssue convertToGitHubIssue(Issues issue)  {

		GitHubIssue gitHubIssue = new GitHubIssue();
		gitHubIssue.setBugId(issue.getNumber().toString());
		gitHubIssue.setTitle(issue);
		gitHubIssue.setClosedTime(issue);
		gitHubIssue.setClosedTime(issue);
		gitHubIssue.setUpdatedTime(issue);
		gitHubIssue.setNumComments(issue);
		gitHubIssue.addLabel(issue);
		gitHubIssue.setMilestone(issue);
		gitHubIssue.setBody(issue);
		gitHubIssue.setAssignee(issue);
		gitHubIssue.setHtmlUrl(issue);
		gitHubIssue.setUrl(issue);

		return gitHubIssue;
	}
	
	/**
	 * Convert 'IssueComments' object to a GitHubComment object that is compatible with the BugTrackingSystem
	 * 
	 * @param issue
	 * @return gitHubComment
	 * 
	 **/
	@SuppressWarnings("unused")
	public static GitHubComment convertToGitHubComment(IssuesComments comment){
		GitHubComment gitHubComment = new GitHubComment();
		
		
		
		return gitHubComment;
	}
	
	/**
	 * Convert 'Pulls' (PullRequest) object to a GitHubPullRequest object that is compatible with the BugTrackingSystem
	 * 
	 * @param issue
	 * @return gitHubPullRequest
	 * 
	 **/
	@SuppressWarnings("unused")
	public static  GitHubPullRequest convertToGitHubPullRequest(PullRequest pullRequest){
		//TODO will also need to handle pullrequest head
		GitHubPullRequest gitHubPullRequest = new GitHubPullRequest();
	
		
		

		return gitHubPullRequest;
	}
	
	/**
	 * Converts ISO 8601 Timestamp from String to Java.util.Date
	 * 
	 * @param isoDate ISO 8601 in string format. For example '2014-11-07T22:01:45Z' (this has the time zone omitted hence 'Z')
	 * @return date Java.util.Date
	 * @throws Exception
	 */
	public static Date convertStringToDate(String isoDate){
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
		DateTime date = parser.parseDateTime(isoDate);
		return date.toDate();
	}

}
