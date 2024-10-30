package com.example.demo.service;

import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.Repo.EmployeeRepo;
import com.example.demo.entity.Employee;

@Service
public class EmployeeService {
   
	
	@Autowired
	EmployeeRepo repo;
	@Autowired
	private JavaMailSender mailSender;
    
	public Boolean validateEmpId(String empId) {
		
		return repo.existsByempId(empId);
	}

	public String generateRandomOtp() {
		 Random random = new Random();
	     int otp = 100000 + random.nextInt(900000); 
	     return String.valueOf(otp);
	}

	public void sendOtpToMail(String otp,String empId) {
		Employee employee=repo.findByEmpId(empId);
		 
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(employee.getEmail());
        message.setSubject("Hello "+employee.getName());
        message.setText("Your OTP code is: " + otp);
      
        mailSender.send(message);
		
    }
}

