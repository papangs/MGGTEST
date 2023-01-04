package com.mgg.model.inquiry;

public class ResInquiry {

	public String responseCode;
	public String responseMessage;
	public ResInquiryVirtualAccountData virtualAccountData;
	
	public ResInquiry() {
		super();
	}

	public ResInquiry(String responseCode, String responseMessage) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}
	
	public ResInquiry(String responseCode, String responseMessage, ResInquiryVirtualAccountData virtualAccountData) {
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
	public ResInquiryVirtualAccountData getVirtualAccountData() {
		return virtualAccountData;
	}
	public void setVirtualAccountData(ResInquiryVirtualAccountData virtualAccountData) {
		this.virtualAccountData = virtualAccountData;
	}
}
