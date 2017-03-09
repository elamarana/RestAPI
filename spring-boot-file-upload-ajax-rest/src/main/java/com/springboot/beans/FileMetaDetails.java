package com.springboot.beans;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import com.springboot.services.FileDetailService;

public class FileMetaDetails implements FileDetailService {

	@Override
	public String getFileMetaDetails(String fileName) {
		BasicFileAttributes attr = null;
		String attrStr = "No Files";
		Path path = Paths.get("D:\\Files\\", fileName);
		try {
			attr = Files.readAttributes(path, BasicFileAttributes.class);
			if(attr != null){
				attrStr = printFileDetails(attr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return attrStr;
	}
	
	public String printFileDetails(BasicFileAttributes attr){
		StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlBuilder.append("<FileMetaDetails>");
		xmlBuilder.append("<CreationTime>").append("<Value>" + attr.creationTime() + "</Value>");
		xmlBuilder.append("</CreationTime>");
		   xmlBuilder.append("<LastAccessTime>").append("<Value>" + attr.lastAccessTime() + "</Value>");
		   xmlBuilder.append("</CreationTime>");
		   xmlBuilder.append("<LastModifiedTime>").append("<Value>" + attr.lastModifiedTime() + "</Value>");
		   xmlBuilder.append("</LastModifiedTime>");
		   xmlBuilder.append("<IsDirectory>").append("<Value>" + attr.isDirectory() + "</Value>");
		   xmlBuilder.append("</IsDirectory>");
		   xmlBuilder.append("<IsOther>").append("<Value>" + attr.isOther() + "</Value>");
		   xmlBuilder.append("</IsOther>");
		   xmlBuilder.append("<IsRegularFile>").append("<Value>" + attr.isRegularFile() + "</Value>");
		   xmlBuilder.append("</IsRegularFile>");
		   xmlBuilder.append("<IsSymbolicLink>").append("<Value>" + attr.isSymbolicLink() + "</Value>");
		   xmlBuilder.append("</IsSymbolicLink>");
		   xmlBuilder.append("<Size>").append("<Value>" + attr.size() + "</Value>");
		   xmlBuilder.append("</Size>");
		   xmlBuilder.append("</FileMetaDetails>");
		   return xmlBuilder.toString();
	}

}
