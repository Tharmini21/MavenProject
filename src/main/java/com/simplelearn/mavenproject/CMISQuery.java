package com.simplelearn.mavenproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
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
	// private static final AlfrescoUtilityImpl Helper = new AlfrescoUtilityImpl();
	// private static final AlfrescoUtility alfrescoHelper = new AlfrescoUtility();
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(CMISQuery.class);
	String username = "admin";
	String password = "admin";
	String hostName = "http://localhost:8080";
	AlfrescoUtilityImpl alfrescoHelper = new AlfrescoUtilityImpl();
	Session alfSession = alfrescoHelper.createSession(username, password, hostName);
	public static void main(String[] args) {
		try {
			CmisClient cmisClient = new CmisClient();
			String connectionName = "martinAlf01";
			Session session = cmisClient.getSession(connectionName, "admin", "admin");
			// cmisClient.listTopFolder(session);
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
				// String query = "SELECT * FROM cmis:sites WHERE cmis:name LIKE 'Files%'";
				// String query = "SELECT * FROM cmis:document WHERE cmis:name LIKE 'test%'";
				// String query = "SELECT cm:userName, cm:homeFolder FROM cm:person";
				// String query = "SELECT * FROM Document D WHERE CONTAINS(D,
				// 'cmis:name:'Sites)";

				// This will fetch you all the children of data dictionary folder.
				// SELECT * FROM cmis:folder WHERE
				// CONTAINS('PATH:"//app:company_home/app:dictionary/*"')
				// SELECT * FROM cmis:folder WHERE
				// CONTAINS('PATH:"//app:company_home/app:dictionary//*"')
				// ANCESTOR:"workspace://SpacesStore/b9f46070-fd06-4a72-8389-e3c76ca574a1" AND
				// TYPE:"cm:folder"
				// "query": {
				// "query":"PARENT:'workspace://SpacesStore/b9f46070-fd06-4a72-8389-e3c76ca574a1'
				// AND TYPE:'cm:folder'"
				// },
				String queryString1 = "select * from cmis:folder where cmis:name = 'Sites'"; // Sites/swsdp/documentLibrary;
				// String queryString = "select * from cmis:folder where cmis:name = 'Sites'";
				// String queryString = "select * from cmis:folder where cmis:contentStreamLength = 1024 AND cmis:contentStreamMimeType =";
				
				String queryString = "select * from st:sites";

				ItemIterable<QueryResult> results = alfSession.query(queryString, false);

				String objectId = "";
				String objectName = "";
				String createdBy = "";
				String contentStreamFileName = "";
				String contentStreamMimeType = "";
				String contentStreamLength = "";
				Long fileLengthLong = null;
				for (QueryResult result : results) {
					for (PropertyData<?> prop : result.getProperties()) {
						System.out.println(prop.getQueryName() + ": " + prop.getFirstValue());
					}
					System.out.println("-------------------@@@@@@@@@@@@@@@-------------------");
					objectId = result.getPropertyValueByQueryName("cmis:objectId");
					objectName = result.getPropertyValueByQueryName("cmis:name");
					createdBy = result.getPropertyValueByQueryName("cmis:createdBy");
					contentStreamFileName = result.getPropertyValueByQueryName("cmis:contentStreamFileName");
					contentStreamMimeType = result.getPropertyValueByQueryName("cmis:contentStreamMimeType");
					contentStreamLength = result.getPropertyValueByQueryName("cmis:contentStreamLength");
					List<CsvModelData> queryres = new ArrayList<>();
					//CsvModelData csvmodel =null;
					//queryres.add(1,objectId,objectName,createdBy,contentStreamFileName,contentStreamMimeType,contentStreamLength);
				}

				// CmisObject object = alfSession.getObject(new ObjectIdImpl(objectId));
				CmisObject object = alfSession.getObject(objectId);
				Folder folder = (Folder) object;
				ItemIterable<CmisObject> children = folder.getChildren();
				long totalNumberOfNodes = children.getTotalNumItems();
				//String queryString2 = "select * from cmis:document where contains('Sites') and in_tree('" + folder.getName() + "')";
				String queryString2 = "select * from cmis:document where in_tree('" + folder.getName() + "')";
				String queryString3 = "SELECT * FROM cmis:document WHERE IN_FOLDER('" + folder.getId() + "')";
				System.out.println("***results from queryString2*********** " + queryString2);
				ItemIterable<QueryResult> res2 = alfSession.query(queryString2, false);
				for (QueryResult result : res2) {
					for (PropertyData<?> prop : result.getProperties()) {
						System.out.println(prop.getQueryName() + ": " + prop.getFirstValue());
					}
				}
				System.out.println("***results from query " + queryString3);
				//dumpQueryResults(getQueryResults(queryString2));
				//Document document = (Document) object;
               // long fileLength = document.getContentStreamLength();
                //String fileName = document.getContentStreamFileName();
                //String mimeType = document.getContentStreamMimeType();
               // System.out.println("***results from fileName******" + fileName);
				//System.out.println("***results from mimeType******" + mimeType);
				//System.out.println("***results from fileLength******" + fileLength);
				
				System.out.println("***results from totalNumberOfNodes********* " + totalNumberOfNodes);
				System.out.println("***results from totalchildren********* " + children);
				System.out.println("***results from query " + queryString);
				System.out.println("***results from objectId******" + objectId);
				System.out.println("***results from objectName******" + objectName);
				System.out.println("***results from createdBy******" + createdBy);
				System.out.println("***results from contentStreamFileName******" + contentStreamFileName);
				System.out.println("***results from contentStreamMimeType******" + contentStreamMimeType);
				System.out.println("***results from contentStreamLength******" + contentStreamLength);
				System.out.println("-------------------@@@@@@@@@@@@@@@-------------------");

				Folder root = session.getRootFolder();		
				System.out.println("***childrenlist******" + root);
				ItemIterable<CmisObject> childrenlist = root.getChildren();

				for (CmisObject o : childrenlist) {
					//System.out.println("Name: " + o.getName());
					//System.out.println("Id: " + o.getId());
					//System.out.println("Type: " + (o.getType()));
					if (o instanceof Document) {
		    			Document docMetadata = (Document)o;
		    			ContentStream docContent = docMetadata.getContentStream();
		    				
		    			System.out.println(docMetadata.getName() + " [size=" +
		    					docContent.getLength()+"][Mimetype=" +
		    					docContent.getMimeType()+"][type=" +
		    					docMetadata.getType().getDisplayName()+"]");
		    		} else {
		    			System.out.println("Name: "+o.getName() + "[type="+o.getType().getDisplayName()+"]");
		    		}
				}
				System.out.println("***childrenlist******" + root);
				if (!session.getRepositoryInfo().getCapabilities().isGetDescendantsSupported()) {
					System.out.println("getDescendants not supported in this repository");
				} else {
					System.out.println("Descendants of " + folder.getName() + ":-");
					for (Tree<FileableCmisObject> t : folder.getDescendants(-1)) {
						printTree(t);
					}
				}
				
				CMISQuery test = new CMISQuery();
		        //test.exportCSV(response);
			
				// List<Tree<FileableCmisObject>> getFolderTree(int depth);
				// String query = "SELECT * FROM cmis:folder F WHERE
				// CONTAINS(F,'PATH:'//app:company_home/app:dictionary/*'')";
				// ItemIterable<QueryResult> q = alfSession.query(query, false);
				// System.out.println("***results from query " + query);
				// System.out.println("***results from queryResult " + results);

				
			}

			System.exit(0);

		} catch (CmisInvalidArgumentException e) {
			System.out.println("Error Occured...");
			e.printStackTrace();
			System.err.println("caught an " + e.getClass().getName() + " exception with message " + e.getMessage());
		}

	}

	public List<CsvModelData> listduplicatefiles() {
		CmisClient cmisClient = new CmisClient();
		String connectionName = "martinAlf01";
		Session session = cmisClient.getSession(connectionName, "admin", "admin");
		String queryString = "select * from st:sites";

		ItemIterable<QueryResult> results = alfSession.query(queryString, false);
		List<CsvModelData> questres = new ArrayList<>();
		int i = 1;
		for (QueryResult qr : results) {
			System.out.println("--------------------------------------------\n" + i + " , "
					+ qr.getPropertyByQueryName("cmis:objectTypeId").getFirstValue() + " , "
					+ qr.getPropertyByQueryName("cmis:name").getFirstValue() + " , "
					+ qr.getPropertyByQueryName("cmis:createdBy").getFirstValue() + " , "
					+ qr.getPropertyByQueryName("cmis:objectId").getFirstValue() + " , "
					+ (qr.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue() != null
							? qr.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue()
							: "NA")
					+ " , "
					+ (qr.getPropertyByQueryName("cmis:contentStreamMimeType").getFirstValue() != null
							? qr.getPropertyByQueryName("cmis:contentStreamMimeType").getFirstValue()
							: "NA")
					+ " , "
					+ (qr.getPropertyByQueryName("cmis:contentStreamLength").getFirstValue() != null
							? qr.getPropertyByQueryName("cmis:contentStreamLength").getFirstValue()
							: "NA"));
			//questres.add(new String[] { "qr.getPropertyByQueryName("cmis:name").getFirstValue()", "qr.getPropertyByQueryName("cmis:name").getFirstValue()",
			//"qr.getPropertyByQueryName("cmis:createdBy").getFirstValue()",
            //"qr.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue()",
			//"qr.getPropertyByQueryName("cmis:contentStreamMimeType").getFirstValue()"
			//"qr.getPropertyByQueryName(\"cmis:contentStreamLength\").getFirstValue().toString()"});
			CsvModelData duplicatelist = new CsvModelData(qr.getPropertyByQueryName("cmis:objectId").getFirstValue().toString(), 
					qr.getPropertyByQueryName("cmis:name").getFirstValue().toString(),
					qr.getPropertyByQueryName("cmis:createdBy").getFirstValue().toString(), 
					qr.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue().toString(),
					qr.getPropertyByQueryName("cmis:contentStreamMimeType").getFirstValue().toString(),
					qr.getPropertyByQueryName("cmis:contentStreamLength").getFirstValue().toString());
			i++;
			questres.add(duplicatelist);
		}
		
		//questres.add(new CsvModelData("439ffd36-ff11-484e-884e-549a21e4a2e5", "Sites", "System", "NA", "NA", "NA"));
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
		//System.out.println("Descendant " + tree.getItem().getProperties());
		for (Tree<FileableCmisObject> t : tree.getChildren()) {
			printTree(t);
		}
	}

}
