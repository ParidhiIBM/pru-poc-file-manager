package com.fileManager.fileManager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping
    public List<DocumentTypeResponse> list() {
        List<DocumentTypeEntity> documentTypeEntities =  documentService.getAllDocumentTypes();
        ModelMapper modelMapper = new ModelMapper(); 
        List<DocumentTypeResponse> resultList = documentTypeEntities.stream().map(obj -> modelMapper.map(obj, DocumentTypeResponse.class))
                .collect(Collectors.toList());
        return resultList;
    }

}
