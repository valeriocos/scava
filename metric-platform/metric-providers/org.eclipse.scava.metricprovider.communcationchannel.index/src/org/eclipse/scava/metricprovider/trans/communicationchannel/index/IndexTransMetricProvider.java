package org.eclipse.scava.metricprovider.trans.communicationchannel.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.index.indexer.Indexer;
import org.eclipse.scava.metricprovider.trans.communicationchannel.index.document.ArticleDocument;
import org.eclipse.scava.metricprovider.trans.communicationchannel.index.document.ForumPostDocument;
import org.eclipse.scava.metricprovider.trans.communicationchannel.index.document.SourceForgeArticleDocument;
import org.eclipse.scava.metricprovider.trans.communicationchannel.index.model.IndexTransMetric;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.communicationchannel.sourceforge.api.SourceForgeArticle;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommuincationChannelForumPost;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum;
import org.eclipse.scava.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.scava.repository.model.sourceforge.Discussion;

import com.mongodb.DB;

/**
 * This class is responsible for preparing data contained within the communication channel deltas in a format that is accepted by the IndexerTool 
 * 
 * TODO - Enable NL Fields once models/classifiers are complete
 * TODO - Add Mappings
 * 
 * @author Dan Campbell
 */
public class IndexTransMetricProvider implements ITransientMetricProvider<IndexTransMetric> {

	protected PlatformCommunicationChannelManager communicationChannelManager;
	protected List<IMetricProvider> uses;
	protected MetricProviderContext context;

	@Override
	public String getIdentifier() {
		return IndexTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public String getShortIdentifier() {
		return "CcIndexer";
	}

	@Override
	public String getFriendlyName() {
		return "CommunicationChannelIndexer";
	}

	@Override
	public String getSummaryInformation() {

		return "This metric is responsible for preparing communication channel documents for the Indexer tool";
	}

	@Override
	public boolean appliesTo(Project project) {
		for (CommunicationChannel communicationChannel : project.getCommunicationChannels()) {
			if (communicationChannel instanceof NntpNewsGroup)
				return true;
			if (communicationChannel instanceof Discussion)
				return true;
			if (communicationChannel instanceof EclipseForum)
				return true;
		}
		return false;
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {

		this.uses = uses;
	}

	@Override
	//TODO - add metric dependencies 
	public List<String> getIdentifiersOfUses() {
		List<String> requiresList = new ArrayList<String>();
		// Here I add all of the TransMetricProvider classes the indexer relies on
		//requiresList.add(PlainTextProcessingTransMetricProvider.class.getCanonicalName());
		//requiresList.add(DetectingCodeTransMetricProvider.class.getCanonicalName());
		//requiresList.add(SeverityClassificationTransMetricProvider.class.getCanonicalName());
		// list.add( .class.getCanonicalName());
		return requiresList;
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.communicationChannelManager = context.getPlatformCommunicationChannelManager();
		this.context = context;
	}

	@Override
	public IndexTransMetric adapt(DB db) {
		return new IndexTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, IndexTransMetric db) {
		
		CommunicationChannelProjectDelta channelProjectDelta = projectDelta.getCommunicationChannelDelta();
		for(CommunicationChannelDelta communicationChannelDelta : channelProjectDelta.getCommunicationChannelSystemDeltas()) {
			CommunicationChannel communicationChannel = communicationChannelDelta.getCommunicationChannel();
			//NNTP
			if (communicationChannel instanceof NntpNewsGroup) {
				prepareNNTP(project, communicationChannel, communicationChannelDelta, projectDelta);
			}
			//Discussions
			if(communicationChannel instanceof Discussion) {
				//This includes Source Forge and Zendesk
				prepareDiscussion(project, communicationChannel, communicationChannelDelta, projectDelta);
			}
			//Eclipse Fourms
			if(communicationChannel instanceof EclipseForum) {
				prepareEclipseFourm(project, communicationChannel, communicationChannelDelta, projectDelta);
				}	
			}
		}

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

		// METHODS RELATING TO THE BUG TRACKING SOURCES

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------
	
	/**
	 * prepares NNTP objects for indexing
	 * 
	 * @param project
	 * @param communicationChannelSystemDelta
	 * @param projectDelta
	 */
	private void prepareNNTP(Project project, CommunicationChannel communicationChannel, CommunicationChannelDelta communicationChannelDelta, ProjectDelta projectDelta) {
		
		for (CommunicationChannelArticle article: communicationChannelDelta.getArticles()) {
			
			String communicationChannelType = article.getCommunicationChannel().getNewsGroupName();
			String documentType = "article";
			String indexName = generateIndexName(communicationChannelType, documentType);
			String uid = generateUniqueDocumentId(projectDelta, article.getArticleId(), communicationChannelType);
			String mapping = loadMapping(indexName);
			
			ArticleDocument articleDocument = articleObjMap(article, project.getName(), uid);			
			Indexer.performIndexing(indexName, mapping, communicationChannelType, documentType, uid, articleDocument);
		}
	}

	/**
	 * prepares Discussion objects for indexing
	 * 
	 * @param project
	 * @param communicationChannelSystemDelta
	 * @param projectDelta
	 */
	private void prepareDiscussion(Project project, CommunicationChannel communicationChannel, CommunicationChannelDelta communicationChannelDelta,
			ProjectDelta projectDelta) {
	
		
		for (CommunicationChannelArticle article : communicationChannelDelta.getArticles()) {
			String documentType = "discussion";
			try {
				/* This handles SourceForge. If the article cannot be casted, an exception is
				 * raised and it is handled as a regular Communication Channel Article. */

				SourceForgeArticle sourceForgeArticle = (SourceForgeArticle) article;
				String communicationChannelType = sourceForgeArticle.getCommunicationChannel().getNewsGroupName();
				String indexName = generateIndexName(communicationChannelType, documentType);
				String uid = generateUniqueDocumentId(projectDelta, article.getArticleId(), communicationChannelType);
				String mapping = loadMapping(indexName);
				SourceForgeArticleDocument articleDocument = sourceForgeArticleObjMap(sourceForgeArticle, project.getName(), uid);

				Indexer.performIndexing(indexName, mapping, communicationChannelType, documentType, uid,
						articleDocument);

			} catch (ClassCastException cce) {

				// ZENDESK
				String communicationChannelType = article.getCommunicationChannel().getNewsGroupName();
				String indexName = generateIndexName(communicationChannelType, documentType);
				String uid = generateUniqueDocumentId(projectDelta, article.getArticleId(), communicationChannelType);
				String mapping = loadMapping(indexName);
				ArticleDocument articleDocument = articleObjMap(article, project.getName(), uid);

				Indexer.performIndexing(indexName, mapping, communicationChannelType, documentType, uid,
						articleDocument);
			}
		}
	}
	
	/**
	 * prepares Eclipse Fourms objects for indexing
	 * 
	 * @param project
	 * @param communicationChannelSystemDelta
	 * @param projectDelta
	 */
	private void prepareEclipseFourm(Project project, CommunicationChannel communicationChannel, CommunicationChannelDelta communicationChannelDelta, ProjectDelta projectDelta) {
		
		for (CommuincationChannelForumPost post : communicationChannelDelta.getPosts()) {
			String communicationChannelType = "eclipse_forum";
			String documentType = "post";
			String indexName = generateIndexName(communicationChannelType, documentType);
			String uid = generateUniqueDocumentId(projectDelta, post.getPostId(), communicationChannelType);
			String mapping = loadMapping(indexName);
			
			ForumPostDocument forumPostDocument = articleObjMap(post, project.getName(), uid);			
			Indexer.performIndexing(indexName, mapping, communicationChannelType, documentType, uid, forumPostDocument);	
		}
	}

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

		// HELPER METHODS

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------
	
	/**
	 * This method returns a unique Identifier based upon the SourceType, Project,
	 * Document Type and source ID;
	 * 
	 * @return String uid - a uniquely identifiable string.
	 */
	private String generateUniqueDocumentId(ProjectDelta projectDelta, String id, String communicationChannelType) {

		String projectName = projectDelta.getProject().getName();
		String uid = communicationChannelType + " " + projectName + " " + id;
		return uid;
	}
	
	/**
	 * Generates a index name based upon the bug tracker type and document type
	 * 
	 * @param communicationChannelType
	 * @param documentType
	 * @return String
	 */
	private String generateIndexName(String communicationChannelType, String documentType) {
		
		String indexName = communicationChannelType +"."+ documentType;
		
		return indexName;
	}

	/**
	 * Loads the mapping for a particular index from the 'mappings'directory
	 * 
	 * @param mapping
	 * @return String
	 */
	private String loadMapping(String indexName) {
		
		String indexmapping = "";
		File file = null;
		try {
			file = new File(locateMappings(indexName));
		} catch (IllegalArgumentException | IOException e1) {
			e1.printStackTrace();
		} 
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line; 
			while ((line = br.readLine()) != null) {
				indexmapping = indexmapping + line;
			  } 
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return indexmapping;
		
	}
	
	/**
	 * Locates the mapping file within the 'mappings' directory and returns a file path
	 * 
	 * @return String 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private String locateMappings(String indexName) throws IllegalArgumentException, IOException {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("bin/"))
			path = path.substring(0, path.lastIndexOf("bin/"));
		File file = new File(path + "mappings/"+ indexName + ".json");
		checkPropertiesFilePath(file.toPath());

		return file.getPath();
	}
	/**
	 * Checks if a file exists
	 * 
	 * @param path
	 */
	private void checkPropertiesFilePath(Path path) {
		
		if (!Files.exists(path)) {
			System.err.println("The file " + path + " has not been found");
		}
	}
	
	/**
	 * Maps the necessary information from a 'CommunicationChannelArticle' object to a 'ArticleDocument'
	 * 
	 * @param article
	 * @param projectName
	 * @param uid
	 * @return GitHubIssueDocument
	 */
	private ArticleDocument articleObjMap(CommunicationChannelArticle article, String projectName, String uid) {
		
		ArticleDocument articleDocument = new ArticleDocument();
		articleDocument.setArticle_id(article.getArticleId());
		articleDocument.setArticleNumber(article.getArticleNumber());
		articleDocument.setBody(article.getText());
		articleDocument.setCreated_at(article.getDate());
		articleDocument.setMessage_thread_id(article.getMessageThreadId());
		articleDocument.setProject(projectName);
		articleDocument.setSubject(article.getSubject());
		articleDocument.setUid(uid);
		articleDocument.setUpdated_at(null);
		articleDocument.setUser(article.getUser());
	
		//NLP components
//		articleDocument.setContains_code(contains_code);
//		articleDocument.setEmotion(emotion);
//		articleDocument.setPlain_text(plain_text);
//		articleDocument.setRequest(request);
//		articleDocument.setSentiment(sentiment);
//		articleDocument.setSeverity(severity);
		
		return  articleDocument;
	}
	
	/**
	 * Maps the necessary information from a 'SourceForgeArticle' object to a 'SourceForgeArticleDocument'
	 * 
	 * @param article
	 * @param projectName
	 * @param uid
	 * @return GitHubIssueDocument
	 */
	private SourceForgeArticleDocument sourceForgeArticleObjMap(SourceForgeArticle article, String projectName, String uid) {
		
		SourceForgeArticleDocument sourceForgeArticleDocument = new SourceForgeArticleDocument();
		sourceForgeArticleDocument.setArticle_id(article.getArticleId());
		sourceForgeArticleDocument.setArticleNumber(article.getArticleNumber());
		sourceForgeArticleDocument.setBody(article.getText());
		sourceForgeArticleDocument.setCreated_at(article.getDate());
		sourceForgeArticleDocument.setForumId(article.getForumId());
		sourceForgeArticleDocument.setMessage_thread_id(article.getMessageThreadId());
		sourceForgeArticleDocument.setProject(projectName);
		sourceForgeArticleDocument.setSubject(article.getSubject());

		//NLP components
//		sourceForgeArticleDocument.setContains_code(contains_code);
//		sourceForgeArticleDocument.setEmotion(emotion);
//		sourceForgeArticleDocument.setPlain_text(plain_text);
//		sourceForgeArticleDocument.setRequest(request);
//		sourceForgeArticleDocument.setSentiment(sentiment);
//		sourceForgeArticleDocument.setSeverity(severity);
		return sourceForgeArticleDocument;
	}

	/**
	 * Maps the necessary information from a 'CommuincationChannelForumPost' object to a 'ForumPostDocument'
	 * 
	 * @param post
	 * @param projectName
	 * @param uid
	 * @return GitHubIssueDocument
	 */
	private ForumPostDocument articleObjMap(CommuincationChannelForumPost post, String projectName, String uid) {
		ForumPostDocument forumPostDocument = new ForumPostDocument();
		forumPostDocument.setBody(post.getText());
		forumPostDocument.setCreated_at(post.getDate());
		forumPostDocument.setCreator(post.getUser());
		forumPostDocument.setPostId(post.getPostId());
		forumPostDocument.setProject(projectName);
		forumPostDocument.setSubject(post.getSubject());

		//NLP Components
//		forumPostDocument.setContains_code(contains_code);
//		forumPostDocument.setEmotion(emotion);
//		forumPostDocument.setPlain_text(plain_text);
//		forumPostDocument.setRequest(request);
//		forumPostDocument.setSentiment(sentiment);
//		forumPostDocument.setSeverity(severity);
		
		return forumPostDocument;
	}
}