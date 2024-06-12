package io.github.vuhoangha.netty;

/**
 * tạo riêng để phân biệt exception cho dễ
 */
public class NettyCustomException extends RuntimeException {

    // Constructors
    public NettyCustomException() {
        super();
    }

    public NettyCustomException(String message) {
        super(message);
    }

    public NettyCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyCustomException(Throwable cause) {
        super(cause);
    }

}
