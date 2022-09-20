package com.fileManager.fileManager.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DOCUMENT_TYPE")
//@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class DocumentTypeEntity implements Comparable<DocumentTypeEntity>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public DocumentTypeEntity(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentTypeEntity other = (DocumentTypeEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
	@Override
	public int compareTo(DocumentTypeEntity obj) {
		if (getName() == null || obj.getName() == null) {
			return 0;
		}
		return getName().compareTo(obj.getName());
	}

	@Override
	public String toString() {
		return "DocumentTypeEntity [id=" + id + ", name=" + name + "]";
	}
}
