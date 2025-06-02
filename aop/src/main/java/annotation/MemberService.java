package annotation;

import org.springframework.stereotype.Component;

@Component // memberservice 객체 생성
public class MemberService {
	public void regist(Member member) {
		System.out.println("MemberService.regist(Member) 메서드 호출");
	}
	// "hong", new UpdateInfo()
	public boolean update(String memberid, UpdateInfo info) {
		System.out.println("MemberService.update(String, UpdateInfo) 메서드 호출");
		return true;
	}
	// "hong", "test", new UpdateInfo()
	public boolean delete(String memberid, String name, UpdateInfo info) {
		System.out.println("MemberService.delete(String, String, UpdateInfo) 메서드 호출");
		return false;
	}
	
}
