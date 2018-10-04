package org.eclipse.scava.index.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
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
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchManager {

	public ElasticSearchManager() {
		
		this.hostname = loadPropertiesFile().get("hostname").toString();// hostname 
		this.port = Integer.valueOf(loadPropertiesFile().getProperty("port").toString());
		this.scheme = loadPropertiesFile().getProperty("scheme").toString();
		this.clusterName = loadPropertiesFile().getProperty("cluster-name").toString(); // name given to the cluster 
		this.clusterport = Integer.valueOf(loadPropertiesFile().getProperty("cluster-port").toString()); //this is a different value to the 'port' check cluster settings 
		this.highLevelclient = getHighLevelClient(); // This client handles 'High Level requests' such as indexing docs
		this.adminClient = getAdminClient(); // this client is used to perform administration tasks on ES
		this.indices = getIndices();
		}

	private Properties indexProperties = new Properties();
	private String hostname;
	private String scheme;
	private int port;
	private int clusterport;
	private String clusterName;
	private RestHighLevelClient highLevelclient;
	private Client adminClient;
	private List<String> indices = new ArrayList<String>();

	private void checkPropertiesFile(Path path) {
		if (!Files.exists(path)) {
			System.err.println("The file " + path + " has not been found");
		}
	}

	private String getProperties() throws IllegalArgumentException, IOException {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("bin/"))
			path = path.substring(0, path.lastIndexOf("bin/"));
		File file = new File(path + "prefs/elasticsearch.properties");
		checkPropertiesFile(file.toPath());

		return file.getPath();
	}

	public List<String> getIndices() {
		List<String> list = new ArrayList<String>();
		ImmutableOpenMap<String, IndexMetaData> indices = this.adminClient.admin().cluster().prepareState().get()
				.getState().getMetaData().getIndices();
		for (ObjectObjectCursor<String, IndexMetaData> x : indices) {
			if (!(x.value == null)) {
				if (!(x.value.getIndex().getName().startsWith("."))) {
					if (!(list.contains(x.value.getIndex().getName()))) {
						System.out.println(x.value.getIndex().getName());
						list.add(x.value.getIndex().getName());
					}
				}
			}
		}
		return list;
	}

	private Client getAdminClient() {

		Client client = null;

		Settings settings = Settings.builder().put("client.transport.sniff", true).put("cluster.name", this.clusterName)
				.build();

		try {
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), clusterport));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("[ELASTICSEARCH MANAGER] \tAdmin Client Created");

		return client;
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
	public void addIndexSetting(String index, String setting) throws IOException {
		UpdateSettingsRequest request = new UpdateSettingsRequest();
		request.indices(index);
		request.settings(setting, XContentType.JSON);
		UpdateSettingsResponse updateSettingsResponse = highLevelclient.indices().putSettings(request,
				getWriteHeaders());
		if (updateSettingsResponse.isAcknowledged() == true) {
			System.out.println("[ELASTICSEARCH MANAGER] \tSettings have been added to "+ index);
		}
	}

	/**
	 * Reads Elasticsearch configuration properties from properties file
	 * 
	 * @return indexProperties
	 */
	private Properties loadPropertiesFile() {
		InputStream iStream = null;
		try {
			// Loading properties file from the path (relative path given here)
			iStream = new FileInputStream(getProperties());
			indexProperties.load(iStream);

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				if (iStream != null) {
					iStream.close();
					return indexProperties;
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return indexProperties;
	}

	/**
	 * 
	 * Builds an Elasticsearch HighLevel REST client
	 * 
	 * @return RestHighLevelClient
	 */
	private RestHighLevelClient getHighLevelClient() {

		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost(this.hostname, this.port, this.scheme),
						new HttpHost(this.hostname, this.port + 1, this.scheme)));

		System.out.println("[ELASTICSEARCH MANAGER] \tHighLevelClient for " + hostname + " has been created");
		return client;
	}

	/**
	 * Creates a new index
	 * 
	 * @param index
	 *            - name of the new index
	 */
	public void createIndex(String index) {

		CreateIndexRequest request = new CreateIndexRequest(index);
		CreateIndexResponse createIndexResponse;
		try {
			createIndexResponse = highLevelclient.indices().create(request, getWriteHeaders());
			if (createIndexResponse.isAcknowledged() == true) {
				System.out.println("[ELASTICSEARCH MANAGER] \tThe index " + index + " has been created");
			}

		} catch (ElasticsearchStatusException e) {
			System.err.println("[ELASTICSEARCH MANAGER] \t" +index + "\tIndexResponse :" + e.getLocalizedMessage());

		} catch (IOException e) {

			// e.printStackTrace();
		}

	}

	/**
	 * Adds a document map (mapping) to a specific index
	 * 
	 * @param index
	 *            - index name
	 * @param type
	 *            - doucment type
	 * @param source
	 *            - a JSON document represented as a string
	 */
	public void addMapping(String index, String type, String source) {

		PutMappingRequest putMappingRequest = new PutMappingRequest(index.toLowerCase());
		putMappingRequest.source(source, XContentType.JSON).type(type.toLowerCase());

		PutMappingResponse putMappingResponse;
		try {
			putMappingResponse = this.highLevelclient.indices().putMapping(putMappingRequest, this.getWriteHeaders());

			if (putMappingResponse.isAcknowledged() == true) {
				System.out.println("[ELASTICSEARCH MANAGER] \tMapping for " + type + " in " + index + " was added");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a basic HTTP write headers
	 * 
	 * 
	 * @return headers
	 */
	private Header[] getWriteHeaders() {

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
	private Header[] getReadHeaders() {

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
	public GetRequest prepareGetRequest(String index, String doc, String doc_id) {

		GetRequest request = new GetRequest(index, doc, doc_id);

		return request;
	}

	/**
	 * FOR TESTING PURPOSES
	 * 
	 * Use this method when the dump that you are using already contains unique
	 * identifiers for each document. For example a 'customerId' or 'postID'. This
	 * will prevent duplicate documents being added to the index. Essentially, if
	 * the same ID already exists in the index, Elasticsearch will simply update the
	 * document with the 'new' changes.
	 * 
	 * @param index
	 *            - index name
	 * @param type
	 *            - document type
	 * @param id
	 *            - unique identifier
	 * @param source
	 *            - a JSON document represented as a string
	 * @return
	 * @return IndexResponse
	 * @throws IOException
	 */
	// TODO Remove this from production
	public void indexDocument(String index, String type, String id, String source) throws IOException {
		// test
		IndexRequest indexRequest = new IndexRequest();
		indexRequest.index(index.toLowerCase()).type(type.toLowerCase()).id(id).source(source, XContentType.JSON);
		IndexResponse res = this.highLevelclient.index(indexRequest, getWriteHeaders());
		System.out.println(res.getResult());
	}

	/**
	 * Use this method when the dump that you are using already contains unique
	 * identifiers for each document. For example a 'customerId' or 'postID'. This
	 * will prevent duplicate documents being added to the index. Essentially, if
	 * the same ID already exists in the index, Elasticsearch will simply update the
	 * document with the 'new' changes.
	 * 
	 * @param index
	 *            - index name
	 * @param type
	 *            - document type
	 * @param id
	 *            - unique identifier
	 * @param object
	 *            - object that represents the structure of a document for indexing
	 * @return
	 * @return IndexResponse
	 * @throws IOException
	 */
	public void indexDocument(String index, String type, String id, Object obj) throws IOException {
		String source = objectToJson(obj);
		IndexRequest indexRequest = new IndexRequest();
		indexRequest.index(index.toLowerCase()).type(type.toLowerCase()).id(id).source(source, XContentType.JSON);
		
		System.out.println("[ELASTICSEARCH MANAGER] \tIndex: " + index + "\tDocument: " + type + "\t UID:" + id + "\t IndexResponse: "
				+ this.highLevelclient.index(indexRequest, getWriteHeaders()).getResult());

	}

	/**
	 * DO NOT USE! Use this method when the dump does not contain unique identifiers
	 * for each document. Elasticsearch will auto-generate a unique identifier. Note
	 * this will not prevent duplicated documents from being added to the index.
	 * 
	 * @param index
	 *            - index name
	 * @param type
	 *            - document type
	 * @param source
	 *            - a JSON document represented as a string
	 * @return IndexResponse
	 * @throws IOException
	 */
	// TODO REMOVE FROM PRODUCTION
	public IndexResponse indexDocument(String index, String type, String source) throws IOException {

		IndexRequest indexRequest = new IndexRequest();
		indexRequest.index(index).type(type).source(source, XContentType.JSON);

		return this.highLevelclient.index(indexRequest, getWriteHeaders());
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
	public SearchResponse searchIndex(String index, String[] type, QueryBuilder queryBuilder) throws IOException {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(queryBuilder);

		// search request
		SearchRequest searchRequest = new SearchRequest(index);
		searchRequest.source(searchSourceBuilder).types(type);
		SearchResponse searchResponse = highLevelclient.search(searchRequest, getReadHeaders());
		return searchResponse;
	}

	/**
	 * Closes the connection to the Elasticsearch index
	 * 
	 * @throws IOException
	 */
	public void closeHighLevelClient() throws IOException {

		this.highLevelclient.close();
		System.out.println("[ELASTICSEARCH MANAGER] \tHight Level Client - Closed");

	}

	/**
	 * Closes the Admin Client
	 */
	public void closeAdminClient() {

		this.adminClient.close();
		System.out.println("[ELASTICSEARCH MANAGER] \tAdmin Client - Closed");
	}

	/**
	 * Closes all clients (HighLevelRest and AdminClients)
	 * 
	 * @throws IOException
	 */

	public void closeAllClients() throws IOException {
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

	private String objectToJson(Object object) throws com.fasterxml.jackson.core.JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(object);

		return jsonString;
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
				DeleteIndexResponse deleteIndexResponse = highLevelclient.indices().delete(request, getWriteHeaders());
				System.out.println("[ELASTICSEARCH MANAGER] \tIndex: " + index + "has been deleted");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} 
	
	
	// TODO REMOVE FROM PRODUCTION
	public void printHits(SearchResponse sr, QueryBuilder query) {

		for (SearchHit hit : sr.getHits()) {
			System.out.println("[" + query.getName() + " query] Response = " + hit.getSourceAsString());
		}

	}

}
