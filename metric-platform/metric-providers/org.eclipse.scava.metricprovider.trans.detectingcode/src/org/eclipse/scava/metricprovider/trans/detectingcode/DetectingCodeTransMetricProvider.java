package org.eclipse.scava.metricprovider.trans.detectingcode;

import java.util.Arrays;
import java.util.List;

import org.eclipse.scava.metricprovider.trans.detectingcode.model.BugTrackerCommentsDetectingCode;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.NewsgroupArticlesDetectingCode;
import org.eclipse.scava.metricprovider.trans.textpreprocessing.TextPreprocessingTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.textpreprocessing.model.BugTrackerCommentsTextPreprocessing;
import org.eclipse.scava.metricprovider.trans.textpreprocessing.model.NewsgroupArticlesTextPreprocessing;
import org.eclipse.scava.metricprovider.trans.textpreprocessing.model.TextpreprocessingTransMetric;
import org.eclipse.scava.nlp.codedetector.CodeDetector;
import org.eclipse.scava.nlp.predictionmanager.Prediction;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.repository.model.BugTrackingSystem;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.scava.repository.model.sourceforge.Discussion;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;

import com.mongodb.DB;

public class DetectingCodeTransMetricProvider implements ITransientMetricProvider<DetectingCodeTransMetric> {

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected PlatformCommunicationChannelManager communicationChannelManager;
	
	protected List<IMetricProvider> uses;
	protected MetricProviderContext context;
	
	@Override
	public String getIdentifier() {
		return DetectingCodeTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public String getShortIdentifier() {
		return "codedetection";
	}

	@Override
	public String getFriendlyName() {
		return "Code Detection";
	}

	@Override
	public String getSummaryInformation() {
		return "This metric determines which parts of a bug comment or a newsgroup article are code and which natural language";
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
		this.uses=uses;
		
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Arrays.asList(TextPreprocessingTransMetricProvider.class.getCanonicalName());
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.communicationChannelManager= context.getPlatformCommunicationChannelManager();
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.context=context;
	}

	@Override
	public DetectingCodeTransMetric adapt(DB db) {
		return new DetectingCodeTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, DetectingCodeTransMetric db) {
		System.err.println("Started " + getIdentifier());
		BugTrackingSystemProjectDelta btspDelta = projectDelta.getBugTrackingSystemDelta();
		clearDB(db);
		
		TextpreprocessingTransMetric preprocessor = ((TextPreprocessingTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));
		
		//We obtain all the comments preprocessed by the Textpreprocessing Trans Metric
		Iterable<BugTrackerCommentsTextPreprocessing> commentsIt = preprocessor.getBugTrackerComments();
		
		for (BugTrackerCommentsTextPreprocessing comment : commentsIt)
		{
			BugTrackerCommentsDetectingCode commentDataInDC = findBugTrackerComment(db, comment);
			if(commentDataInDC == null)
			{
				commentDataInDC = new BugTrackerCommentsDetectingCode();
				commentDataInDC.setBugTrackerId(comment.getBugTrackerId());
				commentDataInDC.setBugId(comment.getBugId());
				commentDataInDC.setCommentId(comment.getCommentId());
				db.getBugTrackerComments().add(commentDataInDC);
			}
			applyCodeDetector(comment.getPlainText(), commentDataInDC);
			db.sync();
		}
		
		Iterable<NewsgroupArticlesTextPreprocessing> articlesIt = preprocessor.getNewsgroupArticles();
		
		for (NewsgroupArticlesTextPreprocessing article : articlesIt)
		{
			NewsgroupArticlesDetectingCode articleDataInDC = findNewsgroupArticle(db, article);
			if(articleDataInDC == null)
			{
				articleDataInDC = new NewsgroupArticlesDetectingCode();
				articleDataInDC.setNewsGroupName(article.getNewsGroupName());
				articleDataInDC.setArticleNumber(article.getArticleNumber());
				db.getNewsgroupArticles().add(articleDataInDC);
			}
			applyCodeDetector(article.getPlainText(), articleDataInDC);
			db.sync();
		}
	}
	
	private void applyCodeDetector(List<String> plainText, BugTrackerCommentsDetectingCode comment)
	{
		String[] output = applyCodeDetector(plainText);
		comment.setCode(output[0]);
		comment.setNaturalLanguage(output[1]);
	}
	
	private void applyCodeDetector(List<String> plainText, NewsgroupArticlesDetectingCode article)
	{
		String[] output = applyCodeDetector(plainText);
		article.setCode(output[0]);
		article.setNaturalLanguage(output[1]);
	}
	
	private String[] applyCodeDetector(List<String> plainText)
	{
		List<Prediction> predictions = CodeDetector.predict(plainText);
		String[] output = new String[2];
		output[0] = String.join("\n", Prediction.getTextsbyLabel(predictions, "__label__Code"));
		output[1] = String.join(" ", Prediction.getTextsbyLabel(predictions, "__label__English"));
		return output;
	}
	
	//See if in the database, we have already analyzed this comment (for CodeDetection)
	private BugTrackerCommentsDetectingCode findBugTrackerComment(DetectingCodeTransMetric db, BugTrackerCommentsTextPreprocessing comment)
	{
		BugTrackerCommentsDetectingCode btcdc = null;
		Iterable<BugTrackerCommentsDetectingCode> btcdcIt = 
				db.getBugTrackerComments().
					find(BugTrackerCommentsDetectingCode.BUGID.eq(comment.getBugId()),
							BugTrackerCommentsDetectingCode.COMMENTID.eq(comment.getCommentId()));
		for(BugTrackerCommentsDetectingCode bcd : btcdcIt)
		{
			btcdc = bcd;
		}
		return btcdc;
	}
	
	private NewsgroupArticlesDetectingCode findNewsgroupArticle(DetectingCodeTransMetric db, NewsgroupArticlesTextPreprocessing article)
	{
		NewsgroupArticlesDetectingCode nadc = null;
		Iterable<NewsgroupArticlesDetectingCode> nadcIt = 
				db.getNewsgroupArticles().
					find(NewsgroupArticlesDetectingCode.NEWSGROUPNAME.eq(article.getNewsGroupName()),
							NewsgroupArticlesDetectingCode.ARTICLENUMBER.eq(article.getArticleNumber()));
		for(NewsgroupArticlesDetectingCode nad : nadcIt)
		{
			nadc = nad;
		}
		return nadc;
	}
	
	private void clearDB(DetectingCodeTransMetric db) {
		db.getBugTrackerComments().getDbCollection().drop();
		db.getNewsgroupArticles().getDbCollection().drop();
		db.sync();
	}

}
