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

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
	public void lockDevice(@PathVariable String id) {

		logger.info("Locking Nexo device '" + id + "'");

		if (!this.serviceClient.deactivateTool(id))
			logger.error("Could not lock Nexo device '" + id + "'");
	}

	@RequestMapping(value = "/devices/{id}/unlock", method = RequestMethod.POST)
	public void unlockDevice(@PathVariable String id) {

		logger.info("Unlocking Nexo device '" + id + "'");

		if (!this.serviceClient.activateTool(id))
			logger.error("Could not unlock Nexo device '" + id + "'");
	}

	@RequestMapping(value = "/devices/{id}/latest", method = RequestMethod.GET)
	public String getLastestTighteningProcessInfo(@PathVariable String id) {

		logger.info("Get latest tightening process info file for '" + id);

		return this.serviceClient.readLatestTighteningProcessInfo(id);
	}

	@RequestMapping(value = "/devices/{id}/programs", method = RequestMethod.GET)
	@ResponseBody
	public String getPrograms(@PathVariable String id) {

		logger.info("Get programs for '" + id);

		return this.serviceClient.readPrograms(id);
	}

	@RequestMapping(value = "/devices/{id}/programs/{prognum}", method = RequestMethod.POST)
	public void selectProgram(@PathVariable String id, @PathVariable int programNum) {

		logger.info("Select program number '" + programNum + "' on device '" + id + "'");

		if (!this.serviceClient.selectProgramNumber(id, programNum))
			logger.error("Could not select program number '" + programNum + "' on device '" + id + "'");
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
				.paths(PathSelectors.any()).build();
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
