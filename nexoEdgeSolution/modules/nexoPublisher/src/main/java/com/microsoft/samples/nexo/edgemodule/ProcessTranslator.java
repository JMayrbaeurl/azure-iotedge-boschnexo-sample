package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.samples.nexo.process.TighteningProcess;
import com.microsoft.samples.nexo.process.TighteningStep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProcessTranslator
 */
public class ProcessTranslator {

    private PublishingDestination destination;

    private static Logger logger = LoggerFactory.getLogger(ProcessTranslator.class);

    public ProcessTranslator(final PublishingDestination dest) {
        this.destination = dest;
    }

    public void streamProcessInfoToDestination(final TighteningProcess processInfo, final MessageFactory messageFactory) throws ParseException {

        if (processInfo != null && processInfo.getTighteningsteps() != null
                && processInfo.getTighteningsteps().size() > 0) {

            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(processInfo.getDate());
            logger.info("Starting streaming graph entries with date " + startTime);

            ObjectMapper objectMapper = new ObjectMapper();
            int counter = 0;
            int stepIndex = 0;

            for (TighteningStep step : processInfo.getTighteningsteps()) {

                stepIndex++;

                if (step.getGraph() != null && step.getGraph().getTimestamps() != null
                        && step.getGraph().getTimestamps().size() > 0) {

                    for (int i = 0; i < step.getGraph().getTimestamps().size(); i++) {
                        if (step.getGraph().getTimestamps().get(i) != null && step.getGraph().getAngles().get(i) != null
                                && step.getGraph().getTorques().get(i) != null) {
                                    
                            Date newDate = new Date();
                            newDate.setTime(
                                    startTime.getTime() + (long) (step.getGraph().getTimestamps().get(i) * 1000));
                            TighteningStepGraphEntry anEntry = this.createEntry(processInfo, step, newDate, stepIndex, i);
                            
                            try {
                                String jsonString = objectMapper.writeValueAsString(anEntry);
                                Message message = this.createMessage(jsonString, processInfo, step, anEntry, messageFactory);

                                logger.debug("Now streaming graph entry: " + jsonString);

                                this.destination.sendEventAsync(message);
                                counter++;
                            } catch (JsonProcessingException e) {
                                logger.error("Could not send Graph entry for timestamp " + newDate + ". Reason: " + e.getMessage());
                            } catch (IOException e) {
                                logger.error("Could not send Graph entry for timestamp " + newDate + ". Reason: " + e.getMessage());
                            }
                        }
                    }
                }
            }

            logger.info("Streamed " + counter + " graph entries to " + this.destination.destinationname());
        } else {
            logger.debug("Message doesn't contain tightening steps");
        }
    }

    private TighteningStepGraphEntry createEntry(final TighteningProcess processInfo, final TighteningStep step, Date newDate, int stepIndex, int i) {

        TighteningStepGraphEntry anEntry = new TighteningStepGraphEntry(newDate,
                                    step.getGraph().getAngles().get(i), step.getGraph().getTorques().get(i));
                            anEntry.setIdcode(processInfo.getIdcode());
                            anEntry.setCycle(processInfo.getCycle());
                            anEntry.setSteprow(step.getRow());
                            anEntry.setStepcol(step.getColumn());
                            anEntry.setStepIndex(stepIndex);
                            anEntry.setGraphIndex(i+1);

        return anEntry;
    }

    private Message createMessage(final String jsonString, final TighteningProcess processInfo, final TighteningStep step, 
        final TighteningStepGraphEntry entry, final MessageFactory messageFactory) {

        Message message = messageFactory.createMessageForGraphEntry(jsonString);                      
        message.setProperty("channel", Integer.toString(processInfo.getCycle()));
        message.setProperty("result", step.getResult());

        // Make IoT Central happy - Application timestamp UTC in ISO8601 format 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        message.setProperty("iothub-creation-time-utc", sdf.format(entry.getTimestamp()));

        return message;
    }

    /**
     * @return the destination
     */
    public PublishingDestination getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(PublishingDestination destination) {
        this.destination = destination;
    }
}