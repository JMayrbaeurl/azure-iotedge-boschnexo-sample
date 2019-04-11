package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class UploadCommands {

    @ShellMethod("Uploads a file to IoT Hub using the Nexo Publisher")
    public String upload(String url, String filepath) throws IOException {

        // read file contents
        String content = new String(Files.readAllBytes(Paths.get(filepath)));
        
        RestTemplate template = new RestTemplate();

         // set headers
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_JSON);
         HttpEntity<String> entity = new HttpEntity<String>(content, headers);

        // send request and parse result
        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, entity, String.class);

        return response.toString();
    }
}