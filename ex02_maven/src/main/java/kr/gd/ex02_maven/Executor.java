package kr.gd.ex02_maven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
@Component 어노테이션
Executor 클래스 파일의 객체를 생성. 
"executor" 이름으로 ApplicationContext(컨테이너) 객체에 저장
*/
@Component
public class Executor {
	@Autowired // 컨테이너에서 Worker 타입의 객체를 worker에 주입. DI(의존성주입)
	private Worker worker;
	public void addUnit(WorkUnit unit) {
		worker.work(unit);
	}
}
