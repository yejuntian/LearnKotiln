package com.study.flutterhybrid.channel;


/**
 * Flutter  Native通信 | 混合开发
 * Author: CrazyCodeBoy
 * 技术博文：http://www.devio.org
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public interface IShowMessage {
    void onShowMessage(String message);
    void sendMessage(String message,boolean useEventChannel);
}
