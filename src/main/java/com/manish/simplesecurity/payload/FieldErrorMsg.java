package com.manish.simplesecurity.payload;

public class FieldErrorMsg {

	private String fieldName;
	private String errorMsg;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public FieldErrorMsg(String fieldName, String errorMsg) {
		super();
		this.fieldName = fieldName;
		this.errorMsg = errorMsg;
	}
	
	
	
	
}
