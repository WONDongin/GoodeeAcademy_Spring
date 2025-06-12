package kr.gdu.config;

import java.beans.PropertyVetoException;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class DBConfig {
	@Bean(destroyMethod="close")
	public DataSource dataSource() { //Connection 객체		
		ComboPooledDataSource ds = new ComboPooledDataSource(); //컨넥션풀객체
		try {
			ds.setDriverClass("org.mariadb.jdbc.Driver");
			ds.setJdbcUrl("jdbc:mariadb://localhost:3306/gdjdb");
			ds.setUser("gduser");
			ds.setPassword("1234");
			ds.setMaxPoolSize(20); //최대 객체의 갯수
			ds.setMinPoolSize(3);  //최소 객체의 갯수 
			ds.setInitialPoolSize(5); //초기화 객체의 갯수
			ds.setAcquireIncrement(5); //증가 단위
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return ds;
	}
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource());
		bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		return bean.getObject();
	}
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() throws Exception{
		return new SqlSessionTemplate(sqlSessionFactory());
	}
}
