package com.fileManager.fileManager.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fileManager.fileManager.model.DocumentTypeEntity;
import com.fileManager.fileManager.model.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
	
	public List<FileEntity> findAllByName(final String name);
	
	public List<FileEntity> findAllByNameAndEmployeeIdAndDocumentType(final String name, final String employeeId, final DocumentTypeEntity documentType);
	
	public List<FileEntity> findAllByEmployeeIdAndDocumentType(final String employeeId, final DocumentTypeEntity documentType);
	
	public List<FileEntity> findAllByDocumentType(final DocumentTypeEntity documentType);
	
	public List<FileEntity> findAllByEmployeeId(final String employeeId);
	
}
