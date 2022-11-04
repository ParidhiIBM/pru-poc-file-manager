package com.fileManager.fileManager.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fileManager.fileManager.model.FileEntity;
import com.fileManager.fileManager.request.FileUploadRequest;
import com.fileManager.fileManager.response.ResponseFile;
import com.fileManager.fileManager.services.FileService;

@CrossOrigin
@RestController
@RequestMapping("files")
public class FileUploadController {

	
	@Autowired
	private FileService fileService;
	
	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@PostMapping()
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("data") String data) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		FileUploadRequest request = mapper.readValue(data, FileUploadRequest.class);
        
		try {
			fileService.saveDocument(file, request);
			return ResponseEntity.status(HttpStatus.OK)
					.body(String.format("File uploaded successfully: %s", file.getOriginalFilename()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(String.format("Could not upload the file: %s!", file.getOriginalFilename()));
		}
	}
	
	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@GetMapping
	public List<ResponseFile> list() {
		return fileService.getAllFiles();
	}

	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@GetMapping("/sampledoc")
	public List<ResponseFile> getSampleDocuments() {
		return fileService.getAllSampleFiles();
	}

	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@GetMapping("/employee/{employeeId}")
	public List<ResponseFile> listByEmployeeId(@PathVariable("employeeId") String employeeId) {
		return fileService.getAllFilesByEmpId(employeeId);
	}
	
	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@GetMapping("/reviewer/{reviewerId}/employee/{employeeId}")
	public List<ResponseFile> listByReviewerIdAndEmployeeId(@PathVariable("reviewerId") String reviewerId, @PathVariable("employeeId") String employeeId) {
		return fileService.getAllFilesByReviewerAndEmployee(reviewerId, employeeId);
	}
	
	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@PutMapping("/reviewer/{reviewerId}/employee/{employeeId}")
	public void updatedReviewedStatus(@PathVariable("reviewerId") String reviewerId, @PathVariable("employeeId") String employeeId) {
		fileService.updatedReviewedStatus(reviewerId, employeeId);
	}
	
	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@GetMapping("{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		Optional<FileEntity> fileEntityOptional = fileService.getFile(id);

		if (!fileEntityOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		FileEntity fileEntity = fileEntityOptional.get();
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getName() + "\"")
				.contentType(MediaType.valueOf(fileEntity.getContentType())).body(fileEntity.getData());
	}
	
}