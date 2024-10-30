package com.example.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.PaymentHistory;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentHistory,Integer> {
	PaymentHistory findByBillBillId(int billId);

}
