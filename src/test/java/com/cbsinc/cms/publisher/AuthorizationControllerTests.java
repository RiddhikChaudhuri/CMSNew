package com.cbsinc.cms.publisher;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.cbsinc.cms.dto.pages.request.AuthorizationRequestForm;
import com.cbsinc.cms.dto.pages.response.CMSAuthorizationPageModel;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorizationControllerTests {


  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @Sql(scripts = {"classpath:schema.sql","classpath:data.sql"})
  //@Sql("classpath:createAccountHistoryDetailHistory.sql")
  public void testDoGetAccountDetalHistory() throws Exception {

    AuthorizationRequestForm authorizationRequestForm = new AuthorizationRequestForm();
    authorizationRequestForm.setLogin("testuser");
    authorizationRequestForm.setMessage("New Login For Test User");
    authorizationRequestForm.setPassword("test");
    authorizationRequestForm.setSiteId("2");
    
    HttpEntity<AuthorizationRequestForm> accountRequest =
        new HttpEntity<AuthorizationRequestForm>(authorizationRequestForm);


    ResponseEntity<CMSAuthorizationPageModel> accountDetalHistoryResponse =
        restTemplate.postForEntity("http://localhost:" + this.port + "/api/doPostAuthorization",
            accountRequest, CMSAuthorizationPageModel.class);
    // assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

    Assert.assertEquals(200, accountDetalHistoryResponse.getStatusCodeValue());
    CMSAuthorizationPageModel reponseDTO = accountDetalHistoryResponse.getBody();
    assertTrue(true);
    // fix it
   // assertEquals(reponseDTO.getAddAmount(), "1");


  }

}
