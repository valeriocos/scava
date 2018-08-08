/*******************************************************************************
 * Copyright (c) 2018 Edge Hill University.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Daniel Campbell - Implementation.
 * 	  Luis Adri�n Cabrera Diego - Implementation.
 *******************************************************************************/
package org.eclipse.scava.platform.bugtrackingsystem.github;

//Java
import java.util.ArrayList;
import java.util.List;
//Misc
import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.scava.platform.bugtrackingsystem.github.utils.GitHubReaderUtils;
import org.eclipse.scava.platform.bugtrackingsystem.github.utils.GitHubSessionUtil;
import org.eclipse.scava.repository.model.github.GitHubBugTracker;
import org.eclipse.scava.workflow.restmule.core.data.IData;
import org.eclipse.scava.workflow.restmule.core.data.IDataSet;
import org.eclipse.scava.workflow.restmule.generated.client.github.api.IGitHubApi;
import org.eclipse.scava.workflow.restmule.generated.client.github.model.Issues;
import org.eclipse.scava.workflow.restmule.generated.client.github.model.IssuesComments;
import org.eclipse.scava.workflow.restmule.generated.client.github.model.PullRequest;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
//OSSMETER
import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemBug;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.IBugTrackingSystemManager;
//DB Model packages
import org.eclipse.scava.repository.model.BugTrackingSystem;
//Mongo DB package
import com.mongodb.DB;

/* ----------------------------------------------------------------------------------
* GitHubManager Logic Begins  
* ----------------------------------------------------------------------------------*/

public class GitHubManager implements IBugTrackingSystemManager<GitHubBugTracker> {

	private List<Issues> repoIssuesList = new ArrayList<>();
	private List<Integer> pullRequestIdList = new ArrayList<>(); // this list is necessary for addressing a limitation with the get all pull requests function
	private List<GitHubComment> repoCommentsList = new ArrayList<>();
	private List<PullRequest> repoPullRequestsList = new ArrayList<>();
	private List<Integer> issueIds = new ArrayList<>();
	private static GitHubSessionUtil gitHubSession = null;

	@Override
	public boolean appliesTo(BugTrackingSystem bugTrackingSystem) {
		return bugTrackingSystem instanceof GitHubBugTracker;
	}
	/*
	 * DELTAS
	 * */
	@Override
	public BugTrackingSystemDelta getDelta(DB db, GitHubBugTracker ghbt, Date date) throws Exception {

		GitHubBugTrackingSystemDelta delta = new GitHubBugTrackingSystemDelta();
		delta.setBugTrackingSystem(ghbt);

		getIssuesOnDay(ghbt, date, delta);
		getPullRequestsOnDay(ghbt, date, delta);
		getCommentsOnDay(ghbt, date, delta);
		return delta;
	}

	@Override
	public Date getFirstDate(DB db, GitHubBugTracker ghbt) throws Exception {

		/*
		 * Since this is the first call the bug tracking system makes, it is
		 * also a suitable place for also getting all issues, comments and pull
		 * requests to reduce the number of calls made to the github server
		 */

		Date earliestDate = getEarliestIssueDate(ghbt);
		getAllData(ghbt, earliestDate);

		return earliestDate;
	}

	/* ----------------------------------------------------------------------------------
	* START get<github entity>OnDay Methods
	* ----------------------------------------------------------------------------------
	*
	* The following methods utilise the lists populated by the 'Get all data
	* methods'
	*
	*----------------------------------------------------------------------------------*/

	private void getCommentsOnDay(GitHubBugTracker ghbt, Date date, GitHubBugTrackingSystemDelta delta)
			throws Exception {

		// converts platform date to java date for comparison.
		java.util.Date today = date.toJavaDate();

		for (GitHubComment comment : repoCommentsList) {

			Date commentDate = new Date(comment.getCreationTime());
			
			if (DateUtils.isSameDay(commentDate.toJavaDate(), today)) {
			
				System.out.println("\t[Comment      ] " + comment.getCreationTime() + "\t" + comment.getText());
				
				delta.getComments().add(comment);
				
			}

		}
	}

	private void getIssuesOnDay(GitHubBugTracker ghbt, Date date, GitHubBugTrackingSystemDelta delta) throws Exception {

		// converts platform date to java date for comparison.
		java.util.Date today = date.toJavaDate();

		for (Issues issue : repoIssuesList) {

			// Simple dates that only provide YYYYMMDD for easy comparison
			Date createdSimple = new Date(issue.getCreatedAt().replace("-", "").substring(0, 8).toString());
			Date updatedSimple = new Date(issue.getUpdatedAt().replace("-", "").substring(0, 8).toString());

			try {
				if (issue.getPullRequest().equals(null)) {

				}

			} catch (Exception e) {
				GitHubIssue gitHubIssue = new GitHubIssue();
				
				// if the issue was created on the day in question add 
				if (DateUtils.isSameDay(createdSimple.toJavaDate(), today)) {
				
					gitHubIssue = GitHubReaderUtils.convertToGitHubIssue(issue, ghbt, today);
	
					System.out.println("\t[Issue Created] " + issue.getCreatedAt() + "\t" + issue.getTitle());
					
					delta.getNewBugs().add(gitHubIssue);
					
					 
				}

				// if issue was updated on this day and was modified after the
				// creation time (on the same day) add to updated bugs list
				if (DateUtils.isSameDay(updatedSimple.toJavaDate(), today)) {

					// Dates that also include time-stamp for exact comparison
					DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
					DateTime isoCreated = parser.parseDateTime(issue.getCreatedAt());
					DateTime isoUpdated = parser.parseDateTime(issue.getUpdatedAt());

					// this also catches issues that were updated on the same
					// day but after the time they were created
					if (isoUpdated.isAfter(isoCreated) == true) {
						
						gitHubIssue = GitHubReaderUtils.convertToGitHubIssue(issue, ghbt, today);
						
						System.out.println("\t[Issue Updated] " + issue.getUpdatedAt() + "\t" + issue.getTitle());
						
						delta.getUpdatedBugs().add(gitHubIssue);

					}

				}
			}

		}
		
	}

	private void getPullRequestsOnDay(GitHubBugTracker ghbt, Date date, GitHubBugTrackingSystemDelta delta)
			throws Exception {

		// converts platform date to java date for comparison.
		java.util.Date today = date.toJavaDate();

		for (PullRequest pullRequest : repoPullRequestsList) {

			Date pullRequestDate = new Date(pullRequest.getCreatedAt().replace("-", "").substring(0, 8).toString());

			if (DateUtils.isSameDay(pullRequestDate.toJavaDate(), today)) {
				
				GitHubPullRequest gitHubPullRequest = new GitHubPullRequest();
				
				gitHubPullRequest = GitHubReaderUtils.convertToGitHubPullRequest(pullRequest, ghbt, today);
				System.out.println("\t[Pull Request ] " + pullRequest.getCreatedAt() + "\t" + pullRequest.getTitle());
				delta.getPullRequests().add(gitHubPullRequest);
			}
			
		}
	}

	/* ----------------------------------------------------------------------------------
	 *	START Get ALL Data Methods
	 * ----------------------------------------------------------------------------------
	 * Beginning from the earliest data, the following methods retrieve all of
	 * the repository's issues, comments and pull requests to reduce the number
	 * of repetitive calls to the GitHub servers.
	 * ----------------------------------------------------------------------------------*/

	private void getAllData(GitHubBugTracker ghbt, Date earliestDate) throws Exception {

		this.getAllIssues(ghbt, earliestDate);
		this.getAllIssueComments(ghbt);
		this.getAllPullRequests(ghbt);

	}

	public Date getEarliestIssueDate(GitHubBugTracker ghbt) throws Exception {

		IGitHubApi gitHubApi = getSession(ghbt);
		IDataSet<Issues> repoIssues = gitHubApi.getReposIssues(ghbt.getOwner(), ghbt.getRepository(), "all", "all", "",
				"created", "asc", null);
		Issues firstIssue = repoIssues.observe().blockingFirst();
		Date earliestDate = new Date(firstIssue.getCreatedAt().replace("-", "").substring(0, 8));
		return earliestDate;

	}

	/**
	 * Gets all issues from the repository from the earliest date
	 * 
	 * @param ghbt
	 * @param earliestDate
	 * @throws Exception
	 */
	private void getAllIssues(GitHubBugTracker ghbt, Date earliestDate) throws Exception {

		IGitHubApi gitHubApi = getSession(ghbt);
		IDataSet<Issues> repoIssues = gitHubApi.getReposIssues(ghbt.getOwner(), ghbt.getRepository(), "all", "all", "",
				"created", "asc", earliestDate.toString());
		repoIssues.observe()
				.doOnNext(issue -> {
					this.issueIds.add(issue.getNumber());
					this.addIssueToList(issue);
					this.checkForPullRequest(issue);
				})
				.doOnError(e -> System.out.println(e))
				.blockingSubscribe();
	}

	/**
	 * Gets all comments from issues that have them
	 * 
	 * @param ghbt
	 * @throws Exception
	 */
	private void getAllIssueComments(GitHubBugTracker ghbt) throws Exception{
	
		for (Integer id : this.issueIds) {
				
				IGitHubApi gitHubApi = getSession(ghbt);
				IDataSet<IssuesComments> repoComments = gitHubApi.getReposIssuesComments(ghbt.getOwner(), ghbt.getRepository(), id);
				repoComments.observe()
						.doOnNext(comment -> {
							this.addCommentToList(GitHubReaderUtils.convertToGitHubComment(comment, ghbt, id));
						})
						.doOnError(e -> System.out.println(e))
						.blockingSubscribe();
				
			}
		

	}

	
	/**
	 * Gets all pullrequests from the repository
	 * 
	 * @param ghbt GitHubBugTracker
	 * @throws Exception
	 */
	private void getAllPullRequests(GitHubBugTracker ghbt) throws Exception {
		
		for (int id : pullRequestIdList){
			
			IGitHubApi gitHubApi = getSession(ghbt);
			IData<PullRequest> pullRequests = gitHubApi.getReposPullsPullRequestByNumber(ghbt.getOwner(), ghbt.getRepository(), id);
			pullRequests.observe()
			.doOnNext(pullRequest -> {
				this.addPullRequestToList(pullRequest);
			})
			.doOnError(e -> System.out.println(e))
			.blockingSubscribe();
		}

		}


	/* ----------------------------------------------------------------------------------
	* START Helper Methods
	* ----------------------------------------------------------------------------------
	* These helper methods are used in the 'Get all methods' and deltas to
	* improve code readability
	* ----------------------------------------------------------------------------------*/

	private void addIssueToList(Issues issue) {

		this.repoIssuesList.add(issue);

	}

	private void addCommentToList(GitHubComment gitHubComment) {

		this.repoCommentsList.add(gitHubComment);

	}

	private void addPullRequestToList(PullRequest pullRequest) {

		this.repoPullRequestsList.add(pullRequest);

	}

	private void checkForPullRequest(Issues issue) {
			
			try{
				if (!issue.getPullRequest().equals(null)){
					this.pullRequestIdList.add(issue.getNumber());
				}
				
			}catch(NullPointerException np){
				/* Do nothing this is needed as if we throw the null pointer
				 *  exception the stream encounters and error */
		}
	}
	
	

	/* ----------------------------------------------------------------------------------
	 * Git Hub Session
	 * ----------------------------------------------------------------------------------
	 * This uses GitHubSessionUtil to create or return an existing session
	 * ----------------------------------------------------------------------------------*/

	public static IGitHubApi getSession(GitHubBugTracker GitHubBugTracker) throws Exception {
		if(gitHubSession == null)
		{
			gitHubSession = new GitHubSessionUtil(GitHubBugTracker.getAuthenticationData());
		}
		return gitHubSession.getSession();
	}

	
	/*----------------------------------------------------------------------------------
	 *  TRASH CODE
	 *  ----------------------------------------------------------------------------------
	 *  The logic contained within these constructors maybe useless code 
	 *  Keep logic in comments until its fate has been decided 
	 *  ----------------------------------------------------------------------------------*/

	@Override
	public String getContents(DB db, GitHubBugTracker ghbt, BugTrackingSystemBug bug) throws Exception {

		return null;
	}

	@Override
	public String getContents(DB db, GitHubBugTracker ghbt, BugTrackingSystemComment comment) throws Exception {

		
		return null;
	}

	
	
	
	

	/*----------------------------------------------------------------------------------
	 * HERE BE DRAGONS!
	 * ----------------------------------------------------------------------------------
	 *  These methods are used for testing the Reader
	 * ----------------------------------------------------------------------------------*/

	@SuppressWarnings({ "unused", "static-access" })
	public static void main(String[] args) throws Exception {

		String login = "Danny2097";
		String user = "Dan Campbell";
		String owner = "Danny2097";
		String token = "6ea3a348c819e6b53cc74f18445df44d2487db3c";
		//String owner = "crossminer";
		String repo = "testing";
		//String repo = "crossminer";
		String since = "2017-01-01T00:00:00z";
		String filter = "all";
		String state = "all";
		String labels = "";
		String sort = "created";
		String direction = "asc";
		
		/* this helps simulate the bugtracking system (today = date + 1 as time
		is 00:00:00) */
		
		Date today = new Date();
		
		// sets GitHubTracker Information
		GitHubBugTracker ghbt = new GitHubBugTracker();
		//ghbt.setProject(user, login, "dccw2097", repo, owner );
		ghbt.setProject(user, login, "dccw2097", "scava", "crossminer");
		
		//creates new instances of...
		GitHubBugTrackingSystemDelta delta = new GitHubBugTrackingSystemDelta();
		GitHubManager ghm = new GitHubManager();

		//clears cache as currently there are probelms
		GitHubSessionUtil.clearGitHubCache(); // clears local github cache
		
		//Gets first Date
		Date on = ghm.getFirstDate(null, ghbt);

		// prints entity information
		ghm.printEntityInformation(on, ghm);

		Thread.sleep(10);// prevents console output bug
		
		// simulates the getDelta functionality of the bugtracking system
		ghm.printRepositoryTimeLine(ghm, on, ghbt, today);

	}
	
	/**
	 * [USED FOR TESTING / DEBUGGING] Prints the repository time line to the console from the perspective of the delta creation per day
	 * @param ghm GitHubManager
	 * @param on Date
	 * @param ghbt GitHubBugTracker
	 * @param today Date
	 * @throws Exception
	 */
	private static void printRepositoryTimeLine(GitHubManager ghm, Date on, GitHubBugTracker ghbt, Date today)
			throws Exception {

		System.out.println("[START]");
		do {
			System.err.println(on);
			ghm.getDelta(null, ghbt, on);
			on.addDays(1);
		} while (on.compareTo(today) < 1);
		System.out.println("[END]");
		
		
		System.exit(0);// terminates reader
	}

	/**
	 * [USED FOR TESTING / DEBUGGING] prints information regarding various entities 
	 * @param on Date
	 * @param ghm GitHubManager
	 */
	private static void printEntityInformation(Date on, GitHubManager ghm) {

		System.err.println("Earliest Issue Date: " + on);
		System.err.println("Repo has a total of " + ghm.repoIssuesList.size() + " issues");
		System.err.println("Repo has a total of " + ghm.repoCommentsList.size() + " comments");
		System.err.println("Repo has a total of " + ghm.repoPullRequestsList.size() + " pull requests \n");
	}
	
}