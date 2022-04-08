package com.simplelearn.mavenproject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.commons.data.RepositoryCapabilities;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.simplelearn.mavenproject.AlfrescoUtilityImpl;
//import com.simplelearn.mavenproject.*;
public class CMISQuery {
	// private static final AlfrescoUtilityImpl Helper = new AlfrescoUtilityImpl();
	// private static final AlfrescoUtility alfrescoHelper = new AlfrescoUtility();
	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(CMISQuery.class);
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
				String queryString1 = "select * from cmis:folder where cmis:name = 'Sites'"; // Sites/swsdp/documentLibrary;
				//String queryString = "select * from cmis:folder where cmis:name = 'Sites'";
				String queryString = "select * from st:sites";
				
				ItemIterable<QueryResult> results = alfSession.query(queryString, false);
				
				String objectId = "";
				String objectName = "";
				
				for (QueryResult qResult : results) {
					objectId = qResult.getPropertyValueByQueryName("cmis:objectId");
					objectName = qResult.getPropertyValueByQueryName("cmis:name");

				}

				// CmisObject object = alfSession.getObject(new ObjectIdImpl(objectId));
				CmisObject object = alfSession.getObject(objectId);
				Folder folder = (Folder) object;
				ItemIterable<CmisObject> children = folder.getChildren();
				long totalNumberOfNodes = children.getTotalNumItems();

				System.out.println("***results from totalNumberOfNodes********* " + totalNumberOfNodes);
				System.out.println("***results from totalchildren********* " + children);

				System.out.println("***results from query " + queryString);
				System.out.println("***results from objectId******" + objectId);
				System.out.println("***results from objectName******" + objectName);
				// SELECT cm:userName, cm:homeFolder FROM cm:person where cm:userName like
				// 'smi%'
				Folder root = session.getRootFolder();
				System.out.println("***Root objectName******" + root);
				ItemIterable<CmisObject> childrens = root.getChildren();
				System.out.println("***childrenlist******" + root);
				ItemIterable<CmisObject> childrenlist = root.getChildren();
				
				for (CmisObject o : childrenlist) {
					  System.out.println("Name: " + o.getName());
					   System.out.println("Id: " + o.getId());
					   System.out.println("Type: " + (o.getType()));
				
					
								
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

				// List<Tree<FileableCmisObject>> getFolderTree(int depth);
				// String query = "SELECT * FROM cmis:folder F WHERE
				// CONTAINS(F,'PATH:'//app:company_home/app:dictionary/*'')";
				// ItemIterable<QueryResult> q = alfSession.query(query, false);
				// System.out.println("***results from query " + query);
				System.out.println("***results from queryResult " + results);

				int i = 1;
				for (QueryResult qr : results) {
					System.out.println("--------------------------------------------\n" + i + " , "
							+ qr.getPropertyByQueryName("cmis:objectTypeId").getFirstValue() + " , "
							+ qr.getPropertyByQueryName("cmis:name").getFirstValue() + " , "
							+ qr.getPropertyByQueryName("cmis:createdBy").getFirstValue() + " , "
							+ qr.getPropertyByQueryName("cmis:objectId").getFirstValue()); 
							//+ " , "
							//+ qr.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue() + " , "
							//+ qr.getPropertyByQueryName("cmis:contentStreamMimeType").getFirstValue() + " , "
							//+ qr.getPropertyByQueryName("cmis:contentStreamLength").getFirstValue());
					i++;
				}
			}
			 System.out.println("Printing repository capabilities...");
	         final RepositoryInfo repInfo = session.getRepositoryInfo();
	         RepositoryCapabilities cap = repInfo.getCapabilities();
	         System.out.println("\nNavigation Capabilities");
	         System.out.println("-----------------------");
	         System.out.println("Get descendants supported: " + (cap.isGetDescendantsSupported()?"true":"false"));
	         System.out.println("Get folder tree supported: " + (cap.isGetFolderTreeSupported()?"true":"false"));
	         System.out.println("\nObject Capabilities");
	         System.out.println("-----------------------");
	         System.out.println("Content Stream: " + cap.getContentStreamUpdatesCapability().value());
	         System.out.println("Changes: " + cap.getChangesCapability().value());
	         System.out.println("Renditions: " + cap.getRenditionsCapability().value()); 
	         System.out.println("\nFiling Capabilities");
	         System.out.println("-----------------------");        
	         System.out.println("Multifiling supported: " + (cap.isMultifilingSupported()?"true":"false"));
	         System.out.println("Unfiling supported: " + (cap.isUnfilingSupported()?"true":"false"));
	         System.out.println("Version specific filing supported: " + (cap.isVersionSpecificFilingSupported()?"true":"false"));
	         System.out.println("\nVersioning Capabilities");
	         System.out.println("-----------------------");        
	         System.out.println("PWC searchable: " + (cap.isPwcSearchableSupported()?"true":"false"));
	         System.out.println("PWC updatable: " + (cap.isPwcUpdatableSupported()?"true":"false"));
	         System.out.println("All versions searchable: " + (cap.isAllVersionsSearchableSupported()?"true":"false"));
	         System.out.println("\nQuery Capabilities");
	         System.out.println("-----------------------");        
	         System.out.println("Query: " + cap.getQueryCapability().value());
	         System.out.println("Join: " + cap.getJoinCapability().value());
	         System.out.println("\nACL Capabilities");
	         System.out.println("-----------------------");        
	         System.out.println("ACL: " + cap.getAclCapability().value()); 
	         System.out.println("End of  repository capabilities");
			System.exit(0);

		} catch (CmisInvalidArgumentException e) {
			System.out.println("Error Occured...");
			e.printStackTrace();
			System.err.println("caught an " + e.getClass().getName() + " exception with message " + e.getMessage());
		}

	}

	private static void printTree(Tree<FileableCmisObject> tree) {
		System.out.println("Descendant " + tree.getItem().getName());
		for (Tree<FileableCmisObject> t : tree.getChildren()) {
			printTree(t);
		}
	}

}
