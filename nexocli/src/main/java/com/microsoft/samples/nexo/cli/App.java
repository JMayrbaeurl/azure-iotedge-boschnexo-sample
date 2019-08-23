package com.microsoft.samples.nexo.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 */
@SpringBootApplication
public class App {

    public App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public NexoDeviceClient nexoDeviceClient() {

        return new NexoDeviceClient();
    }
}
