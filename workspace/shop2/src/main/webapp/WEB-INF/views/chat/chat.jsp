<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/views/chat/chat.jsp --%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<c:set var="port" value="${pageContext.request.localPort}" /> <%-- 8080 : port --%>
<c:set var="server" value="${pageContext.request.serverName}" /> <%-- localhost --%>
<c:set var="path" value="${pageContext.request.contextPath}" /> <%-- / --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>
<script type="text/javascript">
 $(function(){
	 //192.168.7.234
	 //서버에서 수신할 수 있는 준비 필요
   let ws = new WebSocket("ws://${server}:${port}${path}/chatting")
   //서버와 연결 성공된 경우 
   ws.onopen = function() {
	  $("#chatStatus").text("info:connection opened") 
	  //입력칸에 keydown 이벤트 등록
	  $("input[name=chatInput]").on("keydown",function(evt){
		  if(evt.keyCode == 13) { //Enter키 입력
			  //msg : 입력한 내용
			  let msg = $("input[name=chatInput]").val()
			  ws.send(msg) //서버로 전송
			  //입력칸(input 태그)의 값을 초기화
			  $("input[name=chatInput]").val("")
		  }
	  })
   }	 
	// 서버로부터 메세지 수신 
   ws.onmessage = function(event) {
	   $("textarea").eq(0).prepend(event.data+"\n")
   }
	//서버와 접속 종료시
   ws.onclose = function(event){
	   $("#chatStatus").text("info:connection close")
   }
 })
</script></head>
<body>
<p><div id="chatStatus"></div>
<textarea name="chatMsg" rows="15" cols="40" class="w3-input"></textarea><br>
메시지입력:<input type="text" name="chatInput" class="w3-input">
</body></html>