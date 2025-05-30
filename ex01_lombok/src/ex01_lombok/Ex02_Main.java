package ex01_lombok;

public class Ex02_Main {
	public static void main(String[] args) {
		// Ex02_User.builder() : User.Builder 내부 클래스의 객체
		// id() : User.Builder.id() 메서드
		Ex02_User user = Ex02_User.builder()
			.id("hong").pw("1234").build();
			System.out.println(user);
	}
}
