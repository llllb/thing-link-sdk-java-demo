package com.pisx.mqtt;

import com.pisx.util.MqttUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClientWrapper implements MqttCallbackExtended{
    private final Logger logger = LoggerFactory.getLogger(MqttClientWrapper.class);

    String[] topicFilters = {
            "driver/callservice/request/thing/#"
    };

    private String clientId;
    private String ip;
    private short port;
    private MqttClient client;

    private String userName;
    private String password;
    private boolean cleanSession;
    private boolean autoReconnect;
    private MqttConnectOptions connOpt;

    private int qos;
    private boolean retained;

    private MqttUtil mqttUtil;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public short getPort() {
        return this.port;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public boolean getAutoReconnect() {
        return this.autoReconnect;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean getCleanSession() {
        return this.cleanSession;
    }

    public int getQos() {
        return this.qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public boolean getRetained() {
        return this.retained;
    }

    public void setRetained(Boolean retained) {
        this.retained = retained;
    }

    public MqttClientWrapper(String clientId, String ip, short port) throws MqttException {
        this.logger.debug("mqtt client {} connect with broker ip: {} port: {}", clientId, ip, port);

        this.clientId = clientId;
        this.ip = ip;
        this.port = port;

        this.connOpt = new MqttConnectOptions();
        this.client = new MqttClient(String.format("tcp://%s:%d", this.ip, this.port), this.clientId);
        this.client.setCallback(this);
    }

    public void connect() throws MqttSecurityException, MqttException {
        this.logger.debug("mqtt client {} connect with option userName: {} password: {} automaticReconnect: {} cleanSession: {}",
                this.clientId, this.userName, this.password, this.autoReconnect, this.cleanSession);

        this.connOpt.setUserName(this.userName);
        this.connOpt.setPassword(this.password.toCharArray());
        this.connOpt.setAutomaticReconnect(this.autoReconnect);
        this.connOpt.setCleanSession(this.cleanSession);
        this.client.connect(this.connOpt);
    }

    public void disConnect() throws MqttException {
        this.logger.debug("mqtt client {} disconnect with broker ip: {} port: {}", this.clientId, this.ip, this.port);
        this.client.disconnect();
    }

    public void subscribe(String topicFilter) throws MqttException {
        this.logger.debug("mqtt client {} subscribe topic: {}", this.clientId, topicFilter);
        this.client.subscribe(topicFilter);
    }

    public void subscribe(String[] topicFilters) throws MqttException {
        for (String topicFilter : topicFilters) {
            this.logger.debug("mqtt client {} subscribe topic: {}", this.clientId, topicFilter);
        }

        this.client.subscribe(topicFilters);
    }

    public void publish(String topic, String payload) throws MqttPersistenceException, MqttException {
        this.logger.debug("mqtt client {} publish topic: {} with payload: {}", this.clientId, topic, payload);
        this.client.publish(topic, payload.getBytes(), this.qos, this.retained);
    }

    @Override
    public void messageArrived(final String topic, final MqttMessage message) throws Exception {
        System.out.println(topic);
        if (null != this.mqttUtil) {
            String payload = new String(message.getPayload());
            logger.debug("topic: {} messageArrived: {}", topic, payload);
            this.mqttUtil.receiveMessage(topic, payload);
        }
    }

    @Override
    public void deliveryComplete(final IMqttDeliveryToken token) {
        logger.debug("publish message complete.");
    }

    @Override
    public void connectComplete(final boolean reconnect, final String serverURI) {
        try {
            this.subscribe(topicFilters);
        } catch (MqttException e) {
            logger.error("订阅失败！");
        }
        logger.debug("mqtt connect complete.");
    }

    @Override
    public void connectionLost(final Throwable cause) {
        logger.debug("mqtt connect lost.");
    }

    public void setCallback(MqttUtil mqttUtil) {
        this.mqttUtil = mqttUtil;
    }
}