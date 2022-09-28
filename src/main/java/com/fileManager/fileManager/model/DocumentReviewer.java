package com.fileManager.fileManager.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "DOCUMENTS_REVIEWER")
@SQLDelete(sql = "UPDATE DOCUMENTS_REVIEWER SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class DocumentReviewer extends FileEntity {
	
	private String reviewerId;
	private boolean isReviewed = Boolean.FALSE;

	public DocumentReviewer() {
		super();
	}

	public DocumentReviewer(String name, String contentType, Long size, boolean deleted, String employeeId, byte[] data,
			DocumentTypeEntity documentType, Date lastUpdatedDate, String reviewerId, boolean isReviewed) {
		super(name, contentType, size, deleted, employeeId, data, documentType, lastUpdatedDate);
		this.reviewerId = reviewerId;
		this.isReviewed = isReviewed;
	}

	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public boolean isReviewed() {
		return isReviewed;
	}

	public void setReviewed(boolean isReviewed) {
		this.isReviewed = isReviewed;
	}
	
}
