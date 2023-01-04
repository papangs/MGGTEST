package com.mgg.model.inquiry;

public class ReqInquiry {

	public String retnum;
	public String customerNo;
	public String sourceBankCode;
	public String trx_date;
	public String trx_time;
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getSourceBankCode() {
		return sourceBankCode;
	}
	public void setSourceBankCode(String sourceBankCode) {
		this.sourceBankCode = sourceBankCode;
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
	
}
