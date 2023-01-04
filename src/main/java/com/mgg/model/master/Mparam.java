package com.mgg.model.master;

public class Mparam {
	
	private long id;
	private String grp_param;
	private String kd_param;
	private String param;
	private String s_aktif;
	private String desc_param;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getGrp_param() {
		return grp_param;
	}
	
	public void setGrp_param(String grp_param) {
		this.grp_param = grp_param;
	}
	
	public String getKd_param() {
		return kd_param;
	}
	
	public void setKd_param(String kd_param) {
		this.kd_param = kd_param;
	}
	
	public String getParam() {
		return param;
	}
	
	public void setParam(String param) {
		this.param = param;
	}
	
	public String getS_aktif() {
		return s_aktif;
	}
	
	public void setS_aktif(String s_aktif) {
		this.s_aktif = s_aktif;
	}
	
	public String getDesc_param() {
		return desc_param;
	}
	
	public void setDesc_param(String desc_param) {
		this.desc_param = desc_param;
	}

	public Mparam(long id, String grp_param, String kd_param, String param, String s_aktif, String desc_param) {
		super();
		this.id = id;
		this.grp_param = grp_param;
		this.kd_param = kd_param;
		this.param = param;
		this.s_aktif = s_aktif;
		this.desc_param = desc_param;
	}

	public Mparam() {
		super();
	}
	
}
