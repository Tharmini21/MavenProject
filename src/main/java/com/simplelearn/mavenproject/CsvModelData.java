package com.simplelearn.mavenproject;


public class CsvModelData {
	

	    private String objectIdid;
	    private String objectname;
	    private String createdBy;
	    private String contentStreamFileName;
	    private String contentStreamMimeType;
	    private String contentStreamLength;
	   
	    public CsvModelData(String id, String name, String createdBy, String fileName, String mimetype,String length) {
	        this.objectIdid = id;
	        this.objectname = name;
	        this.createdBy = createdBy;
	        this.contentStreamFileName = fileName;
	        this.contentStreamMimeType = mimetype;
	        this.contentStreamLength = length;
	    }
	    
}
