package kr.gdu.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class ShopUtil {
	  public static String getRandomString
	      (int count, boolean letter, boolean number) {
		  //count : 임의의 문자열 갯수
		  //letter : true인 경우 영문자 포함
		  //number : true인 경우 숫자 포함
		    StringBuilder builder = new StringBuilder();
		    List<String> list = new ArrayList<String>();
		    if(letter) {
		      for(char ch = 'A'; ch <= 'Z'; ch++) {  //대문자 추가
		        list.add(ch + "");
		      }
		      for(char ch = 'a'; ch <= 'z'; ch++) { //소문자 추가
		        list.add(ch + "");
		      }
		    }
		    if(number) {
		      for(int n = 0; n <= 9; n++) {
		        list.add(n + "");
		      }
		    }
		    //SecureRandom : Random 클래스보다 보안이 강화된 클래스
		    SecureRandom secureRandom = new SecureRandom();
		    if(letter || number) {
		      while(count > 0) {
		        builder.append(list.get(secureRandom.nextInt(list.size())));
		        count--;
		      }
		    }
		    return builder.toString();
		  }

}
