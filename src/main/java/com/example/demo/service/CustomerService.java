package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repo.BillRepo;
import com.example.demo.Repo.CustomerRepo;
import com.example.demo.dto.CustomerDto;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerrepo;
    
    @Autowired
    private BillRepo billrepo;

    public void saveCustomer(CustomerDto customerdto) {
      
       Long customerId = customerdto.getCustomerId();
        
        if (customerId != null && customerrepo.existsByCustomerId(customerId)) {
           
            Customer existingCustomer = customerrepo.findByCustomerId(customerId);
           
            Bill bill = new Bill();
            bill.setCustomer(existingCustomer);
            bill.setBillDueDate(customerdto.getBillDueDate());
            bill.setBillingEndDate(customerdto.getBillingEndDate());
            bill.setBillingStartDate(customerdto.getBillingStartDate());
            bill.setUnitConsumption(customerdto.getUnitConsumption());
            bill.setAmountDue(customerdto.getUnitConsumption()*41.5);
            bill.setBillStatus("Pending");
            billrepo.save(bill);
        } else {
           
            Customer newCustomer = new Customer();
            newCustomer.setCustomerId(customerdto.getCustomerId());
            newCustomer.setCustomerName(customerdto.getCustomerName());
            newCustomer.setEmail(customerdto.getEmail());
            newCustomer.setMobileNumber(customerdto.getMobileNumber());
            newCustomer.setAddress(customerdto.getAddress());
            newCustomer.setWallet(0);

            Bill bill = new Bill();
            bill.setCustomer(newCustomer);
            bill.setBillDueDate(customerdto.getBillDueDate());
            bill.setBillingEndDate(customerdto.getBillingEndDate());
            bill.setBillingStartDate(customerdto.getBillingStartDate());
            bill.setUnitConsumption(customerdto.getUnitConsumption());
            bill.setAmountDue(customerdto.getUnitConsumption()*41.5);
            bill.setBillStatus("Pending");
            customerrepo.save(newCustomer);
            billrepo.save(bill);
        }
        
        
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
