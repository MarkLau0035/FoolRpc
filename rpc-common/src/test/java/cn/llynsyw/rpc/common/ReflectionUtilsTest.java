package cn.llynsyw.rpc.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author lgc
 */
public class ReflectionUtilsTest {
    ProtoUtils pro;
    Method[] met1;
    @Before
    public void setUp()  {
        //每个test方法之前执行，可以在这里创建需要的对象
        pro = new ProtoUtils();
        met1 = ReflectionUtils.getPublicMethods(ProtoUtils.class);
    }

    @After
    public void tearDown() {
        //每个test方法之后执行
        System.out.println("单测已经执行完毕");
    }

    @Test
    public void newInstance() {
        //测试能否通过Class对象生成实例
        ProtoUtils pro2 = ReflectionUtils.newInstance(ProtoUtils.class);
        assertEquals(ProtoUtils.class,pro2.getClass());
    }
    @Test
    public void invoke() {
        //第五个方法为对象转字符数组，这里比较返回的对象
        //废弃
    }
}