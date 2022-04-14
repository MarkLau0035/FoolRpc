package cn.llynsyw.rpc;

import lombok.Data;

/**
 * 表示一个RPC响应
 *
 * @author luolinyuan
 */
@Data
public class Response {
    /**
     * 服务返回0成功，非零失败
     */
    private int code;
    /**
     * 具体错误信息
     */
    private String message;
    /**
     * 返回的数据
     */
    private Object data;
}
