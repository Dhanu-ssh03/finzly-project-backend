package com.example.demo.dto;

public class ResponseObject {
	  private boolean exists;
	    private String otp;
	    public ResponseObject(boolean exists, String otp) {
	        this.exists = exists;
	        this.otp = otp;
	    }

	    public boolean isExists() {
	        return exists;
	    }

	    public void setExists(boolean exists) {
	        this.exists = exists;
	    }

	    public String getOtp() {
	        return otp;
	    }

	    public void setOtp(String otp) {
	        this.otp = otp;
	    }
}
