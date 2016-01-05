/*
 *  Copyright WSO2 Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.apimgt.frauddetection;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.transport.passthru.util.RelayUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * An API handler which publishes transaction data to DAS for fraud detection.
 */
public class TransactionDataPublishingHandler extends AbstractHandler implements ManagedLifecycle {

    private static final Log log = LogFactory.getLog(TransactionDataPublishingHandler.class);

    private volatile TransactionDataPublisher transactionDataPublisher;

    public void init(SynapseEnvironment synapseEnvironment) {

        transactionDataPublisher = TransactionDataPublisher.getInstance();

        if(!transactionDataPublisher.isReady()){
            DataPublisherConfig config = getDataPublisherConfig();
            transactionDataPublisher.init(config);
        }

    }

    public void destroy() {
        transactionDataPublisher.shutdown();
    }

    public boolean handleRequest(MessageContext messageContext) {
        log.debug("START : TransactionDataPublishingHandler::handleRequest()");
        publishTransactionData(messageContext);
        log.debug("END : TransactionDataPublishingHandler::handleRequest()");
        return true;
    }

    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }


    private void publishTransactionData(MessageContext messageContext){

        OMElement transactionInfoPayload = getTransactionInfoPayload(messageContext);

        if(transactionInfoPayload != null){
            Object[] transactionStreamPayload = buildTransactionStreamPayload(transactionInfoPayload, messageContext);
            log.debug(String.format("transaction stream payload => %s", Arrays.toString(transactionStreamPayload)));
            transactionDataPublisher.publish((transactionStreamPayload));
        }
    }

    private DataPublisherConfig getDataPublisherConfig() {


        DataPublisherConfig config = new DataPublisherConfig();

        File dasPropertiesFile = new File("repository/conf/etc/fraud-detection/fraud-detection.properties");

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(dasPropertiesFile));

            config.setDasHost(properties.getProperty("dasHost"));
            config.setDasPort(properties.getProperty("dasPort"));
            config.setDasUsername(properties.getProperty("dasUsername"));
            config.setDasPassword(properties.getProperty("dasPassword"));

            config.setStreamName(properties.getProperty("streamName"));
            config.setStreamVersion(properties.getProperty("streamVersion"));

            log.debug(String.format("Fraud detection DAS properties were read from the file : '%s'", dasPropertiesFile.getAbsolutePath()));

            return config;

        } catch (IOException e) {
            log.warn(String.format("Cannot read Fraud detection DAS properties from the file : '%s'.",
                    dasPropertiesFile.getAbsolutePath()));
            return null;
        }


    }

    private OMElement getTransactionInfoPayload(MessageContext messageContext) {

        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext).
                getAxis2MessageContext();

        try {
            RelayUtils.buildMessage(axis2MessageContext);
        } catch (IOException e) {
            logDataPublishingException("Cannot build the incoming request message", e);
            return null;
        } catch (XMLStreamException e) {
            logDataPublishingException("Cannot build the incoming request message", e);
            return null;
        }

        Iterator iterator = messageContext.getEnvelope().getBody().getChildElements();

        OMElement payload = null;
        if(iterator.hasNext()){
            payload = (OMElement) iterator.next();
        }

        return payload;
    }

    private Object[] buildTransactionStreamPayload(OMElement transactionInfoPayload, MessageContext messageContext) {

        OMElement orderInfo = transactionInfoPayload.getFirstChildWithName(new QName(null, "Customer"));
        String ip = getClientIPAddress(messageContext);
        String itemNo = orderInfo.getFirstChildWithName(new QName(null,"id")).getText();
        String currency = orderInfo.getFirstChildWithName(new QName(null, "name")).getText();
        long timestamp = System.currentTimeMillis();
        return new Object[]{ currency, ip, itemNo,  timestamp};

    }

    private String getClientIPAddress(MessageContext messageContext) {
        return Util.getClientIPAddress(messageContext);
    }



    private void logDataPublishingException(String reason, Exception e) {
        log.error(String.format("Cannot publish transaction data. Reason : %s", reason), e);
    }

}
