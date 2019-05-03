package com.microsoft.samples.nexo.edgemodule;

import java.io.IOException;
import java.text.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.microsoft.azure.sdk.iot.device.IotHubMessageResult;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.MessageCallback;
import com.microsoft.azure.sdk.iot.device.ModuleClient;
import com.microsoft.samples.nexo.process.TighteningProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * InputMessageHandler
 */
@Component
public class InputMessageHandler implements MessageCallback {

    private static final Logger logger = LoggerFactory.getLogger(InputMessageHandler.class);

    @Value("${nexopublisher_inputname:input1}")
    private String inputName;

    @Autowired
    private MessageFactory messageFactory;

    @Override
    public IotHubMessageResult execute(Message message, Object callbackContext) {

        if (callbackContext instanceof PublishingDestination) {

            PublishingDestination destination = (PublishingDestination) callbackContext;
            if (message != null) {

                if (logger.isDebugEnabled())
                    logger.debug("Received message: "
                            + new String(message.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));
                else
                    logger.info("Received message with id " + message.getMessageId() + " on input channel "
                            + message.getInputName());

                if (this.messageFactory.messageContainsTighteningProcessInfo(message)) {

                    TighteningProcess processInfo = null;

                    try {
                        processInfo = this.messageFactory.readTighteningProcessFromMessage(message);
                        ProcessTranslator translator = new ProcessTranslator(destination);
                        translator.streamProcessInfoToDestination(processInfo, this.messageFactory);

                    } catch (JsonParseException e) {
                        logger.error("Exception on processing input message. " + e.getMessage());
                    } catch (JsonMappingException e) {
                        logger.error("Exception on processing input message. " + e.getMessage());
                    } catch (IOException e) {
                        logger.error("Exception on processing input message. " + e.getMessage());;
                    } catch (ParseException e) {
                        logger.error("Exception on processing input message. " + e.getMessage());
                    }
                }
            }
        }

        return IotHubMessageResult.COMPLETE;
    }

    public void registerWithModuleClient(final ModuleClient moduleClient, final PublishingDestination destination) {

        Assert.notNull(moduleClient, "Parameter moduleClient must not be null");
        Assert.notNull(destination, "Parameter destination must not be null");

        moduleClient.setMessageCallback(inputName, this, destination);

        logger.info("Successfully registered input message callback handler for module client");
    }

    /**
     * @return the inputName
     */
    public String getInputName() {
        return inputName;
    }

    /**
     * @param inputName the inputName to set
     */
    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    /**
     * @return the messageFactory
     */
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    /**
     * @param messageFactory the messageFactory to set
     */
    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

}