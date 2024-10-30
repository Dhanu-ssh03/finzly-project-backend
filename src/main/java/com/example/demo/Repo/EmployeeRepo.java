package com.example.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee,Integer>{
     
	Boolean existsByempId(String empId);
    Employee findByEmpId(String empId);

 }
