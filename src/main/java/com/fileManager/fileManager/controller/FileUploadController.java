package com.fileManager.fileManager.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fileManager.fileManager.model.FileEntity;
import com.fileManager.fileManager.response.DocumentTypeResponse;
import com.fileManager.fileManager.response.ResponseFile;
import com.fileManager.fileManager.services.FileService;

@CrossOrigin
@RestController
@RequestMapping("files")
public class FileUploadController {

	private final FileService fileService;

	@Autowired
	public FileUploadController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
			@RequestParam("document_type") String document_type, @RequestParam("employeeId") String employeeId) {
		try {
			fileService.saveDocument(file, document_type, employeeId);

			return ResponseEntity.status(HttpStatus.OK)
					.body(String.format("File uploaded successfully: %s", file.getOriginalFilename()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(String.format("Could not upload the file: %s!", file.getOriginalFilename()));
		}
	}

	@GetMapping
	public List<ResponseFile> list() {
		return fileService.getAllFiles().stream().sorted(Comparator.comparing(FileEntity::getName))
				.map(this::mapToFileResponse).collect(Collectors.toList());
	}

	@GetMapping("/sampledoc")
	public List<ResponseFile> getSampleDocuments() {
		return fileService.getAllSampleFiles().stream()
				.sorted(Comparator.comparing(FileEntity::getDocumentType)
						.thenComparing(Comparator.comparing(FileEntity::getName)))
				.map(this::mapToFileResponse).collect(Collectors.toList());
	}

	@GetMapping("/employee/{employeeId}")
	public List<ResponseFile> listByEmployeeId(@PathVariable("employeeId") String employeeId) {
		return fileService.getAllFilesByEmpId(employeeId).stream()
				.sorted(Comparator.comparing(FileEntity::getDocumentType)
						.thenComparing(Comparator.comparing(FileEntity::getName)))
				.map(this::mapToFileResponse).collect(Collectors.toList());
	}

	private ResponseFile mapToFileResponse(FileEntity fileEntity) {
		String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
				.path(fileEntity.getId()).toUriString();
		ResponseFile fileResponse = new ResponseFile();
		fileResponse.setId(fileEntity.getId());
		fileResponse.setName(fileEntity.getName());
		fileResponse.setContentType(fileEntity.getContentType());
		fileResponse.setSize(fileEntity.getSize());
		fileResponse.setUrl(downloadURL);
		fileResponse.setDeleted(fileEntity.isDeleted());
		ModelMapper modelMapper = new ModelMapper();
		DocumentTypeResponse documentTypeResponse = modelMapper.map(fileEntity.getDocumentType(),
				DocumentTypeResponse.class);
		fileResponse.setDocumentType(documentTypeResponse);
		return fileResponse;
	}

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