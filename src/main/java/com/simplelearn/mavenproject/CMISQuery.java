package com.simplelearn.mavenproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.QueryStatement;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.data.RepositoryCapabilities;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.http.HttpHeaders;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.simplelearn.mavenproject.AlfrescoUtilityImpl;
//import com.simplelearn.mavenproject.*;
import com.simplelearn.mavenproject.CsvModelData;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CMISQuery {

	String username = "admin";
	String password = "admin";
	String hostName = "http://localhost:8080";
	AlfrescoUtilityImpl alfrescoHelper = new AlfrescoUtilityImpl();
	Session alfSession = alfrescoHelper.createSession(username, password, hostName);

	public static void main(String[] args) {
		try {

			System.out.println("\n\nStarting .....\n");
			String username = "admin";
			String password = "admin";
			String hostName = "http://localhost:8080";
			AlfrescoUtilityImpl alfrescoHelper = new AlfrescoUtilityImpl();
			Session alfSession = alfrescoHelper.createSession(username, password, hostName);

			if (alfSession == null) {
				System.out.println("Error Connecting to Alfresco....");

			} else {
				System.out.println("Connected to Alfresco....\n");
				CMISQuery myObj = new CMISQuery(); // Create an object of Main
				myObj.GetDocumentData();
				String queryString = "select * from st:sites";
				ItemIterable<QueryResult> results = alfSession.query(queryString, false);
				String objectId = "";
				for (QueryResult result : results) {
					objectId = result.getPropertyValueByQueryName("cmis:objectId");
				}
				CmisObject object = alfSession.getObject(objectId);
				Folder folder = (Folder) object;
				int maxItemsPerPage = 50;
				int skipCount = 0;
				int page_num = 0;
				OperationContext operationContext = alfSession.createOperationContext();
				String queryString2 = "SELECT cmis:objectId,cmis:name,cmis:createdBy,cmis:contentStreamFileName,cmis:contentStreamMimeType,cmis:contentStreamLength FROM cmis:document WHERE in_tree('"
						+ folder.getId() + "') order by cmis:name, cmis:contentStreamMimeType";
				String queryString4 = "SELECT cmis:objectId,cmis:name,cmis:createdBy,cmis:contentStreamFileName,cmis:contentStreamMimeType,cmis:contentStreamLength,COUNT(cmis:name) from cmis:document GROUP BY cmis:name HAVING COUNT(cmis:name) > 1";
				ItemIterable<QueryResult> res2 = alfSession.query(queryString2, false, operationContext);
				// .skipTo(page_num * maxItemsPerPage).getPage(maxItemsPerPage);
				System.out.println("***Res*********** " + queryString2);
				System.out.println("***TotalCount*********** " + res2.getTotalNumItems());
				for (QueryResult res : res2) {
					for (PropertyData<?> prop : res.getProperties()) {
						System.out.println(prop.getQueryName() + ": " + prop.getFirstValue());
					}
					System.out.println("-------------------Next One@@@@@@@@@@@@@@@-------------------");
				}
				page_num++;
				System.out.println(res2.getHasMoreItems());
				System.out.println("-------------------@Done@-------------------");
			}
			System.exit(0);

		} catch (CmisInvalidArgumentException e) {
			System.out.println("Error Occured...");
			e.printStackTrace();
			System.err.println("caught an " + e.getClass().getName() + " exception with message " + e.getMessage());
		}

	}

	public void GetDocumentData() {
		String queryString = "select * from st:sites";
		ItemIterable<QueryResult> res = alfSession.query(queryString, false);
		String objectId = "";
		for (QueryResult result : res) {
			objectId = result.getPropertyValueByQueryName("cmis:objectId");
		}
		CmisObject object = alfSession.getObject(objectId);
		Folder folder = (Folder) object;
		String query = "SELECT cmis:objectId,cmis:name,cmis:createdBy,cmis:contentStreamFileName,cmis:contentStreamMimeType,cmis:contentStreamLength FROM cmis:document WHERE in_tree('"
				+ folder.getId() + "') order by cmis:name, cmis:contentStreamMimeType";
		String csvFilePath = "DocumentsqlData-export.csv";
		//Statement statement = (Statement) alfSession.createQueryStatement(query);
		//Statement statement = query.
		QueryStatement qs = alfSession.createQueryStatement(query);
		String statement = qs.toQueryString();
		ItemIterable<QueryResult> resultset = qs.query(false);

		System.out.println("QueryStatement:"+ qs);
		System.out.println("Statement:"+ statement);
		ItemIterable<QueryResult> res2 = alfSession.query(query, false);
		ResultSet results = null;
		PreparedStatement statement1 = null;
		//Statement stmt=(Statement) statement;
		//results = stmt.executeQuery();
		try {
			//results = statement.executeQuery(query);
			results = null;
			//results = res2.;

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath));

			// write header line containing column names
			fileWriter.write(
					"cmis_objectId,cmis_name,cmis_createdBy,cmis_contentStreamFileName,cmis_contentStreamMimeType,cmis_contentStreamLength");

			while (results.next()) {
				String ObjectId = results.getString("cmis:objectId");
				String Name = results.getString("cmis:name");
				String CreatedBy = results.getString("cmis:createdBy");
				String ContentStreamFileName = results.getString("cmis:contentStreamFileName");
				String ContentStreamMimeType = results.getString("cmis:contentStreamMimeType");
				String ContentStreamLength = results.getString("cmis:contentStreamLength");

				String line = String.format("\"%s\",%s,%.1f,%s,%s", ObjectId, Name, CreatedBy, ContentStreamFileName,
						ContentStreamMimeType, ContentStreamLength);

				fileWriter.newLine();
				fileWriter.write(line);
			}

			//statement.close();
			fileWriter.close();
		} catch (Exception e) {
			System.out.println("Error Occured...");
			e.printStackTrace();
		}
	}

	public List<CsvModelData> listduplicatefiles() {
		CmisClient cmisClient = new CmisClient();
		String connectionName = "martinAlf01";
		Session session = cmisClient.getSession(connectionName, "admin", "admin");
		String queryString = "select * from st:sites";
		ItemIterable<QueryResult> results = alfSession.query(queryString, false);
				
		List<CsvModelData> questres = new ArrayList<>();
		List<CsvModelData> data = new ArrayList<>();

		int i = 1;
		for (QueryResult qr : results) {
			// CsvModelData dl = new CsvModelData();
			CsvModelData duplicatelist = new CsvModelData(
					qr.getPropertyByQueryName("cmis:objectId").getFirstValue().toString(),
					qr.getPropertyByQueryName("cmis:name").getFirstValue().toString(),
					qr.getPropertyByQueryName("cmis:createdBy").getFirstValue().toString(),
					qr.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue().toString(),
					qr.getPropertyByQueryName("cmis:contentStreamMimeType").getFirstValue().toString(),
					qr.getPropertyByQueryName("cmis:contentStreamLength").getFirstValue().toString());
			i++;
			questres.add(duplicatelist);
		}

		// questres.add(new CsvModelData("439ffd36-ff11-484e-884e-549a21e4a2e5",
		// "Sites", "System", "NA", "NA", "NA"));
		return questres;
	}

	// @GetMapping("/export-sites")
	public void exportCSV(HttpServletResponse response) throws Exception {

		String filename = "sitedata.csv";
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_LOCATION, "attachment; filename=\"" + filename + "\"");
		StatefulBeanToCsv<CsvModelData> writer = new StatefulBeanToCsvBuilder<CsvModelData>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();
		writer.write(this.listduplicatefiles());

	}

	private static void printTree(Tree<FileableCmisObject> tree) {
		System.out.println("Descendant " + tree.getItem().getName());
		// System.out.println("Descendant " + tree.getItem().getProperties());
		for (Tree<FileableCmisObject> t : tree.getChildren()) {
			printTree(t);
		}
	}

}
