package com.microsoft.samples.nexo.edgemodule;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.nexo.process.TighteningProcess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * ProcessTranslatorTest
 */
@RunWith(SpringRunner.class)
public class ProcessTranslatorTest {

    @Test
    public void testStreamTo() throws IOException, ParseException {
        
        File resource = new ClassPathResource("Fullprocess.json").getFile();
        Assert.notNull(resource, "Could not load json file");

        ObjectMapper objectMapper = new ObjectMapper();
        TighteningProcess processInfo = objectMapper.readValue(resource, TighteningProcess.class);
        
        ProcessTranslator translator = new ProcessTranslator(new NullPublishingDestination());
        translator.streamProcessInfoToDestination(processInfo, new MessageFactory());
    }
}