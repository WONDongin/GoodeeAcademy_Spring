package kr.gdu.sitemesh;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

import jakarta.servlet.annotation.WebFilter;

@WebFilter("/*")
public class SiteMeshFilter extends ConfigurableSiteMeshFilter{
	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		builder.addDecoratorPaths("/*", "layout/gdulayout.jsp")
		.addExcludedPath("user/idsearch*")
		.addExcludedPath("user/pwsearch*");
	}
}
