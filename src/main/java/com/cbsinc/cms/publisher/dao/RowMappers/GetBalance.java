package com.cbsinc.cms.publisher.dao.RowMappers;

public class GetBalance {

	private String amount;
	private String currency_lable;
	private String currency_desc;

	public GetBalance(String amount, String currency_lable, String currency_desc) {
		super();
		this.amount = amount;
		this.currency_lable = currency_lable;
		this.currency_desc = currency_desc;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency_lable() {
		return currency_lable;
	}

	public void setCurrency_lable(String currency_lable) {
		this.currency_lable = currency_lable;
	}

	public String getCurrency_desc() {
		return currency_desc;
	}

	public void setCurrency_desc(String currency_desc) {
		this.currency_desc = currency_desc;
	}

	@Override
	public String toString() {
		return "GetBalance [amount=" + amount + ", currency_lable=" + currency_lable + ", currency_desc="
				+ currency_desc + "]";
	}

}
