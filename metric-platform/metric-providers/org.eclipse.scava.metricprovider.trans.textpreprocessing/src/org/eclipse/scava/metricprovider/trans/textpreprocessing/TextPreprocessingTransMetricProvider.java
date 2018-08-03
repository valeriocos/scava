package org.eclipse.scava.metricprovider.trans.textpreprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.scava.metricprovider.trans.textpreprocessing.model.BugTrackerCommentsTextPreprocessing;
import org.eclipse.scava.metricprovider.trans.textpreprocessing.model.NewsgroupArticlesTextPreprocessing;
import org.eclipse.scava.metricprovider.trans.textpreprocessing.model.TextpreprocessingTransMetric;
import org.eclipse.scava.nlp.htmlparser.HtmlParser;
import org.eclipse.scava.nlp.preprocessor.markdown.github.MarkdownParserGitHub;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.BugTrackingSystem;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.scava.repository.model.sourceforge.Discussion;

import com.mongodb.DB;

public class TextPreprocessingTransMetricProvider implements ITransientMetricProvider<TextpreprocessingTransMetric> {

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected PlatformCommunicationChannelManager communicationChannelManager;
	
	@Override
	public String getIdentifier() {
		return TextPreprocessingTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public String getShortIdentifier() {
		return "textpreprocessing";
	}

	@Override
	public String getFriendlyName() {
		return "Text Preprocessing";
	}

	@Override
	public String getSummaryInformation() {
		return "This metric preprocess each bug comment or newsgroup article into a split " +
				"plain text format.";
	}

	@Override
	public boolean appliesTo(Project project) {
		for (CommunicationChannel communicationChannel: project.getCommunicationChannels()) {
			if (communicationChannel instanceof NntpNewsGroup) return true;
			if (communicationChannel instanceof Discussion) return true;
		}
		return !project.getBugTrackingSystems().isEmpty();
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {
		// DO NOTHING -- we don't use anything
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Collections.emptyList();
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.communicationChannelManager = context.getPlatformCommunicationChannelManager();
	}

	@Override
	public TextpreprocessingTransMetric adapt(DB db) {
		return new TextpreprocessingTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, TextpreprocessingTransMetric db) {

		clearDB(db);
		System.err.println("Started " + getIdentifier());
		
		BugTrackingSystemProjectDelta btspDelta = projectDelta.getBugTrackingSystemDelta();
		for (BugTrackingSystemDelta bugTrackingSystemDelta : btspDelta.getBugTrackingSystemDeltas()) {
			BugTrackingSystem bugTracker = bugTrackingSystemDelta.getBugTrackingSystem();
			for (BugTrackingSystemComment comment: bugTrackingSystemDelta.getComments()) {
				BugTrackerCommentsTextPreprocessing commentsData = findBugTrackerComment(db, bugTracker, comment);
				if (commentsData == null) {
					commentsData = new BugTrackerCommentsTextPreprocessing();
					commentsData.setBugTrackerId(bugTracker.getOSSMeterId());
					commentsData.setBugId(comment.getBugId());
					commentsData.setCommentId(comment.getCommentId());
					db.getBugTrackerComments().add(commentsData);
				}
				List<String> plainText = new ArrayList<String>();
				switch(bugTracker.getBugTrackerType())
				{
					case "github": plainText=processGitHub(comment.getText()); break;
					case "bugzilla": plainText=processBugzilla(comment.getText()); break;
				}
				commentsData.setPlainText(plainText);
			}
			db.sync();
		}
		
		CommunicationChannelProjectDelta ccpDelta = projectDelta.getCommunicationChannelDelta();
		for ( CommunicationChannelDelta communicationChannelDelta: ccpDelta.getCommunicationChannelSystemDeltas()) {
			CommunicationChannel communicationChannel = communicationChannelDelta.getCommunicationChannel();
			String communicationChannelName;
			if (!(communicationChannel instanceof NntpNewsGroup))
				communicationChannelName = communicationChannel.getUrl();
			else {
				NntpNewsGroup newsgroup = (NntpNewsGroup) communicationChannel;
				communicationChannelName = newsgroup.getNewsGroupName();
			}
			for (CommunicationChannelArticle article: communicationChannelDelta.getArticles()) {
				NewsgroupArticlesTextPreprocessing newsgroupArticlesData = 
						findNewsgroupArticle(db, communicationChannelName, article);
				if (newsgroupArticlesData == null) {
					newsgroupArticlesData = new NewsgroupArticlesTextPreprocessing();
					newsgroupArticlesData.setNewsGroupName(communicationChannelName);
					newsgroupArticlesData.setArticleNumber(article.getArticleNumber());
					db.getNewsgroupArticles().add(newsgroupArticlesData);
				}
				List<String> plainText = processPlainText(article.getText());
				newsgroupArticlesData.setPlainText(plainText);
			}
			db.sync();
		}
		
	}
	
	private List<String> processGitHub(String text)
	{
		text = MarkdownParserGitHub.parse(text);
		return HtmlParser.parse(text);
	}
	
	@Deprecated
	//Temporal solution while we get a new version of Bugzilla?
	private List<String> processBugzilla(String text)
	{
		List<String> textLines = Arrays.asList(text.split("\\h\\h+"));
		return textLines;
	}
	
	private List<String> processPlainText(String text)
	{
		//What about \r instead of \n?
		List<String> textLines = Arrays.asList(text.split("\n"));
		return textLines;
	}
	
	private void clearDB(TextpreprocessingTransMetric db) {
		db.getBugTrackerComments().getDbCollection().drop();
	}

	private BugTrackerCommentsTextPreprocessing findBugTrackerComment(TextpreprocessingTransMetric db, 
								BugTrackingSystem bugTracker, BugTrackingSystemComment comment) {
		BugTrackerCommentsTextPreprocessing bugTrackerCommentsData = null;
		Iterable<BugTrackerCommentsTextPreprocessing> bugTrackerCommentsDataIt = 
				db.getBugTrackerComments().
						find(BugTrackerCommentsTextPreprocessing.BUGTRACKERID.eq(bugTracker.getOSSMeterId()), 
								BugTrackerCommentsTextPreprocessing.BUGID.eq(comment.getBugId()),
								BugTrackerCommentsTextPreprocessing.COMMENTID.eq(comment.getCommentId()));
		for (BugTrackerCommentsTextPreprocessing bcd:  bugTrackerCommentsDataIt) {
			bugTrackerCommentsData = bcd;
		}
		return bugTrackerCommentsData;
	}
	

	private NewsgroupArticlesTextPreprocessing findNewsgroupArticle(TextpreprocessingTransMetric db, 
							String communicationChannelName, CommunicationChannelArticle article) {
		NewsgroupArticlesTextPreprocessing newsgroupArticlesData = null;
		Iterable<NewsgroupArticlesTextPreprocessing> newsgroupArticlesDataIt = 
				db.getNewsgroupArticles().
						find(NewsgroupArticlesTextPreprocessing.NEWSGROUPNAME.eq(communicationChannelName), 
								NewsgroupArticlesTextPreprocessing.ARTICLENUMBER.eq(article.getArticleNumber()));
		for (NewsgroupArticlesTextPreprocessing nad:  newsgroupArticlesDataIt) {
			newsgroupArticlesData = nad;
		}
		return newsgroupArticlesData;
	}
}
