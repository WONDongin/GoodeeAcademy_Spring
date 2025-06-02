package annotation;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
/*
 UpdateTraceAspect 연습
[LA] Before 메서드 실행 전 호출
MemberService.regist(Member) 메서드 호출
[LA] AfterReturning 메서드 정상 종료후 호출, 리턴값=null
[LA] After 메서드 종료 후 실행

[LA] Before 메서드 실행 전 호출
MemberService.update(String, UpdateInfo) 메서드 호출
[LA] AfterReturning 메서드 정상 종료후 호출, 리턴값=true
[LA] After 메서드 종료 후 실행 ==> 실행후 order 순서 역순으로 발송하여 [TA] 마지막 실행!
[TA] 정보 수정 결과:true,대상 ID:hong,수정정보:annotation.UpdateInfo@7a527389

[LA] Before 메서드 실행 전 호출
MemberService.delete(String, String, UpdateInfo) 메서드 호출
[LA] AfterReturning 메서드 정상 종료후 호출, 리턴값=true
[LA] After 메서드 종료 후 실행
[TA] 정보 수정 결과:true,대상 ID:test,수정정보:annotation.UpdateInfo@485a3466
*/
public class UpdateTraceAspect {
	// args(..,id,info) => 매개변수로만 pointcut 지정
	// ..,id,info : 매개변수의 마지막 목록이 id(String), info(UpdateInfo)인 메서드를 선택 
	@AfterReturning(pointcut="args(..,id,info)", 
			argNames="ret,id,info", returning="ret")
	public void traceReturn(Object ret, String id, UpdateInfo info) {
		System.out.println("[TA] 정보 수정 결과:" + ret + ",대상 ID:" + id + ",수정정보:" + info);
	}
}
