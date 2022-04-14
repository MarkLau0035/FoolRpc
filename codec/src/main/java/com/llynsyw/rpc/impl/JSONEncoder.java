package com.llynsyw.rpc.impl;

import com.alibaba.fastjson.JSON;
import com.llynsyw.rpc.Encoder;

/**
 * @Description 基于JSON的序列化
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
public class JSONEncoder implements Encoder {
    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }
}
