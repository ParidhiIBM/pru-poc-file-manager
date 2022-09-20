package com.fileManager.fileManager.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	
	public List<DocumentTypeEntity> getAllDocumentTypesForAssociates() {
		List<DocumentTypeEntity> documentsList = documentRepository.findAll();
		return documentsList.stream().filter(obj -> !(obj.getName().equalsIgnoreCase("Sample Documents")))
				.collect(Collectors.toList());
	}
	
	public List<DocumentTypeEntity> getSampleDocumentTypes() {
		List<DocumentTypeEntity> documentsList = documentRepository.findAll();
		return documentsList.stream().filter(obj -> (obj.getName().equalsIgnoreCase("Sample Documents") || obj.getId()==1))
				.collect(Collectors.toList());
	}
	
	public DocumentTypeEntity getDocumentTypeByName(String name) {
		Optional<DocumentTypeEntity> option = documentRepository.findAllByName(name);
		if(option.isPresent()) {
			return option.get();
		}
		return null;
    }
	
	public DocumentTypeEntity getDocumentTypeById(Integer id) {
		Optional<DocumentTypeEntity> option = documentRepository.findById(id);
		if(option.isPresent()) {
			return option.get();
		}
		return null;
    }

}
