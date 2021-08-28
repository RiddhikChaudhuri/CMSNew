package com.cbsinc.cms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {                                    
   
  public static final Contact DEFAULT_CONTACT = new Contact(
      "Konstantin Grabko", "konstantin.grabko@yahoo.com", "konstantin.grabko@gmail.com");
  
  public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
      "Content Manager System", "Awesome API Description", "1.0",
      "Description: System building web application develop by Konstantin Grabko.", DEFAULT_CONTACT, 
      "CENTER BUSINESS SOLUTIONS INC", "konstantin.grabko@yahoo.com",Arrays.asList());
  
  
  private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(Arrays.asList("application/json"));
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                             
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any()).build() 
          .apiInfo(DEFAULT_API_INFO)
          .produces(DEFAULT_PRODUCES_AND_CONSUMES)
          .consumes(DEFAULT_PRODUCES_AND_CONSUMES);           
        
    }
}