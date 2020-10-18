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



    private String loginTopic = "device/request/driver/online/%s";
    private String loginReplyTopic = "driver/response/device/online/%s";

    private String logoutTopic = "device/request/driver/offline/%s";
    private String logoutReplyTopic = "driver/response/device/offline/%s";


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


    public String loginTopic(String identifier) {
        return String.format(this.loginTopic, identifier);
    }

    public String loginReplyTopic(String identifier) {
        return String.format(this.loginReplyTopic, identifier);
    }

    public String logoutTopic(String identifier) {
        return String.format(this.logoutTopic, identifier);
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