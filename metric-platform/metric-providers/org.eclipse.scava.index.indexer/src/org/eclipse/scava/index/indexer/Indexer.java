package org.eclipse.scava.index.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Indexer {

	private static RestHighLevelClient highLevelClient;
	private static Client adminClient;

	static {
		
		IndexerSingleton singleton = IndexerSingleton.getInstance();
		highLevelClient = singleton.getHighLevelclient();
		adminClient = singleton.getAdminClient();

	}

	/**
	 * Retrieves all non-default indices from Elasticsearch  
	 * 
	 * @return List<String>
	 */
	private static List<String> getIndices() {
		List<String> list = new ArrayList<String>();
		ImmutableOpenMap<String, IndexMetaData> indices = adminClient.admin().cluster().prepareState().get().getState()
				.getMetaData().getIndices();
		for (ObjectObjectCursor<String, IndexMetaData> x : indices) {
			if (!(x.value == null)) {
				if (!(x.value.getIndex().getName().startsWith("."))) {
					if (!(list.contains(x.value.getIndex().getName()))) {
						list.add(x.value.getIndex().getName());
					}
				}
			}
		}
		return list;
	} 

	/**
	 * Adds settings to index
	 * 
	 * @param index
	 *            - name of the index
	 * @param setting
	 *            - settings represented as a JSON String
	 * @throws IOException
	 */
	private static void addIndexSetting(String index, String setting) throws IOException {
		UpdateSettingsRequest request = new UpdateSettingsRequest();
		request.indices(index);
		request.settings(setting, XContentType.JSON);
		UpdateSettingsResponse updateSettingsResponse = highLevelClient.indices().putSettings(request,
				getWriteHeaders());
		if (updateSettingsResponse.isAcknowledged() == true) {
			System.out.println("[ELASTICSEARCH MANAGER] \tSettings have been added to " + index);
		}
	}

	/**
	 * Creates a new index
	 * 
	 * @param indexName
	 *            - name of the new index
	 */
	private static void createIndex(String indexName) {

		CreateIndexRequest request = new CreateIndexRequest(indexName);
		CreateIndexResponse createIndexResponse;
		try {
			createIndexResponse = highLevelClient.indices().create(request, getWriteHeaders());
			if (createIndexResponse.isAcknowledged() == true) {
				System.err.println("\t -Index " + indexName + " has been created");
			}

		} catch (ElasticsearchStatusException e) {
			System.err.println("\t-" + indexName + "\tIndexResponse :" + e.getLocalizedMessage());

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Adds a document map (mapping) to a specific index
	 * 
	 * @param index
	 *            - index name
	 * @param type
	 *            - doucment type
	 * @param mapping
	 *            - a JSON document represented as a string
	 */
	private static void addMappingToIndex(String indexName, String documentType, String mapping) {

		PutMappingRequest putMappingRequest = new PutMappingRequest(indexName.toLowerCase());
		putMappingRequest.source(mapping, XContentType.JSON).type(documentType.toLowerCase());

		PutMappingResponse putMappingResponse;
		try {
			putMappingResponse = highLevelClient.indices().putMapping(putMappingRequest, getWriteHeaders());

			if (putMappingResponse.isAcknowledged() == true) {
				System.out.println(
						"[ELASTICSEARCH MANAGER] \tMapping for " + documentType + " in " + indexName + " was added");
			}

		} catch (IOException e) {
			//Do Nothing
		}
	}

	/**
	 * Returns a basic HTTP write headers
	 * 
	 * 
	 * @return headers
	 */
	private static Header[] getWriteHeaders() {

		Header[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
				new BasicHeader("Role", "Write") };
		return headers;
	}

	/**
	 * Returns a basic HTTP read headers
	 * 
	 * 
	 * @return headers
	 */
	private static Header[] getReadHeaders() {

		Header[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
				new BasicHeader("Role", "Read") };
		return headers;
	}

	/**
	 * Not yet Implemented
	 * 
	 * @param index
	 * @param doc
	 * @param doc_id
	 * @return
	 */
	@SuppressWarnings("unused")
	public static GetRequest prepareGetRequest(String index, String doc, String doc_id) {

		GetRequest request = new GetRequest(index, doc, doc_id);

		return request;
	}

	/**
	 * Use this method when the dump that you are using already contains unique
	 * identifiers for each document. For example a 'customerId' or 'postID'. This
	 * will prevent duplicate documents being added to the index. Essentially, if
	 * the same ID already exists in the index, Elasticsearch will simply update the
	 * document with the 'new' changes.
	 * 
	 * @param indexName
	 *            - index name
	 * @param documentType
	 *            - document type
	 * @param uid
	 *            - unique identifier
	 * @param object
	 *            - object that represents the structure of a document for indexing
	 * @return
	 * @return IndexResponse
	 * @throws IOException
	 */
	public static void indexDocument(String indexName, String documentType, String uid, Object document)
			throws IOException {

		String source = objectToJson(document);
		IndexRequest indexRequest = new IndexRequest();
		indexRequest.index(indexName.toLowerCase()).type(documentType.toLowerCase()).id(uid).source(source, XContentType.JSON);
		System.err.println("\t -Document: " + documentType + ": "+ uid + " has been " + highLevelClient.index(indexRequest, getWriteHeaders()).getResult());

	}

	/**
	 * Method for searching the index
	 * 
	 * @param index
	 *            - index name
	 * @param type
	 *            - document type
	 * @param queryBuilder
	 *            - a query built using a query builder
	 * @return response
	 * @throws IOException
	 */
	public static SearchResponse searchIndex(String index, String[] type, QueryBuilder queryBuilder)
			throws IOException {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(queryBuilder);

		// search request
		SearchRequest searchRequest = new SearchRequest(index);
		searchRequest.source(searchSourceBuilder).types(type);
		SearchResponse searchResponse = highLevelClient.search(searchRequest, getReadHeaders());
		return searchResponse;
	}

	/**
	 * Closes the HighLevel Rest Client's connection to the Elasticsearch index
	 * 
	 * @throws IOException
	 */
	public static void closeHighLevelClient() throws IOException {

		highLevelClient.close();
		System.out.println("[ELASTICSEARCH MANAGER] \tHight Level Client - Closed");

	}

	/**
	 * Closes the Admin Client
	 */
	public static void closeAdminClient() {

		adminClient.close();
		System.out.println("[ELASTICSEARCH MANAGER] \tAdmin Client - Closed");
	}

	/**
	 * Closes all clients (HighLevelRest and AdminClients)
	 * 
	 * @throws IOException
	 */

	public static void closeAllClients() throws IOException {
		
		closeHighLevelClient();
		closeAdminClient();
	}

	/**
	 * This method maps a objects attributes and returns a string in a JSON
	 * structure.
	 * 
	 * @param object
	 * @return string
	 * @throws com.fasterxml.jackson.core.JsonProcessingException
	 */

	private static String objectToJson(Object object) throws com.fasterxml.jackson.core.JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(object);

		return jsonString;
	}

	/**
	 * This method is used to create / check the existence of an index, add mappings
	 * to an index and index or update a document related to bug tracking systems.
	 * 
	 * @param indexName
	 * @param bugTrackerType
	 * @param document
	 */
	public static void performIndexing(String indexName, String mapping, String bugTrackerType, String documentType, String uid,
			Object document) {
		
		System.err.println("[Performing Indexing] -----------------------------------------------------------");
		if (!(getIndices().contains(indexName))) {
			createIndex(indexName);
			//String mapping = getMapping(indexName);
			if(!(mapping.isEmpty())) {
			addMappingToIndex(indexName, documentType, mapping);
			}
			try {
				indexDocument(indexName, documentType, uid, document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				indexDocument(indexName, documentType, uid, document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

		// TODO THE Methods below from production Used only for testing

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

	public void printHits(SearchResponse sr, QueryBuilder query) {

		for (SearchHit hit : sr.getHits()) {
			System.out.println("[" + query.getName() + " query] Response = " + hit.getSourceAsString());
		}
	}

	/**
	 * Remove in production used for debugging
	 * 
	 * @param sr
	 * @param query
	 */
	public void deleteIndex(String index) {

		DeleteIndexRequest request = new DeleteIndexRequest(index.toLowerCase());
		try {
			DeleteIndexResponse deleteIndexResponse = highLevelClient.indices().delete(request, getWriteHeaders());
			if (deleteIndexResponse.isAcknowledged()) {
				System.out.println("[ELASTICSEARCH MANAGER] \tIndex: " + index + "has been deleted");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------
	
		// OLD CODE - to afraid to delete :)
	
	// ------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------

// public Indexer() {
//
// this.hostname = loadPropertiesFile().get("hostname").toString();// hostname
// this.port =
// Integer.valueOf(loadPropertiesFile().getProperty("port").toString());
// this.scheme = loadPropertiesFile().getProperty("scheme").toString();
// this.clusterName =
// loadPropertiesFile().getProperty("cluster-name").toString(); // name given to
// the cluster
// this.clusterport =
// Integer.valueOf(loadPropertiesFile().getProperty("cluster-port").toString());
// //this is a different value to the 'port' check cluster settings
// this.highLevelclient = getHighLevelClient(); // This client handles 'High
// Level requests' such as indexing docs
// this.adminClient = getAdminClient(); // this client is used to perform
// administration tasks on ES
// this.indices = getIndices();
// }
//
// private Properties indexProperties = new Properties();
// private String hostname;
// private String scheme;
// private int port;
// private int clusterport;
// private String clusterName;
// private RestHighLevelClient highLevelclient;
// private Client adminClient;
// private List<String> indices = new ArrayList<String>();
//
// private void checkPropertiesFile(Path path) {
// if (!Files.exists(path)) {
// System.err.println("The file " + path + " has not been found");
// }
// }

// private String getProperties() throws IllegalArgumentException, IOException {
// String path =
// getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
// if (path.endsWith("bin/"))
// path = path.substring(0, path.lastIndexOf("bin/"));
// File file = new File(path + "prefs/elasticsearch.properties");
// checkPropertiesFile(file.toPath());
//
// return file.getPath();
// }

/// **
// * Reads Elasticsearch configuration properties from properties file
// *
// * @return indexProperties
// */
// private Properties loadPropertiesFile() {
// InputStream iStream = null;
// try {
// // Loading properties file from the path (relative path given here)
// iStream = new FileInputStream(getProperties());
// indexProperties.load(iStream);
//
// } catch (IOException e) {
//
// e.printStackTrace();
// } finally {
// try {
// if (iStream != null) {
// iStream.close();
// return indexProperties;
// }
// } catch (IOException e) {
//
// e.printStackTrace();
// }
// }
// return indexProperties;
// }

/// **
// *
// * Builds an Elasticsearch HighLevel REST client
// *
// * @return RestHighLevelClient
// */
// private RestHighLevelClient getHighLevelClient() {
//
// RestHighLevelClient client = new RestHighLevelClient(
// RestClient.builder(new HttpHost(this.hostname, this.port, this.scheme),
// new HttpHost(this.hostname, this.port + 1, this.scheme)));
//
// System.out.println("[ELASTICSEARCH MANAGER] \tHighLevelClient for " +
/// hostname + " has been created");
// return client;
// }
// private Client getAdminClient() {
//
// Client client = null;
//
// Settings settings = Settings.builder().put("client.transport.sniff",
/// true).put("cluster.name", this.clusterName)
// .build();
//
// try {
// client = new PreBuiltTransportClient(settings)
// .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"),
/// clusterport));
// } catch (UnknownHostException e) {
// e.printStackTrace();
// }
// System.out.println("[ELASTICSEARCH MANAGER] \tAdmin Client Created");
//
// return client;
// }
