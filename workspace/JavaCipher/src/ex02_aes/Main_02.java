package ex02_aes;
// 키를 설정하여 암호화 복호화 하기
public class Main_02 {
	public static void main(String[] args) {
		String plain1 = "안녕하세요 홍길동 입니다.";
		String key = "abc1234567";
		String cipher1 = CipherUtil.encrypt(plain1,key);
		System.out.println("암호문:"+cipher1); 
		key = "abc1234567";
		String plain2 = CipherUtil.decrypt(cipher1,key);
		System.out.println("복호문:"+plain2);
	}
}
