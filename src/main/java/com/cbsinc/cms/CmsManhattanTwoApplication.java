package com.cbsinc.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = {"com.cbsinc.*"} )
public class CmsManhattanTwoApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
	  new CmsManhattanTwoApplication().configure(new SpringApplicationBuilder(CmsManhattanTwoApplication.class)).run(args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CmsManhattanTwoApplication.class);
    }

}
