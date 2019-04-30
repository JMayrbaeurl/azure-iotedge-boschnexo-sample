package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

public class FileUploader implements FileUploadMethods {

    private final static Logger logger = LoggerFactory.getLogger(FileUploader.class);

    @Autowired
    private RestTemplate restTemplate;

    public boolean uploadFile(final String uploadURL, final String filepath, boolean deleteFilesAfterUpload) throws IOException {

        Assert.hasText(uploadURL, "Parameter uploadURL must not be empty");

        Assert.hasText(filepath, "Parameter filepath must not be empty");
        File file = new File(filepath);
        Assert.isTrue(file.exists(), "File '" + filepath + "' doesn't exist");

        logger.debug("Reading contents of file '" + filepath + "'");

        // read file contents
        String content;
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        channel.lock();
        try {
            long fileSize = channel.size();
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            channel.read(buffer);
            buffer.flip();
            content = new String(buffer.array());
        } finally {
            channel.close(); randomAccessFile.close();
        }
                
        logger.debug("Now uploading file contents to " + uploadURL);
        // send request and parse result
        ResponseEntity<String> response = this.restTemplate.exchange(uploadURL, HttpMethod.POST, 
            this.createHttpEntity(content), String.class);

        logger.debug("Upload responded with " + response.toString());

        boolean result = response.getStatusCode() == HttpStatus.OK;
        if (result && deleteFilesAfterUpload) {
            file.delete();
        }

        return result;
    }

    public List<String> uploadAllFilesInFolder(final String uploadURL, final String folderPath, boolean deleteFilesAfterUpload) throws IOException {

        Assert.hasText(uploadURL, "Parameter uploadURL must not be empty");

        Assert.hasText(folderPath, "Parameter folderPath must not be empty");
        File dir = new File(folderPath);
        Assert.isTrue(dir.exists(), "Directory '" + folderPath + "' doesn't exist");
        Assert.isTrue(dir.isDirectory(), "Directory '" + folderPath + "' is not a directory");

        logger.debug("Uploading all files from " + folderPath);

        List<String> result = new ArrayList<String>();

        for (final File fileEntry : dir.listFiles()) {
            if (!fileEntry.isDirectory()) {
                if (this.uploadFile(uploadURL, fileEntry.getAbsolutePath(), deleteFilesAfterUpload))
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