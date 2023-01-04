package com.mgg.model.master;

public class TbTrans {
	
	private String trxid;
	private String trans_dt;
	private String trans_tm;
	private String status;
	private String retnum;
	private String customer_id;
	private String customer_name;
	private String customer_address;
	private String amount;
	private String fine;
	private String total;
	private String response_inquiry;
	private String response_payment;
	private String additional_data;
	private String product_id;
	private String merchant_id;
	private String periode;
	public String getTrxid() {
		return trxid;
	}
	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}
	public String getTrans_dt() {
		return trans_dt;
	}
	public void setTrans_dt(String trans_dt) {
		this.trans_dt = trans_dt;
	}
	public String getTrans_tm() {
		return trans_tm;
	}
	public void setTrans_tm(String trans_tm) {
		this.trans_tm = trans_tm;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRetnum() {
		return retnum;
	}
	public void setRetnum(String retnum) {
		this.retnum = retnum;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomer_address() {
		return customer_address;
	}
	public void setCustomer_address(String customer_address) {
		this.customer_address = customer_address;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFine() {
		return fine;
	}
	public void setFine(String fine) {
		this.fine = fine;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAdditional_data() {
		return additional_data;
	}
	public void setAdditional_data(String additional_data) {
		this.additional_data = additional_data;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getPeriode() {
		return periode;
	}
	public void setPeriode(String periode) {
		this.periode = periode;
	}
	public String getResponse_inquiry() {
		return response_inquiry;
	}
	public void setResponse_inquiry(String response_inquiry) {
		this.response_inquiry = response_inquiry;
	}
	public String getResponse_payment() {
		return response_payment;
	}
	public void setResponse_payment(String response_payment) {
		this.response_payment = response_payment;
	}
}
