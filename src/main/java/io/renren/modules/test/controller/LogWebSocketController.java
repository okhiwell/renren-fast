package io.renren.modules.test.controller;

import io.renren.modules.test.utils.LoggerMessage;
import io.renren.modules.test.utils.LoggerQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.PostConstruct;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.Executors.*;

/**
 * @author CT
 * @date 2019/7/29:39
 * @describe LogWebSocketController用于
 * Xcloud-Api By IDEA
 * 配置WebSocket消息代理端点，即stomp服务端
 * 为了连接安全，setAllowedOrigins设置的允许连接的源地址
 * 如果在非这个配置的地址下发起连接会报403
 * 进一步还可以使用addInterceptors设置拦截器，来做相关的鉴权操作
 * Created by LaoWang on 2018/8/25.
 */

@Slf4j
@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class LogWebSocketController extends AbstractWebSocketMessageBrokerConfigurer {

    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/logwebsocket")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 推送日志到/topic/pullLogger
     */
    @PostConstruct
    public void pushLogger(){
        ExecutorService executorService= newFixedThreadPool(2);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        LoggerMessage log = LoggerQueue.getInstance().poll();
                        if(log!=null){
                            if(messagingTemplate!=null){
                                messagingTemplate.convertAndSend("/topic/pullLogger",log);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        executorService.submit(runnable);
    }
}
