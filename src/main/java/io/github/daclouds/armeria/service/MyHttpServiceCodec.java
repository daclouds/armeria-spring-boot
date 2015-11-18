package io.github.daclouds.armeria.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EnumSet;
import java.util.Set;

import com.linecorp.armeria.common.Scheme;
import com.linecorp.armeria.common.SerializationFormat;
import com.linecorp.armeria.common.ServiceInvocationContext;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.ServiceCodec;

class MyHttpServiceCodec implements ServiceCodec {

    private static final Set<SessionProtocol> ALLOWED_PROTOCOLS = EnumSet.of(
            SessionProtocol.H1, SessionProtocol.H1C,
            SessionProtocol.H2, SessionProtocol.H2C,
            SessionProtocol.HTTP, SessionProtocol.HTTPS);

    private final String loggerName;

    MyHttpServiceCodec(String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public DecodeResult decodeRequest(Channel ch, SessionProtocol sessionProtocol, String hostname,
                                      String path, String mappedPath, ByteBuf in,
                                      Object originalRequest, Promise<Object> promise) throws Exception {

        if (!ALLOWED_PROTOCOLS.contains(sessionProtocol)) {
            throw new IllegalStateException("unsupported session protocol: " + sessionProtocol);
        }

        return new MyHttpServiceInvocationContext(
                ch, Scheme.of(SerializationFormat.NONE, sessionProtocol),
                hostname, path, mappedPath, loggerName, (FullHttpRequest) originalRequest);
    }

    @Override
    public boolean failureResponseFailsSession(ServiceInvocationContext ctx) {
        return true;
    }

    @Override
    public ByteBuf encodeResponse(ServiceInvocationContext ctx, Object response) throws Exception {
//        throw new IllegalStateException("unsupported message type: " + response.getClass().getName());
		return Unpooled.wrappedBuffer(response.toString().getBytes(CharsetUtil.UTF_8));
    }

    @Override
    public ByteBuf encodeFailureResponse(ServiceInvocationContext ctx, Throwable cause) throws Exception {
        final StringWriter sw = new StringWriter(512);
        final PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        pw.flush();
        return Unpooled.wrappedBuffer(sw.toString().getBytes(CharsetUtil.UTF_8));
    }
}
