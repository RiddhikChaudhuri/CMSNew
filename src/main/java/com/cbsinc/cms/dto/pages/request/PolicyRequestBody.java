package com.cbsinc.cms.dto.pages.request;

public class PolicyRequestBody {

	private String policy_byproductid;

	private String page;

	private String itemDescriptionBeanId;

	private String rate;

	public String getPolicy_byproductid() {
		return policy_byproductid;
	}

	public void setPolicy_byproductid(String policy_byproductid) {
		this.policy_byproductid = policy_byproductid;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getItemDescriptionBeanId() {
		return itemDescriptionBeanId;
	}

	public void setItemDescriptionBeanId(String itemDescriptionBeanId) {
		this.itemDescriptionBeanId = itemDescriptionBeanId;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "PolicyRequestBody [policy_byproductid=" + policy_byproductid + ", page=" + page
				+ ", itemDescriptionBeanId=" + itemDescriptionBeanId + ", rate=" + rate + "]";
	}
	
	

}
