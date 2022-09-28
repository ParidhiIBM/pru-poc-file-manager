package com.fileManager.fileManager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fileManager.fileManager.model.DocumentAssociate;
import com.fileManager.fileManager.model.DocumentTypeEntity;

public interface DocumentAssociateRepository extends JpaRepository<DocumentAssociate, String> {
	
	public List<DocumentAssociate> findAllByName(final String name);
	
	public List<DocumentAssociate> findAllByNameAndEmployeeIdAndDocumentType(final String name, final String employeeId, final DocumentTypeEntity documentType);
	
	public List<DocumentAssociate> findAllByEmployeeIdAndDocumentType(final String employeeId, final DocumentTypeEntity documentType);
	
	public List<DocumentAssociate> findAllByDocumentType(final DocumentTypeEntity documentType);
	
	public List<DocumentAssociate> findAllByEmployeeId(final String employeeId);
	
}
