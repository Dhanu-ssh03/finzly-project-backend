package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseObject;
import com.example.demo.service.EmployeeService;
 
@RestController
@CrossOrigin
public class employeeController {
         
	@Autowired
	EmployeeService employeeservice;
	
	 @GetMapping("/validateEmpId/{empId}")
	 public ResponseEntity<ResponseObject> validateEmpId(@PathVariable String empId) {
	     Boolean exists = employeeservice.validateEmpId(empId);
         String otp = null;
         if (exists) {
	         otp = employeeservice.generateRandomOtp();
	         employeeservice.sendOtpToMail(otp, empId);
	     }
         ResponseObject response = new ResponseObject(exists, otp);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	 
}
