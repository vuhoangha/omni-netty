package io.github.vuhoangha.netty;

public class AuthData {

    // đã được xác thực chưa
    public boolean isAuth;

    // uid người gửi req
    public long uid;


    public void clear(){
        isAuth = false;
        uid = -1;
    }

}
