package kr.gdu.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.gdu.domain.Item;
import kr.gdu.domain.Sale;
import kr.gdu.domain.SaleItem;
import kr.gdu.domain.User;
import kr.gdu.dto.CartDto;
import kr.gdu.dto.ItemDto;
import kr.gdu.dto.ItemSetDto;
import kr.gdu.repository.ItemRepository;
import kr.gdu.repository.SaleItemRepository;
import kr.gdu.repository.SaleRepository;

@Service  //@Component + Service : 객체화 + 서비스기능
public class ShopService {
	@Autowired //ItemDao 객체를 주입
	private ItemRepository itemDao;
	@Autowired 
	private SaleRepository saleDao;
	@Autowired
	private SaleItemRepository saleItemDao;	
//	@Autowired
//	private ExchangeDao exDao;	
	
	public List<Item> itemList() {
		return itemDao.findAll();
	}
	public Item getItem(Integer id) {
		//Optional<Item> findById(id)
		return itemDao.findById(id).get();
	}	
	public void itemCreate(ItemDto item, HttpServletRequest request) {
		//item.getPicture() : 업로드된 파일이 존재. 파일의 내용 저장
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			//업로드 폴더 지정
		  String path = request.getServletContext().getRealPath("/")+"img/";
		  uploadFileCreate(item.getPicture(),path);
		  item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		int maxid = itemDao.findMaxId(); //db에서 id의 최대값 조회
		item.setId(maxid + 1); //
		itemDao.save(new Item(item)); //insert, update 기능
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
	public void itemUpdate(ItemDto item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
		  String path = request.getServletContext().getRealPath("/")+"img/";
		  uploadFileCreate(item.getPicture(),path);
		  item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.save(new Item(item));	//수정	
	}
	public void itemDelete(Integer id) {
		itemDao.deleteById(id); //
	}
	public Sale checkend(User loginUser, CartDto cart) { 	
	    int maxsaleid = saleDao.getMaxSaleId(); //최종 주문번호 조회
	    Sale sale = new Sale();
	    sale.setSaleid(maxsaleid+1); //최종주문번호 + 1
	    sale.setUser(loginUser); //주문자 정보
	    sale.setUserid(loginUser.getUserid()); //db의 userid값으로 저장
	    saleDao.save(sale); //sale 테이블에 추가 
	    int seq = 0;
	    for(ItemSetDto is : cart.getItemSetList()) {
	    	SaleItem saleItem = new SaleItem(sale.getSaleid(),++seq,is);
	    	sale.getItemList().add(saleItem);
	    	saleItemDao.save(saleItem);
	    }
		return sale; //주문정보, 고객정보, 주문상품
	}
	public List<Sale> saleList(String userid) {
		//list : Sale 목록. db 정보만 저장
		List<Sale> list = saleDao.findByUserid(userid); //userid 사용자가 주문정보목록
		for(Sale sa : list) {
			//saleItemList : 주문번호에 맞는 주문 상품 목록. db 정보만 조회
			List<SaleItem> saleItemList = 
					 saleItemDao.findBySaleid(sa.getSaleid());
			for(SaleItem si : saleItemList) {
				//상품번호에 해당하는 상품조회
				Item item = itemDao.findById(si.getItemid()).orElse(null);
				si.setItem(item); //주문상품(SaleItem)에 상품정보 저장.
			}
			sa.setItemList(saleItemList); //주문정보(Sale)에 주문상품 저장
		}
		return list; //db정보, SaleItem(주문상품)정보
	}
//	public void exchangeCreate() {
//		Document doc = null;
//		List<List<String>> trlist = new ArrayList<>();
//		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
//		String exdate = null;
//		try {
//			doc = Jsoup.connect(url).get();
//			Elements trs = doc.select("tr"); 
//			exdate = doc.select("p.table-unit").html();
//			// 조회기준일 : 2025-06-23
//			exdate = exdate.substring(exdate.indexOf(":")+2);
//			for(Element tr : trs) {
//				List<String> tdlist = new ArrayList<>();
//				Elements tds = tr.select("td");
//				for(Element td : tds) {
//					tdlist.add(td.html()); 
//				}
//			    if (tdlist.size() > 0) {
//				    trlist.add(tdlist);
//			    }
//			 }
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
//		for(List<String> tds : trlist) {
//			Exchange ex = new Exchange
//					(0,tds.get(0),tds.get(1), //1235.12
//					 Float.parseFloat(tds.get(2).replace(",", "")),
//					 Float.parseFloat(tds.get(3).replace(",", "")),
//					 Float.parseFloat(tds.get(4).replace(",", "")),
//                    exdate.trim());
//			exDao.insert(ex);
//		}		
//	}
}
