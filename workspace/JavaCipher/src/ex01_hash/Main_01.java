package ex01_hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Scanner;
import java.util.Set;

public class Main_01 {
	public static void main(String[] args) 
			        throws NoSuchAlgorithmException {
		byte[] plain = null;
		byte[] hash = null;
		//MessageDigest : Hash 알고리즘
		// 자바에서 제공하는 Hash 알고리즘 목록 
		Set<String> algorithms = Security.getAlgorithms("MessageDigest");
		System.out.println(algorithms);
		//MD5 : 128비트
		//SHA-512 : 512비트
		String[] algo = {"MD5","SHA-1","SHA-256","SHA-512"};
		System.out.println("해쉬값을 구할 문자열을 입력하세요");
		Scanner scan = new Scanner(System.in);
		String str = scan.nextLine();
		plain = str.getBytes();
		for(String al : algo) {
			MessageDigest md = MessageDigest.getInstance(al);
			hash = md.digest(plain);
			System.out.println(al + "해쉬값 크기:" + (hash.length*8) + "bits"); //512비트 => 64바이트
			System.out.print("해쉬값:");
			for(byte b : hash) System.out.printf("%02X",b);
			System.out.println();
		}
	}
}
