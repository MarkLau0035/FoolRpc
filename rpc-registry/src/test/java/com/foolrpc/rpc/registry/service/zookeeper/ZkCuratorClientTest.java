package com.foolrpc.rpc.registry.service.zookeeper;

import com.foolrpc.rpc.registry.config.ZookeeperConfig;
import com.foolrpc.rpc.registry.zookeeper.ZkCuratorClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertTrue;

/**
 * ZkCuratorClientTest
 *
 * @author luolinyuan
 * @date 2022/4/30
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ZookeeperConfig.class, ZkCuratorClient.class})
@EnableAutoConfiguration
public class ZkCuratorClientTest {

	@Autowired
	private ZkCuratorClient client;


	@Test
	public void testCreatePersistentNode() throws Exception {
		String path = "/persistent";
		client.createPersistent(path);
		assertTrue(client.checkExists(path));
	}

	@Test
	public void testCreateEphemeralNode() {
		String path = "/ephemeral";
		client.createEphemeral(path, "test");
		assertTrue(client.checkExists(path));
	}

	@After
	public void destroy() {
		client.doClose();
	}

}
