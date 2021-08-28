package com.cbsinc.cms;

import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.cbsinc.cms.publisher.models.ImageServlet;
import com.cbsinc.cms.publisher.models.UploadServlet;
import com.google.common.collect.ImmutableMap;

/*Servlet Registeration is being done here*/
@Configuration
@ComponentScan("com.cbsinc.*")
@PropertySource("classpath:application.properties")
public class CMSConfig {
  
  @Value("${spring.datasource.url}")
  private String springDataSourceUrl;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.username}")
  private String databaseUsername;

  @Value("${spring.datasource.driver.class}")
  private String springDriverClass;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(springDriverClass);
    dataSource.setUrl(springDataSourceUrl);
    dataSource.setUsername(databaseUsername);
    dataSource.setPassword(password);

    return dataSource;
  }

  @Bean
  public ServletRegistrationBean<HttpServlet> uploadServlet() {
    ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
    servRegBean.setServlet(new UploadServlet());
    servRegBean.addUrlMappings("/uploadservlet/*");
    servRegBean.setLoadOnStartup(1);
    return servRegBean;
  }

  @Bean
  public ServletRegistrationBean<HttpServlet> imageServlet() {
    ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
    servRegBean.setServlet(new ImageServlet());
    servRegBean.addUrlMappings("/imageServlet/*");
    servRegBean.setLoadOnStartup(1);
    return servRegBean;
  }



}
