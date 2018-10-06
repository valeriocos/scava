package org.eclipse.scava.metricprovider.trans.index.document;

import java.io.IOException;

import org.eclipse.scava.index.indexer.ElasticSearchManager;

public class test {
	
	
	public static void main(String[] args) {
		
		GitHubIssueDocument issue = new GitHubIssueDocument();
		String x = null;
		
		issue.setUid("GitIssue_1234");
		issue.setBody("This is an example of a body");
		issue.setCreator("Dan");
		issue.setEmotion("Sadness");
		issue.setSentiment("Positive");
		issue.setSeverity("Bad");
		issue.setRequest(true);
		issue.setCreated_at(null);
		issue.setUpdated_at(null);
		issue.setContains_code(false);
		issue.setBug_id("1234");
		issue.setSummary(x);
		issue.setAssignee("Dan");
		issue.setHtml_url("www.google.com");
		issue.setUrl("www.google.com");
		issue.setLabel(null);
		issue.setNumber_of_comments(110);
		issue.setClosed_at(null);


		String i = "{\n" + 
				"  \"uid\" : \"GitIssue_1234\",\n" + 
				"  \"body\" : \"This is an exdddample of a body\",\n" + 
				"  \"creator\" : \"Dan\",\n" + 
				"  \"emotion\" : \"Sadness\",\n" + 
				"  \"sentiment\" : \"Positive\",\n" + 
				"  \"severity\" : \"Bad\",\n" + 
				"  \"created_at\" : null,\n" + 
				"  \"updated_at\" : null,\n" + 
				"  \"request\" : true,\n" + 
				"  \"contains_code\" : false,\n" + 
				"  \"bug_id\" : \"1234\",\n" + 
				"  \"summary\" : null,\n" + 
				"  \"assignee\" : \"Dan\",\n" + 
				"  \"html_url\" : \"www.google.com\",\n" + 
				"  \"url\" : \"www.google.com\",\n" + 
				"  \"label\" : null,\n" + 
				"  \"number_of_comments\" : 110,\n" + 
				"  \"closed_at\" : null\n" + 
				"}";
		
		ElasticSearchManager esm = new ElasticSearchManager();
		
		String indexSetting = "{\n" + 
				"        \"number_of_replicas\" : 2\n" + 
				"    }";
		
		String map = "{\n" + 
				"  \"properties\": {\n" + 
				"    \"bug_id\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"summary\": {\n" + 
				"      \"type\": \"text\"\n" + 
				"    },\n" + 
				"    \"created_at\": {\n" + 
				"      \"type\": \"date\"\n" + 
				"    },\n" + 
				"    \"closed_at\": {\n" + 
				"      \"type\": \"date\"\n" + 
				"    },\n" + 
				"    \"updated_at\": {\n" + 
				"      \"type\": \"date\"\n" + 
				"    },\n" + 
				"    \"number_of_comments\": {\n" + 
				"      \"type\": \"integer\"\n" + 
				"    },\n" + 
				"    \"label\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"body\": {\n" + 
				"      \"type\": \"text\"\n" + 
				"    },\n" + 
				"    \"assignee\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"html_url\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"url\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"creator\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"contains_code\": {\n" + 
				"      \"type\": \"boolean\"\n" + 
				"    },\n" + 
				"    \"severity\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"sentiment\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"emotion\": {\n" + 
				"      \"type\": \"keyword\"\n" + 
				"    },\n" + 
				"    \"request\": {\n" + 
				"      \"type\": \"boolean\"\n" + 
				"    }\n" + 
				"  }\n" + 
				"}\n";
		
		try {
			for (String index : esm.getIndices()) {System.out.println(x);}
			esm.createIndex("github.issue");
			esm.addIndexSetting("github.issue",indexSetting );
			esm.addMapping("github.issue", "issue", map);
			esm.indexDocument("github.issue", "issue", "githubissue_1234", issue);
			
			esm.closeAllClients();
		} catch (IOException e) {

			e.printStackTrace();
		}


	}

}
