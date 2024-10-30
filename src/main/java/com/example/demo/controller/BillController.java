package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Bill;
import com.example.demo.service.BillService;
import com.itextpdf.text.DocumentException;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin
public class BillController {
	
	@Autowired 
	BillService billservice;
	
	 @GetMapping("/getPaymentData")
	 public ResponseEntity<List<Bill>> getPaymentData() {
	        List<Bill> bill = billservice.getAllPaymentData();
	        return ResponseEntity.ok(bill);
	 }
	
	 @GetMapping("/generateInvoice/{billId}")
	 public ResponseEntity<InputStreamResource> generateInvoice(@PathVariable int billId) throws DocumentException {
		    ByteArrayInputStream pdfStream = billservice.generateInvoice(billId);

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "inline; filename=invoice.pdf");

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(new InputStreamResource(pdfStream));
	 }
	 @PostMapping("/updatePayStatus/{billId}")
	 public ResponseEntity<String> updatePayStatus(@PathVariable int billId){
		  try {
		        billservice.updatePayStatus(billId);
		        return new ResponseEntity<>(HttpStatus.OK);
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		    }
	 }
	 @PostMapping("/invoiceMail/{billId}")
     public ResponseEntity<String> invoiceMail(@PathVariable int billId) {
		  try {
		        billservice.invoiceMail(billId);
		        return new ResponseEntity<>(HttpStatus.OK);
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		    }
			
	 }
}
