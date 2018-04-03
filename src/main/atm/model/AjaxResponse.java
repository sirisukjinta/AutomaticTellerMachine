// Copyright 2001-2016: Thomson Reuters Global Resources: All Rights Reserved. Proprietary and Confidential information of TRGR.
// Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
package main.atm.model;

public class AjaxResponse {
	public static final String STATUS_FAIL = "FAIL";
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_FORBIDDEN = "FORBIDDEN";

	private String status;
	private Object result;

	public AjaxResponse(String status) {
		this.status = status;
	}

	public AjaxResponse() {
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getResult() {
		return this.result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
