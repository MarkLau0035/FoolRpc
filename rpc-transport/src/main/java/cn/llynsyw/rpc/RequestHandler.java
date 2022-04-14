package cn.llynsyw.rpc;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
public interface RequestHandler {
    void onRequestSteam(InputStream receive, OutputStream toResponse);
}
