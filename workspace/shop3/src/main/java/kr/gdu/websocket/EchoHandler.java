package kr.gdu.websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Component
public class EchoHandler extends TextWebSocketHandler 
                                      implements InitializingBean {
	private Set<WebSocketSession> clients = new HashSet<WebSocketSession>();
	//클라이언트로부터 요청되어 연결 완료
	@Override  
	public void afterConnectionEstablished(WebSocketSession session) 
			                                             throws Exception {
		//session : 웹소켓 접속 객체의 고유한 ID 값
		super.afterConnectionEstablished(session);
		System.out.println("클라이언트 접속 : " + session.getId());
		clients.add(session); //HashSet에 접속 객체 저장
	}
	//메세지가 수신된 경우
	@Override 
	public void handleMessage
	(WebSocketSession session,WebSocketMessage<?> message) throws Exception {
		//loadMessage : 클라이언트가 전송한 메세지. 
		String loadMessage = (String)message.getPayload();
		System.out.println(session.getId()+":클라이언트 메세지:"+loadMessage);
		clients.add(session); //클라이언트를 clients에 추가
		for(WebSocketSession s : clients) {
			//s : 접속된 클라이언트 객체
			s.sendMessage(new TextMessage(loadMessage)); //모든 클라이언트에 전송
		}
	}
	@Override 
	public void handleTransportError
	  (WebSocketSession session,Throwable exception) throws Exception {
		super.handleTransportError(session,exception);
		System.out.println("오류발생 : " + exception.getMessage());
	}
	@Override
	public void afterConnectionClosed
	 (WebSocketSession session,	CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		System.out.println("클라이언트 접속 해제 : " + status.getReason());
		clients.remove(session);
	}
	@Override
	public void afterPropertiesSet() throws Exception {	}
}