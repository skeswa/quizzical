package org.quizzical.backend.notification.api.model;

public enum EXCEPTIONTYPE {
   SYSTEM(100,"System Error",
    "<span style=\"font-weight: bold;\">Error: </span><br>&nbsp;Description: $exception_description<br>&nbsp; Date/Time Exception: $dt_exception<br>&nbsp; Exception Impact: $exception_impact<br>");
	
	private Integer errorNum;
	private String errorDescription;
	private String exceptionVMTemplate;
	
	private EXCEPTIONTYPE(Integer errorNum, String errorDescription, String exceptionVMTemplate)
	{
		this.errorNum = errorNum;
		this.errorDescription = errorDescription;
		this.exceptionVMTemplate = exceptionVMTemplate;
	}

	public Integer getErrorNum() {
		return errorNum;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public String getExceptionVMTemplate() {
		return exceptionVMTemplate;
	}
}
