package io.github.vuhoangha.netty;

import lombok.Getter;
import lombok.Setter;

/**
 * tạo riêng để phân biệt exception cho dễ
 */
@Getter
@Setter
public class NettyCustomException extends RuntimeException {


    private ErrorCode errorCode;

    private String msg;


    public NettyCustomException(ErrorCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public NettyCustomException(ErrorCode errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
        this.msg = msg;
    }

}
