package com.fileManager.fileManager.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fileManager.fileManager.model.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
}
