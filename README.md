RPC可以分为两部分：用户调用接口 + 具体网络协议。前者为开发者需要关心的，后者由框架来实现。

举个例子，我们定义了一个简单计算器接口CalcService，里面包含了加法运算add(int,int),减法运算minus(int,int)，并编写其实现类把该服务注册到服务端中以待调用，示例代码如下：

首先在idea中使用version control直接将项目导入

![image-20220411101326963](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411101326963.png)

![image-20220411101451534](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411101451534.png)

然后在右侧栏中的maven中点击Reload All Maven Projects以便下载依赖

![image-20220411101917353](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411101917353.png)

然后在最外层(root层)点击install将该项目安装到本地仓库，这样在其他项目就可以使用maven导入该项目进行使用

![image-20220411102217393](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411102217393.png)

success代表成功装入本地仓库，为进一步确认可以在本地仓库(默认在用户家目录下.m2/cn/llynsyw)中查看是否存在以下文件

![image-20220411102419611](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411102419611.png)

然后新建一个Empty project，然后分别新建两个模块，选择maven

![image-20220411103043486](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411103043486.png)

![image-20220411103118202](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411103118202.png)

![image-20220411103407574](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411103407574.png)

同理新建一个客户端模块，新建完成后目录结构如下

![image-20220411103739128](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411103739128.png)

既然要使用rpc框架就需要在pom.xml中引入相应的依赖

- 在server模块的pom.xml中添加

```xml
 <dependencies>
        <dependency>
            <groupId>cn.llynsyw</groupId>
            <artifactId>rpc-server</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

- 在client模块中的pom.xml添加

```xml
<dependencies>
        <dependency>
            <groupId>cn.llynsyw</groupId>
            <artifactId>rpc-client</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

之后同样点击maven reload all 刷新一下

下面就可以开始使用该rpc框架了，比如现在定义一个计算器服务

```java
package cn.llynsyw.stub;

public interface Calculator {
	
	int add(int a, int b);

	int minus(int a, int b);

}
```

相应的给出实现类

```java
package cn.llynsyw.stub.impl;

import cn.llynsyw.stub.Calculator;

/**
 * TODO
 * @author luolinyuan
 * @date 2022/4/11
 **/
public class MyCalculator implements Calculator {
	@Override
	public int add(int a, int b) {
		return a + b;
	}

	@Override
	public int minus(int a, int b) {
		return a - b;
	}
}
```

然后就是将这个服务注册到rpc服务端中

~~~java
package cn.llynsyw.boot;

import RpcServer;
import cn.llynsyw.stub.Calculator;
import cn.llynsyw.stub.impl.MyCalculator;

/**
 * rpc服务启动类
 * @author luolinyuan
 * @date 2022/4/11
 **/
public class Server {
	public static void main(String[] args) {
		/*新建一个rpc服务*/
		RpcServer server = new RpcServer();
		/*注册计算器服务*/
		server.register(Calculator.class,new MyCalculator());
		/*启动服务*/
		server.start();
	}
}
~~~

以上代码目录结构

![image-20220411133154366](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411133154366.png)

这就相当于注册了一个计算器服务，注册服务时具体的实现类可以使用反射或者配合spring进行动态注入(后续优化点)，

而在客户端中，因为需要确定需要调用什么服务这个服务，所以同样需要有一个这样Calculator的接口，而且其所在的包名路径必须与服务端的包路径一直，这个接口就可以称为客户端的一个服务存根，在这里直接将Calculator复制到相应包下即可，然后模拟一个client进行调用

```java
package cn.llynsyw.boot;

import cn.llynsw.rpc.client.impl.RpcClientDirectConnectImpl;
import cn.llynsyw.stub.Calculator;

/**
 * TODO
 * @author luolinyuan
 * @date 2022/4/11
 **/
public class Client {
	public static void main(String[] args) {
		/*通过RpcClient发生调用请求*/
		RpcClient client = new RpcClient();
		Calculator proxy = client.getProxy(Calculator.class);

		int addResult = proxy.add(10, 18);
		int minusResult = proxy.minus(100, 7);

		System.out.println("addResult: " + addResult);
		System.out.println("minusResult: " + minusResult);

	}
}
```

![image-20220411133807077](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411133807077.png)

- 测试启动
    - 首先启动服务端
    - 然后启动客户端

![image-20220411140001206](https://llynsyw.oss-cn-beijing.aliyuncs.com/img/image-20220411140001206.png)

