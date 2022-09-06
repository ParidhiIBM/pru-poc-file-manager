package com.fileManager.fileManager.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fileManager.fileManager.model.DocumentTypeEntity;
import com.fileManager.fileManager.model.FileEntity;
import com.fileManager.fileManager.repositories.FileRepository;
import com.fileManager.fileManager.util.FileDownloadUtil;

@Service
public class FileService {

    private final FileRepository fileRepository;
    
    @Autowired
    private DocumentService documentService;
    
    @Autowired
	FileDownloadUtil downloadUtil;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    
    @Transactional
    public void save(MultipartFile file) throws IOException {
    	
    	FileEntity fileEntity = new FileEntity();
		
        fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());

        fileRepository.save(fileEntity);
    }
    
    @Transactional
    public void saveDocument(MultipartFile file, String documentTypeId, String employeeId) throws IOException {
    	
    	FileEntity fileEntity = getIfFileExist(employeeId, documentTypeId);
		if(fileEntity==null) {
			fileEntity = new FileEntity();
		}
		
		DocumentTypeEntity documentType = documentService.getDocumentTypeById(Integer.parseInt(documentTypeId));
		
        fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());
        if(documentType!=null) {
        	fileEntity.setDocumentType(documentType);
        }
        fileEntity.setEmployeeId(employeeId);
        fileEntity.setLastUpdatedDate(new Date());

        fileRepository.save(fileEntity);
    }

    public Optional<FileEntity> getFile(String id) {
        return fileRepository.findById(id);
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }
    
    @Transactional
    public List<FileEntity> getAllFilesByEmpId(String employeeId) {
        return fileRepository.findAllByEmployeeId(employeeId);
    }
    
    @Transactional
    public List<FileEntity> getAllSampleFiles() {
    	DocumentTypeEntity documentType = documentService.getDocumentTypeByName("Sample Documents");
    	List<FileEntity> files = fileRepository.findAllByDocumentType(documentType); 
        return files;
    }

    public void remove(String id){
        fileRepository.deleteById(id);
    }
    
    private FileEntity getIfFileExist(String employeeId, String documentTypeId) {
    	DocumentTypeEntity documentType = new DocumentTypeEntity();
    	documentType.setId(Integer.parseInt(documentTypeId));
    	Optional<FileEntity> favorites = fileRepository.findAllByEmployeeIdAndDocumentType(employeeId, documentType).stream().findAny();
		if(favorites.isPresent()) {
			return favorites.get();
		}
		return null;
	}
    
    @Transactional
    public void downloadSampleDocsInZip(HttpServletResponse response) {
        List<FileEntity> fileNames = getAllSampleFiles();

        System.out.println("############# file size ###########" + fileNames.size());

       downloadUtil.downloadInZip(response, fileNames, "SampleDocuments");
	}

}