package com.microsoft.samples.nexo.process;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * JsonTest
 */
@RunWith(SpringRunner.class)
public class JsonTest {

    @Test
    public void testReadProcessFromFile() throws IOException {

        File resource = new ClassPathResource("Cycletest.json").getFile();
        Assert.notNull(resource, "Could not load json file");

        ObjectMapper objectMapper = new ObjectMapper();
        TighteningProcess process = objectMapper.readValue(resource, TighteningProcess.class);
        Assert.notNull(process, "Could not deserialize tightening process from json file");
    }

    @Test
    public void testReadGraphFromFile() throws IOException {

        File resource = new ClassPathResource("Graphtest.json").getFile();
        Assert.notNull(resource, "Could not load json file");

        ObjectMapper objectMapper = new ObjectMapper();
        Graph graph = objectMapper.readValue(resource, Graph.class);
        Assert.notNull(graph, "Could not deserialize graph from json file");
    }

    @Test
    public void testReadStepFromFile() throws IOException {

        File resource = new ClassPathResource("Steptest.json").getFile();
        Assert.notNull(resource, "Could not load json file");

        ObjectMapper objectMapper = new ObjectMapper();
        TighteningStep step = objectMapper.readValue(resource, TighteningStep.class);
        Assert.notNull(step, "Could not deserialize tightening step from json file");
    }

    @Test
    public void testReadFullProcessFromFile() throws IOException {

        File resource = new ClassPathResource("Fullprocess.json").getFile();
        Assert.notNull(resource, "Could not load json file");

        ObjectMapper objectMapper = new ObjectMapper();
        TighteningProcess process = objectMapper.readValue(resource, TighteningProcess.class);
        Assert.notNull(process, "Could not deserialize tightening process from json file");
    }
}