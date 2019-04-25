package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.text.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.samples.nexo.process.TighteningProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@RestController
public class App implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    @Autowired
    PublishingDestination destination;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Successfully started Nexo publisher app");
    }

    @RequestMapping("/")
    public String home() {
        return "Hello IoT Edge World";
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST, consumes = { "application/JSON" })
    public void publishToHub(@RequestBody String pBody) {

        Assert.notNull(this.destination, "Property destination must not be null. Check hub configuration");

        if (pBody != null && pBody.length() > 0) {
            logger.info("Publishing to " + this.destination.destinationname());
            logger.debug("Sending message: " + pBody);

            TighteningProcess processInfo = this.readTighteningProcessFromBody(pBody);
            Message message = this.createMessage(processInfo, pBody);
            try {
                this.destination.sendEventAsync(message);
            } catch (IOException e) {
                logger.warn("Exception sending message to " + this.destination.destinationname() + ". Message: "
                        + e.getMessage());
            }
        } else {
            logger.debug("Nothing to publish. Message is empty");
        }
    }

    @RequestMapping(value = "/forward", method = RequestMethod.POST, consumes = { "application/JSON" })
    public void simplyForwardToHub(@RequestBody String pBody) {

        Assert.notNull(this.destination, "Property destination must not be null. Check hub configuration");

        if (pBody != null && pBody.length() > 0) {
            logger.info("Forwarding to " + this.destination.destinationname());
            logger.debug("Sending message: " + pBody);

            Message message = new Message(pBody);
            message.setProperty("source", "nexopublisher");
            message.setProperty("messagetype", "any");
            try {
                this.destination.sendEventAsync(message);
            } catch (IOException e) {
                logger.warn("Exception sending message to " + this.destination.destinationname() + ". Message: "
                        + e.getMessage());
            }
        } else {
            logger.debug("Nothing to forward. Message is empty");
        }
    }

    @RequestMapping(value = "/stream", method = RequestMethod.POST, consumes = { "application/JSON" })
    public void streamIoTHub(@RequestBody String pBody) {

        Assert.notNull(this.destination, "Property destination must not be null. Check hub configuration");

        if (pBody != null && pBody.length() > 0) {
            logger.info("Streaming to " + this.destination.destinationname());
            logger.debug("Sending message contents of: " + pBody);

            TighteningProcess processInfo = this.readTighteningProcessFromBody(pBody);
            ProcessTranslator translator = new ProcessTranslator(this.destination);
            try {
                translator.streamProcessInfoToDestination(processInfo);
            } catch (ParseException e) {
                logger.error("Exception streaming graph entries of message to " + this.destination.destinationname() + ". Message: "
                    + e.getMessage());
            }
        } else {
            logger.debug("Nothing to stream. Message is empty");
        } 
    }

    private TighteningProcess readTighteningProcessFromBody(String pBody) {

        ObjectMapper objectMapper = new ObjectMapper();
        TighteningProcess process = null;
        try {
            process = objectMapper.readValue(pBody, TighteningProcess.class);
        } catch (IOException e) {
            logger.error("Exception on parsing message. " + e.getMessage());
        }

        return process;
    }

    private Message createMessage(final TighteningProcess process, final String jsonString) {

        Message message = new Message(jsonString);
        message.setProperty("source", "nexopublisher");
        message.setProperty("messagetype", "process");

        if (process != null) {
            message.setProperty("nr", Integer.toString(process.getNr()));
            message.setProperty("result", process.getResult());
            //message.setProperty("channel", process.getChannel());
            message.setProperty("prg_nr", Integer.toString(process.getPrgnr()));
            message.setProperty("prg_name", process.getPrgname());
            //message.setProperty("prg_date", process.getPrgdate());
            message.setProperty("cycle", Integer.toString(process.getCycle()));
            //message.setProperty("nominal torque", Double.toString(process.getNominaltorque()));
            //message.setProperty("date", process.getDate());
            message.setProperty("id_code", process.getIdcode());
            message.setProperty("job_nr", Integer.toString(process.getJobnr()));
        }

        return message;
    }

    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.microsoft.samples.nexo.edgemodule"))              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
}
