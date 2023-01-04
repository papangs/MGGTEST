package com.mgg.model.payment;

public class ResPaymentAdditionalInfo {

	public String idApp;
	public String info1;
	
	public ResPaymentAdditionalInfo(String idApp, String info1) {
		super();
		this.idApp = idApp;
		this.info1 = info1;
	}
	public String getIdApp() {
		return idApp;
	}
	public void setIdApp(String idApp) {
		this.idApp = idApp;
	}
	public String getInfo1() {
		return info1;
	}
	public void setInfo1(String info1) {
		this.info1 = info1;
	}
}
