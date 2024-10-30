package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repo.BillRepo;
import com.example.demo.Repo.PaymentRepo;
import com.example.demo.entity.Bill;
import com.example.demo.entity.PaymentHistory;

@Service
public class PaymentService {
   @Autowired
   PaymentRepo paymentrepo;
   
   @Autowired
   BillRepo billrepo;
	public void savePayment(int billId) {
		Optional<Bill> billDetail = billrepo.findById(billId);
		
		if(billDetail.isPresent()) {
			 PaymentHistory paymentHistory = new PaymentHistory();
			    Bill bill = billDetail.get();
			    paymentHistory.setBill(bill);
			    paymentHistory.setAmount(bill.getAmountDue());
			   
			    LocalDate currentDate = LocalDate.now();
			    
			    double discount = 0;
			    if (bill.getBillDueDate() != null && currentDate.isBefore(bill.getBillDueDate())) {  
	                discount = bill.getAmountDue() * 0.05;  // 5% discount
	            }
			    paymentHistory.setDiscount(discount);
			    
			    double netAmount = bill.getAmountDue() - discount;
			    paymentHistory.setNetAmount(netAmount);
			    paymentHistory.setPaymentDate(currentDate);
			    paymentHistory.setPaymentMethod("Cash");
			    paymentHistory.setPaymentStatus("Success");
			   
			     paymentrepo.save(paymentHistory);
			
		}
		else {
			 throw new RuntimeException("User not found ID: " + billId);
		}
		
	}
	public List<PaymentHistory> getPaymentHistory() {
		
		return paymentrepo.findAll();
		
	}

}
