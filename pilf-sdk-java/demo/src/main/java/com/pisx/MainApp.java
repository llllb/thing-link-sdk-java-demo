package com.pisx;

import com.alibaba.fastjson.JSON;
import com.pisx.util.DataFormatter;
import com.pisx.util.MqttUtil;

import java.util.HashMap;
import java.util.Map;

public class MainApp {
    //设备唯一识别号
    public static final String IDENTIFIER = "2c9480ed71b4ee7f0171b4ee7fe20000";

    //测试数据
    public static final String MESSAGECONTENT = "{\n  \"timestamp\" : 1600756505311,\n  \"values\" : [ {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 35,\n    \"q\" : true,\n    \"t\" : 1600756496041\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 34,\n    \"q\" : true,\n    \"t\" : 1600756496041\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 9,\n    \"q\" : true,\n    \"t\" : 1600756497037\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 67,\n    \"q\" : true,\n    \"t\" : 1600756497037\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : -1,\n    \"q\" : true,\n    \"t\" : 1600756498042\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 99,\n    \"q\" : true,\n    \"t\" : 1600756498042\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : -9,\n    \"q\" : true,\n    \"t\" : 1600756499042\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 63,\n    \"q\" : true,\n    \"t\" : 1600756499042\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 95,\n    \"q\" : true,\n    \"t\" : 1600756500035\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 47,\n    \"q\" : true,\n    \"t\" : 1600756500035\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 59,\n    \"q\" : true,\n    \"t\" : 1600756501036\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 7,\n    \"q\" : true,\n    \"t\" : 1600756501036\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 91,\n    \"q\" : true,\n    \"t\" : 1600756502036\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : -12,\n    \"q\" : true,\n    \"t\" : 1600756502036\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 55,\n    \"q\" : true,\n    \"t\" : 1600756503039\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 37,\n    \"q\" : true,\n    \"t\" : 1600756503039\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : -16,\n    \"q\" : true,\n    \"t\" : 1600756504041\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 87,\n    \"q\" : true,\n    \"t\" : 1600756504041\n  }, {\n    \"id\" : \"bbb.Random1\",\n    \"v\" : 60,\n    \"q\" : true,\n    \"t\" : 1600756505039\n  }, {\n    \"id\" : \"aaa.Ramp1\",\n    \"v\" : 51,\n    \"q\" : true,\n    \"t\" : 1600756505039\n  } ]\n}";

    public static void main(String... args) {
        //实例化一个设备
        ThingSample device = new ThingSample(IDENTIFIER);
        Map<String, Object> stringObjectMap = DataFormatter.formatData(MESSAGECONTENT);
        //上传设备属性数据
        device.reportProperties(stringObjectMap);
    }

}
