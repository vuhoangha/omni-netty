package io.github.vuhoangha.netty;

public interface IHandler {

    byte[] process(HttpData httpData);

}
