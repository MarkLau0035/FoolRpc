package com.llynsyw.rpc.impl;

import com.alibaba.fastjson.JSON;
import com.llynsyw.rpc.Decoder;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
public class JSONDecoder implements Decoder {
    @Override
    public <T> T decode(byte[] bytes,Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}
