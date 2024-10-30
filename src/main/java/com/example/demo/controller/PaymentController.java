package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PaymentHistory;
import com.example.demo.service.PaymentService;

@RestController
@CrossOrigin
public class PaymentController {

	@Autowired
	PaymentService paymentservice;
	
	@PostMapping("/savePayment/{billId}")
	public ResponseEntity<String> savePayment(@PathVariable int billId){
		paymentservice.savePayment(billId);
		return null;
	}
	@GetMapping("/getPaymentHistory")
	public ResponseEntity<List<PaymentHistory>> getPaymentHistory() {
	     List<PaymentHistory> allPayments = paymentservice.getPaymentHistory();
         return new ResponseEntity<>(allPayments, HttpStatus.OK);
	}
	
}
