package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UploadCommands {

    private ExecutorService executorService;
    private AtomicBoolean doUpload = new AtomicBoolean(false);

    @Autowired
    private FileUploader uploader;

    @ShellMethod("Uploads a file to IoT Hub using the Nexo Publisher")
    public String upload(String url, String filepath) throws IOException {

         return uploader.uploadFile(url, filepath, false) ? "DONE" : "ERROR";
    }

    @ShellMethod("Upload all files from a folder to IoT Hub using the Nexo Publisher")
    public String uploadAll(String url, String folderpath) throws IOException {

         return uploader.uploadAllFilesInFolder(url, folderpath, false) != null ? "DONE" : "ERROR";
    }

    @ShellMethod("Starts uploading files from folder")
    public void startUpload(String url, String folderPath) throws IOException {

        if (this.executorService == null)
            this.executorService = Executors.newSingleThreadExecutor();

        uploader.uploadAllFilesInFolder(url, folderPath, true);
        this.doUpload.set(true);

        this.executorService.submit(new FileUploaderTask(uploader, url, folderPath, this.doUpload));
    }

    @ShellMethod("Stops uploading files from folder")
    public void stopUpload() {

        this.doUpload.set(false);
        this.executorService.shutdown();
    }
}