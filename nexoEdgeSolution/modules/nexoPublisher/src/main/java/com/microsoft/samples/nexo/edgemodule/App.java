package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.microsoft.azure.sdk.iot.device.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
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

            Message message = new Message(pBody);
            message.setProperty("source", "nexopublisher");
            try {
                this.destination.sendEventAsync(message);
            } catch (IOException e) {
               logger.warn("Exception sending message to " + this.destination.destinationname() +
                ". Message: " + e.getMessage());
            }
        } else {
            logger.debug("Nothing to publish. Message is empty");
        }
    }
}
