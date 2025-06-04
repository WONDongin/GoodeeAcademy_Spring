package kr.gd.ex02_maven;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(AppCtx.class);
		Executor exec = ctx.getBean("executor", Executor.class);
		exec.addUnit(new WorkUnit());
		exec.addUnit(new WorkUnit());
	}
}
/*
main()
  ↓
AppCtx → @ComponentScan → Executor, Worker 등록
  ↓
Executor 생성 → Worker 자동 주입
  ↓
main()에서 Executor 호출 → addUnit 실행
  ↓
Worker가 WorkUnit 수행
*/