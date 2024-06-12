package io.github.vuhoangha.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HttpServer {


    private final int port;

    private final HttpRequestHandler httpRequestHandler;


    public HttpServer(int port, ExecutorService vExec, TriConsumer<String, String, HttpData> authHandler, Consumer<HttpData> httpDataConsumer, Supplier<HttpData> httpDataSupplier, ObjectMapper objectMapper) throws Exception {
        this.port = port;
        httpRequestHandler = new HttpRequestHandler(vExec, authHandler, httpDataConsumer, httpDataSupplier, objectMapper);
    }


    public void addRoute(String path, HttpMethod method, boolean requiresAuth, IHandler handler, Consumer<Object> bodyConsumer, Supplier<Object> bodySupplier) {
        httpRequestHandler.addRoute(path, method, requiresAuth, handler, bodyConsumer, bodySupplier);
    }


    public void start() throws Exception {

        // các worker chạy trên các logical core khác nhau
        // TODO đoạn này cần test lại và theo dõi thực tế
        ThreadFactory threadFactory = new AffinityThreadFactory("netty-worker", AffinityStrategies.DIFFERENT_CORE);

        // để lại 1 core cho bossGroup (xác thực req đầu vào) và hệ điều hành xử lý
        // TODO cần theo dõi và tối ưu thêm
        int numberWorkerCore = Runtime.getRuntime().availableProcessors() - 1;

        boolean isLinux = isLinux();
        EventLoopGroup bossGroup = isLinux ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = isLinux ? new EpollEventLoopGroup(numberWorkerCore, threadFactory) : new NioEventLoopGroup(numberWorkerCore, threadFactory);
        Class<? extends ServerChannel> clazz = isLinux ? EpollServerSocketChannel.class : NioServerSocketChannel.class;

        // TODO đoạn này nhớ tối ưu thêm
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(clazz)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpServerCodec());
                            p.addLast(new HttpContentCompressor()); // Tự động chọn gzip/deflate
                            p.addLast(new HttpObjectAggregator(65536));     // TODO tìm cách tái sử dụng đối tượng này
                            p.addLast(httpRequestHandler);
                        }
                    })
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // Sử dụng PooledByteBufAllocator
                    .childOption(ChannelOption.SO_KEEPALIVE, false)     // ko giữ kết nối này nếu xong
                    .childOption(ChannelOption.TCP_NODELAY, false); // Bật Nagle's algorithm;

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    private boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nix") || os.contains("nux") || os.contains("aix");
    }

}
