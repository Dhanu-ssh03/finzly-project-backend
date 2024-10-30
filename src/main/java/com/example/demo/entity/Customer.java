package com.example.demo.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "customer_details")
public class Customer {

    @Id
   
    @Column(name = "customerid", unique = true)
    private Long customerId;

    @Column(name = "name", nullable = false)
    private String customerName;

    @Column(name = "address")
    private String address;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;
    
    @Column(name = "wallet", nullable = false)
    private double wallet;
    
	

    
    
    public double getWallet() {
		return wallet;
	}

	public void setWallet(double wallet) {
		this.wallet = wallet;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

  

    @Override
    public String toString() {
        return "Customer [customerId=" + customerId + ", customerName=" + customerName + 
               ", email=" + email + ", mobileNumber=" + mobileNumber + ", address=" + address + "]";
    }
}
