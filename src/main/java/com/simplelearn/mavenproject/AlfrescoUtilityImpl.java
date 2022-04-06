package com.simplelearn.mavenproject;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.bindings.CmisBindingFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.spi.CmisBinding;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class AlfrescoUtilityImpl implements AlfrescoUtility {
	// private static final Logger LOGGER = LoggerFactory.getLogger(AlfrescoUtilityImpl.class);
	 
		@Override
		public Session createSession(String username, String password, String host) {
			try {

				// default factory implementation
			    SessionFactory factory = SessionFactoryImpl.newInstance();
			    Map<String, String> parameter = new HashMap<String, String>();
			    parameter.put(SessionParameter.USER, username);
			    parameter.put(SessionParameter.PASSWORD, password);
			    parameter.put(SessionParameter.ATOMPUB_URL, host+ "/alfresco/api/-default-/public/cmis/versions/1.1/atom");
			    parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
			    parameter.put(SessionParameter.REPOSITORY_ID, "-default-");
			    //Session session = factory.createSession(parameter);
			    Session session = factory.createSession(parameter);
			    CmisBindingFactory factorycmis = CmisBindingFactory.newInstance();
			    CmisBinding binding = factorycmis.createCmisAtomPubBinding(parameter);
			    return factory.getRepositories(parameter).get(0).createSession();
			    
			} catch (org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException ce) {
				System.out.println(ce);
			   // LOGGER.error("CMIS Excpetion:", ce);
			}
			
			return null;

		}
		public void Duplicatefilelist(Session alfSession) {
			String query = "SELECT * FROM cmis:sites WHERE cmis:name LIKE 'Files%'";
			ItemIterable<QueryResult> q = alfSession.query(query, false);

            System.out.println("***results from query " + query);

            int i = 1;
            for (QueryResult qr : q) {
                System.out.println("--------------------------------------------\n" + i + " , "
                        + qr.getPropertyByQueryName("cmis:objectTypeId").getFirstValue() + " , "
                        + qr.getPropertyByQueryName("cmis:name").getFirstValue() + " , "
                        + qr.getPropertyByQueryName("cmis:createdBy").getFirstValue() + " , "
                        + qr.getPropertyByQueryName("cmis:objectId").getFirstValue() + " , "
                        + qr.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue() + " , "
                        + qr.getPropertyByQueryName("cmis:contentStreamMimeType").getFirstValue() + " , "
                        + qr.getPropertyByQueryName("cmis:contentStreamLength").getFirstValue());
                i++;
            }
		}
		public void listTopFolder(Session alfSession) {
			Folder root = alfSession.getRootFolder();
			ItemIterable<CmisObject> contentItems= root.getChildren();
			for (CmisObject contentItem : contentItems) {
				if (contentItem instanceof Document) {
					Document docMetadata = (Document)contentItem;
					ContentStream docContent = docMetadata.getContentStream();
						
					System.out.println(docMetadata.getName() + " [size=" + docContent.getLength()+"][Mimetype=" + docContent.getMimeType()+"][type=" +
							docMetadata.getType().getDisplayName()+"]");
				} else {
					System.out.println(contentItem.getName() + "[type="+contentItem.getType().getDisplayName()+"]");
				}
			}
		}

		public Folder createFolder(Session alfSession) {
			String folderName = "OpenCMISTest";
			Folder parentFolder = alfSession.getRootFolder();
			
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			properties.put(PropertyIds.NAME, folderName);
			//LOGGER.info("Creating Folder:" + parentFolder.getPath());
			
			// create the folder
			return parentFolder.createFolder(properties);
			
		}
		@Override
		public Folder createFolder(Session alfSession, Folder objFolder, String folderName) {
			// TODO Auto-generated method stub
			return null;
		}
}
