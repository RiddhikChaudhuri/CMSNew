package com.cbsinc.cms.dto.pages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayRequestBody {

	@JsonProperty("Amount")
	private String Amount;

	private String currency_id;

	private String paysystem_id;

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}

	public String getPaysystem_id() {
		return paysystem_id;
	}

	public void setPaysystem_id(String paysystem_id) {
		this.paysystem_id = paysystem_id;
	}

	@Override
	public String toString() {
		return "PayRequestBody [Amount=" + Amount + ", currency_id=" + currency_id + ", paysystem_id=" + paysystem_id
				+ "]";
	}

}
