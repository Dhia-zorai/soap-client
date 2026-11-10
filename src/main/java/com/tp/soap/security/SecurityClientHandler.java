package com.tp.soap.security;

import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import javax.xml.namespace.QName;
import java.util.Set;

public class SecurityClientHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        // Check if the message is leaving the client (outgoing)
        Boolean isOutbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (isOutbound != null && isOutbound) {
            try {
                SOAPMessage msg = context.getMessage();
                SOAPHeader header = msg.getSOAPHeader();

                // If the header doesn't exist yet, create it
                if (header == null) {
                    header = msg.getSOAPPart().getEnvelope().addHeader();
                }

                // Create the <auth:Security> element with its namespace
                QName securityName = new QName("http://security.tp.com/", "Security", "auth");
                SOAPHeaderElement securityElement = header.addHeaderElement(securityName);

                // Add Username and Password sub-elements
                SOAPElement usernameElement = securityElement.addChildElement("Username");
                usernameElement.setValue("admin");

                SOAPElement passwordElement = securityElement.addChildElement("Password");
                passwordElement.setValue("secret123");

                msg.saveChanges();
            } catch (Exception e) {
                throw new RuntimeException("Error adding security header: " + e.getMessage(), e);
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {}

    @Override
    public Set<QName> getHeaders() {
        return null;
    }
}

