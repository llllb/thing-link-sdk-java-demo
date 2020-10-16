package com.pisx;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pisx.exception.ErrorCode;
import com.pisx.model.ThingData;
import com.pisx.model.VirtualThing;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 定义物模型
 * @author: boliu
 * @date: 2020年10月14日 14:07
 */
@Slf4j
public class ThingSample extends VirtualThing {

    private String propertyDefinitions;

    //private IotGatewayTopic iotGatewayTopic;

    public ThingSample(String identifier) {
        super(identifier);
        this.propertyDefinitions = getThingShap().get("propertyDefinitions").toString();
    }

    public String getPropertyDefinitions() {
        return propertyDefinitions;
    }

    public void setPropertyDefinitions(String propertyDefinitions) {
        this.propertyDefinitions = propertyDefinitions;
    }


    /**
     * 从设备获取属性发送到平台
     *
     * @param bind
     * @return
     */
    @Override
    public ThingData getProperties(String bind) {
        HashMap<String, Object> data = new HashMap<>();
        if ("true".equals(bind)) {
            data.put(propertyDefinitions, this.propertyDefinitions);
        }
        return new ThingData(ErrorCode.SUCCESS, data);
    }

    /**
     * 从平台获取属性设置到设备
     *
     * @param properties
     * @return
     */
    @Override
    public int setProperties(HashMap<String, Object> properties) {
        return super.setProperties(properties);
    }

    /**
     * 调用设备上的自定义方法
     *
     * @param methodName
     * @param params
     * @return
     */
    @Override
    public ThingData callService(String methodName, HashMap<String, Object> params) {
        log.info("自定义方法名称:" + methodName);
        HashMap<String, Object> map = new HashMap<>();
        //调用自定义方法
        long sum = sum(params.get("var1"), params.get("var2"));
        String sss = "" + sum;
        map.put("sum", sss);
        return new ThingData(ErrorCode.SUCCESS, map);
    }

    /**
     * 自定义方法
     *
     * @param a
     * @param b
     * @return
     */
    private long sum(Object a, Object b) {
        Long aValue = Long.valueOf(a.toString());
        Long bValue = Long.valueOf(b.toString());
        return aValue + bValue;
    }


    private JSONObject getThingShap() {
        //通过绝对路径读取json文件
        File file = FileUtil.file("thingShap.json");
        return JSONUtil.readJSONObject(file, Charset.defaultCharset());
    }




}
