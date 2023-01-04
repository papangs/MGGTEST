package com.mgg.model.payment;

public class ReqPaymentTotalAmount {

	public String value;
	public String currency;
	
	public ReqPaymentTotalAmount() {
		super();
	}
	public ReqPaymentTotalAmount(String value, String currency) {
		super();
		this.value = value;
		this.currency = currency;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
