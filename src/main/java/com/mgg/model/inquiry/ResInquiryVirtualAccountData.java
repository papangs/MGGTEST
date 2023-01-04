package com.mgg.model.inquiry;

public class ResInquiryVirtualAccountData {

	public String retnum;
	public String trx_date;
	public String trx_time;
	public String customerNo;
	public String virtualAccountName;
	public String virtualAccountAddress;
	public ResInquiryTotalAmount totalAmount;
	public ResInquiryAdditionalInfo additionalInfo;
	
	public ResInquiryVirtualAccountData() {
		super();
	}
	public ResInquiryVirtualAccountData(String retnum, String trx_date, String trx_time, String customerNo,
			String virtualAccountName, String virtualAccountAddress, ResInquiryTotalAmount totalAmount,
			ResInquiryAdditionalInfo additionalInfo) {
		super();
		this.retnum = retnum;
		this.trx_date = trx_date;
		this.trx_time = trx_time;
		this.customerNo = customerNo;
		this.virtualAccountName = virtualAccountName;
		this.virtualAccountAddress = virtualAccountAddress;
		this.totalAmount = totalAmount;
		this.additionalInfo = additionalInfo;
	}
	public String getRetnum() {
		return retnum;
	}
	public void setRetnum(String retnum) {
		this.retnum = retnum;
	}
	public String getTrx_date() {
		return trx_date;
	}
	public void setTrx_date(String trx_date) {
		this.trx_date = trx_date;
	}
	public String getTrx_time() {
		return trx_time;
	}
	public void setTrx_time(String trx_time) {
		this.trx_time = trx_time;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getVirtualAccountName() {
		return virtualAccountName;
	}
	public void setVirtualAccountName(String virtualAccountName) {
		this.virtualAccountName = virtualAccountName;
	}
	public String getVirtualAccountAddress() {
		return virtualAccountAddress;
	}
	public void setVirtualAccountAddress(String virtualAccountAddress) {
		this.virtualAccountAddress = virtualAccountAddress;
	}
	public ResInquiryTotalAmount getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(ResInquiryTotalAmount totalAmount) {
		this.totalAmount = totalAmount;
	}
	public ResInquiryAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(ResInquiryAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	
}
