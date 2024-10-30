package com.example.demo.Repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Bill;

public interface BillRepo extends JpaRepository<Bill,Integer>{
	List<Bill> findBybillStatus(String string);
    Bill findCustomerByBillId(int billId);
	public boolean existsByCustomerCustomerIdAndBillingStartDateAndBillingEndDate(Long customerId, LocalDate billingStartDate, LocalDate billingEndDate);
 
}
