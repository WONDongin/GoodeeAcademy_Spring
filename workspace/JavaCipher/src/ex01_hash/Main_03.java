package ex01_hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/*
 * 화면에서 아이디,비밀번호를 입력받기
 * usercipher 테이블을 읽기
 * - 아이디없으면 : 아이디없음 출력
 * - 비밀번호오류 : 비밀번호 오류 출력
 * - 일치 : 반갑습니다. 이름님 출력
 */
public class Main_03 {
	public static void main(String[] args) throws Exception{
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection
			("jdbc:mariadb://localhost:3306/gdjdb","gduser","1234");
		PreparedStatement pstmt = conn.prepareStatement
		    ("select password,username from usercipher where userid=?");
		Scanner scan = new Scanner(System.in);
		System.out.println("아이디를 입력하세요");
		String id = scan.nextLine();
		System.out.println("비밀번호를 입력하세요");
		String pw = scan.nextLine();
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String hashpass = "";
			byte[] plain = pw.getBytes();
			byte[] hash = md.digest(plain);
			for(byte b : hash) hashpass += String.format("%02X", b);
			System.out.println(rs.getString("password"));
			System.out.println(hashpass);
			if(rs.getString("password").equals(hashpass)) {
				System.out.println("반갑습니다." + rs.getString("username"));
			} else {
				System.out.println("비밀번호 오류");
			}
		} else {
			System.out.println("아이디가 없습니다.");
		}
	}
}
