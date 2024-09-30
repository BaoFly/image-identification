package com.bf.image.constant;

import com.bf.image.exception.CustomException;

public class RabbitConstant {

    public RabbitConstant() {
        throw new CustomException("实体类不能NEW对象");
    }

    public static final String TEST_DIRECT_MQ = "testDirectMQ";


    public static final String TEST_DELAY_MQ = "testDelayMQ";

}
