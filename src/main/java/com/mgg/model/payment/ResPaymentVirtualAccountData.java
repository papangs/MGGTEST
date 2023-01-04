package com.mgg.model.payment;

public class ResPaymentVirtualAccountData {

	public String retnum;
	public String trx_date;
	public String trx_time;
	public String customerNo;
	public String virtualAccountName;
	public String virtualAccountAddress;
	public ResPaymentPaidAmount paidAmount;
	public String trxid;
	
	public ResPaymentVirtualAccountData() {
		super();
	}
	public ResPaymentVirtualAccountData(String retnum, String trx_date, String trx_time, String customerNo,
			String virtualAccountName, String virtualAccountAddress, ResPaymentPaidAmount paidAmount, String trxid) {
		super();
		this.retnum = retnum;
		this.trx_date = trx_date;
		this.trx_time = trx_time;
		this.customerNo = customerNo;
		this.virtualAccountName = virtualAccountName;
		this.virtualAccountAddress = virtualAccountAddress;
		this.paidAmount = paidAmount;
		this.trxid = trxid;
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
	public ResPaymentPaidAmount getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(ResPaymentPaidAmount paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getTrxid() {
		return trxid;
	}
	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}
	
	
}
