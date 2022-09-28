package com.fileManager.fileManager.request;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest implements Serializable {
	
	//private MultipartFile file;
	private String document_type;
	private String employeeId;
	private String role;
	private String reviewerId;
	
	public FileUploadRequest(String document_type, String employeeId, String role) {
		super();
		this.document_type = document_type;
		this.employeeId = employeeId;
		this.role = role;
	}

	public FileUploadRequest(String document_type, String employeeId, String role, String reviewerId) {
		super();
		this.document_type = document_type;
		this.employeeId = employeeId;
		this.role = role;
		this.reviewerId = reviewerId;
	}

	public FileUploadRequest() {
		super();
	}

	public String getDocument_type() {
		return document_type;
	}

	public void setDocument_type(String document_type) {
		this.document_type = document_type;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}
}
