<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- /WEB-INF/views/board/detail.jsp
  http://localhost:8080/board/detail?num=1 요청
    - service.getBoard(num)
      boardDao.selectOne(num) 
      num 파라미터에 해당 게시물을 정보를 db에서 읽어 Board 객체에 저장
    - 조회 수 증가
      service.addReadcnt(num)
      boardDao.addReadcnt(num)
      
    Controller, BoardService, BoardDao, BoardMapper 구현하기    
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>게시물 상세보기</title>
<style type="text/css">
  .leftcol {	text-align: left;	vertical-align : top;  }
  .lefttoptable {  height : 250px;	    border-width: 0px;
	text-align: left;	vertical-align : top;	padding: 0px; }
</style></head><body>
<table class="w3-table"><tr><td colspan="2">${boardName}</td></tr>
   <tr><td width="15%">글쓴이</td>
       <td width="85%" class="leftcol">${board.writer}</td></tr>
   <tr><td>제목</td><td class="leftcol">${board.title}</td></tr>
   <tr><td>내용</td><td class="leftcol">
     <table class="lefttoptable">
   <tr><td class="leftcol lefttoptable">${board.content}</td></tr></table></td></tr>
   <tr><td>첨부파일</td><td>&nbsp;
    <c:if test="${!empty board.fileurl}">
     <a href="file/${board.fileurl}">${board.fileurl}</a>
    </c:if></td></tr>
   <tr><td colspan="2">
     <a href="reply?num=${board.num}">[답변]</a>
     <a href="update?num=${board.num}&boardid=${board.boardid}">[수정]</a>
     <a href="delete?num=${board.num}&boardid=${board.boardid}">[삭제]</a>
     <a href="list?boardid=${board.boardid}">[게시물목록]</a>
   </td></tr></table>
 </body></html>