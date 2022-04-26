# FoolRpc

## Rpc基本介绍

### 概述

Rpc：`Remote Procedure Call`，即`远程过程调用`，是分布式系统常见的通信方法。

- 远程：从`跨进程`到`跨物理机`的两个服务相互访问都能称之为远程。
- 过程：通俗来说在JAVA中的方法称之为过程。
- 调用：即跨进程调用方法。

Rpc的优势：可以像调用本地方法一样调用远程方法

### 原理

![image1](https://raw.githubusercontent.com/sqilxvelr/demo/master/Image/QQ%E5%9B%BE%E7%89%8720220424223319.png)

- Server：服务提供者，暴露服务	
- Client：服务消费者，调用远程服务
- Stub：存根、服务描述，服务注册与发现

流程：

1. Server将暴露的服务和地址信息注册到Registry
2. Client订阅Registry
3. Registry将Client关注的服务通知给Client
4. Client根据获得的信息调用需要的服务

调用（call）过程：

![image2](https://raw.githubusercontent.com/sqilxvelr/demo/master/Image/QQ%E5%9B%BE%E7%89%8720220424223401.png)

1. Client从存根中调用需要的方法
2. 序列化
3. 网络传输
4. 反序列化
5. 找到接口具体实现类的对象，通过反射调用方法
6. 调用方法后拿到计算的结果
7. 序列化
8. 网络传输
9. 反序列化
10. 调用完成

## 模块作用

### common模块

​	存放通用的工具方法。

- ReflectionUtils类：反射工具类
  - newInstance方法：根据传入的Class创建一个对象
  - getPublicMethods方法：获取传入Class的所有公共方法
  - invoke方法：调用某个对象指定的方法
- RegistryUtils类：注册工具类
  - serviceRegister(ServiceDescriptor)方法：根据传入的Class和Method生成相应的ServiceDescriptor

### proto模块

​	存放Client和Server的协议。

- ServiceDescriptor类：表示服务的类
  - clazz(String)：服务的类名
  - method(String)：服务类中的方法名
  - returnType(String)：方法返回类型
  - parameterTypes(String[])：方法参数类型
- Request类：表示Rpc的一个请求
  - service(ServiceDescriptor)：请求的服务
  - paremeters(Object[])：传入的参数
- Response类：表示Rpc的返回
  - code(int)：服务返回的编码，0表示成功，非0表示失败
  - message(String)：具体的错误信息
  - data(Object)：返回的数据

### transport模块

​	网络通信模块。

- peer类：用来表示网络传输的一个端点
  - host(String)：网络地址
  - port(int)：端口号
- TransportClient接口：客户端
  - connect(void)方法：与传入的网络端点Peer创建连接
  - write(InputStream)方法：写数据
  - close(void)方法：关闭资源
- TransportServer接口：服务端
  - start(void)方法：启动监听
  - stop(void)方法：停止监听
  - init(void)方法：初始化时将监听端口port和请求处理器handler传入
- RequsetHandler接口：处理网络请求的字节流
  - onRequest(void)方法：接受字节流InputStream，处理后输出字节流OutputStream
- HTTPTransportClient类：实现Client
  - url(String)：端点的url
  - connect(void)方法：根据传入的端点Peer获取url
  - write(InputStream)方法：使用HttpURLConnection建立连接，将传入的data数据发送给server，并调用HttpURLConnection对象的getInputOutStream方法返回得到的数据
- HTTPTransportServer类：实现Server
  - handler(RequestHandler)：保存请求处理器handler
  - server(Server)：jetty的server
  - init(void)方法：根据传入的handler获得handler，根据传入的port设置server；使用ServletContextHandler接受请求，使用jetty接受网络请求的抽象ServletHolder，传入新的内部类Servlet对象；将holder传入ServletContextHandler处理所有路径<img src="C:\Users\22574\AppData\Roaming\Typora\typora-user-images\image-20220424014125702.png" alt="image-20220424014125702" style="zoom: 67%;" />
  - RequestServlet内部类：重写Post方法，设置接受Client传来的数据流in，并设置返回数据的通道out；调用handler的onRequest方法，传入设置的in和out处理并输出字节流
  - start(void)方法：启动server
  - stop(void)方法：停止server

### server模块

​	服务端

- RpcServerConfig类：server配置
  - transportClass(Class<? extends TransportServer>)：使用的网络模块
  - port(int)：监听的端口
- ServiceInstance类：表示一个具体的服务
  - target(Object)：提供服务的对象
  - method(Method)：服务对应的方法
- ServiceManager类：管理Rpc暴露的服务
  - service(Map<ServiceDescriptor,ServiceInstance>)：注册的服务
  - register(void)方法：根据传入的注册服务的接口interfaceClass和接口的实现类bean来注册服务，具体为：通过ReflectionUtils中的方法扫描出接口中所有的方法，和实现接口的类进行绑定，形成一个ServiceInstance实例，再通过RegistryUtils类生成对应的ServiceDescriptor，放入service的Map中
  - lookup(ServiceInstance)方法：根据传入的Request查找对应的service
- ServiceInvoker类：调用具体的服务
  - invoke(Object)方法：使用ReflectionUtils中的invoke()方法调用服务
- RpcServer类：Server类
  - 基于config的构造方法中：传入RpcServiceConfig，根据config的端口号port和handler初始化TransportServer，创建ServiceManager和ServiceInvoker
  - register(void)方法：注册方法，通过ServiceManager进行注册
  - start(void)方法：启动网络模块TransportServer
  - stop(void)方法：停止网络模块TransportServer
  - RequestHandler.onRequest方法：接受Request的二进制数据InputStream，反序列化，通过ServiceInvoker调用相应服务，再序列化，得到的结果通过OutputStream写回客户端

### client模块

​	客户端

- TransportSelect接口：表示选择哪个Server去连接
- RandomTransportSelector类：实现随机选择Server
  - clients(List<TransportClient>)：存储已连接的client
  - init(void)方法：通过传入的Server端点信息peer和客户端的实现类TransportClient建立连接（HTTPTransportClient.connect()方法），初始化selector
  - select(TransportClient)方法：随机选择一个TransportClient与Server做交互
  - release(void)方法：选择完成后将该Client释放，重新放入List中
  - close(void)方法：关闭所有的Client并清空List
- RpcClientConfig类：Client的配置
  - transportClass(Class<? extends TransportClient>)：实现的网络模块
  - selectorClass(Class<? extends TransportServer>)：路由选择
  - connectCount(int)：一个端点建立的连接数
  - servers(List<Peer>)：可以连接的端点
- RpcClient类：客户端Client类
  - 基于config的构造方法：设置config，基于config中的Selector生成并初始化Selector（设置servers、connectCounts、transportClass）
  - getProxy(<T>)方法：根据传入的class和RemoteInvoker获取代理对象
- RemoteInvoker类：调用远程服务的代理类
  - 构造方法：通过传入的class选择访问哪个类，以及路由选择
  - invoke(Object)方法：构造一个请求Request，通过invokeRemote方法发送给Server，等待响应，并从响应中获取返回的数据
  - invokeRemote(Response)方法：通过Select选择一个Client，将传入的Request序列化为byte数组，使用client.write将byte数组写入，并接收返回的数据后反序列化后作为Respose返回

## 运行流程

### 创建Client

- 向构造方法中传入配置信息RpcClientConfig
- 通过配置信息中的路由选择器SelectorClass使用反射工具ReflectionUtils中的创建对象方法newInstance创建路由选择器TransportSelector
- 通过配置信息中的端点列表Services、连接数量ConnectCount、网络模块TransportClient来初始化路由选择器，创建好服务器连接池List<TransportClient>

### 创建Server

- 通过构造方法传入配置信息RpcServerConfig
- 通过配置信息RpcServerConfig中的网络模块TransportServer使用反射工具ReflectionUtils中的创建对象方法newInstance创建服务端对象TransportServer
- TransportServer对象初始化：传入配置信息中的端口号和请求处理器handler进行初始化，创建Servlet，Holder
- 创建服务管理器ServiceManager
- 创建调用的服务ServiceInvoker

### Server注册服务

- RpcService调用register方法：通过服务管理器ServiceManager中的register方法，传入要注册的方法接口和其的实现类，通过反射工具类中获取所有公共方法的方法getPublicMethods，获取传入接口的所有公共方法，再将每一个公共方法与实现类绑定并生成一个服务实例ServiceInstance，再通过注册工具类RegistryUtils中的注册方法serviceRegister将接口和方法绑定生成服务描述ServiceDescriptor，将服务实例和服务描述作为k-v放入服务管理器中的所有服务记录service中

### Server开启服务

- RpcServer调用网络模块TransportServer的start方法，启动server线程

### Client通过代理调用服务

- RpcClient调用代理服务getProxy，将要调用的方法的对象传入
- 根据传入的对象和Client的路由选择器生成代理对象
- Client通过代理对象调用响应的方法
- 代理对象调用方法的方法invoke，传入具体方法和参数；根据传入的方法名和参数生成请求Request，把Request传入远程调用方法invokeRemote
- invokeRemote方法：
  - 创建网络模块中的客户端TransportClient，使用路由选择器选择一个客户端
  - 将传入的请求消息Request序列化
  - 调用客户端的write方法写入二进制数据并获得返回数据
  - 将返回数据反序列化成响应消息Response
  - 返回响应消息Response
- invoke方法返回响应消息Response