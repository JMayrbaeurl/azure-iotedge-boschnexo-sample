package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileUploaderTask implements Runnable {

    private final FileUploader fileUploader;
    private String folder;
    private final AtomicBoolean doProcess;

    public FileUploaderTask(FileUploader uploader, String folderPath, AtomicBoolean process) {

        this.fileUploader = uploader;
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
            while (this.doProcess.get() && ((key = watchService.poll(2, TimeUnit.SECONDS)) != null)) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                }
                key.reset();
            }
        } catch (Exception ex) {

        }
    }
}