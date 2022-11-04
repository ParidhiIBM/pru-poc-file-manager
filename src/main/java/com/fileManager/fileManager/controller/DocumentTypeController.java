package com.fileManager.fileManager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fileManager.fileManager.model.DocumentTypeEntity;
import com.fileManager.fileManager.response.DocumentTypeResponse;
import com.fileManager.fileManager.services.DocumentService;

@CrossOrigin
@RestController
@RequestMapping("document")
public class DocumentTypeController {
	
	@Autowired
	private DocumentService documentService;
	
	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@GetMapping
	public List<DocumentTypeResponse> getAllDocumentTypesForAssociates() {
		List<DocumentTypeEntity> documentTypeEntities = new ArrayList<>();
		documentTypeEntities = documentService.getAllDocumentTypesForAssociates();

		ModelMapper modelMapper = new ModelMapper();
		List<DocumentTypeResponse> resultList = documentTypeEntities.stream()
				.map(obj -> modelMapper.map(obj, DocumentTypeResponse.class)).collect(Collectors.toList());
		return resultList;
	}
	
	@PreAuthorize("hasAnyRole({'ROLE_ONBOARDING_REVIEWER','ROLE_ONBOARDING_MANAGER'})")
	@GetMapping("/sample")
    public List<DocumentTypeResponse> getSampleDocumentTypes() {
		List<DocumentTypeEntity> documentTypeEntities =  documentService.getSampleDocumentTypes();
		
        ModelMapper modelMapper = new ModelMapper(); 
        List<DocumentTypeResponse> resultList = documentTypeEntities.stream().map(obj -> modelMapper.map(obj, DocumentTypeResponse.class))
                .collect(Collectors.toList());
        return resultList;
    }

}
