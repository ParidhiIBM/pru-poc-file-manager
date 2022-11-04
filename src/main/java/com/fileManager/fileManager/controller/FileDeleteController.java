package com.fileManager.fileManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fileManager.fileManager.services.FileService;

@CrossOrigin
@RestController
public class FileDeleteController {

	private final FileService fileService;

	@Autowired
	public FileDeleteController(FileService fileService) {
		this.fileService = fileService;
	}

	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@DeleteMapping("/files/delete/{id}")
	public ResponseEntity<String> removeOne(@PathVariable("id") String id) {
		try {
			fileService.remove(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(String.format("File deleted successfully."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(String.format("Could not delete the file."));
		}
	}

	// @Autowired
	// FileDeleteUtil deleteUtil ;

	// @CrossOrigin
	// @DeleteMapping("/delete/{fileName}")
	// public String deleteFile(@PathVariable("fileName") String fileName) {
	// 	String resp = null;
		
	// 		try {
	// 			resp = deleteUtil.removeFileFromPath(fileName);
	// 		} catch (IOException e) {
	// 			e.printStackTrace();
	// 		}
	// 	return resp;
        
	// }

}
