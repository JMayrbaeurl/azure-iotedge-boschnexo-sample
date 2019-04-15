package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUploaderTask implements Runnable {

    private final FileUploader fileUploader;
    private String uploadURL;
    private String folder;
    private final AtomicBoolean doProcess;

    private static final Logger logger = LoggerFactory.getLogger(FileUploaderTask.class);

    public FileUploaderTask(FileUploader uploader, String url, String folderPath, AtomicBoolean process) {

        this.fileUploader = uploader;
        this.uploadURL = url;
        this.folder = folderPath;
        this.doProcess = process;
    }

    @Override
    public void run() {

        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();

            Path path = Paths.get(this.folder);
    
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    
            WatchKey key;
            while (this.doProcess.get()) {
                key = watchService.poll(2, TimeUnit.SECONDS);
                if (key != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        logger.debug("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                        String filename = ((Path)event.context()).toString();
                        try {
                            this.fileUploader.uploadFile(this.uploadURL, this.folder + File.separatorChar + filename, true);
                        } catch (IOException ioEx) {
                            logger.error("Exception on upload of file '"+ filename + "' from watch folder " + this.folder + ". " + ioEx.getMessage());
                        }
                    }
                    key.reset();
                }
            }
        } catch (Exception ex) {
            logger.error("Exception watching folder for new files. " + ex.getMessage());
        }
    }
}