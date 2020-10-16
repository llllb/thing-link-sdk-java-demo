package com.pisx.mqtt;

public class IotGatewayTopic {

    private String reportPropertyTopic = "iotgateway/%s";

    private String reportEventTopic = "/device/%s/broadcast/thing/%s/%s/event/%s";

    private String setPropertyTopic = "driver/proxy/x/request/device/%s/%s/%s/callservice/thing/service/property/set";
    private String setPropertyReplyTopic = "device/callservice/response/thing/service/property/set/%s";

    private String getPropertyTopic = "driver/request/device/callservice/thing/service/property/get/%s";
    //获取设备属性到平台
    private String getPropertyReplyTopic = "device/response/driver/callservice/thing/service/property/get/%s";

    //接收平台服务调用
    private String serviceTopic = "driver/request/device/callservice/thing/service/%s";
    //回应平台服务调用
    private String serviceReplyTopic = "device/response/driver/callservice/thing/service/%s";



    private String loginTopic = "edge/device/%s/%s/%s/request/iot/driver/proxy/x/online";
    private String loginReplyTopic = "edge/iot/driver/proxy/x/response/device/%s/%s/%s/online";

    private String logoutTopic = "edge/device/%s/%s/%s/request/iot/driver/proxy/x/offline";
    private String logoutReplyTopic = "edge/iot/driver/proxy/x/response/device/%s/%s/%s/offline";


    public String reportPropertyTopic(String identifier) {
        return String.format(this.reportPropertyTopic, identifier);
    }

    public String reportEventTopic(String identifier, String deviceName, String eventName) {
        return String.format(this.reportEventTopic, deviceName, deviceName, eventName);
    }

    public String serviceTopic(String serviceName, String identifier) {
        return String.format(this.serviceTopic, serviceName, identifier);
    }

    public String serviceReplyTopic(String serviceName, String identifier) {
        return String.format(this.serviceReplyTopic, serviceName, identifier);
    }


    public String setPropertyTopic(String productKey, String deviceName) {
        return String.format(this.setPropertyTopic, productKey, deviceName);
    }

    public String setPropertyReplyTopic(String identifier) {
        return String.format(this.setPropertyReplyTopic, identifier);
    }


    public String loginTopic(String productKey, String deviceName) {
        return String.format(this.loginTopic, productKey, deviceName);
    }

    public String loginReplyTopic(String productKey, String deviceName) {
        return String.format(this.loginReplyTopic, productKey, deviceName);
    }

    public String logoutTopic(String productKey, String deviceName) {
        return String.format(this.logoutTopic, productKey, deviceName);
    }

    public String logoutReplyTopic(String productKey, String deviceName) {
        return String.format(this.logoutReplyTopic, productKey, deviceName);
    }

    public String getPropertyTopic(String productKey, String deviceName) {
        return String.format(this.getPropertyTopic, productKey, deviceName);
    }

    public String getPropertyReplyTopic(String identifier) {
        return String.format(this.getPropertyReplyTopic, identifier);
    }


}