package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CustomerDto;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Customer;
import com.example.demo.entity.PaymentHistory;
import com.example.demo.dto.CustomerDto;
import com.example.demo.entity.Customer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CustomerEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendCustomerEmail(CustomerDto customerdto) {
    	
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customerdto.getEmail()); 
        message.setSubject("BBC Bill Service"); 
        message.setText("Dear " + customerdto.getCustomerName() + ",\n\n" +
                        "you consumed"+customerdto.getUnitConsumption()+"unit"+",\n\n"+
                         "your bill due date is"+customerdto.getBillDueDate()+",\n\n"+
                        "for this bill you have to pay Rs"+customerdto.getUnitConsumption()*41.5+",\n\n"+
                         "Please Pay your bill within duedate"+",\n\n"+
                       
                        "Thank you for using our service!"); 

       
        mailSender.send(message);
    }
    
}
