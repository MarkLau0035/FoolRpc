package cn.llynsyw.rpc.transport;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表示网络传输的一个结点
 *
 * @author luolinyuan
 */
@Data
@AllArgsConstructor
public class Peer {
    private String host;
    private int port;
}
