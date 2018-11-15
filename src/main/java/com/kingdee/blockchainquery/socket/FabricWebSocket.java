package com.kingdee.blockchainquery.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/")
@Component
public class FabricWebSocket {

    private static Logger logger = LoggerFactory.getLogger(FabricWebSocket.class);
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<FabricWebSocket> webSocketSet = new CopyOnWriteArraySet<FabricWebSocket>();
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private int onlineCount = 0;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {

        try {
            this.session = session;
            webSocketSet.add(this);
            logger.info("webSocket的数量：{}", webSocketSet.size());
        } catch (Exception e) {
            logger.error("IO异常:{}", e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        //群发消息
        for (FabricWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                logger.error("收到客户端消息异常:{}", e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("webSocket错误异常:{}", error);
    }


    public void send(String flag) {

        logger.info("开始发送消息");
        logger.info("websocket数量:{}", webSocketSet.size());
        //群发消息
        for (FabricWebSocket item : webSocketSet) {
            try {
                logger.info("websocket开始发送消息");
                item.sendMessage(flag);
                logger.info("websocket发送消息结束");
            } catch (IOException e) {
                logger.info("{}", e);
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        if (this.session == null) {
            logger.error("session值为空");
        }
        this.session.getBasicRemote().sendText(message);
    }
}