package com.fileManager.fileManager.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.fileManager.fileManager.model.DocumentAssociate;
import com.fileManager.fileManager.model.FileEntity;

@Service
public class FileDownloadUtil {
	private Path foundFile;
	public Resource getFileAsResource(String filecode) throws IOException {
		 
		 
		 Path dirPath = Paths.get("Files-Upload");
		  
	        Files.list(dirPath).forEach(file -> {
	            if (file.getFileName().toString().startsWith(filecode)) {
	            	foundFile = file;
	                return ;
	            }
	        });
	 
	        if (foundFile != null) {
	            return new UrlResource(foundFile.toUri());
	        }
	         
	        return null;
	}
	
	public void downloadInZip(HttpServletResponse response, List<DocumentAssociate> fileNames, String filename) {
		response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".zip");
		
		try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
            for (FileEntity file : fileNames) {
                ByteArrayResource resource = new ByteArrayResource(file.getData());
                

                ZipEntry e = new ZipEntry(file.getName());
                // Configure the zip entry, the properties of the file
                e.setSize(resource.contentLength());
                e.setTime(System.currentTimeMillis());
                // etc.
                zippedOut.putNextEntry(e);
                // And the content of the resource:
                StreamUtils.copy(resource.getInputStream(), zippedOut);
                zippedOut.closeEntry();
            }
            zippedOut.finish();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}

	public byte[] downloadDocsInZip(HttpServletResponse response, List<DocumentAssociate> fileNames, String fileName) {
		byte[] bytes = null;
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		
		try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
			for (FileEntity file : fileNames) {
                ByteArrayResource resource = new ByteArrayResource(file.getData());

                ZipEntry e = new ZipEntry(file.getName());
                // Configure the zip entry, the properties of the file
                e.setSize(resource.contentLength());
                e.setTime(System.currentTimeMillis());
                // etc.
                zippedOut.putNextEntry(e);
                // And the content of the resource:
                StreamUtils.copy(resource.getInputStream(), zippedOut);
                zippedOut.closeEntry();
            }
            bytes = zippedOut.toString().getBytes();
            zippedOut.finish();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
		return bytes;
	}

	
}
