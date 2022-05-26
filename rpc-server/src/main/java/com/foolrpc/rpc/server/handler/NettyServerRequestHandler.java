package com.foolrpc.rpc.server.handler;

import com.foolrpc.rpc.common.bean.ServiceInstance;
import com.foolrpc.rpc.protocol.Request;
import com.foolrpc.rpc.protocol.Response;
import com.foolrpc.rpc.protocol.util.ProtoUtils;
import com.foolrpc.rpc.transport.RequestHandler;
import com.foolrpc.rpc.transport.exception.NoStreamException;
import com.foolrpc.rpc.transport.server.NettyTransportServer;
import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foolrpc.rpc.registry.service.ServiceManager;
import com.foolrpc.rpc.server.impl.ServiceInvoker;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;


/**
 * @author guokun
 * @date 2022/5/26 9:59
 */
@Slf4j
@Component("nettyServerRequestHandler")
@ChannelHandler.Sharable
public class NettyServerRequestHandler extends ChannelInboundHandlerAdapter implements RequestHandler {
    private ServiceManager serviceManager;

    private ServiceInvoker serviceInvoker;

    @Autowired
    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Autowired
    public void setServiceInvoker(ServiceInvoker serviceInvoker) {
        this.serviceInvoker = serviceInvoker;
    }

    @Override
    @Deprecated
    public void onRequestStream(InputStream receive, OutputStream toResponse) throws Exception{
        throw new NoStreamException();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            Response resp = null;
            Request request = null;

            try {
                log.debug(fullHttpRequest.uri());
                ByteBuf content = fullHttpRequest.content();
                byte[] inBytes = new byte[content.readableBytes()];
                content.readBytes(inBytes);
                request = ProtoUtils.generateRequest(inBytes);
                List<ByteString> parameterList = request.getParameterList();
                Object[] parameters = new Object[parameterList.size()];
                for (int i = 0; i < parameterList.size(); i++) {
                    parameters[i] = ProtoUtils.bytesToObject(parameterList.get(i).toByteArray());
                }
                log.info("get request:\n{}parameters:{}", request.getServiceDescriptor(), parameters);
                ServiceInstance sis = serviceManager.lookup(request.getServiceDescriptor().toString());

                Object ret = serviceInvoker.invoke(sis, parameters);
                resp = ProtoUtils.generateResponse(ret);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                String message = ("RpcServer got error"
                        + e.getClass().getName()
                        + " : " + e.getMessage());
                resp = ProtoUtils.generateResponse( Response.Code.RPC_INVALID_RESPONSE_VALUE, message);
            } finally {
                byte[] outBytes = null;
                DefaultFullHttpResponse response = null;
                // 返回响应
                if (resp == null){
                    outBytes = "".getBytes(StandardCharsets.UTF_8);
                    response =
                            new DefaultFullHttpResponse(fullHttpRequest.protocolVersion(), HttpResponseStatus.BAD_REQUEST);
                } else {
                    outBytes = resp.toByteArray();
                    response =
                            new DefaultFullHttpResponse(fullHttpRequest.protocolVersion(), HttpResponseStatus.OK);
                }

                response.headers().setInt(CONTENT_LENGTH, outBytes.length);
                response.content().writeBytes(outBytes);

                // 写回响应
                ctx.writeAndFlush(response);
                log.info("response client");
            }
        }
        ctx.fireChannelRead(msg);
    }
}
