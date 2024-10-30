package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CustomerDto;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerEmailService;
import com.example.demo.service.CustomerService;

@RestController
@CrossOrigin
public class CustomerController{

    @Autowired
    private CustomerService customerservice;

    @Autowired
    private CustomerEmailService customerEmailService;

    @PostMapping("/saveCustomer")
    public ResponseEntity<Customer> saveCustomer(@RequestBody CustomerDto customerdto) {
       try {
            customerservice.saveCustomer(customerdto);
            customerEmailService.sendCustomerEmail(customerdto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.err.println("Error saving customer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
   
    
  
}
