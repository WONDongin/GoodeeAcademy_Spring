package kr.gdu.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import kr.gdu.dao.UserDao;
import kr.gdu.logic.Mail;
import kr.gdu.logic.User;

@Service
public class UserService {
	@Value("${resources.dir}")
	private String RESOURCE_DIR;
	
	@Autowired
	UserDao userdao;
	
	public void userInsert(User user) {
		userdao.insert(user);
	}

	public User selectUser(String userid) {
		return userdao.selectOne(userid);
	}

	public void userUpdate(User user) {
		userdao.update(user);
	}

	public void userDelete(String userid) {
		userdao.delete(userid);		
	}

	public void userChgpass(String userid, String chgpass) {
		userdao.chgpass(userid,chgpass);		
	}
	public String getSearch(User user) {
		return userdao.search(user);
	}

	public List<User> userlist() {
		return userdao.list();
	}
	public List<User> getUserList(String[] idchks) {
		return userdao.list(idchks);
	}
	//메일 전송을 위한 인증 클래스
	private final class MyAuthenticator extends Authenticator {
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id, pw);
		}		
	}

	public boolean mailSend(Mail mail) {
		String sender = mail.getGoogleid() + "@gmail.com";
		Properties prop = new Properties();
		try {
			FileInputStream fis = 
					 new FileInputStream(RESOURCE_DIR+ "mail.properties");
			prop.load(fis);
			prop.put("mail.smtp.user",sender); //전송이메일 설정
		} catch (IOException e) {
			e.printStackTrace();
		}		
		//구글아이디@gmail.com
	    //passwd : 앱비밀번호 
		MyAuthenticator auth =  //인증객체
				   new MyAuthenticator(sender,mail.getGooglepw());
		//session : 메일 서버에 접속 객체
		Session session = Session.getInstance(prop,auth);
		//msg : 이메일 전송 내용 전체
		MimeMessage msg = new MimeMessage(session);
		try {
			//보내는 이메일 설정
			msg.setFrom(new InternetAddress(sender));
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			//emails : [수신인이름<수신자이메일주소>,...]
			String[] emails = mail.getRecipient().split(",");
			for(String email : emails) {
				addrs.add(new InternetAddress(email));
			}
			//arr : 수신이메일 목록
			InternetAddress[] arr = new InternetAddress[emails.length];
			for(int i=0;i<addrs.size();i++) {
				arr[i]=addrs.get(i);
			}
			msg.setRecipients(Message.RecipientType.TO,arr);
			msg.setSentDate(new Date()); //전송시간
			msg.setSubject(mail.getTitle()); //제목
			//내용, 첨부파일들 저장
			MimeMultipart multipart =new MimeMultipart();
			//내용PART
			MimeBodyPart message = new MimeBodyPart();
			message.setContent(mail.getContents(),mail.getMtype()); //내용설정
			multipart.addBodyPart(message); //내용추가
			//첨부파일 설정
			//List<MultipartFile> getFile1()
			for(MultipartFile mf : mail.getFile1()) {
				//mf : 첨부파일 1개
				if ((mf != null) && (!mf.isEmpty())) { //첨부된 내용 존재
					multipart.addBodyPart(bodyPart(mf)); //첨부파일 추가
				}
			}
			msg.setContent(multipart);
			Transport.send(msg); //메일 전송
			return true; 
		} catch(MessagingException me) {
			me.printStackTrace();
		}
		return false;
	}	
	private BodyPart bodyPart(MultipartFile mf) {
		//mf : 파일 한개
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename();
		String path = RESOURCE_DIR + "mailupload/"; //업로드되는 폴더
		File f1 = new File(path);
		if(!f1.exists()) f1.mkdirs();
		File f2 = new File(path + orgFile);
		try {
			mf.transferTo(f2);
			body.attachFile(f2); //이메일 전송을 위한 첨부파일 추가
			body.setFileName(orgFile); //첨부된 파일의 이름 설정
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}

	public void mailfileDelete(Mail mail) {
		String path = RESOURCE_DIR + "mailupload/";
		List<String> filenames = new ArrayList<>();
		for (MultipartFile mf : mail.getFile1()) {
			filenames.add(mf.getOriginalFilename());
		}
		for (String f : filenames) {
			File df = new File(path,f);
			df.delete();
		}
	}
}