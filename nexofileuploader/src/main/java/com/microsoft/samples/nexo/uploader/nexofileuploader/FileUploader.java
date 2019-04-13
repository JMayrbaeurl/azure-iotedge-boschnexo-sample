package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

public class FileUploader {

    private final static Logger logger = LoggerFactory.getLogger(FileUploader.class);

    private String uploadURL;

    private boolean deleteFilesAfterUpload = true;

    @Autowired
    private RestTemplate restTemplate;

    public FileUploader(String url) {
        this.uploadURL = url;

        Assert.hasText(url, "Upload URL must not be empty");
    }

    public boolean uploadFile(final String filepath) throws IOException {

        Assert.hasText(filepath, "Parameter filepath must not be empty");
        File file = new File(filepath);
        Assert.isTrue(file.exists(), "File '" + filepath + "' doesn't exist");

        logger.debug("Reading contents of file '" + filepath + "'");

        // read file contents
        String content = new String(Files.readAllBytes(Paths.get(filepath)));
                
        logger.debug("Now uploading file contents to " + this.uploadURL);
        // send request and parse result
        ResponseEntity<String> response = this.restTemplate.exchange(this.uploadURL, HttpMethod.POST, 
            this.createHttpEntity(content), String.class);

        logger.debug("Upload responded with " + response.toString());

        boolean result = response.getStatusCode() == HttpStatus.OK;
        if (result && this.deleteFilesAfterUpload) {
            file.delete();
        }

        return result;
    }

    public List<String> uploadAllFilesInFolder(final String folderPath) throws IOException {

        Assert.hasText(folderPath, "Parameter folderPath must not be empty");
        File dir = new File(folderPath);
        Assert.isTrue(dir.exists(), "Directory '" + folderPath + "' doesn't exist");
        Assert.isTrue(dir.isDirectory(), "Directory '" + folderPath + "' is not a directory");

        List<String> result = new ArrayList<String>();

        for (final File fileEntry : dir.listFiles()) {
            if (!fileEntry.isDirectory()) {
                if (this.uploadFile(fileEntry.getAbsolutePath()))
                    result.add(fileEntry.getAbsolutePath());
            }
        }

        return result;
    }

    private HttpEntity<String> createHttpEntity(String forcontents) {

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(forcontents, headers);

        return entity;
    }

    /**
     * @return the uploadURL
     */
    public String getUploadURL() {
        return uploadURL;
    }

    /**
     * @param uploadURL the uploadURL to set
     */
    public void setUploadURL(String uploadURL) {
        this.uploadURL = uploadURL;
    }

    /**
     * @return the deleteFilesAfterUpload
     */
    public boolean isDeleteFilesAfterUpload() {
        return deleteFilesAfterUpload;
    }

    /**
     * @param deleteFilesAfterUpload the deleteFilesAfterUpload to set
     */
    public void setDeleteFilesAfterUpload(boolean deleteFilesAfterUpload) {
        this.deleteFilesAfterUpload = deleteFilesAfterUpload;
    }

    /**
     * @return the restTemplate
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * @param restTemplate the restTemplate to set
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
}