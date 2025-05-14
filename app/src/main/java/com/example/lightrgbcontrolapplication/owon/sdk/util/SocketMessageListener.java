package com.example.lightrgbcontrolapplication.owon.sdk.util;

public interface SocketMessageListener {

    void getMessage(int commandID, Object bean);
}