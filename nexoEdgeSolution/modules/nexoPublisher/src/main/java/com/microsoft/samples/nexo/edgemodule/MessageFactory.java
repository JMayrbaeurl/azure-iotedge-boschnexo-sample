package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.samples.nexo.process.TighteningProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * MessageFactory
 */
public class MessageFactory {

    private static final Logger logger = LoggerFactory.getLogger(MessageFactory.class);

    public static final String STD_SOURCE_PROPNAME = "source";
    public static final String STD_SOURCE_PROPVALUE = "nexopublisher";
    public static final String STD_MESSAGETYPE_PROPNAME = "messagetype";
    public static final String STD_MESSAGETYPE_PROCESS = "process";
    public static final String STD_MESSAGETYPE_GRAPHENTRY = "events";
    public static final String STD_MESSAGETYPE_ANY = "any";

    private String sourcePropName = STD_SOURCE_PROPNAME;

    private String sourcePropValue = STD_SOURCE_PROPVALUE;

    private String messageTypePropName = STD_MESSAGETYPE_PROPNAME;

    private String messageTypeProcess = STD_MESSAGETYPE_PROCESS;

    private String messageTypeGraphEntry = STD_MESSAGETYPE_GRAPHENTRY;

    private String messageTypeAny = STD_MESSAGETYPE_ANY;

    public Message createMessageForProcessInfo(final String jsonString) {

        Message message = new Message(jsonString);
        message.setProperty(this.sourcePropName, this.sourcePropValue);
        message.setProperty(this.messageTypePropName, this.messageTypeProcess);

        return message;
    }

    public Message createMessageForGraphEntry(final String jsonString) {

        Message message = new Message(jsonString);
        message.setProperty(this.sourcePropName, this.sourcePropValue);
        message.setProperty(this.messageTypePropName, this.messageTypeGraphEntry);

        return message;
    }

    public Message createAnyMessage(final String jsonString) {

        Message message = new Message(jsonString);
        message.setProperty(this.sourcePropName, this.sourcePropValue);
        message.setProperty(this.messageTypePropName, this.messageTypeAny);

        return message;
    }

    public void addProcessInfoToMessageProps(final TighteningProcess process, Message message) {

        if (message != null && process != null) {
            message.setProperty("nr", Integer.toString(process.getNr()));
            if (process.getResult() != null)
                message.setProperty("result", this.createValidMessagePropertyValue(process.getResult()));
            if (process.getChannel() != null)
                message.setProperty("channel", this.createValidMessagePropertyValue(process.getChannel()));
            message.setProperty("prg_nr", Integer.toString(process.getPrgnr()));
            if (process.getPrgname() != null)
                message.setProperty("prg_name", this.createValidMessagePropertyValue(process.getPrgname()));
            // message.setProperty("prg_date", process.getPrgdate());
            message.setProperty("cycle", Integer.toString(process.getCycle()));
            // message.setProperty("nominal torque", Double.toString(process.getNominaltorque()));
            // message.setProperty("date", process.getDate());
            if (process.getIdcode() != null)
                message.setProperty("id_code", this.createValidMessagePropertyValue(process.getIdcode()));
            message.setProperty("job_nr", Integer.toString(process.getJobnr()));
        }
    }

    public String createValidMessagePropertyValue(String fromString) {

        Assert.notNull(fromString, "Parameter 'fromString' must not be null");

        String result = fromString.replaceAll("[^a-zA-Z0-9!#$%*+-.^_`|~]","");
        return result;
    }

    public TighteningProcess readTighteningProcessFromBody(String pBody)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        TighteningProcess process = objectMapper.readValue(pBody, TighteningProcess.class);
        
        return process;
    }

    public TighteningProcess readTighteningProcessFromMessage(final Message message)
            throws JsonParseException, JsonMappingException, IOException {

        TighteningProcess result = null;

        if (message != null) {

            String jsonString = new String(message.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET);
            result = this.readTighteningProcessFromBody(jsonString);
        }

        return result;
    }

    public boolean messageContainsTighteningProcessInfo(final Message message) {

        boolean result = false;

        if (message != null) {
            String jsonString = new String(message.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET);
            result = this.messageContainsTighteningProcessInfo(jsonString);
        }

        return result;
    }

    public boolean messageContainsTighteningProcessInfo(final String jsonString) {

        boolean result = false;

        if (jsonString != null && jsonString.length() > 0) {

            try {
                result = this.readTighteningProcessFromBody(jsonString) != null;
            } catch (IOException e) {
                logger.debug("Exception on parsing Tightenting process info. " + e.getMessage());
            }
        }

        return result;
    }

    public boolean isMessageForProcessInfo(final Message message) {

        Assert.notNull(message, "Parameter message must not be null");

        boolean result = message.getProperty(this.sourcePropName) != null && 
            message.getProperty(this.sourcePropName).equals(this.sourcePropValue) && 
            message.getProperty(this.messageTypePropName) != null &&
            message.getProperty(this.messageTypePropName).equals(this.messageTypeProcess);

        return result;
    }

    public boolean isMessageForGraphEntry(final Message message) {

        Assert.notNull(message, "Parameter message must not be null");

        boolean result = message.getProperty(this.sourcePropName) != null && 
            message.getProperty(this.sourcePropName).equals(this.sourcePropValue) && 
            message.getProperty(this.messageTypePropName) != null &&
            message.getProperty(this.messageTypePropName).equals(this.messageTypeGraphEntry);

        return result;
    }

    public boolean isMessageForAny(final Message message) {

        Assert.notNull(message, "Parameter message must not be null");

        boolean result = message.getProperty(this.sourcePropName) != null && 
            message.getProperty(this.sourcePropName).equals(this.sourcePropValue) && 
            message.getProperty(this.messageTypePropName) != null &&
            message.getProperty(this.messageTypePropName).equals(this.messageTypeAny);

        return result;
    }

    /**
     * @return the sourcePropName
     */
    public String getSourcePropName() {
        return sourcePropName;
    }

    /**
     * @param sourcePropName the sourcePropName to set
     */
    public void setSourcePropName(String sourcePropName) {
        this.sourcePropName = sourcePropName;
    }

    /**
     * @return the sourcePropValue
     */
    public String getSourcePropValue() {
        return sourcePropValue;
    }

    /**
     * @param sourcePropValue the sourcePropValue to set
     */
    public void setSourcePropValue(String sourcePropValue) {
        this.sourcePropValue = sourcePropValue;
    }

    /**
     * @return the messageTypePropName
     */
    public String getMessageTypePropName() {
        return messageTypePropName;
    }

    /**
     * @param messageTypePropName the messageTypePropName to set
     */
    public void setMessageTypePropName(String messageTypePropName) {
        this.messageTypePropName = messageTypePropName;
    }

    /**
     * @return the messageTypeProcess
     */
    public String getMessageTypeProcess() {
        return messageTypeProcess;
    }

    /**
     * @param messageTypeProcess the messageTypeProcess to set
     */
    public void setMessageTypeProcess(String messageTypeProcess) {
        this.messageTypeProcess = messageTypeProcess;
    }

    /**
     * @return the messageTypeGraphEntry
     */
    public String getMessageTypeGraphEntry() {
        return messageTypeGraphEntry;
    }

    /**
     * @param messageTypeGraphEntry the messageTypeGraphEntry to set
     */
    public void setMessageTypeGraphEntry(String messageTypeGraphEntry) {
        this.messageTypeGraphEntry = messageTypeGraphEntry;
    }

    /**
     * @return the messageTypeAny
     */
    public String getMessageTypeAny() {
        return messageTypeAny;
    }

    /**
     * @param messageTypeAny the messageTypeAny to set
     */
    public void setMessageTypeAny(String messageTypeAny) {
        this.messageTypeAny = messageTypeAny;
    }

}