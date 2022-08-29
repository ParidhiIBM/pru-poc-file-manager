package com.fileManager.fileManager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fileManager.fileManager.model.DocumentTypeEntity;
import com.fileManager.fileManager.repositories.DocumentTypeRepository;

@Service
public class DocumentService {
	
	@Autowired
	private DocumentTypeRepository documentRepository;
	
	public List<DocumentTypeEntity> getAllDocumentTypes() {
        return documentRepository.findAll();
    }
	
	public DocumentTypeEntity getDocumentTypeById(Integer id) {
		Optional<DocumentTypeEntity> option = documentRepository.findById(id);
		if(option.isPresent()) {
			return option.get();
		}
		return null;
    }

}
