package ex02_aes;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
useraccount 테이블 email를 읽어서 usercipher 테이블에 암호화하여 저장하기

1. usercipher 테이블의 email 컬럼의 크기를 1000으로 변경하기
ALTER TABLE usercipher MODIFY COLUMN email VARCHAR(1000)

2. key userid의 해쉬값(SHA-256)의 앞 16자리로 설정한다

*/
public class Main_04 {
	public static void main(String[] args) throws Exception {
		 Class.forName("org.mariadb.jdbc.Driver");
		    Connection conn = DriverManager.getConnection(
		        "jdbc:mariadb://localhost:3306/gdjdb", "gduser", "1234");

		    PreparedStatement pstmt = conn.prepareStatement(
		        "select userid,email from useraccount");

		    ResultSet rs = pstmt.executeQuery();

		    while (rs.next()) {
		        String id = rs.getString("userid");
		        String email = rs.getString("email");
		        if(email == null) continue;
		        MessageDigest md = MessageDigest.getInstance("SHA-256");
		        String hashpass = id;
		        byte[] plain = email.getBytes(); // 원본 비밀번호. byte[]로
		        byte[] hash = md.digest(plain);
		        for(byte b : hash) hashpass += String.format("%02X", b);
		        pstmt.close();
		        pstmt = conn.prepareStatement(
		        	    "update usercipher set email=? where userid=?");
		        	pstmt.setString(1, hashpass); 
		        	pstmt.setString(2, id);       
		        	pstmt.executeUpdate();
		    }}
}
