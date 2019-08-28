package com.microsoft.samples.nexo.edgemodule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * AppConfigurationManager
 */
@Component
public class AppConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(AppConfigurationManager.class);

    @Value("${nexopublisher_config_dirpath:}")
    private String configDirPath;

    @Value("${nexopublisher_config_provConfigFilename:nexoIoTHubConfiguration.json}")
    private String provConfigFilename;

    private File configDir;

    @PostConstruct
    public void finishCreation() {
        
        this.createConfigDirIfNotExists();
    }

    private void createConfigDirIfNotExists() {

        if (this.configDirPath == null || this.configDirPath.length() == 0) {

            this.configDirPath = System.getProperty("user.home") + File.separator + "nexo";
        }
        
        this.configDir = new File(this.configDirPath);
        if (!this.configDir.exists())
            if (!this.configDir.mkdirs())
                throw new IllegalStateException("Cannot create non-existing configuration directory: " + this.configDirPath);
    }

    public ProvisioningConfiguration readProvisioningConfiguration() {

        ProvisioningConfiguration result = null;

        File provConfigurationFile = this.createProvisioningConfigFile();
        if (provConfigurationFile.exists()) {

            try {
                byte[] jsonData = Files.readAllBytes(provConfigurationFile.toPath());

                ObjectMapper objectMapper = new ObjectMapper();
            
                //convert json string to object
                result = objectMapper.readValue(jsonData, ProvisioningConfiguration.class);
            } catch (IOException ex) {
                logger.error("Could not read Nexo IoT Hub configuration from file. " + ex.getMessage() );
            }
        }

        return result;
    }

    public boolean updateProvisioningConfiguration(final ProvisioningConfiguration config) { 

        boolean result = false;

        Assert.notNull(config, "Parameter config must not be null");

        File provConfigurationFile = this.createProvisioningConfigFile();

        try {
            JsonFactory jsonFactory = new JsonFactory(); 
            FileOutputStream file = new FileOutputStream(provConfigurationFile);
            JsonGenerator jsonGen = jsonFactory.createGenerator(file, JsonEncoding.UTF8);
        
            jsonGen.setCodec(new ObjectMapper());
            jsonGen.setPrettyPrinter(new DefaultPrettyPrinter());
            jsonGen.writeObject(config);

            result = true;
        } catch (IOException ex) {
            logger.error("Could not update Nexo IoT Hub configuration in file. " + ex.getMessage());
        }

        return result;
    }

    private File createProvisioningConfigFile() {

        return new File(this.configDir, this.provConfigFilename);
    }

    public String getConfigDirPath() {
        return configDirPath;
    }

    public void setConfigDirPath(String configDirPath) {
        this.configDirPath = configDirPath;
    }

    public File getConfigDir() {
        return configDir;
    }

    public void setConfigDir(File configDir) {
        this.configDir = configDir;
    }

    public String getProvConfigFilename() {
        return provConfigFilename;
    }

    public void setProvConfigFilename(String provConfigFilename) {
        this.provConfigFilename = provConfigFilename;
    }
}