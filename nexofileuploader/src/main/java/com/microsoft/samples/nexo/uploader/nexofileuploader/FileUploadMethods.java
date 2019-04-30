package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.io.IOException;
import java.util.List;

import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;

/**
 * FileUploadMethods
 */
public interface FileUploadMethods {

    @Retryable(value = { IOException.class }, maxAttempts = 3, backoff = @Backoff(delay = 200))
    public boolean uploadFile(final String uploadURL, final String filepath, boolean deleteFilesAfterUpload) throws IOException;

    public List<String> uploadAllFilesInFolder(final String uploadURL, final String folderPath, boolean deleteFilesAfterUpload) throws IOException;
}