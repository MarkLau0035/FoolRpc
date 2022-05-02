package cn.llynsyw.rpc.registry.zookeeper;

import cn.llynsyw.rpc.common.util.NetAddressUtil;
import cn.llynsyw.rpc.registry.config.ZookeeperConfig;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;

/**
 * TODO
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
	public void testCreatePersistentNode() throws ExecutionException, InterruptedException {
		String path = "/persistent";
		client.createPersistent(path, NetAddressUtil.getPubIp());
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
