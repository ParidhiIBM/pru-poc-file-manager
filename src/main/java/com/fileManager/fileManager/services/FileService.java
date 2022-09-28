package com.fileManager.fileManager.services;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fileManager.fileManager.model.DocumentAssociate;
import com.fileManager.fileManager.model.DocumentReviewer;
import com.fileManager.fileManager.model.DocumentTypeEntity;
import com.fileManager.fileManager.model.FileEntity;
import com.fileManager.fileManager.repositories.DocumentAssociateRepository;
import com.fileManager.fileManager.repositories.DocumentReviewerRepository;
import com.fileManager.fileManager.repositories.FileRepository;
import com.fileManager.fileManager.request.FileUploadRequest;
import com.fileManager.fileManager.response.DocumentTypeResponse;
import com.fileManager.fileManager.response.ResponseFile;
import com.fileManager.fileManager.util.FileDownloadUtil;

@Service
public class FileService {
	
	@Autowired
    private DocumentService documentService;
	
	@Autowired
	private FileRepository fileRepository;
    
	@Autowired
	FileDownloadUtil downloadUtil;

    @Autowired
    private DocumentAssociateRepository assoRepository;
    
    @Autowired
    private DocumentReviewerRepository reviewerRepository;
    
    @Transactional
    public void saveDocument(MultipartFile file, FileUploadRequest request) throws IOException {
    	if(request.getDocument_type().equals("0")) {
    		saveDocumentInAssociate(file, request); 
    	} else {
    		if(request.getRole().equalsIgnoreCase("ROLE_ASSOCIATE")) {
				saveDocumentInAssociate(file, request);
			} else {
				saveDocumentForAssociate(file, request);
			}
    	}
	}
    
    @Transactional
    public void saveDocumentInAssociate(MultipartFile file, FileUploadRequest request) throws IOException {
    	
    	DocumentAssociate fileEntity = null;
    	if(request.getDocument_type().equals("0")) {
    		fileEntity = getIfExistInAssociateTable(StringUtils.cleanPath(file.getOriginalFilename()), request.getEmployeeId(), request.getDocument_type()); 
    	} else {
    		fileEntity = getIfExistInAssociateTable(request.getEmployeeId(), request.getDocument_type());
    	}
    			
		if(fileEntity==null) {
			fileEntity = new DocumentAssociate();
			DocumentTypeEntity documentType = documentService.getDocumentTypeById(Integer.parseInt(request.getDocument_type()));
			if(documentType!=null) {
	        	fileEntity.setDocumentType(documentType);
	        }
		}
		
        fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());
        fileEntity.setEmployeeId(request.getEmployeeId());
        fileEntity.setLastUpdatedDate(new Date());

        assoRepository.save(fileEntity);
    }
    
    @Transactional
    public void saveDocumentForAssociate(MultipartFile file, FileUploadRequest request) throws IOException {
    	
    	DocumentReviewer fileEntity = null;
    	if(request.getDocument_type().equals("0")) {
    		fileEntity = getIfExistInReviewerTable(StringUtils.cleanPath(file.getOriginalFilename()), request.getEmployeeId(), request.getDocument_type()); 
    	} else {
    		fileEntity = getIfExistInReviewerTable(request.getEmployeeId(), request.getDocument_type());
    	}
    			
		if(fileEntity==null) {
			fileEntity = new DocumentReviewer();
			DocumentTypeEntity documentType = documentService.getDocumentTypeById(Integer.parseInt(request.getDocument_type()));
			if(documentType!=null) {
	        	fileEntity.setDocumentType(documentType);
	        }
		}
		
        fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());
        fileEntity.setEmployeeId(request.getEmployeeId());
        fileEntity.setReviewerId(request.getReviewerId());
        fileEntity.setLastUpdatedDate(new Date());

        reviewerRepository.save(fileEntity);
    }
    
    @Transactional
    public List<ResponseFile> getAllFilesByEmpId(String employeeId) {
        return assoRepository.findAllByEmployeeId(employeeId).stream()
				.sorted(Comparator.comparing(FileEntity::getDocumentType)
						.thenComparing(Comparator.comparing(FileEntity::getName)))
				.map(this::mapToFileResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public List<ResponseFile> getAllFilesByReviewerAndEmployee(String reviewerId, String employeeId) {
        return reviewerRepository.findAllByReviewerIdAndEmployeeId(reviewerId, employeeId).stream()
				.sorted(Comparator.comparing(FileEntity::getDocumentType)
						.thenComparing(Comparator.comparing(FileEntity::getName)))
				.map(this::mapToFileResponse).collect(Collectors.toList());
    }
    
    public List<ResponseFile> getAllFiles() {
        return assoRepository.findAll().stream().sorted(Comparator.comparing(FileEntity::getName))
				.map(this::mapToFileResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public List<ResponseFile> getAllSampleFiles() {
    	List<ResponseFile> files = getAllSampleFilesFromRepository().stream()
				.sorted(Comparator.comparing(DocumentAssociate::getDocumentType)
						.thenComparing(Comparator.comparing(DocumentAssociate::getName)))
				.map(this::mapToFileResponse).collect(Collectors.toList()); 
        return files;
    }
    
    @Transactional
    public List<DocumentAssociate> getAllSampleFilesFromRepository() {
    	DocumentTypeEntity documentType = documentService.getDocumentTypeByName("Sample Documents");
    	List<DocumentAssociate> files = assoRepository.findAllByDocumentType(documentType); 
        return files;
    }
    
	public Optional<FileEntity> getFile(String id) {
		return fileRepository.findById(id);
	}

	public void remove(String id) {
		fileRepository.deleteById(id);
	}
    
    @Transactional
    public void updatedReviewedStatus(String reviewerId, String employeeId) {
		reviewerRepository.updateDocumentReviewerByReviewerIdAndEmployeeId(true, reviewerId, employeeId);
	}
    
	@Transactional
	public void downloadSampleDocsInZip(HttpServletResponse response) {
		List<DocumentAssociate> fileNames = getAllSampleFilesFromRepository();

		System.out.println("############# file size ###########" + fileNames.size());

		downloadUtil.downloadInZip(response, fileNames, "SampleDocuments");
	}
    
    @Transactional
    private DocumentAssociate getIfExistInAssociateTable(String fileName, String employeeId, String documentTypeId) {
    	DocumentTypeEntity documentType = new DocumentTypeEntity();
    	documentType.setId(Integer.parseInt(documentTypeId));
    	Optional<DocumentAssociate> favorites = assoRepository.findAllByNameAndEmployeeIdAndDocumentType(fileName, employeeId, documentType).stream().findAny();
		if(favorites.isPresent()) {
			return favorites.get();
		}
		return null;
	}
    
    @Transactional
    private DocumentAssociate getIfExistInAssociateTable(String employeeId, String documentTypeId) {
    	DocumentTypeEntity documentType = new DocumentTypeEntity();
    	documentType.setId(Integer.parseInt(documentTypeId));
    	Optional<DocumentAssociate> favorites = assoRepository.findAllByEmployeeIdAndDocumentType(employeeId, documentType).stream().findAny();
		if(favorites.isPresent()) {
			return favorites.get();
		}
		return null;
	}
    
    @Transactional
    private DocumentReviewer getIfExistInReviewerTable(String fileName, String employeeId, String documentTypeId) {
    	DocumentTypeEntity documentType = new DocumentTypeEntity();
    	documentType.setId(Integer.parseInt(documentTypeId));
    	Optional<DocumentReviewer> favorites = reviewerRepository.findAllByNameAndEmployeeIdAndDocumentType(fileName, employeeId, documentType).stream().findAny();
		if(favorites.isPresent()) {
			return favorites.get();
		}
		return null;
	}
    
    @Transactional
    private DocumentReviewer getIfExistInReviewerTable(String employeeId, String documentTypeId) {
    	DocumentTypeEntity documentType = new DocumentTypeEntity();
    	documentType.setId(Integer.parseInt(documentTypeId));
    	Optional<DocumentReviewer> favorites = reviewerRepository.findAllByEmployeeIdAndDocumentType(employeeId, documentType).stream().findAny();
		if(favorites.isPresent()) {
			return favorites.get();
		}
		return null;
	}
    
    private ResponseFile mapToFileResponse(FileEntity fileEntity) {
		String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
				.path(fileEntity.getId()).toUriString();
		ResponseFile fileResponse = new ResponseFile();
		fileResponse.setId(fileEntity.getId());
		fileResponse.setName(fileEntity.getName());
		fileResponse.setContentType(fileEntity.getContentType());
		fileResponse.setSize(fileEntity.getSize());
		fileResponse.setUrl(downloadURL);
		fileResponse.setDeleted(fileEntity.isDeleted());
		fileResponse.setEmployeeId(fileEntity.getEmployeeId());
		
		ModelMapper modelMapper = new ModelMapper();
		DocumentTypeResponse documentTypeResponse = modelMapper.map(fileEntity.getDocumentType(),
				DocumentTypeResponse.class);
		fileResponse.setDocumentType(documentTypeResponse);
		return fileResponse;
	}
    
    private ResponseFile mapToFileResponse(DocumentAssociate fileEntity) {
		String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
				.path(fileEntity.getId()).toUriString();
		ResponseFile fileResponse = new ResponseFile();
		fileResponse.setId(fileEntity.getId());
		fileResponse.setName(fileEntity.getName());
		fileResponse.setContentType(fileEntity.getContentType());
		fileResponse.setSize(fileEntity.getSize());
		fileResponse.setUrl(downloadURL);
		fileResponse.setDeleted(fileEntity.isDeleted());
		fileResponse.setEmployeeId(fileEntity.getEmployeeId());
		
		ModelMapper modelMapper = new ModelMapper();
		DocumentTypeResponse documentTypeResponse = modelMapper.map(fileEntity.getDocumentType(),
				DocumentTypeResponse.class);
		fileResponse.setDocumentType(documentTypeResponse);
		return fileResponse;
	}
    
    private ResponseFile mapToFileResponse(DocumentReviewer fileEntity) {
		String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
				.path(fileEntity.getId()).toUriString();
		ResponseFile fileResponse = new ResponseFile();
		fileResponse.setId(fileEntity.getId());
		fileResponse.setName(fileEntity.getName());
		fileResponse.setContentType(fileEntity.getContentType());
		fileResponse.setSize(fileEntity.getSize());
		fileResponse.setUrl(downloadURL);
		fileResponse.setDeleted(fileEntity.isDeleted());
		fileResponse.setEmployeeId(fileEntity.getEmployeeId());
		fileResponse.setReviewerId(fileEntity.getReviewerId());
		fileResponse.setReviewed(fileEntity.isReviewed());
		
		ModelMapper modelMapper = new ModelMapper();
		DocumentTypeResponse documentTypeResponse = modelMapper.map(fileEntity.getDocumentType(),
				DocumentTypeResponse.class);
		fileResponse.setDocumentType(documentTypeResponse);
		return fileResponse;
	}
    
    @Transactional
	public List<DocumentAssociate> downloadDocsForAssociate(HttpServletResponse response, String reviewerId, String employeeId) {
		List<DocumentAssociate> fileNames = assoRepository.findAllByEmployeeId(employeeId).stream()
				.sorted(Comparator.comparing(FileEntity::getDocumentType)
						.thenComparing(Comparator.comparing(FileEntity::getName)))
				.collect(Collectors.toList());

		return fileNames;
	}

    @Transactional
	public byte[] downloadDocsForAssociate(HttpServletResponse response, String reviewerId, String employeeId,
			String fileName) {
		List<DocumentAssociate> fileNames = downloadDocsForAssociate(response, reviewerId, employeeId);
		byte[] bytes = downloadUtil.downloadDocsInZip(response, fileNames, fileName);
		return bytes;
	}

}