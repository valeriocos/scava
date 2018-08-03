//Adrián was here
package org.eclipse.scava.platform.bugtrackingsystem.github;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;

public class GitHubBugTrackingSystemDelta extends BugTrackingSystemDelta {

	private static final long serialVersionUID = 1L;

	private List<GitHubPullRequest> pullRequests = new ArrayList<GitHubPullRequest>();

	public List<GitHubPullRequest> getPullRequests() {
		return pullRequests;
	}
}
