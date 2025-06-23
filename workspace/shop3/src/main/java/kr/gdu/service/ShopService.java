package kr.gdu.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.gdu.logic.Item;
import kr.gdu.repository.ItemRepository;

@Service  //@Component + Service : 객체화 + 서비스기능
public class ShopService {
	@Autowired //ItemDao 객체를 주입
	private ItemRepository itemDao;	
	
	public List<Item> itemList() {
		return itemDao.findAll();
	}

	public Item getItem(Integer id) {
		// Optional<Item> findById(id)
		return itemDao.findById(id).get();
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		//item.getPicture() : 업로드된 파일이 존재. 파일의 내용 저장
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			//업로드 폴더 지정
		  String path = request.getServletContext().getRealPath("/")+"img/";
		  uploadFileCreate(item.getPicture(),path);
		  item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		int maxid = itemDao.findMaxId(); //db에서 id의 최대값 조회
		item.setId(maxid + 1); //
		itemDao.save(item); // insert, update 기능
	}
	
	//파일 업로드하기
	private void uploadFileCreate(MultipartFile picture, String path) {
		String orgFile = picture.getOriginalFilename(); //원본 파일 이름 
		File f = new File(path);
		if(!f.exists()) f.mkdirs();  //폴더가 없으면 생성
		try {
			//picture : 파일의 내용
			// transferTo : pipcture의 내용을 new File의 위치로 저장 
			picture.transferTo(new File(path + orgFile));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
