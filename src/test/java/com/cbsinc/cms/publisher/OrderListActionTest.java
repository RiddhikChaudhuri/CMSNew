package com.cbsinc.cms.publisher;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cbsinc.cms.dto.pages.request.OrderListRequestForm;
import com.cbsinc.cms.publisher.models.OrderListBean;

public class OrderListActionTest extends AbstractTest {
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void test() throws Exception {

		OrderListRequestForm orderListRequestForm = new OrderListRequestForm();

		MockHttpSession session = new MockHttpSession();

		session.setAttribute("orderListBeanId", Optional.ofNullable(new OrderListBean()));

		MvcResult andReturn = mvc.perform(MockMvcRequestBuilders.post("/doPostOrderList").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(mapToJson(orderListRequestForm))).andReturn();

		Assert.assertEquals(200, andReturn.getResponse().getStatus());
		String reponseDTO = andReturn.getResponse().getContentAsString();
		assertTrue(true);
	}

}
