package com.mgg.model.jwt;

import java.io.Serializable;

public class ReqJwt implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	private String param;

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}