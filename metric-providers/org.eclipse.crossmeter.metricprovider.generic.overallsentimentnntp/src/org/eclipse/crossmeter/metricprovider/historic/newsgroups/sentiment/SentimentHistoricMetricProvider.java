/*******************************************************************************
 * Copyright (c) 2014 CROSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yannis Korkontzelos - Implementation.
 *******************************************************************************/
package org.eclipse.crossmeter.metricprovider.historic.newsgroups.sentiment;

import java.util.Arrays;
import java.util.List;

import org.eclipse.crossmeter.metricprovider.historic.newsgroups.sentiment.model.NewsgroupsSentimentHistoricMetric;
import org.eclipse.crossmeter.metricprovider.trans.newsgroups.sentiment.SentimentTransMetricProvider;
import org.eclipse.crossmeter.metricprovider.trans.newsgroups.sentiment.model.NewsgroupsSentimentTransMetric;
import org.eclipse.crossmeter.metricprovider.trans.newsgroups.sentiment.model.ThreadStatistics;
import org.eclipse.crossmeter.platform.AbstractHistoricalMetricProvider;
import org.eclipse.crossmeter.platform.IMetricProvider;
import org.eclipse.crossmeter.platform.MetricProviderContext;
import org.eclipse.crossmeter.repository.model.CommunicationChannel;
import org.eclipse.crossmeter.repository.model.Project;
import org.eclipse.crossmeter.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.crossmeter.repository.model.sourceforge.Discussion;

import com.googlecode.pongo.runtime.Pongo;

public class SentimentHistoricMetricProvider extends AbstractHistoricalMetricProvider{

	public final static String IDENTIFIER = 
			"org.eclipse.crossmeter.metricprovider.historic.newsgroups.sentiment";

	protected MetricProviderContext context;
	
	/**
	 * List of MPs that are used by this MP. These are MPs who have specified that 
	 * they 'provide' data for this MP.
	 */
	protected List<IMetricProvider> uses;
	
	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}
	
	@Override
	public boolean appliesTo(Project project) {
		for (CommunicationChannel communicationChannel: project.getCommunicationChannels()) {
			if (communicationChannel instanceof NntpNewsGroup) return true;
			if (communicationChannel instanceof Discussion) return true;
		}
		return false;
	}

	@Override
	public Pongo measure(Project project) {
		if (uses.size()!=1) {
			System.err.println("Metric: avgnumberofrequestsreplies failed to retrieve " + 
								"the transient metric it needs!");
			System.exit(-1);
		}

		 NewsgroupsSentimentTransMetric sentimentTransMetric = 
				 ((SentimentTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));

		float overallSentiment = 0,
			  startSentiment = 0,
			  endSentiment = 0;
		for (ThreadStatistics threadStatistics: sentimentTransMetric.getThreads()) {
			overallSentiment += threadStatistics.getAverageSentiment();
			String start = threadStatistics.getStartSentiment();
			if (start.equals("Positive"))
				startSentiment+=1;
			else if (start.equals("Negative"))
				startSentiment-=1;
			String end = threadStatistics.getEndSentiment();
			if (end.equals("Positive"))
				endSentiment+=1;
			else if (end.equals("Negative"))
				endSentiment-=1;
		}
		long size = sentimentTransMetric.getThreads().size();
		if (size>0) {
			overallSentiment /= size;
			startSentiment /= size;
			endSentiment /= size;
		}
		
		NewsgroupsSentimentHistoricMetric sentiment = 
										new NewsgroupsSentimentHistoricMetric();
		sentiment.setOverallAverageSentiment(overallSentiment);
		sentiment.setOverallSentimentAtThreadBeggining(startSentiment);
		sentiment.setOverallSentimentAtThreadEnd(endSentiment);
		System.out.println("overallSentiment: " + overallSentiment + "\t" + 
				"startSentiment: " + startSentiment + "\t" + 
				"endSentiment: " + endSentiment);
		return sentiment;
	}
			
	@Override
	public void setUses(List<IMetricProvider> uses) {
		this.uses = uses;
	}
	
	@Override
	public List<String> getIdentifiersOfUses() {
		return Arrays.asList(SentimentTransMetricProvider.class.getCanonicalName());
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.context = context;
	}

	@Override
	public String getShortIdentifier() {
		return "newsgroupsentiment";
	}

	@Override
	public String getFriendlyName() {
		return "Overall Sentiment of Newsgroup Articles";
	}

	@Override
	public String getSummaryInformation() {
		return "This metric computes the overall sentiment per repository up to the processing date." +
				"The overall sentiment score ranges from -1 (negative sentiment) to +1 (positive sentiment)." +
				"In the computation, the sentiment score of each thread contributes equally, independently of its size.";
	}
}
