package com.cbsinc.cms;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import com.cbsinc.cms.publisher.models.GBSControllers;

public class CMSApplicationFilter {


  @Bean
  public FilterRegistrationBean<GBSControllers> authenticationfilter() {
    FilterRegistrationBean<GBSControllers> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new GBSControllers());
    registrationBean.addUrlPatterns("*/api/*");

    return registrationBean;
  }
}
