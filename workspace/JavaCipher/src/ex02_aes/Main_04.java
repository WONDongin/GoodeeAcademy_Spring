package ex02_aes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * useraccount  테이블 email을 읽어서 usercipher 테이블에 암호화하여 저장하기
 * 1. usercipher 테이블의 email 컬럼의 크기를 1000으로 변경하기
 *    ALTER TABLE usercipher MODIFY COLUMN email VARCHAR(1000);
 * 2. key는 userid의 해쉬값(SHA-256)의 앞16자리로 설정한다    
 */
public class Main_04 {
	public static void main(String[] args) throws Exception{
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection
	 ("jdbc:mariadb://localhost:3306/gdjdb","gduser","1234");
		PreparedStatement pstmt = conn.prepareStatement
				       ("select * from useraccount");
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			String userid = rs.getString("userid");
			String email = rs.getString("email");
			//userid값의 SHA-256 기준의 해귀값 리턴
			String key = CipherUtil.makehash(userid);
			String cipherEmail = CipherUtil.encrypt(email,key); 
			pstmt = conn.prepareStatement
					("update usercipher set email=? where userid=?");
			pstmt.setString(1, cipherEmail);
			pstmt.setString(2, userid);
			pstmt.executeUpdate();			
		}

	}
}
