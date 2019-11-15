package com.microsoft.samples.nexo.services.nexofleetmgmtsvc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Api(value = "Bosch Rexroth Nexo Fleet management service", description = "Sample implementation of Nexo fleet management operations")
@EnableSwagger2
@SpringBootApplication
@RestController
public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);

	@Value("${nexoservice_protocol:AMQPS}")
	private String protocol;

	@Value("${nexoservice_connectionString:}")
	private String connectionString;

	@Value("${nexoservice_archiveConString:}")
	private String archiveConnectionString;

	@Value("${nexoservice_archiveContainername:archive}")
	private String archiveContainername;

	@Autowired
	private NexoServiceClient serviceClient;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@ApiOperation(value = "Send Bosch Rexroth Open protocol command to nexo device", response = String.class, consumes = "text/plain")
	@RequestMapping(value = "/devices/{id}/rop", method = RequestMethod.POST, consumes = { "text/plain" })
	@ResponseBody
	public String callNexo(@ApiParam(value="IoT Hub device id of nexo device", required = true) @PathVariable String id, 
		@ApiParam(value="Bosch Rexroth Open protocol command", required = true, example="00200001001000000000 ") @RequestBody String ropcommand) {

		logger.info("Raw message call to device '" + id + "' and message '" + ropcommand + "'");

		return this.serviceClient.callNexo(id, ropcommand);
	}

	@ApiOperation(value="Returns all registered Nexo devices (classified with tags)", response = String.class)
	@RequestMapping(value = "/devices", method = RequestMethod.GET)
	@ResponseBody
	public String getNexoDevices() {

		logger.info("Get all Nexo devices from registry");

		return this.serviceClient.getNexoDevices();
	}


	@ApiOperation(value = "Returns the lock state of the specified nexo device", response = String.class)
	@RequestMapping(value = "/devices/{id}/lockstate", method = RequestMethod.GET)
	@ResponseBody
	public String getNexoDeviceLockState(@ApiParam(value="IoT Hub device id of nexo device", required = true) @PathVariable String id) {

		logger.info("Get Nexo device '" + id + "' lock state");

		return this.serviceClient.getLockStateOfNexo(id);
	}

	@ApiOperation(value = "Locks the specified nexo device")
	@RequestMapping(value = "/devices/{id}/lock", method = RequestMethod.POST)
	public void lockDevice(@ApiParam(value="IoT Hub device id of nexo device", required = true) @PathVariable String id) {

		logger.info("Locking Nexo device '" + id + "'");

		if (!this.serviceClient.deactivateTool(id))
			logger.error("Could not lock Nexo device '" + id + "'");
	}

	@ApiOperation(value = "Unlocks the specified nexo device")
	@RequestMapping(value = "/devices/{id}/unlock", method = RequestMethod.POST)
	public void unlockDevice(@ApiParam(value="IoT Hub device id of nexo device", required = true) @PathVariable String id) {

		logger.info("Unlocking Nexo device '" + id + "'");

		if (!this.serviceClient.activateTool(id))
			logger.error("Could not unlock Nexo device '" + id + "'");
	}

	@ApiOperation(value = "Returns the last tightening results of the specified nexo device")
	@RequestMapping(value = "/devices/{id}/latest", method = RequestMethod.GET)
	public String getLastestTighteningProcessInfo(@ApiParam(value="IoT Hub device id of nexo device", required = true) @PathVariable String id) {

		logger.info("Get latest tightening process info file for '" + id);

		return this.serviceClient.readLatestTighteningProcessInfo(id);
	}

	@ApiOperation(value = "Returns the list of program numbers of the nexo device")
	@RequestMapping(value = "/devices/{id}/programs", method = RequestMethod.GET)
	@ResponseBody
	public String getPrograms(@ApiParam(value="IoT Hub device id of nexo device", required = true) @PathVariable String id) {

		logger.info("Get programs for '" + id);

		return this.serviceClient.readPrograms(id);
	}

	@ApiOperation(value = "Selects the specified program number on the nexo device")
	@RequestMapping(value = "/devices/{id}/programs/{prognum:[\\d]+}", method = RequestMethod.POST)
	public void selectProgram(@ApiParam(value="IoT Hub device id of nexo device", required = true) @PathVariable String id, 
		@ApiParam(value="Program number", required = true) @PathVariable int prognum) {

		logger.info("Select program number '" + prognum + "' on device '" + id + "'");

		if (!this.serviceClient.selectProgramNumber(id, prognum))
			logger.error("Could not select program number '" + prognum + "' on device '" + id + "'");
	}

	@Bean
	public NexoServiceClient createServiceClient() throws IOException {

		logger.info("Creating service client for IoT Hub connection string '" + this.connectionString
				+ "' and archive at '" + this.archiveConnectionString + "'");

		NexoServiceClient result = new NexoServiceClient(this.protocol, this.connectionString,
				this.archiveConnectionString, this.archiveContainername);
		result.openConnection();

		return result;
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.microsoft.samples.nexo.services.nexofleetmgmtsvc"))
				.paths(PathSelectors.any()).build().apiInfo(apiEndPointsInfo());
	}

	private ApiInfo apiEndPointsInfo() {

        return new ApiInfoBuilder().title("Bosch Rexroth Nexo Fleet management service API")
            .description("Sample implementation of the Nexo Fleet management REST API")
            .contact(new Contact("Jürgen Mayrbäurl", "www.microsoft.com", "jurgenma@microsoft.com"))
            .license("MIT")
            .licenseUrl("https://github.com/JMayrbaeurl/azure-iotedge-boschnexo-sample/blob/master/LICENSE")
            .version("1.0.0")
            .build();
    }

	@Bean
	public WebSecurityConfigurerAdapter webSecurityConfigurer() {
		return new WebSecurityConfigurerAdapter() {

			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http.csrf().disable().authorizeRequests().anyRequest().authenticated()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().httpBasic();
			}

			
			@Override
			protected void configure(AuthenticationManagerBuilder auth) throws Exception {
				auth.inMemoryAuthentication().withUser("Bosch")
				.password(pwdEncoder().encode("Robert")).authorities("ROLE_USER");
			}
		};
	}

	@Bean
	public PasswordEncoder pwdEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
	}
}
