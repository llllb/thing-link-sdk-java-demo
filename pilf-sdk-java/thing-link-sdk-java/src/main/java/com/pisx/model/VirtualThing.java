package com.pisx.model;

import com.pisx.exception.ErrorCode;
import com.pisx.util.MqttUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 设备基类
 * @author: boliu
 * @date: 2020年10月14日 10:22
 */
@Data
@Slf4j
public abstract class VirtualThing {
    /**
     * 设备唯一识别码
     */
    private String identifier;
    /**
     * 上下线
     */
    private boolean isOnline;

    public VirtualThing(String identifier) {
        this.identifier = identifier;
        this.isOnline = false;
        MqttUtil.getInstance().setDevice(identifier, this);
    }

    /**
     * 从设备获取属性发送到平台
     *
     * @return
     */
    public ThingData getProperties(String propertyDefinitions) {
        return null;
    }

    /**
     * 从平台获取属性设置到设备
     *
     * @param properties
     * @return
     */
    public int setProperties(HashMap<String, Object> properties) {
        return ErrorCode.SUCCESS;
    }


    /**
     * 调用设备上的自定义方法
     *
     * @param methodName
     * @param params
     * @return
     */
    public ThingData callService(String methodName, HashMap<String, Object> params) {
        return null;
    }

    /**
     * 设备上线到平台
     * @return
     */
    public int online() {
        log.debug("isOnline: {}>>>>>>>>>>>>>>>>>>>>>>>>>"+this.isOnline);
        if (this.isOnline) {
            return ErrorCode.SUCCESS;
        }
        int code = MqttUtil.getInstance().online(this.identifier);
        log.debug("code:{}>>>>>>>>>>>>>>>>>>>>>>"+code);
        if (ErrorCode.SUCCESS == code) {
            this.isOnline = true;
        }
        log.debug("isOnline: {}>>>>>>>>>>>>>>>>>>>>>>>>>"+this.isOnline);
        return code;
    }

    /**
     * 设备下线
     * @return
     */
    public int offline() {
        if (!this.isOnline) {
            return ErrorCode.SUCCESS;
        }

        int code = MqttUtil.getInstance().offline(this.identifier);
        if (ErrorCode.SUCCESS == code ){
            this.isOnline = false;
        }

        return code;
    }

    /**
     * 上传设备属性数据到平台
     *
     * @param properties
     * @return
     */
    public int reportProperties(Map<String ,Object> properties) {
        if (null == properties || 0 == properties.size()) {
            return ErrorCode.ERROR_INVALID_PARAM;
        }
        if (!this.isOnline) {
            return ErrorCode.ERROR_DEVICE_UNREGISTER;
        }
        return MqttUtil.getInstance().reportProperties(this.identifier, properties);
    }


    /**
     * 上传设备事件到平台
     * @param eventName
     * @param outputData
     * @return
     */
//    public int reportEvents(String eventName, HashMap<String, Object> outputData) {
//        if (null == eventName || 0 == eventName.length()) {
//            return ErrorCode.ERROR_INVALID_PARAM;
//        }
//
//        if (null == outputData || 0 == outputData.size()) {
//            return ErrorCode.ERROR_INVALID_PARAM;
//        }
//
//        if (!this.isOnline) {
//            return ErrorCode.ERROR_DEVICE_UNREGISTER;
//        }
//
//        return MqttUtil.getInstance().proxyReportEvents(this.identifier,eventName, outputData);
//    }


}
