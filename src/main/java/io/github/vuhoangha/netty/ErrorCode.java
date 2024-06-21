package io.github.vuhoangha.netty;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorCode {


    private short code;

    private HttpResponseStatus httpStatus;


    public static ErrorCode BAD_REQUEST = new ErrorCode((short) 400, HttpResponseStatus.BAD_REQUEST);                           // body/param invalid
    public static ErrorCode UNAUTHORIZED = new ErrorCode((short) 401, HttpResponseStatus.UNAUTHORIZED);                         // token/api_key invalid
    public static ErrorCode FORBIDDEN = new ErrorCode((short) 403, HttpResponseStatus.FORBIDDEN);                               // ko có quyền truy cập
    public static ErrorCode NOT_FOUND = new ErrorCode((short) 404, HttpResponseStatus.NOT_FOUND);                               // ko tìm thấy (vd user_id, order_id...vvv)
    public static ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode((short) 500, HttpResponseStatus.INTERNAL_SERVER_ERROR);       // lỗi nội bộ chung chung


}
