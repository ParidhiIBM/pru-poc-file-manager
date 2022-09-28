package com.fileManager.fileManager.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fileManager.fileManager.services.FileService;
import com.fileManager.fileManager.util.FileDownloadUtil;

@CrossOrigin
@RestController
public class FileDownloadController {
    
	@Autowired
	FileDownloadUtil downloadUtil ;
	
	@Autowired
	private FileService fileService;
	
	@GetMapping("downloadFile/{fileCode}")
	public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String filecode) {
		Resource resource = null;
		try {
			resource = downloadUtil.getFileAsResource(filecode);
		}
		catch(IOException e){
			return  ResponseEntity.internalServerError().build();
		}
		if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
		String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource); 
	}
	
	@GetMapping("files/sampledoc/zip")
	public void downloadSampleDocsInZip(HttpServletResponse response) {
		fileService.downloadSampleDocsInZip(response);
	}
	
	@GetMapping("files/reviewer/{reviewerId}/employee/{employeeId}/zip")
	public ResponseEntity<byte[]> getFile(@PathVariable("reviewerId") String reviewerId,
			@PathVariable("employeeId") String employeeId, HttpServletResponse response) {

		String fileName = "Onboarding_" + employeeId + ".zip";
		byte[] bytes = fileService.downloadDocsForAssociate(response, reviewerId, employeeId, fileName);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.contentType(MediaType.valueOf("application/zip")).body(bytes);
	}
	
}
