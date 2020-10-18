package com.pisx;

import cn.hutool.core.util.ObjectUtil;
import com.pisx.exception.ErrorCode;
import com.pisx.util.DataFormatter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@SuppressWarnings("InfiniteLoopStatement")
public class MainApp {
    //设备唯一识别号
    public static final String IDENTIFIER = "2c9480ed71b4ee7f0171b4ee7fe20000";

    //测试数据
    public static final String MESSAGECONTENT = "{\n  \"timestamp\" : 1600756505311,\n  \"values\" : [ {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 35,\n    \"q\" : true,\n    \"t\" : 1600756496041\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 34,\n    \"q\" : true,\n    \"t\" : 1600756496041\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 9,\n    \"q\" : true,\n    \"t\" : 1600756497037\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 67,\n    \"q\" : true,\n    \"t\" : 1600756497037\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : -1,\n    \"q\" : true,\n    \"t\" : 1600756498042\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 99,\n    \"q\" : true,\n    \"t\" : 1600756498042\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : -9,\n    \"q\" : true,\n    \"t\" : 1600756499042\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 63,\n    \"q\" : true,\n    \"t\" : 1600756499042\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 95,\n    \"q\" : true,\n    \"t\" : 1600756500035\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 47,\n    \"q\" : true,\n    \"t\" : 1600756500035\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 59,\n    \"q\" : true,\n    \"t\" : 1600756501036\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 7,\n    \"q\" : true,\n    \"t\" : 1600756501036\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 91,\n    \"q\" : true,\n    \"t\" : 1600756502036\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : -12,\n    \"q\" : true,\n    \"t\" : 1600756502036\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 55,\n    \"q\" : true,\n    \"t\" : 1600756503039\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 37,\n    \"q\" : true,\n    \"t\" : 1600756503039\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : -16,\n    \"q\" : true,\n    \"t\" : 1600756504041\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 87,\n    \"q\" : true,\n    \"t\" : 1600756504041\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 60,\n    \"q\" : true,\n    \"t\" : 1600756505039\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 51,\n    \"q\" : true,\n    \"t\" : 1600756505039\n  } ]\n}";

    public static void main(String[] args) {
        //实例化一个设备
        ThingSample thingSample = new ThingSample(IDENTIFIER);
        ArrayList<ThingSample> deviceList = new ArrayList<>();
        Map<String, Object> propertyDataMap = DataFormatter.formatData(MESSAGECONTENT);
        if (ObjectUtil.isNotNull(thingSample) && ObjectUtil.isNotEmpty(thingSample)) {
            if (ErrorCode.SUCCESS == thingSample.online()) {
                deviceList.add(thingSample);
            }
        }
        while (true) {
            for (ThingSample device : deviceList) {
                //上传设备属性数据
                device.reportProperties(propertyDataMap);
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    log.error("线程休眠时发生异常:｛｝ " + e);
                }
            }
        }
    }
}

