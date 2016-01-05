package org.wso2.carbon.apimgt.frauddetection;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.transport.passthru.util.RelayUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * The mediator which publisher transaction data to WSO2 DAS.
 */
public class TransactionDataPublishingMediator extends AbstractMediator{

    private String dasHost;
    private String dasPort;
    private String dasUsername;
    private String dasPassword;

    public String streamName;
    public String streamVersion;

    public boolean mediate(MessageContext messageContext) {

        TransactionDataPublisher publisher = getTransactionDataPublisher();
        publishTransactionData(publisher, messageContext);
        return true;
    }

    private void publishTransactionData(TransactionDataPublisher publisher, MessageContext messageContext) {

        OMElement transactionInfoPayload = getTransactionInfoPayload(messageContext);

        if(transactionInfoPayload != null){
            Object[] transactionStreamPayload = buildTransactionStreamPayload(transactionInfoPayload, messageContext);
            log.debug(String.format("transaction stream payload => %s", Arrays.toString(transactionStreamPayload)));
            publisher.publish((transactionStreamPayload));
        }


    }

    public String getDasHost() {
        return dasHost;
    }

    public void setDasHost(String dasHost) {
        this.dasHost = dasHost;
    }

    public String getDasPort() {
        return dasPort;
    }

    public void setDasPort(String dasPort) {
        this.dasPort = dasPort;
    }

    public String getDasUsername() {
        return dasUsername;
    }

    public void setDasUsername(String dasUsername) {
        this.dasUsername = dasUsername;
    }

    public String getDasPassword() {
        return dasPassword;
    }

    public void setDasPassword(String dasPassword) {
        this.dasPassword = dasPassword;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getStreamVersion() {
        return streamVersion;
    }

    public void setStreamVersion(String streamVersion) {
        this.streamVersion = streamVersion;
    }

    private Object[] buildTransactionStreamPayload(OMElement transactionInfoPayload, MessageContext messageContext) {


        OMElement orderInfo = transactionInfoPayload.getFirstChildWithName(new QName(null, "Order"));
        String itemNo = orderInfo.getFirstChildWithName(new QName(null,"additions")).getText();
        String currency = orderInfo.getFirstChildWithName(new QName(null, "drinkName")).getText();
        long creditCardNumber = Long.parseLong(orderInfo.getFirstChildWithName(new QName(null, "orderQuantity")).getText());
        long timestamp = System.currentTimeMillis();
        return new Object[]{ currency,itemNo, creditCardNumber,timestamp};

    }

    private String getClientIPAddress(MessageContext messageContext) {
        return Util.getClientIPAddress(messageContext);
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

    private TransactionDataPublisher getTransactionDataPublisher() {

        TransactionDataPublisher publisher = TransactionDataPublisher.getInstance();

        if(!publisher.isReady()){

            synchronized (publisher){
                if(!publisher.isReady()){
                    DataPublisherConfig config = getDataPublisherConfig();
                    publisher.init(config);
                }
            }

        }

        return publisher;
    }

    private DataPublisherConfig getDataPublisherConfig() {

        DataPublisherConfig config = new DataPublisherConfig();

        config.setDasHost(dasHost);
        config.setDasPort(dasPort);
        config.setDasUsername(dasUsername);
        config.setDasPassword(dasPassword);
        config.setStreamName(streamName);
        config.setStreamVersion(streamVersion);

        return config;
    }

    private void logDataPublishingException(String reason, Exception e) {
        log.error(String.format("Cannot publish transaction data. Reason : %s", reason), e);
    }

}
