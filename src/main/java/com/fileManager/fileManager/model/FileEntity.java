package com.fileManager.fileManager.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILES")
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
//@Builder
@NoArgsConstructor
@SQLDelete(sql = "UPDATE FILES SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class FileEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String contentType;

    private Long size;

    private boolean deleted = Boolean.FALSE;
    
	private String employeeId; 

    @Lob
    private byte[] data;
    
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "document_type", referencedColumnName = "id")
    private DocumentTypeEntity documentType;
    
    private Date lastUpdatedDate;
    
	public FileEntity(String name, String contentType, Long size, boolean deleted, String employeeId, byte[] data,
			DocumentTypeEntity documentType, Date lastUpdatedDate) {
		super();
		this.name = name;
		this.contentType = contentType;
		this.size = size;
		this.deleted = deleted;
		this.employeeId = employeeId;
		this.data = data;
		this.documentType = documentType;
		this.lastUpdatedDate = lastUpdatedDate;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

	public DocumentTypeEntity getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentTypeEntity documentType) {
		this.documentType = documentType;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public String toString() {
		return "FileEntity [id=" + id + ", name=" + name + ", deleted="
				+ deleted /* + ", employeeId=" + employeeId */
				+ ", documentType=" + documentType + ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}

}