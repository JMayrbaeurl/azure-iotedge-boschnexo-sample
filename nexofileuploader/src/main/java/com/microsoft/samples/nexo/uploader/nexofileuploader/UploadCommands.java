package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UploadCommands {

    private ExecutorService executorService;
    private AtomicBoolean doUpload;

    @ShellMethod("Uploads a file to IoT Hub using the Nexo Publisher")
    public String upload(String url, String filepath) throws IOException {

        FileUploader uploader = new FileUploader(url);
        return uploader.uploadFile(filepath) ? "DONE" : "ERROR";
    }

    @ShellMethod("Starts uploading files from folder")
    public void startUpload(String url, String folderPath) throws IOException {

        if (this.executorService == null)
            this.executorService = Executors.newSingleThreadExecutor();

        FileUploader uploader = new FileUploader(url);
        uploader.uploadAllFilesInFolder(folderPath);
        this.doUpload.set(true);

        this.executorService.submit(new FileUploaderTask(uploader, folderPath, this.doUpload));
    }

    @ShellMethod("Stops uploading files from folder")
    public void stopUpload() {

        this.doUpload.set(false);
    }
}