package com.FoolRpc.rpc.registry.zookeeper;

import java.util.List;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/4/30
 **/
public interface ChildListener {

	void childChanged(String path, List<String> children);

}