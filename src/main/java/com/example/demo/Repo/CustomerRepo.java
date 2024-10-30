package com.example.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
	Customer findByCustomerId(Long customerId);
    boolean existsByCustomerId(Long customerId);
	

	
}
