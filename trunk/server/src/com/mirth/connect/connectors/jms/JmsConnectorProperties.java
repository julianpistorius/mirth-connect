package com.mirth.connect.connectors.jms;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;

public abstract class JmsConnectorProperties extends ConnectorProperties {
    private boolean useJndi;
    private String jndiProviderUrl;
    private String jndiInitialContextFactory;
    private String jndiConnectionFactoryName;
    private String connectionFactoryClass;
    private String username;
    private String password;
    private String destinationName;
    private String clientId;
    private boolean topic;
    private String reconnectIntervalMillis;
    private Map<String, String> connectionProperties;

    public JmsConnectorProperties() {
        useJndi = false;
        jndiProviderUrl = "";
        jndiInitialContextFactory = "";
        jndiConnectionFactoryName = "";
        connectionFactoryClass = "";
        username = "";
        password = "";
        destinationName = "";
        clientId = "";
        topic = false;
        reconnectIntervalMillis = "10000";
        connectionProperties = new HashMap<String, String>();
    }

    @Override
    public String getProtocol() {
        return "JMS";
    }

    @Override
    public String toFormattedString() {
        String newLine = "\n";
        StringBuilder builder = new StringBuilder();

        if (useJndi) {
            builder.append("PROVIDER URL: " + jndiProviderUrl + newLine);
            builder.append("INITIAL CONTEXT FACTORY: " + jndiInitialContextFactory + newLine);
            builder.append("CONNECTION FACTORY NAME: " + jndiConnectionFactoryName + newLine);
        } else {
            builder.append("CONNECTION FACTORY CLASS: " + connectionFactoryClass + newLine);
        }
        
        if (!username.isEmpty()) {
            builder.append("USERNAME: " + username + newLine);
        }

        if (!clientId.isEmpty()) {
            builder.append("CLIENT ID: " + clientId + newLine);
        }

        if (topic) {
            builder.append("TOPIC: ");
        } else {
            builder.append("QUEUE: ");
        }

        builder.append(destinationName + newLine);
        
        if (!connectionProperties.isEmpty()) {
            builder.append(newLine + "[CONNECTION PROPERTIES]" + newLine);
            
            for (Entry<String, String> property : connectionProperties.entrySet()) {
                builder.append(property.getKey() + ": " + property.getValue() + newLine);
            }
            
            builder.append(newLine);
        }
        
        return builder.toString();
    }

    public boolean isUseJndi() {
        return useJndi;
    }

    public void setUseJndi(boolean useJndi) {
        this.useJndi = useJndi;
    }

    public String getJndiProviderUrl() {
        return jndiProviderUrl;
    }

    public void setJndiProviderUrl(String jndiProviderUrl) {
        this.jndiProviderUrl = jndiProviderUrl;
    }

    public String getJndiInitialContextFactory() {
        return jndiInitialContextFactory;
    }

    public void setJndiInitialContextFactory(String jndiInitialContextFactory) {
        this.jndiInitialContextFactory = jndiInitialContextFactory;
    }

    public String getJndiConnectionFactoryName() {
        return jndiConnectionFactoryName;
    }

    public void setJndiConnectionFactoryName(String jndiConnectionFactoryName) {
        this.jndiConnectionFactoryName = jndiConnectionFactoryName;
    }

    public String getConnectionFactoryClass() {
        return connectionFactoryClass;
    }

    public void setConnectionFactoryClass(String connectionFactoryClass) {
        this.connectionFactoryClass = connectionFactoryClass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isTopic() {
        return topic;
    }

    public void setTopic(boolean topic) {
        this.topic = topic;
    }

    public String getReconnectIntervalMillis() {
        return reconnectIntervalMillis;
    }

    public void setReconnectIntervalMillis(String reconnectIntervalMillis) {
        this.reconnectIntervalMillis = reconnectIntervalMillis;
    }

    public Map<String, String> getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Map<String, String> connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
}