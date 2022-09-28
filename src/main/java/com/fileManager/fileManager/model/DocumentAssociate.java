package com.fileManager.fileManager.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "DOCUMENTS_ASSOCIATE")
@SQLDelete(sql = "UPDATE DOCUMENTS_ASSOCIATE SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class DocumentAssociate extends FileEntity {
	
	public DocumentAssociate(String name, String contentType, Long size, boolean deleted, String employeeId,
			byte[] data, DocumentTypeEntity documentType, Date lastUpdatedDate, String emplo) {
		super(name, contentType, size, deleted, employeeId, data, documentType, lastUpdatedDate);
	}

	public DocumentAssociate() {
		super();
	}

}
