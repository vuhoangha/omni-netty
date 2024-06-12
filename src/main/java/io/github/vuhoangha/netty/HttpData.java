package io.github.vuhoangha.netty;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * chứa tất cả thông tin về request gửi lên. Cần được chứa lại trong pool để lấy ra xử lý khi cần
 */
@Getter
@Setter
@Accessors(chain = true)
public class HttpData {

    // chứa thông tin path và query string dạng thô
    private final URI uri = new URI();

    // thông tin xác thực
    private final AuthData authData = new AuthData();

    // body request
    private Object body;


    public void clear() {
        uri.clear();
        authData.clear();
        body = null;
    }


}