import cn.llynsyw.rpc.common.ProtoUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/4/14
 **/
public class TestProtoUtils {
	@Test
	public void testGenerateRequest() {

	}

	@Test
	public void testObjectToBytes() throws IOException, ClassNotFoundException {
		System.out.println(ProtoUtils.bytesToObject(ProtoUtils.objectToBytes(11)));
	}

}
