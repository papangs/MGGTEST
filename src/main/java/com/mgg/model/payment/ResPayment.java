package com.mgg.model.payment;

public class ResPayment {

	public String responseCode;
	public String responseMessage;
	public ResPaymentVirtualAccountData virtualAccountData;
	
	public ResPayment() {
		super();
	}

	public ResPayment(String responseCode, String responseMessage) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}
	
	public ResPayment(String responseCode, String responseMessage, ResPaymentVirtualAccountData virtualAccountData) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.virtualAccountData = virtualAccountData;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public ResPaymentVirtualAccountData getVirtualAccountData() {
		return virtualAccountData;
	}
	public void setVirtualAccountData(ResPaymentVirtualAccountData virtualAccountData) {
		this.virtualAccountData = virtualAccountData;
	}
	
}
