package com.llynsyw.rpc;

/**
 * @Description 、
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
public interface Decoder {
    <T> T decode(byte[] bytes,Class<T> clazz);
}
