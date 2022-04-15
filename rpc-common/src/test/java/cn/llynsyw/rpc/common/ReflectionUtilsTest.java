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
    JavaTest java;
    Method[] met1;
    @Before
    public void setUp()  {
        //每个test方法之前执行，可以在这里创建需要的对象
        java = new JavaTest();
        met1 = ReflectionUtils.getPublicMethods(JavaTest.class);
    }

    @After
    public void tearDown() {
        //每个test方法之后执行
        System.out.println("单测已经执行完毕");
    }

    @Test
    public void newInstance() {
        //测试能否通过Class对象生成实例
        JavaTest java2 = ReflectionUtils.newInstance(JavaTest.class);
        assertEquals(java.getClass(),java2.getClass());
    }

    @Test
    public void getPublicMethods() {
        //有三个public方法，这里断言数量是否为三
        Method[] met = ReflectionUtils.getPublicMethods(JavaTest.class);
        assertEquals(3,met.length);
    }

    @Test
    public void invoke() {
        //方法一为求和，这里比较返回的对象
        Object sum = ReflectionUtils.invoke(java,met1[0],1,2);
        assertEquals(3,sum);
    }
}
