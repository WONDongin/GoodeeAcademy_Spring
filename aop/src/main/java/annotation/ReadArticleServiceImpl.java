package annotation;

import org.springframework.stereotype.Component;

@Component("readArticleService") // readArticleService 객체 생성(이름 직접 설정)
public class ReadArticleServiceImpl implements ReadArticleService {
	@Override
	public Article getArticleAndReadCnt(int id) throws Exception{
		System.out.println("getArticleAndReadCnt(" + id + ")");
		if(id == 0) {
			throw new Exception(" id는 0이 안됨");
		}
		return new Article(id);
	}
}
