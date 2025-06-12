<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%-- /webapp/layout/layout.jsp --%>
<c:set var="path" value="${pageContext.request.contextPath }"/>    
<!DOCTYPE html>
<html>
<head>
  <title><sitemesh:write property="title" /></title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">

<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">  

  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <style>
  .fakeimg {
    height: 200px;
    background: #aaa;
  }
  /* Footer 관련 스타일 */
  .footer{
    display:flex;
    flex-direction:column;
   }
   .footer_link{height:15%; display:flex; align-items:center;}
   .footer_link a{text-decoration:none; color:black; font-weight:bold; margin:15px;}
   .footer_company{height:70%;}
   .footer_company>ul{list-style:"- "; padding-left:15px;}
   .footer_copyright{height:15%; text-align:center}     
   .footer>div{border-top:1px solid gray}
</style>
  <sitemesh:write property="head" />
</head>
<body>
<div class="jumbotron text-center" style="margin-bottom:0">
  <h1>클라우드(AWS)활용 자바/스프링 개발 부트캠프</h1>
  <p>GDJ90</p> 
</div>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
  <a class="navbar-brand" href="#">Goodee</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse d-flex justify-content-between" id="collapsibleNavbar">
    <ul class="navbar-nav ">
      <li class="nav-item">
        <a class="nav-link" href="${path}/member/main">회원관리</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="${path}/board/list?boardid=1">공지사항</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="${path}/board/list?boardid=2">자유게시판</a>
      </li>
    </ul>
    <ul class="navbar-nav">
      <c:if test="${sessionScope.login == null }">
      <li class="text-end">
        <a class="nav-link" href="${path}/member/loginForm">로그인</a>
      </li>    
      <li class="text-end">
        <a class="nav-link" href="${path}/member/joinForm">회원가입</a>
      </li>    
      </c:if>
      <c:if test="${sessionScope.login != null }">
      <li>
        <span class="nav-link text-success" href="#" style="cursor: default;">
        ${sessionScope.login}님 반갑습니다.</span>
      </li>    
      <li><a class="nav-link" href="${path}/member/logout">로그아웃</a>
      </li>    
      </c:if>
    </ul>
  </div>  
</nav>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>

<div class="container" style="margin-top:30px">
<div class="row">
   <div class="col-6" style="border:1px solid #eeeeee">
   <%-- 작성자별 게시물 등록 건수 파이그래프 : 가장 많이 작성한 작성자 5명--%>
      <canvas id="canvas1"></canvas>
   </div>
   <div class="col-6" style="border:1px solid #eeeeee">
   <%-- 최근작성일자별 게시물 등록 건수 막대그래프 : 최근 7일간--%>
      <canvas id="canvas2"></canvas>
   </div>
</div>
 <sitemesh:write property="body" />  
</div>
<footer class="footer">
<div><span id="si">
       <select name="si" onchange="getText('si')">
          <option value="">시도를 선택하세요</option>
       </select></span>
      <span id="gu">
       <select name="gu" onchange="getText('gu')">
          <option value="">구군을 선택하세요</option>
       </select></span>
      <span id="dong">
       <select name="dong">
          <option value="">동리를 선택하세요</option>
       </select></span>
</div>

  <div class="footer_link">
      <a href="">이용약관</a> |
      <a href="">개인정보취급방침</a> |
      <a href="">인재채용</a> |
      <a href="">고객센터</a>
  </div>
  <div class="footer_company">
   <ul>
      <li>상호명 : GooDee Academy</li>
      <li>대표자 : 이승엽</li>
      <li>전화 : 02-818-7950</li>
      <li>개인정보책임자 : 주승재 / jsj@goodee.co.kr</li>
      <li>(08505) 서울특별시 금천구 가산디지털2로 95
           (가산동, km타워) 2층, 3층</li>
   </ul>
  </div>
  <div class="footer_copyright">
     Copyright ⓒ GooDee Academy. All rights reserved.
  </div>
 </footer>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script type="text/javascript" 
	  src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
<script type="text/javascript">
$(function(){
	piegraph(); //작성자별 게시물 등록 건수 파이그래프로 구현
	bargraph(); //최근일자별 게시물 등록 건수 막대그래프로 구현
	//ajax을 이용하여 시도 데이터 조회하기
	let divid;
	let si;
	$.ajax({
		url : "${path}/ajax/select",
		success : function(data) {
			//data : ["서울특별시","경기도",...]
			let arr = JSON.parse(data)
			$.each(arr,function(i,item){
				//item : 서울특별시
				$("select[name=si]").append(function(){
					return "<option>"+item+"</option>"
				})
			})
		},
		error: function(e) {
			alert("서버오류:"+e.status)
		}
	})
})
function getText(name) {
	let city = $("select[name='si']").val()
	let gun = $("select[name='gu']").val()
	let disname;
    let toptext='구군을 선택하세요'
    let params = ''
    if(name=='si') { //시도 선택
    	params = "si=" + city.trim()
    	disname = "gu"
    } else if (name=='gu') { // 구군
    	params = "si=" + city.trim()+"&gu="+gun.trim()
    	disname = "dong"
    	toptext='동리를 선택하세요'
    } else {
    	return 
    }
    $.ajax({
    	url : "${path}/ajax/select",
    	type : "POST",
    	data:params,
    	success : function(data) {
    		let arr = JSON.parse(data)
    		//변경되는 select 태그의 option 태그를 제거
    		$("select[name="+disname+"] option").remove()
    		//첫번째 내용 추가.
    		$("select[name="+disname+"]").append(function(){
    			return "<option value=''>"+toptext+"</option>"
    		})
    		$.each(arr,function(i,item){
        		$("select[name="+disname+"]").append(function(){
        			return "<option>"+item+"</option>"
    		    })
    		})
    	   },
    	error : function(e){
    		alert("서버오류:"+e.status)
    	}
    })	
}
function piegraph() {
	$.ajax("${path}/ajax/graph1",{
		success : function(data) {
			//data : [{"cnt":9,"writer":"홍길동"},{"cnt":3,"writer":"홍길순"},{"cnt":3,"writer":"111"},{"cnt":2,"writer":"홍길수"},{"cnt":1,"writer":"삼삼삼"}]
			pieGraphPrint(data);
		},
		error : function(e) {
			alert("서버오류:" + e.status)
		}
	})
}
function pieGraphPrint(data) {
	let rows = JSON.parse(data); //서버에서 JSON 형태로 데이터 전송
	let writers = [] //작성자목록. 라벨값. 배열
	let datas = []   //파이 데이터값 배열
	let colors = []  //색상값 배열
	$.each(rows,function(i,item) {
		//item : {"cnt":3,"writer":"홍길순"}
		writers[i] = item.writer; //[홍길동,홍길순,....]
		datas[i] = item.cnt       //[9,3,...] 
		colors[i] = randomColor(1);
	})
	let config = {
		type:'pie',
		data : {
			datasets : [{
				data : datas,
				backgroundColor : colors
			}],
			labels : writers
		},
		options : {
			responsive : true,
			legend : { display : true, position:"right"},
			title : {
				display : true,
				text : '게시물 작성자별등록건수(최대 5명)',
				position:"bottom"
			}
		}
	}
	let ctx = document.querySelector("#canvas1");
	new Chart(ctx,config)
}
function bargraph() {
    $.ajax("${path}/ajax/graph2",{
    	success : function(data) {
    		barGraphPrint(data);
    	},
    	error : function(e) {
    		alert("서버오류:"+e.status)
    	}
    })
}
function barGraphPrint(data){
	  let rows = JSON.parse(data)
	  let regdates = []
	  let datas = []
	  let colors = []
	  $.each(rows,function(i,item){
		  regdates[i]=item.regdate
		  datas[i]=item.cnt
		  colors[i] = randomColor(1)
	  })
	  let chartData = {
		  labels : regdates,
		  datasets : [{
			  type : "line",
			  borderWidth : 2,
			  borderColor:colors,
			  label :'건수',
			  fill:false,
			  data : datas
		  },{
			  type : 'bar',
			  label : '건수',
			  backgroundColor : colors,
			  data : datas
		  }]
	  }
	  let config = {
		  type : 'bar',
		  data : chartData,
		  options : {
			  responsive : true,
			  title : {display : true,
				  text : '최근 7일 게시판 등록건수',
				  position : 'bottom'},
			  legend : {display : false},
			  scales : {
				  xAxes : [{display : true}],
				  yAxes : [{display : true}]
			  }
		  }
	  }
	  let ctx = document.getElementById("canvas2");
	  new Chart(ctx,config)
}

function randomColorFactor () {
	  return Math.round(Math.random() * 255)
}
function randomColor(opa) {
	  return "rgba(" + randomColorFactor() + ","
			  + randomColorFactor() + ","
			  + randomColorFactor() + "," 
			  + (opa || '.3') + ")"
}

</script>

</body>
</html>
