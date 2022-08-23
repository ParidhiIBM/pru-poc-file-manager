package com.fileManager.fileManager.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.springframework.stereotype.Service;

@Service
public class FileDeleteUtil {
	private Path foundFile;
	public String removeFileFromPath(String fileName) throws IOException  {
		 
		foundFile = null;
		Path dirPath = Paths.get("Files-Upload");
		  
	        Files.list(dirPath).forEach(file -> {
				String fullFileName = file.getFileName().toString();
				String docName = fullFileName.substring(fullFileName.indexOf('-')+1);
	            if (docName.equals(fileName)) {
	            	 
					try {
	            		foundFile = file;
						Files.delete(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
	                return ;
	            }
	        });
	 
	        if (foundFile != null) {
	            return "Delete successful";
	        }
	        return "File not found";
	}

}
