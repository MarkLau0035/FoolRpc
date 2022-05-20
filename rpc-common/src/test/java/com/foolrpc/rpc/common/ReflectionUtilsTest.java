package com.foolrpc.rpc.common;

/**
 * @author lgc
 */
public class ReflectionUtilsTest {
 /*   RegistryUtils reg;//提供方法的实体
    Method[] met1;//提供参数方法
    Method[] met2;//提供方法
    @Before
    public void setUp()  {
        //每个test方法之前执行，可以在这里创建需要的对象
        reg = new RegistryUtils();
        met1 = ReflectionUtils.getPublicMethods(ProtoUtils.class);
        met2 = ReflectionUtils.getPublicMethods(RegistryUtils.class);
    }

    @After
    public void tearDown() {
        //每个test方法之后执行
        System.out.println("单测已经执行完毕");
    }

    @Test
    public void testNewInstance() {
        //测试能否通过Class对象生成实例
        ProtoUtils pro2 = ReflectionUtils.newInstance(ProtoUtils.class);
        assertEquals(ProtoUtils.class,pro2.getClass());
    }
    @Test
    public void testInvoke() {
        //RegistryUtils的方法
        Object ser = ReflectionUtils.invoke(reg,met2[0],ProtoUtils.class,met1[0]);
        assertEquals(ServiceDescriptor.class,ser.getClass());
    }*/
}