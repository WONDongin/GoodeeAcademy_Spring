package kr.gdu.sitemesh;

import java.io.IOException;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class SiteMeshFilter extends ConfigurableSiteMeshFilter {
	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		builder.addDecoratorPath("/*", "layout/gdulayout.jsp")
		.addExcludedPath("/user/idsearch*")
		.addExcludedPath("/user/pwsearch*")
		.addExcludedPath("/ajax/*"); // /ajax의 모든 요청은 sitemesh 제외시킴
	}
	@Override
	public void doFilter(ServletRequest servletRequest, 
			            ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		request.setCharacterEncoding("UTF-8");
		String url = request.getRequestURI(); //요청된 url 정보
		if(url.contains("/user/")) url="user";
		else if(url.contains("/admin/")) url="user";
		else if(url.contains("/board/")) url="board";
		else if(url.contains("/item/")) url="item";
		else if(url.contains("/cart/")) url="item";
		else if(url.contains("/chat/")) url="chat";
		else if(url.contains("/naver/")) url="naver";
		else url="";
		request.setAttribute("url", url); //속성 등록
		super.doFilter(servletRequest, servletResponse, filterChain); //다음 프로세스 진행
	}
	
}
