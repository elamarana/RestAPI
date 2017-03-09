package com.springboot.controller;

import com.springboot.beans.FileMetaDetails;
import com.springboot.model.UploadModel;
import com.springboot.services.FileDetailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


@RestController
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "D://Files//";

    //Single file upload
    @PostMapping("/api/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {

        logger.debug("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    // Multiple file upload
    @PostMapping("/api/upload/multi")
    public ResponseEntity<?> uploadFileMulti(
            @RequestParam("extraField") String extraField,
            @RequestParam("files") MultipartFile[] uploadfiles) {

        logger.debug("Multiple file upload!");

        String uploadedFileName = null; 
        // Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
        // .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));*/
       
        for(MultipartFile mpf : uploadfiles){
        	if(!mpf.isEmpty()){
        		uploadedFileName = mpf.getName();	
        	}
        }
        

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfiles));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFileName, HttpStatus.OK);

    }

    // maps html form to a Model
    @PostMapping("/api/upload/multi/model")
    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadModel model) {

        logger.debug("Multiple file upload! With UploadModel");

        try {

            saveUploadedFiles(Arrays.asList(model.getFiles()));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);

    }
    
    
	@RequestMapping(value = "/api/fileDetails/{fileName}", method = RequestMethod.GET)
	public ResponseEntity<FileMetaDetails> getEmployee(@PathVariable("fileName") String fileName) {
		
		File folder = new File(UPLOADED_FOLDER);
		File[] listOfFiles = folder.listFiles();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	String fName = listOfFiles[i].getName(); 
		    	if(fName.contains(fileName)){
		    		fileName = fName;
		    		break;
		    	}
		        logger.debug("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        logger.debug("Directory " + listOfFiles[i].getName());
		      }
		    }
		FileDetailService fService  = new FileMetaDetails();
		String metaDetails = fService.getFileMetaDetails(fileName);
		ResponseEntity respEntity = null;
		if (metaDetails == null) {
			logger.debug("FileName  " + fileName + " does not exists");
			respEntity = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}else{
			logger.debug("Found File Details :: " + metaDetails);
			respEntity =  new ResponseEntity<String>(metaDetails, HttpStatus.OK);
		}
		
		return respEntity;
	}

    //save file
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

        }

    }
}
