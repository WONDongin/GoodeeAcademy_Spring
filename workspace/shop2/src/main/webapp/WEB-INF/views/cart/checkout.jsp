<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/cart/checkout.jsp--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>주문 전 상품 목록 보기</title></head>
<body><h2>배송지 정보</h2>
<table><tr><td width="30%">주문아이디</td>
      <td width="70%">${sessionScope.loginUser.userid}</td></tr>
  <tr><td width="30%">이름</td>
      <td width="70%">${sessionScope.loginUser.username}</td></tr>
  <tr><td width="30%">우편번호</td>
      <td width="70%">${sessionScope.loginUser.postcode}</td></tr>
  <tr><td width="30%">주소</td>
      <td width="70%">${sessionScope.loginUser.address}</td></tr>
  <tr><td width="30%">전화번호</td>
      <td width="70%">${sessionScope.loginUser.phoneno}</td></tr>
</table>
<h2>구매 상품 </h2>
<table><tr><th>상품명</th><th>가격</th><th>수량</th><th>합계</th></tr>
  <c:forEach items="${sessionScope.CART.itemSetList}"
      var="itemSet" varStatus="stat">
   <tr><td>${itemSet.item.name}</td><td>${itemSet.item.price}</td>
   <td>${itemSet.quantity}</td>
   <td>${itemSet.item.price *itemSet.quantity}</td></tr>
  </c:forEach>
  <tr><td colspan="4" align="right">
          총 구입 금액 : ${sessionScope.CART.total}원</td></tr>
  <tr><td colspan="4"><a href="javascript:kakaopay()">결제하기</a>&nbsp;
     <a href="../item/list">상품 목록</a>&nbsp;
  </td></tr></table>
<%-- 
결제하기 모듈 : 개발자 문서
https://developers.portone.io/docs/ko/ready/readme
--%>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.8.js"></script>
<script type="text/javascript">
  let IMP = window.IMP;
  IMP.init("imp18303127")  // 가맹점 식별코드

  function kakaopay() {
    $.ajax("kakao", {
      success: function(json) {
    	// json : 서버에서 전달한 데이터. 주문정보
        iamPay(json)
      }
    })
  }
  
  function iamPay(json){
	  // IMP 주문 요청 데이터
	  IMP.request_pay({
		  pg: "kakaopay",      // 상점 구분. 카카오페이
		  pay_method : "card", // 카드 결제
		  merchant_uid : json.merchant_uid,       // 주문번호 : 주문별 유일한 값이 필요
		  name : json.name,                       // 주문 상품명
		  amount : json.amount,                   // 전체 주문금액
		  buyer_email : "dongin971228@gmail.com", // 주문자 이메일. 테스트
		  buyer_name : json.buyer_name,           // 주문자 성명
		  buyer_tel : json.buyer_tel,             // 주문자 전화번호
		  buyer_addr : json.buyer_addr,           // 주문자 주소
		  buyer_postcode : json.buyer_postcode,   // 주문자 우편번호
		  
	  }, function(rsp){   // 응답 데이터
		  if(rsp.success){// 결제 성공
			  let msg = "결제가 완료 되었습니다."
			  msg += "\n:고유ID : " + rsp.imp_uid
			  msg += "\n:상품ID : " + rsp.merchant_uid
			  msg += "\n:결제금액 : " + rsp.paid_amount
			  alert(msg)
			  location.href = "end"
		  } else {
			  alert("결제에 실패 했습니다. : " + rsp.error_msg)
		  }
	  })
  }
</script>

</body></html>