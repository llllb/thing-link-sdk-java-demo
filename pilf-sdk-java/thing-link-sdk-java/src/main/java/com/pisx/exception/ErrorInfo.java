package com.pisx.exception;

public class ErrorInfo {
    private int mErrorCode;
    private String mErrorMsg;

    public ErrorInfo(int errorCode, String errorMsg) {
        this.mErrorCode = errorCode;
        this.mErrorMsg = errorMsg;
    }

    public ErrorInfo(AError error) {
        this.mErrorCode = error.getCode();
        this.mErrorMsg = error.getMsg();
    }

    public boolean isSuccess() {
        return this.mErrorCode == 0 || this.mErrorCode >= 200 && this.mErrorCode < 300;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        this.mErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.mErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.mErrorMsg = errorMsg;
    }
}
