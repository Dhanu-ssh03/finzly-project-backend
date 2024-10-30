package com.example.demo.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;


public class CustomerDto {


	private Long customerId;
	private String customerName;
	private int unitConsumption;
	private LocalDate billingStartDate;
	private LocalDate  billingEndDate;
	private LocalDate  billDueDate;
	private String email;
	private String mobileNumber;
	private String address;

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getUnitConsumption() {
		return unitConsumption;
	}
	public void setUnitConsumption(int unitConsumption) {
		this.unitConsumption = unitConsumption;
	}
	public LocalDate getBillingStartDate() {
		return billingStartDate;
	}
	public void setBillingStartDate(LocalDate billingStartDate) {
		this.billingStartDate = billingStartDate;
	}
	public LocalDate getBillingEndDate() {
		return billingEndDate;
	}
	public void setBillingEndDate(LocalDate billingEndDate) {
		this.billingEndDate = billingEndDate;
	}
	public LocalDate getBillDueDate() {
		return billDueDate;
	}
	public void setBillDueDate(LocalDate billDueDate) {
		this.billDueDate = billDueDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
