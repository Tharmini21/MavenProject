package com.simplelearn.mavenproject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;

public class CmisClient {
	 private static Map<String, Session> connections = new ConcurrentHashMap<String, Session>();
	    public CmisClient() { }
	    
	    public Session getSession(String connectionName, String username, String pwd) {
	    	Session session = connections.get(connectionName);

	    	if (session == null) {
	    		System.out.println("Not connected, creating new connection to" +
	    				" Alfresco with the connection id (" + connectionName +	")");
	    			
	    		// No connection to Alfresco available, create a new one
	    		SessionFactory sessionFactory =	SessionFactoryImpl.newInstance();
	    		Map<String, String> parameters = new HashMap<String, String>();
	    		parameters.put(SessionParameter.USER, username);
	    		parameters.put(SessionParameter.PASSWORD, pwd);
	    		parameters.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/alfresco/api/-default-/cmis/versions/1.1/atom");
	    		parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
	    		parameters.put(SessionParameter.COMPRESSION, "true");
	    		parameters.put(SessionParameter.CACHE_TTL_OBJECTS, "0");
	    			
	    		// If there is only one repository exposed (e.g. Alfresco),
	    		// these lines will help detect it and its ID
	    		List<Repository> repositories = sessionFactory.getRepositories(parameters);
	    		Repository alfrescoRepository = null;
	    		if (repositories != null && repositories.size() > 0) {
	    			System.out.println("Found (" + repositories.size() + ") Alfresco repositories");
	    			alfrescoRepository = repositories.get(0);
	    			System.out.println("Info about the first Alfresco repo [ID=" +
	    					alfrescoRepository.getId() + "][name=" +
	    					alfrescoRepository.getName() + "][CMIS ver supported=" +
	    					alfrescoRepository.getCmisVersionSupported() + "]");
	    		} else {
	    			throw new CmisConnectionException("Could not connect to the Alfresco Server, " +
	    							"no repository found!");
	    		}
	    			
	    		// Create a new session with the Alfresco repository
	    		session = alfrescoRepository.createSession();
	    			
	    		// Save connection for reuse
	    		connections.put(connectionName, session);
	    	} else {
	    		System.out.println("Already connected to Alfresco with the " +
	    				"connection id (" + connectionName + ")");
	    	}
	    		
	    	return session;
	    }
	    
	    public void listTopFolder(Session session) {
	    	Folder root = session.getRootFolder();
	    	ItemIterable<CmisObject> contentItems= root.getChildren();
	    	for (CmisObject contentItem : contentItems) {
	    		if (contentItem instanceof Document) {
	    			Document docMetadata = (Document)contentItem;
	    			ContentStream docContent = docMetadata.getContentStream();
	    				
	    			System.out.println(docMetadata.getName() + " [size=" +
	    					docContent.getLength()+"][Mimetype=" +
	    					docContent.getMimeType()+"][type=" +
	    					docMetadata.getType().getDisplayName()+"]");
	    		} else {
	    			System.out.println(contentItem.getName() + "[type="+contentItem.getType().getDisplayName()+"]");
	    		}
	    	}
	    }


}
