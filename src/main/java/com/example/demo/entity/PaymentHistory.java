package com.example.demo.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_ref")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    
    @Column(name="net_amount")
    private double netAmount;
    
  
    public double getNetAmount() {
		return netAmount;
	}


	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	@Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false, length = 10)
    private String paymentStatus;

    @Column(name = "discount", nullable = false)
    private double discount;

 
    public PaymentHistory() {
        super();
    }

  
    public PaymentHistory(Bill bill, LocalDate paymentDate, double amount, String paymentMethod,
                   String paymentStatus, double discount,double netAmount) {
        this.bill = bill;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.discount = discount;
        this.netAmount=netAmount;
    }

    // Getters and Setters
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", bill=" + bill + ", paymentDate=" + paymentDate + ", netAmount="
				+ netAmount + ", amount=" + amount + ", paymentMethod=" + paymentMethod + ", paymentStatus="
				+ paymentStatus + ", discount=" + discount + ", getNetAmount()=" + getNetAmount() + ", getPaymentId()="
				+ getPaymentId() + ", getBill()=" + getBill() + ", getPaymentDate()=" + getPaymentDate()
				+ ", getAmount()=" + getAmount() + ", getPaymentMethod()=" + getPaymentMethod()
				+ ", getPaymentStatus()=" + getPaymentStatus() + ", getDiscount()=" + getDiscount() + "]";
	}
}
