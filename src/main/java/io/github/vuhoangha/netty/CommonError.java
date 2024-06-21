package io.github.vuhoangha.netty;

import lombok.Getter;

/**
 * Các lỗi chung chung thường thấy
 */
@Getter
public enum CommonError {


    BAD_REQUEST((short) 400),                           // body/param invalid
    UNAUTHORIZED((short) 401),                          // token/api_key invalid
    FORBIDDEN((short) 403),                             // ko có quyền truy cập
    NOT_FOUND((short) 404),                             // ko tìm thấy (vd user_id, order_id...vvv)
    INTERNAL_SERVER_ERROR((short) 500);                 // lỗi nội bộ chung chung


    private final short code;


    CommonError(short code) {
        this.code = code;
    }


}
