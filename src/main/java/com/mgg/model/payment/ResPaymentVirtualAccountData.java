package com.mgg.model.payment;

public class ResPaymentVirtualAccountData {

	public String partnerServiceId;
	public String customerNo;
	public String virtualAccountNo;
	public String virtualAccountName;
	public String paymentRequestId;
	public ResPaymentPaidAmount paidAmount;
	public ResPaymentAdditionalInfo additionalInfo ;
	
	public ResPaymentVirtualAccountData(String partnerServiceId, String customerNo, String virtualAccountNo,
			String virtualAccountName, String paymentRequestId, ResPaymentPaidAmount paidAmount,
			ResPaymentAdditionalInfo additionalInfo) {
		super();
		this.partnerServiceId = partnerServiceId;
		this.customerNo = customerNo;
		this.virtualAccountNo = virtualAccountNo;
		this.virtualAccountName = virtualAccountName;
		this.paymentRequestId = paymentRequestId;
		this.paidAmount = paidAmount;
		this.additionalInfo = additionalInfo;
	}
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
	public String getVirtualAccountName() {
		return virtualAccountName;
	}
	public void setVirtualAccountName(String virtualAccountName) {
		this.virtualAccountName = virtualAccountName;
	}
	public String getPaymentRequestId() {
		return paymentRequestId;
	}
	public void setPaymentRequestId(String paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}
	public ResPaymentPaidAmount getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(ResPaymentPaidAmount paidAmount) {
		this.paidAmount = paidAmount;
	}
	public ResPaymentAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(ResPaymentAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}
