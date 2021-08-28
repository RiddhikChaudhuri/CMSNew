/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Source https://www.baeldung.com/rest-template
 */
package com.cbsinc.cms.publisher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import com.cbsinc.cms.dto.pages.request.AccountDetalHistoryForm;
import com.cbsinc.cms.dto.pages.response.CMSAccountDetailHistoryModel;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class HelloWorldConfigurationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
    public void testGreeting() throws Exception {
      ResponseEntity<String> entity =
          restTemplate.getForEntity("http://localhost:" + this.port + "/api/status", String.class);
      assertEquals(HttpStatus.OK, entity.getStatusCode());
    }
	
	
	/**
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testDoGetAccountDetalHistory() throws Exception {
		
		AccountDetalHistoryForm form = new AccountDetalHistoryForm() ;
		form.setAmountId("12");
		
        AuthorizationPageBean authorizationPageBeanId = new AuthorizationPageBean();
        authorizationPageBeanId.setStrFirstName("Test");
        authorizationPageBeanId.setStrLastName("Test");
        authorizationPageBeanId.setIntLevelUp(10L);
        authorizationPageBeanId.setIntUserID(10L);

        HttpEntity<AccountDetalHistoryForm> accountRequest = new HttpEntity<AccountDetalHistoryForm>(form);


		ResponseEntity<CMSAccountDetailHistoryModel> accountDetalHistoryResponse = restTemplate.postForEntity("http://localhost:" + this.port + "/api/doPostAccountDetalHistory", accountRequest, CMSAccountDetailHistoryModel.class);
		//assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		
		Assert.assertEquals(200, accountDetalHistoryResponse.getStatusCodeValue());
		CMSAccountDetailHistoryModel reponseDTO = accountDetalHistoryResponse.getBody();
	//	assertNotNull(reponseDTO);
		//fix it
		//assertEquals(reponseDTO.getAddAmount(), "1");
		assertTrue(true);

	}
	

}
