package kr.gdu.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
    private static byte[] randomKey;
    //초기화벡터
    private final static byte[] iv = new byte[] {
    		(byte)0x8E,0x12,0x39,(byte)0x90,
    		0x07,0x72,0x6F,(byte)0x5A,
    		(byte)0x8E,0x12,0x39,(byte)0x90,
    		0x07,0x72,0x6F,(byte)0x5A};
    static Cipher cipher; //암호 객체
    static {
    	try {
    		/*
    		 * AES: 암호화 알고리즘. 블럭암호화
    		 * CBC : 블럭모드 
    		 * PKCS5Padding : padding 방식 설정
    		 */
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	public static byte[] getRandomKey(String algo) 
			                       throws NoSuchAlgorithmException {
		//AES 알고리즘 용 키 생성 : 128비트 
		KeyGenerator keyGen = KeyGenerator.getInstance(algo);
		keyGen.init(128); //AES용 128비트 키 생성
		SecretKey key = keyGen.generateKey();
		return key.getEncoded(); 
	}    
	//평문 => 암호문으로 변경
	public static String encrypt(String plain) {
		byte[] cipherMsg = new byte[1024];
		try {
			//randomKey : AES용 128비트 키값 저장
			randomKey = getRandomKey("AES");
			Key key = new SecretKeySpec(randomKey, "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//Cipher.ENCRYPT_MODE : 암호화기능
			//key : 대칭키
			//paramSpec : 초기화 벡터
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			//암호화 실행 
			//plain.getBytes() : 평문
			cipherMsg = cipher.doFinal(plain.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg).trim();
	}
	public static String decrypt(String cipherMsg) {
		byte[] plainMsg = new byte[1024];
		try {
			Key key = new SecretKeySpec(randomKey, "AES");
			//초기화 벡터
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//Cipher.DECRYPT_MODE : 복호화 모드
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			plainMsg = cipher.doFinal(hexToByte(cipherMsg.trim()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim(); //byte[]형태의 평문 => 문자열 
	}
	// 비트형태데이터 => 16진수형태의 문자열 변환
	private static String byteToHex(byte[] cipherMsg) {
		if (cipherMsg == null)	return null;
		String str = "";
		for (byte b : cipherMsg) {
			str += String.format("%02X", b);
		}
		return str; //16진수값들을 문자열로 리턴
	}
	//16진수형태의 문자열 => 비트 형태 데이터로 변경
	private static byte[] hexToByte(String str) {
		if (str == null || str.length() < 2)	return null; 
		int len = str.length() / 2; //2개의문자 => 1바이트
		byte[] buf = new byte[len];
		for (int i = 0; i < len; i++) {
			buf[i] = (byte)Integer.parseInt
					      (str.substring(i * 2, i * 2 + 2), 16);
		}
		return buf;
	}
	//AES 키 크기 : 128비트 : 16바이트
	private static byte[] makeKey(String key) {
		int len = key.length();
		char ch='A';
		for(int i=len;i < 16; i++) { //abc1234567ABCDEF
			key += ch++;
		}
		return key.substring(0,16).getBytes();
	}	
	public static String encrypt(String plain1, String key) {
		byte[] cipherMsg = new byte[1024];
		try {
			Key genKey = new SecretKeySpec(makeKey(key),"AES"); 
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, genKey, paramSpec);
			cipherMsg = cipher.doFinal(plain1.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg);
	}
	public static String decrypt(String cipher1, String key) {
		byte[] plainMsg = new byte[1024];
		try {
			Key genKey = new SecretKeySpec(makeKey(key),"AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, genKey, paramSpec);
			plainMsg = cipher.doFinal(hexToByte(cipher1.trim()));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	//암호화
	//plainFile => 암호화실행 => 암호문 cipherFile 파일로 저장
	public static void encryptFile
	               (String plainFile, String cipherFile, String strkey) {
		try {
			//strkey 문자열을 파일로 저장
			getKey(strkey);
			//키 파일을 읽기
			ObjectInputStream ois = 
					new ObjectInputStream(new FileInputStream("key.ser"));
			Key key = (Key)ois.readObject(); //파일의 키객체를 복원
			ois.close();
			
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			FileInputStream fis = new FileInputStream(plainFile); //평문
			FileOutputStream fos = new FileOutputStream(cipherFile); //암호문
			//CipherOutputStream : 암호화 스트림. cipher 객체의 모드가 암호화모드임
			//cos : 암호화하여 cipherFile 파일에 저장
			CipherOutputStream cos = new CipherOutputStream(fos, cipher);
			byte[] buf = new byte[1024];
			int len;
			while ((len = fis.read(buf)) != -1) { // 평문을 읽기
				cos.write(buf, 0, len); //출력스트림으로 암호화
			}
			fis.close(); cos.flush();	fos.flush();
			cos.close(); fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void getKey(String key) throws Exception {
		Key genkey = new SecretKeySpec(makeKey(key), "AES");
		ObjectOutputStream out = 
				new ObjectOutputStream(new FileOutputStream("key.ser"));
		out.writeObject(genkey); //key를 파일로 등록 
		out.flush();	out.close();
	}
	public static void decryptFile
	               (String cipherFile, String plainFile, String strkey) {
		try {
			ObjectInputStream ois = 
					new ObjectInputStream(new FileInputStream("key.ser"));
			Key key = (Key) ois.readObject(); 
			ois.close();
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//Cipher.DECRYPT_MODE : 복호화 모드
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			//암호화된 파일 읽기
			FileInputStream fis = new FileInputStream(cipherFile);
			//복화하여 plainFile파일로 저장. plainText는 평문
			FileOutputStream fos = new FileOutputStream(plainFile);
			CipherOutputStream cos = new CipherOutputStream(fos, cipher);
			byte[] buf = new byte[1024];
			int len;
			while ((len = fis.read(buf)) != -1) {
				cos.write(buf, 0, len);
			}
			fis.close();			cos.flush();
			fos.flush();			cos.close();			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String makehash(String userid)  {
		MessageDigest md=null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] plain = userid.getBytes();
		byte[] hash = md.digest(plain);
		return byteToHex(hash);
	}

}
