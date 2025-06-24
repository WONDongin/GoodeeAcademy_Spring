package ex01_hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
1. CREATE TABLE usercupher SELECT * FROM 
   SELECT * FROM useraccount
2. usercipher의 password 컬럼의 크기를 300으로 변경
   ALTER TABLE usercipher MODIFY COLUMN PASSWORD VARCHAR(300)
3. userid 칼럼을 기본키로 설정하기
   ALTER TABLE usercipher ADD CONSTRAINT PRIMARY KEY (userid)
   
   useraccount 테이블을 읽어서 usercipher 테이블의 password 를 sha-256
   알고리즘으로 해쉬값 저장하기
 
*/
public class Main_02 {
	public static void main(String[] args) throws Exception {
	    Class.forName("org.mariadb.jdbc.Driver");
	    Connection conn = DriverManager.getConnection(
	        "jdbc:mariadb://localhost:3306/gdjdb", "gduser", "1234");

	    PreparedStatement pstmt = conn.prepareStatement(
	        "select userid,password from useraccount");

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
	        String id = rs.getString("userid");
	        String pass = rs.getString("password");
	        if(pass == null) continue;
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        String hashpass = "";
	        byte[] plain = pass.getBytes(); // 원본 비밀번호. byte[]로
	        byte[] hash = md.digest(plain);
	        for(byte b : hash) hashpass += String.format("%02X", b);
	        pstmt.close();
	        pstmt = conn.prepareStatement(
	        	    "update usercipher set password=? where userid=?");
	        	pstmt.setString(1, hashpass); // 첫 번째 ? 자리에 해시 비밀번호
	        	pstmt.setString(2, id);       // 두 번째 ? 자리에 사용자 ID
	        	pstmt.executeUpdate();
	    }}
}

