package kr.gd.ex02_maven;

import org.springframework.stereotype.Component;

@Component // 객체화됨. 
public class Worker {
	public void work(WorkUnit unit) {
		System.out.println(this + ":work:" + unit);
	}
}
