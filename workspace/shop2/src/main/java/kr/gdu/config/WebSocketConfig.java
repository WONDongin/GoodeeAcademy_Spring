package kr.gdu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import kr.gdu.websocket.EchoHandler;


@Configuration
@EnableWebSocket  //웹소켓 처리.
public class WebSocketConfig implements WebSocketConfigurer{
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	registry.addHandler(new EchoHandler(),"chatting")
	.setAllowedOrigins("*"); 
  }
}