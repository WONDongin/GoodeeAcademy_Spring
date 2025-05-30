package ex01_lombok;

public class Ex01_Main {
	public static void main(String[] args) {
		Ex01_User user = new Ex01_User();
		user.setId("hong");
		user.setPw("1234");
		System.out.println(user);
		System.out.println(user.getId());
		System.out.println(user.getPw());
		
		Ex01_User user2 = new Ex01_User("kim","5678");
		System.out.println(user2);
	}
}
// equals :  두 객체의 내용이 같은지, 동등성(equality) 를 비교하는 연산자
// hashCode : 두 객체가 같은 객체인지, 동일성(identity) 를 비교하는 연산자