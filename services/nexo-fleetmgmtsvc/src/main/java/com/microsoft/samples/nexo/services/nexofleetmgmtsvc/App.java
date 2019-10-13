package com.microsoft.samples.nexo.services.nexofleetmgmtsvc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);

	@Value("${nexoservice_protocol:AMQPS}")
	private String protocol;

	@Value("${nexoservice_connectionString:}")
	private String connectionString;

	@Autowired
	private NexoServiceClient serviceClient;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@RequestMapping(value = "/devices/{id}/rop", method = RequestMethod.POST, consumes = { "text/plain" })
	@ResponseBody
	public String callNexo(@PathVariable String id, @RequestBody String pBody) {

		logger.info("Raw message call to device '" + id + "' and message '" + pBody + "'");

		return this.serviceClient.callNexo(id, pBody);
	}

	@RequestMapping(value = "/devices", method = RequestMethod.GET)
	@ResponseBody
	public String getNexoDevices() {

		logger.info("Get all Nexo devices from registry");

		return this.serviceClient.getNexoDevices();
	}

	@RequestMapping(value = "/devices/{id}/lock", method = RequestMethod.POST)
	public void lockDevice(@PathVariable String id)
	{

	}

	
	@RequestMapping(value = "/devices/{id}/unlock", method = RequestMethod.POST)
	public void unlockDevice(@PathVariable String id)
	{

	}

	@Bean
	public NexoServiceClient createServiceClient() throws IOException {

		NexoServiceClient result = new NexoServiceClient(this.protocol, this.connectionString);
		result.openConnection();

		return result;
	}
}
