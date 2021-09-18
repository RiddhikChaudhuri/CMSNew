package com.cbsinc.cms.publisher;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cbsinc.cms.dto.pages.request.PayRequestBody;

public class PayControllerTests extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	@Ignore
	public void submitPayRequest() throws Exception {

		PayRequestBody request = new PayRequestBody();
		request.setAmount("100");
		request.setCurrency_id("EN");

		// Not able to access the PayAction class.
		// TODO: Endpoint tried GET & POST: /api/pay, /api/payAction/pay

		MvcResult andReturn = mvc.perform(
				MockMvcRequestBuilders.get("/pay").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
				.andReturn();

		Assert.assertEquals(200, andReturn.getResponse().getStatus());
		String reponseDTO = andReturn.getResponse().getContentAsString();
		assertTrue(true);
	}
}
