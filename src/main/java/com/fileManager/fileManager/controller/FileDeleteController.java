package com.fileManager.fileManager.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fileManager.fileManager.util.FileDeleteUtil;

@CrossOrigin
@RestController
public class FileDeleteController {
    
	@Autowired
	FileDeleteUtil deleteUtil ;
	@GetMapping("delete/{fileName}")
	public String deleteFile(@PathVariable("fileName") String fileName) {
		String resp = null;
		
			try {
				resp = deleteUtil.removeFileFromPath(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return resp;
        
	}

}
