package com.microsoft.samples.nexo.uploader.nexofileuploader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class FileUploaderTests {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private FileUploader uploader;

	private MockRestServiceServer mockServer;

	private String uploadURL = "http://localhost:8080/publish";

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void init() {
		this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
	}

	@Test
	public void singleFileUpload() throws URISyntaxException, IOException {
		
		Assert.notNull(this.restTemplate, "Did not load");

		mockServer.expect(ExpectedCount.once(), requestTo(new URI(this.uploadURL)))
		.andExpect(method(HttpMethod.POST))
		.andRespond(withStatus(HttpStatus.OK)
		.contentType(MediaType.APPLICATION_JSON));

		String content = "{ \"Test\" : \"A value\"}";
		File createdFile = temporaryFolder.newFile("test.txt");
		Files.copy(new ByteArrayInputStream(content.getBytes("UTF8")), createdFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		// FileUploader call
		Assert.isTrue(uploader.uploadFile(uploadURL, createdFile.getAbsolutePath()), "Upload failed");
		Assert.isTrue(!createdFile.exists(), "Didn't delete file after upload");

		mockServer.verify();
	}

	@Test
	public void multipleFileseUpload() throws URISyntaxException, IOException {
		
		Assert.notNull(this.restTemplate, "Did not load");

		mockServer.expect(ExpectedCount.times(3), requestTo(new URI(this.uploadURL)))
		.andExpect(method(HttpMethod.POST))
		.andRespond(withStatus(HttpStatus.OK)
		.contentType(MediaType.APPLICATION_JSON));

		String content = "{ \"Test\" : \"A value\"}";
		File folder = temporaryFolder.newFolder("test");
		Files.copy(new ByteArrayInputStream(content.getBytes("UTF8")), Paths.get(folder.toPath().toString(), "first.txt"));
		Files.copy(new ByteArrayInputStream(content.getBytes("UTF8")), Paths.get(folder.toPath().toString(), "second.txt"));
		Files.copy(new ByteArrayInputStream(content.getBytes("UTF8")), Paths.get(folder.toPath().toString(), "third.txt"));

		// FileUploader call
		List<String> uploads = uploader.uploadAllFilesInFolder(uploadURL, folder.getAbsolutePath());
		Assert.isTrue(uploads != null && uploads.size() == 3, "Upload failed");
		mockServer.verify();
	}

}