package com.simplelearn.mavenproject;

import java.util.HashMap;
import java.util.Map;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.simplelearn.mavenproject.AlfrescoUtilityImpl;
//import com.simplelearn.mavenproject.*;
public class CMISQuery {
	//private static final AlfrescoUtilityImpl Helper = new AlfrescoUtilityImpl();
	//private static final AlfrescoUtility alfrescoHelper = new AlfrescoUtility();
	 //private static final Logger LOGGER = LoggerFactory.getLogger(CMISQuery.class);
	public static void main(String[] args) {
		try {
			 CmisClient cmisClient = new CmisClient();
			 String connectionName = "martinAlf01";
			 Session session = cmisClient.getSession(connectionName, "admin", "admin");
			cmisClient.listTopFolder(session);
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
				//String query = "SELECT * FROM cmis:sites WHERE cmis:name LIKE 'Files%'";
				//String query = "SELECT * FROM cmis:document WHERE cmis:name LIKE 'test%'";
				String query = "SELECT cm:userName, cm:homeFolder FROM cm:person";
				//SELECT cm:userName, cm:homeFolder FROM cm:person where cm:userName like 'smi%'

				ItemIterable<QueryResult> q = alfSession.query(query, false);

	            System.out.println("***results from query " + query);
	            System.out.println("***results from queryResult " + q);
	           
	            
			}
						
			System.exit(0);        
			
    	} catch (Exception e) {
    		System.out.println("Error Occured...");
    		e.printStackTrace();
    	}
			
	 }
	
}
