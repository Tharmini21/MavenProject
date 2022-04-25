package com.simplelearn.mavenproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Comparator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.opencsv.CSVWriter;
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

	static String username = "admin";
	static String password = "admin";
	static String hostName = "http://localhost:8080";
	static AlfrescoUtilityImpl alfrescoHelper = new AlfrescoUtilityImpl();
	static Session alfSession = alfrescoHelper.createSession(username, password, hostName);

	public static void main(String[] args) {
		try {

			System.out.println("\n\nStarting .....\n");
			// String username = "admin";
			// String password = "admin";
			// String hostName = "http://localhost:8080";
			// AlfrescoUtilityImpl alfrescoHelper = new AlfrescoUtilityImpl();
			// Session alfSession = alfrescoHelper.createSession(username, password,
			// hostName);

			if (alfSession == null) {
				System.out.println("Error Connecting to Alfresco....");

			} else {
				System.out.println("Connected to Alfresco....\n");
				CMISQuery myObj = new CMISQuery(); // Create an object of Main
				myObj.GetDocumentData();
				myObj.duplicatefile();
				/*
				 * String queryString = "select * from st:sites"; ItemIterable<QueryResult>
				 * results = alfSession.query(queryString, false); String objectId = ""; for
				 * (QueryResult result : results) { objectId =
				 * result.getPropertyValueByQueryName("cmis:objectId"); } CmisObject object =
				 * alfSession.getObject(objectId); Folder folder = (Folder) object; int
				 * maxItemsPerPage = 50; int skipCount = 0; int page_num = 0; OperationContext
				 * operationContext = alfSession.createOperationContext(); String queryString2 =
				 * "SELECT cmis:objectId,cmis:name,cmis:createdBy,cmis:contentStreamFileName,cmis:contentStreamMimeType,cmis:contentStreamLength FROM cmis:document WHERE in_tree('"
				 * + folder.getId() + "') order by cmis:name, cmis:contentStreamMimeType";
				 * String queryString4 =
				 * "SELECT cmis:objectId,cmis:name,cmis:createdBy,cmis:contentStreamFileName,cmis:contentStreamMimeType,cmis:contentStreamLength,COUNT(cmis:name) from cmis:document GROUP BY cmis:name HAVING COUNT(cmis:name) > 1"
				 * ; ItemIterable<QueryResult> res2 = alfSession.query(queryString2, false,
				 * operationContext); // .skipTo(page_num *
				 * maxItemsPerPage).getPage(maxItemsPerPage);
				 * System.out.println("***Res*********** " + queryString2);
				 * System.out.println("***TotalCount*********** " + res2.getTotalNumItems());
				 * for (QueryResult res : res2) { for (PropertyData<?> prop :
				 * res.getProperties()) { System.out.println(prop.getQueryName() + ": " +
				 * prop.getFirstValue()); } System.out.
				 * println("-------------------Next One@@@@@@@@@@@@@@@-------------------"); }
				 * page_num++; System.out.println(res2.getHasMoreItems());
				 * System.out.println("-------------------@Done@-------------------");
				 */
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
				+ folder.getId() + "') order by cmis:name";
		String csvFilePath = "C://Users//Tharmini//Downloads//DocumentsqlData-export.csv";
		// Statement statement = (Statement) alfSession.createQueryStatement(query);
		QueryStatement qs = alfSession.createQueryStatement(query);
		String statement = qs.toQueryString();
		ItemIterable<QueryResult> resultset = qs.query(false);

		System.out.println("QueryStatement:" + qs);
		System.out.println("Statement:" + statement);
		ItemIterable<QueryResult> res2 = alfSession.query(query, false);
		System.out.println("***TotalCount*********** " + res2.getTotalNumItems());

		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath));
			// write header line containing column names
			fileWriter.write(
					"cmis_objectId,cmis_name,cmis_createdBy,cmis_contentStreamFileName,cmis_contentStreamMimeType,cmis_contentStreamLength");

			for (QueryResult response : res2) {
				for (PropertyData<?> prop : response.getProperties()) {
					System.out.println(prop.getQueryName() + ": " + prop.getFirstValue());
				}
				System.out.println("-------------------Next One@@@@@@@@@@@@@@@-------------------");
				String ObjectId = response.getPropertyValueByQueryName("cmis:objectId");
				String Name = response.getPropertyValueByQueryName("cmis:name");
				String CreatedBy = response.getPropertyValueByQueryName("cmis:createdBy");
				String ContentStreamFileName = response.getPropertyValueByQueryName("cmis:contentStreamFileName");
				String ContentStreamMimeType = response.getPropertyValueByQueryName("cmis:contentStreamMimeType");
				BigInteger ContentStreamLength = response.getPropertyValueByQueryName("cmis:contentStreamLength");

				String line = String.format("\"%s\",%s,%s,%s,%s,%s", ObjectId, Name, CreatedBy, ContentStreamFileName,
						ContentStreamMimeType, ContentStreamLength);

				fileWriter.newLine();
				fileWriter.write(line);
			}
			System.out.println(res2.getHasMoreItems());
			System.out.println("-------------------@Done@-------------------");

			// while (results.next()) {

			// }

			// statement.close();
			fileWriter.close();
		} catch (Exception e) {
			System.out.println("Error Occured...");
			e.printStackTrace();
		}
		ResultSet results = null;
		PreparedStatement statement1 = null;
		try {
			results = null;

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void duplicatefile() {
		String line = "";
		String splitBy = ",";
		String csvFile = "C://Users//Tharmini//Downloads//DocumentsqlData-export.csv";
		try {

			List<String> validationFile = new ArrayList<>();

			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				String[] grouped = line.split(splitBy); // use comma as separator
				validationFile.add(line);
			}
			List<String> validationFileCopy = Collections.unmodifiableList(validationFile);
			// System.out.println(validationFile);
			for (String lineres : validationFile) {
				int comp = Collections.binarySearch(validationFileCopy, lineres, new ComparatorLine());
				if (comp > 0) {
					System.out.println(lineres);
				}
			}
			Set<String> uniqueLines = new HashSet<>();
			Set<String> duplicateLines = new HashSet<>();
			for (String newline : validationFile) {
				if (!uniqueLines.add(newline.toLowerCase())) {
				 duplicateLines.add(newline.toLowerCase());
				}
			}
			Set<String> uniques = new HashSet<>();      
			List<Object> duplicates = validationFile.stream().filter(i->!uniqueLines.add(i)).collect(Collectors.toList());
		    System.out.println("duplicateLines" +duplicates);
			System.out.println("uniqueLines" + uniqueLines);
			 System.out.println("duplicateLines" +duplicateLines);
			// Option 3 : unique lines and duplicate lines by Java Streams
			/*
			 * Set<String> uniquesJava8 = new HashSet<>(); List<String> duplicatesJava8 =
			 * validationFile .stream() .filter(element ->
			 * !uniquesJava8.add(element.toLowerCase())) .map(element ->
			 * element.toLowerCase()) .collect(Collectors.toList());
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * var lines = File.ReadLines("yourFile.ext"); // uniqueness is defined by the
		 * first two columns var grouped = lines.GroupBy(line => string.Join(", ",
		 * line.Split(',').Take(2))).ToArray();
		 * 
		 * // "unique entry and first occurrence of duplicate entry" -> first entry in
		 * group var unique = grouped.Select(g => g.First()); var dupes =
		 * grouped.Where(g => g.Count() > 1).SelectMany(g => g);
		 * 
		 * Console.WriteLine("unique"); foreach (var name in unique)
		 * Console.WriteLine(name);
		 * 
		 * Console.WriteLine("\nDupes"); foreach (var name in dupes)
		 * Console.WriteLine(name);
		 */
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
