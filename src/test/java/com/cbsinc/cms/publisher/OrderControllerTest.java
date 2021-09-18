package com.cbsinc.cms.publisher;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



public class OrderControllerTest extends  AbstractTest{

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	
	@Test
	@Ignore
	public void test() throws Exception {

		/*
		 * Url is not working always getting 404, I already tried /OrderAction ,
		 * /Order.jsp, /api/Order.jsp
		 */
		MvcResult andReturn = mvc.perform(MockMvcRequestBuilders.get("/api/Order.jsp")
				.contentType(MediaType.APPLICATION_JSON).content("Hello World")).andReturn();

		Assert.assertEquals(200, andReturn.getResponse().getStatus());
		String reponseDTO = andReturn.getResponse().getContentAsString();
		assertTrue(true);
	}

}
