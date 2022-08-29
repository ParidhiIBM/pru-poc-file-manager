package com.fileManager.fileManager.response;

public class ResponseFile {

    private String id;
    private String name;
    private Long size;
    private String url;
    private String contentType;
	private boolean deleted;
	private DocumentTypeResponse documentType;

    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

	public DocumentTypeResponse getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentTypeResponse documentType) {
		this.documentType = documentType;
	}
        
}