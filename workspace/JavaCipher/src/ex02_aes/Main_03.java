package ex02_aes;
/*
파일 암호화 하기
*/
public class Main_03 {

	public static void main(String[] args) {
		String key = "abc1235467";
		CipherUtil.encryptFile("p1.txt","c.sec", key);
		// 파일 복호화
		CipherUtil.decryptFile("c.sec","p2.txt", key);
	}

}
