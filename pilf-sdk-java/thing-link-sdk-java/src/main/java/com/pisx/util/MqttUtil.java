package com.pisx.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.setting.dialect.Props;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pisx.exception.ErrorCode;
import com.pisx.model.ThingData;
import com.pisx.model.VirtualThing;
import com.pisx.mqtt.IotGatewayTopic;
import com.pisx.mqtt.MqttClientWrapper;
import com.pisx.value.RequestMessage;
import com.pisx.value.ResponseMessage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MqttUtil {
    private static MqttUtil mqttUtil = new MqttUtil();

    public static MqttUtil getInstance() {
        return mqttUtil;
    }

    private final Logger logger = LoggerFactory.getLogger(MqttUtil.class);

    private final static String PROTOCOL_VERSION = "1.0";
    private final static int SYNC_SEND_TIMEOUT = 5 * 1000;
    private final static int THREAD_POOL_SIZE = 5;

    private long msgId = 1;
    private IotGatewayTopic iotGatewayTopic;
    private MqttClientWrapper mqttClient;
    private Map<String, VirtualThing> deviceList;
    private Map<String, ResponseMessage> rspMsgQueque;
    private ExecutorService threadPool;

    private MqttUtil() {
        this.logger.info("start java driver id {}  >>>>>>>>>>>>>>>>", "ET99999");
        this.initMqttClientWrapper();
        this.iotGatewayTopic = new IotGatewayTopic();
        this.deviceList = new Hashtable<String, VirtualThing>();
        this.rspMsgQueque = new Hashtable<String, ResponseMessage>();
        this.threadPool = new ThreadPoolExecutor(MqttUtil.THREAD_POOL_SIZE,
                MqttUtil.THREAD_POOL_SIZE,
                0,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }


    private void initMqttClientWrapper() {
        Props props = new Props("device/properties/device.properties");
        String mqttBrokerIp = props.getProperty("mqtt.broker.ip");
        Short mqttBrokerPort = Short.valueOf(props.getProperty("mqtt.broker.port"));
        String emqxUsername = props.getProperty("emqx.username");
        String emqxPassword = props.getProperty("emqx.password");
        String clientId = props.getProperty("client.id") + RandomUtil.randomString(5);
        while (true) {
            try {
                if (null == mqttClient) {
                    if (System.getenv("EMQX_IPADDR") != null) {
                        this.mqttClient = new MqttClientWrapper(clientId, System.getenv("EMQX_IPADDR"), mqttBrokerPort);
                    } else {
                        this.mqttClient = new MqttClientWrapper(clientId, mqttBrokerIp, mqttBrokerPort);
                    }

                    this.mqttClient.setUserName(emqxUsername);
                    this.mqttClient.setPassword(emqxPassword);
                    this.mqttClient.setCleanSession(true);
                    this.mqttClient.setAutoReconnect(true);
                    this.mqttClient.setQos(0);
                    this.mqttClient.setRetained(false);
                    this.mqttClient.setCallback(this);
                }

                if (null != mqttClient) {
                    this.mqttClient.connect();
                    String[] topicFilters = {
                            "driver/callservice/request/thing/#",
                            "driver/request/device/callservice/thing/service/#",
                    };
                    //"driver/response/device/online/2c9480ed71b4ee7f0171b4ee7fe20000",
                    //"driver/request/device/callservice/thing/service/property/get/2c9480ed71b4ee7f0171b4ee7fe20000"
                    try {
                        this.mqttClient.subscribe(topicFilters);
                    } catch (MqttException e) {
                        logger.error("订阅失败！");
                    }
                    break;
                }
            } catch (Exception e) {
                this.logger.warn("it's have exception {} happen when init mqtt client", e);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                this.logger.warn("it's have exception {} happen when call thread sleep", e);
            }
        }
    }

    public Map<String, VirtualThing> getDeviceList() {
        return this.deviceList;
    }

    public IotGatewayTopic getIotGatewayTopic() {
        return iotGatewayTopic;
    }

    public void setIotGatewayTopic(IotGatewayTopic iotGatewayTopic) {
        this.iotGatewayTopic = iotGatewayTopic;
    }

    private void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    private long getMsgId() {
        return this.msgId++;
    }

    private int sendRequest(String topic) {
        int ret = ErrorCode.SUCCESS;
        RequestMessage reqMsg = new RequestMessage(String.valueOf(this.getMsgId()),
                MqttUtil.PROTOCOL_VERSION,
                new JSONObject());
        try {
            this.mqttClient.publish(topic, JSON.toJSONString(reqMsg));
        } catch (Exception e) {
            this.logger.warn("it's have exception {} happen when publish topic {}", e, topic);
            ret = ErrorCode.ERROR_UNKNOWN;
        }

        return ret;
    }

    private int sendRequest(String topic, JSONObject params) {
        int ret = ErrorCode.SUCCESS;
        RequestMessage reqMsg = new RequestMessage(String.valueOf(this.getMsgId()),
                MqttUtil.PROTOCOL_VERSION,
                params);
        try {
            this.mqttClient.publish(topic, JSON.toJSONString(reqMsg));
        } catch (Exception e) {
            this.logger.warn("it's have exception {} happen when publish topic {}", e, topic);
            ret = ErrorCode.ERROR_UNKNOWN;
        }
        return ret;
    }

    private ResponseMessage sendSyncRequest(String topic) {
        RequestMessage reqMsg = new RequestMessage(String.valueOf(this.getMsgId()),
                MqttUtil.PROTOCOL_VERSION,
                new JSONObject());
        ResponseMessage rspMsg = new ResponseMessage();
        try {
            this.rspMsgQueque.put(reqMsg.getId(), rspMsg);
            synchronized (rspMsg) {
                this.mqttClient.publish(topic, JSON.toJSONString(reqMsg));
                long startTimeStamp = System.currentTimeMillis();
                rspMsg.wait(MqttUtil.SYNC_SEND_TIMEOUT);
                long endTimeStamp = System.currentTimeMillis();
                if (endTimeStamp - startTimeStamp > MqttUtil.SYNC_SEND_TIMEOUT) {
                    rspMsg.setCode(ErrorCode.ERROR_TIMEOUT);
                }
            }
        } catch (Exception e) {
            this.logger.warn("it's have exception {} happen when publish topic {}", e, topic);
            rspMsg.setCode(ErrorCode.ERROR_UNKNOWN);
        } finally {
            try {
                this.rspMsgQueque.remove(reqMsg.getId());
            } catch (Exception e) {
                this.logger.warn("it's have exception {} happen when remove msgId {} to topic {}", e, reqMsg.getId(), topic);
            }
        }

        return rspMsg;
    }

    private ResponseMessage sendSyncRequest(String topic, JSONObject params) {
        RequestMessage reqMsg = new RequestMessage(String.valueOf(this.getMsgId()),
                MqttUtil.PROTOCOL_VERSION,
                params);
        ResponseMessage rspMsg = new ResponseMessage();
        try {
            this.rspMsgQueque.put(reqMsg.getId(), rspMsg);
            synchronized (rspMsg) {
                this.mqttClient.publish(topic, JSON.toJSONString(reqMsg));

                long startTimeStamp = System.currentTimeMillis();
                rspMsg.wait(MqttUtil.SYNC_SEND_TIMEOUT);
                long endTimeStamp = System.currentTimeMillis();
                if (endTimeStamp - startTimeStamp > MqttUtil.SYNC_SEND_TIMEOUT) {
                    rspMsg.setCode(ErrorCode.ERROR_TIMEOUT);
                }
            }
        } catch (Exception e) {
            this.logger.warn("it's have exception {} happen when publish topic {}", e, topic);
            rspMsg.setCode(ErrorCode.ERROR_UNKNOWN);
        } finally {
            try {
                this.rspMsgQueque.remove(reqMsg.getId());
            } catch (Exception e) {
                this.logger.warn("it's have exception {} happen when remove msgId {} to topic {}", e, reqMsg.getId(), topic);
            }
        }

        return rspMsg;
    }

    public void setDevice(String identifier, VirtualThing virtualThing) {
        this.deviceList.put(identifier, virtualThing);
    }


    public int online(String identifier) {
        ResponseMessage response = this.sendSyncRequest(this.iotGatewayTopic.loginTopic(identifier));
        if (null == response) {
            return ErrorCode.ERROR_UNKNOWN;
        }

        return response.getCode();
    }

    public int offline(String identifier) {
        ResponseMessage response = this.sendSyncRequest(this.iotGatewayTopic.logoutTopic(identifier));
        if (null == response) {
            return ErrorCode.ERROR_UNKNOWN;
        }

        return response.getCode();
    }

    /**
     * 上报属性数据
     *
     * @param identifier
     * @param properties
     * @return
     */
    public int reportProperties(String identifier, Map<String, Object> properties) {
        JSONObject params = JSONObject.parseObject(JSON.toJSONString(properties));
        return this.sendRequest(this.iotGatewayTopic.reportPropertyTopic(identifier), params);
    }

    /**
     * 上报默认属性
     *
     * @param identifier
     * @param properties
     * @return
     */
    public int reportDefaultProperties(String identifier, Map<String, Object> properties) {
        JSONObject params = JSONObject.parseObject(JSON.toJSONString(properties));
        return this.sendRequest(this.iotGatewayTopic.getPropertyReplyTopic(identifier), params);
    }

    public int reportEvents(String identifier, String eventName, HashMap<String, Object> outputData) {
        JSONObject params = new JSONObject();
        JSONObject value = new JSONObject();
        for (Map.Entry<String, Object> data : outputData.entrySet()) {
            value.put(data.getKey(), data.getValue());
        }
        params.put("value", value);
        params.put("time", System.currentTimeMillis());

        return this.sendRequest(this.iotGatewayTopic.reportEventTopic(identifier, eventName), params);
    }

    public void sendResponse(String methodName, String topic, String msgId, Object object) {
        JSONObject response = new JSONObject();
        response.put("id", msgId);
        response.put("version", MqttUtil.PROTOCOL_VERSION);

        try {
            if (null != object) {
                String getMethod = "get";
                String setMethod = "set";
                if (getMethod.equals(methodName)) {
                    ThingData data = (ThingData) object;
                    response.put("code", data.code);
                    response.put("data", data.data);
                } else if (setMethod.equals(methodName)) {
                    response.put("code", object);
                    response.put("data", new JSONObject());
                } else {
                    ThingData data = (ThingData) object;
                    response.put("code", data.code);
                    response.put("data", data.data);
                }
            } else {
                response.put("code", ErrorCode.ERROR_UNKNOWN);
                response.put("data", new JSONObject());
            }

            this.mqttClient.publish(topic, response.toJSONString());
        } catch (Exception e) {
            this.logger.warn("it's have exception {} happen when send topic {} response {}", e, topic, response.toJSONString());
        }
    }

    private void parseRequestMessage(final String topic, final String message) {
        if (-1 != topic.indexOf("callservice")) {
            String[] topicElementArray = topic.split("/");
            String condition = "property";
            if (topicElementArray.length == 9 && condition.equals(topicElementArray[6])) {
                this.threadPool.execute(new ThingCallback(topicElementArray[topicElementArray.length - 1], topicElementArray[topicElementArray.length - 2], message, this));
            } else {
                this.threadPool.execute(new ThingCallback(topicElementArray[topicElementArray.length - 1], topicElementArray[topicElementArray.length - 2], message, this));
            }
        }
    }

    private void parseResponseMessage(final String topic, final String message) {
        try {
            ResponseMessage rspMsg = JSON.parseObject(message, ResponseMessage.class);
            if (null != rspMsg) {
                ResponseMessage localRspMsg = this.rspMsgQueque.get(rspMsg.getId());
                if (null != localRspMsg) {
                    localRspMsg.setCode(rspMsg.getCode());
                    localRspMsg.setData(rspMsg.getData());
                    synchronized (localRspMsg) {
                        localRspMsg.notifyAll();
                    }
                }
            }
        } catch (Exception e) {
            this.logger.warn("it's have exception {} happen when parse topic {} response {}", e, topic, message);
        }
    }

    public void receiveMessage(final String topic, final String message) {
        if (-1 != topic.indexOf("response")) {
            this.parseResponseMessage(topic, message);
        } else {
            this.parseRequestMessage(topic, message);
        }
    }

    class ThingCallback implements Runnable {
        private String identifier;
        private String methodName;
        private String message;

        private MqttUtil mqttUtil;

        ThingCallback(String identifier, String methodName, String message, MqttUtil mqttUtil) {
            this.identifier = identifier;
            this.methodName = methodName;
            this.message = message;
            this.mqttUtil = mqttUtil;
        }

        private void getProperties() {
            String topic;
            Object object;

            RequestMessage reqMsg = JSON.parseObject(this.message, RequestMessage.class);
            JSONObject jsonObject = JSON.parseObject(reqMsg.getParams().toString());
            String bind = (String) jsonObject.get("_bind");
            object = this.mqttUtil.getDeviceList().get(identifier).getProperties(bind);
            topic = this.mqttUtil.getIotGatewayTopic().getPropertyReplyTopic(this.identifier);
            this.mqttUtil.sendResponse(this.methodName, topic, reqMsg.getId(), object);
        }

        @SuppressWarnings("unchecked")
        private void setProperties() {
            String topic;
            Object object;

            RequestMessage reqMsg = JSON.parseObject(this.message, RequestMessage.class);
            HashMap<String, Object> params = JSON.parseObject(((JSONObject) (reqMsg.getParams())).toJSONString(), HashMap.class);

            object = this.mqttUtil.getDeviceList().get(identifier).setProperties(params);
            topic = this.mqttUtil.getIotGatewayTopic().setPropertyReplyTopic(this.identifier);
            this.mqttUtil.sendResponse(this.methodName, topic, reqMsg.getId(), object);
        }

        @SuppressWarnings("unchecked")
        private void callService() {
            String topic;
            Object object;

            RequestMessage reqMsg = JSON.parseObject(this.message, RequestMessage.class);

            HashMap<String, Object> params = JSON.parseObject(((JSONObject) (reqMsg.getParams())).toJSONString(), HashMap.class);
            String methodName = params.get("methodName").toString();
            object = this.mqttUtil.getDeviceList().get(identifier).callService(methodName, params);
            topic = this.mqttUtil.getIotGatewayTopic().serviceReplyTopic(methodName, this.identifier);
            this.mqttUtil.sendResponse(this.methodName, topic, reqMsg.getId(), object);
        }

        @Override
        public void run() {
            String getMethod = "get";
            String setMethod = "set";

            if (getMethod.equals(this.methodName)) {
                this.getProperties();
            } else if (setMethod.equals(this.methodName)) {
                this.setProperties();
            } else {
                this.callService();
            }
        }
    }
}