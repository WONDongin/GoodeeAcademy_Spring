package kr.gdu.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.gdu.domain.Board;
import kr.gdu.domain.Comment;
import kr.gdu.domain.CommentId;
import kr.gdu.dto.BoardDto;
import kr.gdu.repository.BoardRepository;
import kr.gdu.repository.CommRepository;

@Service
public class BoardService {
	@Value("${summernote.imgupload}")
	private String UPLOAD_IMAGE_DIR; //application.properties의 환경변수값 읽기

	@Autowired
	BoardRepository boardDao;
	@Autowired
	CommRepository commDao;
	
	//boardid : 필수값
	//searchtype,searchcontent : 옵션값
	public int boardcount(String boardid, String searchtype, String searchcontent) {
		/*
		 * root : 조회 대상 Entity. Board 객체
		 * query : 쿼리 전체 구조. JPA CriteriaQuery 객체 
		 * cri : CriteriaBuilder. 조건문(비교,like,and, or....)
		 *  
		 *  root.get("boardid"), boardid : Board 테이블의 boardid 와 같은 레코드
		 */
		Specification<Board> spec = 
			(root,query,cri) -> cri.equal(root.get("boardid"), boardid);
			
		if(searchtype != null) { //검색부분
		  if(searchtype.equals("title")) {
			spec = spec.and((root, query, cr)->
			 cr.like(root.get("title"),"%"+searchcontent+"%"));
		  } else if (searchtype.equals("writer")) {
			spec = spec.and((root, query, cr)->
			cr.like(root.get("writer"),"%"+searchcontent+"%"));
		  } else if (searchtype.equals("content")) {
			spec = spec.and((root, query, cr)->
			cr.like(root.get("content"),"%"+searchcontent+"%"));
		  }
		}
		//spec에 맞는 레코드의 건수 리턴.
		return (int)boardDao.count(spec); //리턴값이 long 임
	}
	/*
	 * 페이징을 위한 게시물 목록 조회.
	 */
	public Page<Board> boardlist
	(Integer pageNum, int limit, String boardid, String searchtype, String searchcontent) {
		Specification<Board> spec = 
			(root,query,cri) -> cri.equal(root.get("boardid"), boardid);
				
		if(searchtype != null) { //검색부분
		  if(searchtype.equals("title")) {
			spec = spec.and((root, query, cr)->
			 cr.like(root.get("title"),"%"+searchcontent+"%"));
		  } else if (searchtype.equals("writer")) {
			spec = spec.and((root, query, cr)->
			cr.like(root.get("writer"),"%"+searchcontent+"%"));
		  } else if (searchtype.equals("content")) {
			spec = spec.and((root, query, cr)->
			cr.like(root.get("content"),"%"+searchcontent+"%"));
		  }
		}
		/*
		 * pageNum-1 : 현재 페이지. 0부터 시작. 0:첫번째 페이지, 1:2번째 페이지
		 * limit : 한페이지에 출력할 게시물 갯수. 한번에 조회되는 레코드 갯수
		 */
		Pageable pageable = PageRequest.of(pageNum-1, limit,
		  Sort.by(Sort.Order.desc("grp"),Sort.Order.asc("grpstep")));
		
		return boardDao.findAll(spec,pageable);
	}
	public Board getBoard(Integer num) {
		return boardDao.findById(num).orElseGet(()->null);  //board 레코드 조회
	}
	public void addReadcnt(Integer num) {
		boardDao.addReadcnt(num);       //조회수 증가
	}
	public void boardWrite(BoardDto board, HttpServletRequest request) {
		int maxnum = boardDao.maxNum();
		board.setNum(++maxnum);
		board.setGrp(maxnum);
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = 
					request.getServletContext().getRealPath("/") 
					                          + "board/file/";
			this.uploadFileCreate(board.getFile1(), path);
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		
		boardDao.save(new Board(board));		
	}
	public void uploadFileCreate(MultipartFile file, String path) {
		String orgFile = file.getOriginalFilename();
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		try {
			file.transferTo(new File(path+orgFile));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void boardUpdate(BoardDto board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")
					                                    + "board/file/";
			this.uploadFileCreate(board.getFile1(), path);  
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.save(new Board(board));
	}
	public void boardDelete(int num) {
		boardDao.deleteById(num);
	}
	public void boardReply(BoardDto board) {
		boardDao.grpStepAdd(board.getGrp(),board.getGrpstep());  //이미 등록된 grpstep값 1씩 증가
		int max = boardDao.maxNum();    //최대 num 조회
		board.setNum(++max);  //원글의 num => 답변글의 num 값으로 변경
		                      //원글의 grp => 답변글의 grp 값을 동일. 설정 필요 없음
                              //원글의 boardid => 답변글의 boardid 값을 동일. 설정 필요 없음
		board.setGrplevel(board.getGrplevel() + 1); //원글의 grplevel => +1 답변글의 grplevel 설정
		board.setGrpstep(board.getGrpstep() + 1);   //원글의 grpstep => +1 답변글의 grpstep 설정
		boardDao.save(new Board(board));		
	}
	public List<Comment> commentlist(Integer num) {
		return commDao.findByNum(num);
	}
	public int commmaxseq(int num) {
		return commDao.maxseq(num);
	}
	public void comminsert(Comment comm) {
		commDao.save(comm);		
	}
	public Comment commSelectOne(int num, int seq) {
		//기본키값으로 조회 : Optional<> findById(키)
		CommentId id = new CommentId(num,seq);
		return commDao.findById(id).orElseGet(()->null);
	}
	public void commdel(int num, int seq) {
		//키값으로 레코드 제거 : deleteById(키)
		CommentId id = new CommentId(num,seq);
		commDao.deleteById(id);
	}
	public String summernoteImageUpload(MultipartFile multipartFile) {
		File dir = new File(UPLOAD_IMAGE_DIR + "board/image");
		if (!dir.exists()) dir.mkdirs();
		String filesystemName = multipartFile.getOriginalFilename();
		//File(parent,filename) : dir 폴더에 filesystemName 이름의 파일
		File file = new File(dir,filesystemName);
		try {
			multipartFile.transferTo(file); //이미지 업로드
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "/board/image/" + filesystemName;//url 리턴
	}
	public String sidoSelect1(String si, String gu) {
		BufferedReader fr = null;
		String path = UPLOAD_IMAGE_DIR+"data/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path));
		}catch(Exception e) {
			e.printStackTrace();	
		}
		Set<String> set = new LinkedHashSet<>();
		String data= null;
		if(si==null && gu==null) {
			try {
				while((data=fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3) set.add(arr[0].trim()); 
				}
			} catch(IOException e) {   e.printStackTrace();	}
		}
		List<String> list = new ArrayList<>(set); 
		return list.toString(); //[서울특별시,경기도,.....]		
	}
	public List<String> sigunSelect2(String si, String gu) {
		BufferedReader fr = null;
		String path = UPLOAD_IMAGE_DIR+"data/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path));
		}catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>();
		String data= null;
		if(si==null && gu==null) {
			try {
				while((data=fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3) set.add(arr[0].trim()); 
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else if(gu == null) { //시도 선택한 경우. 서울특별시,경기도 ....
		   si = si.trim();
		   try {
			  while ((data = fr.readLine()) != null) {
				 String[] arr = data.split("\\s+");
			  	 if(arr.length >= 3 && arr[0].equals(si)
			  			 && !arr[1].contains(arr[0]) ) {
					 set.add(arr[1].trim());
				 }
			   }
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		} else { //구군을 선택한경우.
		   si = si.trim();
		   gu = gu.trim();
		   try {
			  while ((data = fr.readLine()) != null) {
				  String[] arr = data.split("\\s+");
		          if(arr.length >= 3 && arr[0].equals(si) &&
			    	 arr[1].equals(gu) && !arr[0].equals(arr[1]) && 
			    	    !arr[2].contains(arr[1])) {
			          	 if(arr.length > 3 ) {
			          		if(arr[3].contains(arr[1])) continue;
			          		arr[2] += " " + arr[3];
			          	 }
			          	 set.add(arr[2].trim());
			      }
			  }
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set); 
		return list;
	}
	public String exchange1() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>();
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); //tr 태그 목록
			//조회기준일 부분 조회
			exdate = doc.select("p.table-unit").html();
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td"); //tr 태그 내부의 td 태그 목록
				for(Element td : tds) {
					//td.html() : td 태그의 내용들. 
					tdlist.add(td.html()); //[USD,미국달러,1350...,...]
				}
			    if (tdlist.size() > 0) {
				   if(tdlist.get(0).equals("USD") 
			    	|| tdlist.get(0).equals("CNH") 
			        || tdlist.get(0).equals("JPY(100)")
			        || tdlist.get(0).equals("EUR")) { 
				    trlist.add(tdlist);
				    //trlist : USD,CNH,JPY,EUR 통화코드가 속한 데이터만 저장
				   }
			    }
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<h4 class='w3-center'>수출입은행<br>" + exdate + "</h4>");
		sb.append("<table class='w3-table-all'>");
		sb.append
	("<tr><th>통화</th><th>기준율</th><th>받을실때</th><th>보내실때</th></tr>");
		for(List<String> tds : trlist) {
			sb.append
("<tr><td>"+tds.get(0)+"<br>"+tds.get(1)+"</td><td>"+tds.get(4)+"</td>");
			sb.append("<td>"+tds.get(2)+"</td><td>"+tds.get(3)+"</td><tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	public Map<String, Object> exchange2() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>();
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			//DOM
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); 
			exdate = doc.select("p.table-unit").html(); //조회기준일
			for(Element tr : trs) {
				//Elements : tr 태그 모두. 
				//Element : tr 태그 한개
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td");
				for(Element td : tds) {
					tdlist.add(td.html());
				}
			    if (tdlist.size() > 0) {
				   if(tdlist.get(0).equals("USD") //미달러
			    	|| tdlist.get(0).equals("CNH") //중국
			        || tdlist.get(0).equals("JPY(100)") //일본
			        || tdlist.get(0).equals("EUR")) { //유로
				    trlist.add(tdlist);
				   }
			    }
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<>();
		map.put("exdate", exdate); //조회기준일
		map.put("trlist", trlist); //4개통화 데이터
		return map;
	}
	public Map<String, Integer> graph1(String id) {
		// list :   [{writer="홍길동",cnt:10 },{writer="김삿갓",cnt:7 },..]
		List<Map<String,Object>> list = boardDao.graph1(id);
		Map<String, Integer> map = new HashMap<>();//{홍길동:10,김삿갓:7}
		for(Map<String,Object> m : list) {
			//m : {writer="김삿갓",cnt:7 }
		    String writer =(String)m.get("writer"); //김삿갓
		    long cnt = (Long) m.get("cnt"); //7
		    map.put(writer,(int)cnt);
		}		
		return map; //{홍길동:10,김삿갓:7,...}
	}
	public Map<String, Integer> graph2(String id) {
		//list : [{day=2025-06-05,cnt=5},{day=2025-06-04,cnt=7},...]
		List<Map<String,Object>> list = boardDao.graph2(id);
		Map<String,Integer> map = new TreeMap<>(Comparator.reverseOrder());
		for(Map<String,Object> m : list) { 
			String day =(String)m.get("day");
			long cnt = (long)m.get("cnt"); 
			map.put(day,(int)cnt); //날짜의 역순으로 정렬 상태
		}
		return map; // {2025-06-05=5,2025-06-04=7,.....}
	}	
}
