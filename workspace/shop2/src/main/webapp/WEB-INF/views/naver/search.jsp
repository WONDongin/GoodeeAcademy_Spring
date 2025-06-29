<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/naver/search.jsp --%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>네이버 검색</title>
</head>
<body>
<select id="type" class="w3-select w3-border">
  <option value="blog">블로그</option>
  <option value="news">뉴스</option>
  <option value="book">책</option>
  <option value="encyc">백과사전</option>
  <option value="cafearticle">카페글</option>
  <option value="kin">지식인</option>
  <option value="local">지역</option>
  <option value="webkr">웹문서</option>
  <option value="image">이미지</option>
  <option value="shop">쇼핑</option>
  <option value="doc">전문자료</option>
</select>
페이지 별 검색 갯수 :
  <select id="display"><option>10</option><option>20</option>
  <option>50</option></select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
검색어 :<input type="text" id="data"  placeholder="제시어">
<button class="w3-btn w3-blue" onclick="naversearch(1)">검색</button>
<div id="result"></div>
<script type="text/javascript">
 function naversearch(start) {
   $.ajax({
	 type:"POST",
	 url:"naversearch",
	 data: {"data":$("#data").val(),  "display":$("#display").val(), 
		    "start":start,  "type":$("#type").val()},
	 success : function(json) { 
		 let total = json.total; //검색 총 건수
		 let html = "";
		 let num = (start-1) * $("#display").val() + 1;
		 let maxpage = Math.ceil(total / parseInt($("#display").val()))
		 let startpage = (Math.ceil(start/10) -1 ) * 10 + 1;
		 let endpage = startpage + 9;
		 if(endpage > maxpage) endpage = maxpage;
    html += "<table class='w3-table-all'><tr><td colspan='4' align='center'>"
  + " 전체 조회 건수:" + total+",  현재페이지:"+start +"/"+maxpage +"</td></tr>";
	     $.each(json.items, function(i,item){
			 html+="<tr><td>"+num+"</td><td>"+item.title +"</td><td>"
			 if($("#type").val() == 'image') {
				html += "<a href='"+item.link + 
				"'><img src='"+item.thumbnail +  "'></a><td>"
			 } else {	 
				html += "<a href='"+item.link + "'>"+item.link
					     + "</a></td><td>"+ item.description
				 }	
			     html += "</td></tr>";
			     num++;
	         })
			 html +="<tr><td colspan='4' align='center'>";
			 if(start > 1) {
	    html += "<a href='javascript:naversearch("+(start-1)+")'>[이전]</a>"
			 }    
			 for(let i = startpage ; i<= endpage ;i++) {
        html += "<a href='javascript:naversearch("+i+")'>[" + i + "]</a>";
			 }
			 if(maxpage > start) {
		html += "<a href='javascript:naversearch("+(start+1)+")'>[다음]</a>"
			 }
			 html +="</td></tr></table>";
			 $("#result").html(html);
		 }
	 })	   
  }
</script></body></html>