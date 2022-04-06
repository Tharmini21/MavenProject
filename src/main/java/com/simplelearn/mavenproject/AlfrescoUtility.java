package com.simplelearn.mavenproject;

import java.util.Map;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;

public interface AlfrescoUtility {

	public Session createSession(String username, String password, String host);

	public Folder createFolder(Session alfSession, Folder objFolder, String folderName);
	
	public Folder createFolder(Session alfSession);

	}
