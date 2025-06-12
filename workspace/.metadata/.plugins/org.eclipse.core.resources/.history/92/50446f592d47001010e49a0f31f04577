package service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import logic.Item;

@Service // @Component + Service : 객체화 + 서비스 기능
public class ShopService {
	@Autowired // ItemDao 객체를 주입
	private ItemDao itemDao;
	
	public List<Item> itemList(){
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.select(id);
	}
	
	// 상품등록
	public void itemCreate(Item item, HttpServletRequest request) {
		// item.getPicture() : 업로드된 파일이 존재, 파일의 내용 저장
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			// path : 업로드 폴더 지정
			String path = request.getServletContext().getRealPath("/") + "img/";
			// item.getPicture() : 파일 내용
			uploadFileCreate(item.getPicture(),path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		int maxid = itemDao.maxId(); // db에서 id의 최대값 조회
		item.setId(maxid + 1);       // 최대값 + 1 등록
		itemDao.insert(item);
	}
	
	// 파일 업로드
	public void uploadFileCreate(MultipartFile picture, String path) {
		String orgFile = picture.getOriginalFilename(); // 원본 파일 이름
		File f = new File(path);
		if(!f.exists()) f.mkdirs(); // 폴더가 없으면 생성
		try {
			// picture : 파일의 내용
			// transferTo : picture의 내용을 new File의 위치로 저장
			picture.transferTo(new File(path + orgFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 상품수정
	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			String path = request.getServletContext().getRealPath("/") + "img/";
			uploadFileCreate(item.getPicture(),path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}
	
	// 상품삭제
	public void itemDelete(Integer id) {
		itemDao.delete(id);
	}
}
