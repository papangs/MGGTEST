package com.mgg.model.payment;

public class ReqPayment {

	public String partnerServiceId;
	public String customerNo;
	public String virtualAccountNo;
	public ReqPaymentPaidAmount paidAmount;
	public String sourceBankCode;
	public String paymentRequestId;
	public String getPartnerServiceId() {
		return partnerServiceId;
	}
	public void setPartnerServiceId(String partnerServiceId) {
		this.partnerServiceId = partnerServiceId;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getVirtualAccountNo() {
		return virtualAccountNo;
	}
	public void setVirtualAccountNo(String virtualAccountNo) {
		this.virtualAccountNo = virtualAccountNo;
	}
	public ReqPaymentPaidAmount getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(ReqPaymentPaidAmount paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getSourceBankCode() {
		return sourceBankCode;
	}
	public void setSourceBankCode(String sourceBankCode) {
		this.sourceBankCode = sourceBankCode;
	}
	public String getPaymentRequestId() {
		return paymentRequestId;
	}
	public void setPaymentRequestId(String paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

}
