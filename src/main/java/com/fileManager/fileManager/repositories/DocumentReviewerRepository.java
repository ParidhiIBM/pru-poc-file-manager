package com.fileManager.fileManager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.fileManager.fileManager.model.DocumentReviewer;
import com.fileManager.fileManager.model.DocumentTypeEntity;

public interface DocumentReviewerRepository extends JpaRepository<DocumentReviewer, String> {
	
	public List<DocumentReviewer> findAllByName(final String name);
	
	public List<DocumentReviewer> findAllByNameAndEmployeeIdAndDocumentType(final String name, final String employeeId, final DocumentTypeEntity documentType);
	
	public List<DocumentReviewer> findAllByEmployeeIdAndDocumentType(final String employeeId, final DocumentTypeEntity documentType);
	
	public List<DocumentReviewer> findAllByDocumentType(final DocumentTypeEntity documentType);
	
	public List<DocumentReviewer> findAllByEmployeeId(final String employeeId);
	
	public List<DocumentReviewer> findAllByReviewerIdAndEmployeeId(final String reviewerId, final String employeeId);
	
	@Modifying
	@Query("update DocumentReviewer d set d.isReviewed = ?1 where d.reviewerId = ?2 AND d.employeeId = ?3")
	public void updateDocumentReviewerByReviewerIdAndEmployeeId(boolean isReviewed, String reviewerId, final String employeeId);
	
}
