package com.example.demo.controller;


import com.example.demo.Repo.BillRepo;
import com.example.demo.Repo.CustomerRepo;
import com.example.demo.dto.CustomerDto;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class CustomerCsvProcessController {

    @Autowired
    private CustomerRepo customerRepository;

    @Autowired
    private BillRepo billRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final List<String> failedRecords = new ArrayList<>();


    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {
        successCount.set(0);  
        failureCount.set(0);
        failedRecords.clear();

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String header = reader.readLine();
            List<String> lines = reader.lines().collect(Collectors.toList());


            List<CompletableFuture<Void>> futures = lines.stream()
                    .map(line ->CompletableFuture.runAsync(() -> processRecord(line), taskExecutor))
                    .collect(Collectors.toList());

           
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

       
        if (!failedRecords.isEmpty()) {


            File outputDir = new File("data/failedRecords"); 
            if (!outputDir.exists()) {
                outputDir.mkdirs(); 
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = "failed_records_" + timestamp + ".csv";

            File failedRecordsFile = new File(outputDir, fileName);

            try (FileWriter writer = new FileWriter(failedRecordsFile)) {
                    for (String record : failedRecords) {
                        writer.write(record + "\n");
                    }
                    System.out.println("Failed records written to: " + failedRecordsFile.getAbsolutePath());// FileWriter will be closed automatically here

            } catch (IOException e) {
                
                System.err.println("Error creating or writing to failed_records.csv: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error writing failed records: " + e.getMessage()); 
            }
        }

        return ResponseEntity.ok("CSV processing initiated. Success: " + successCount.get() + ", Failures: " + failureCount.get());
    }



    private void processRecord(String record) {
        String[] fields = record.split(",");
        try {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setCustomerId(Long.parseLong(fields[0]));
            customerDto.setCustomerName(fields[1]);
            customerDto.setUnitConsumption(Integer.parseInt(fields[2]));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            customerDto.setBillingStartDate(LocalDate.parse(fields[3], formatter));
            customerDto.setBillingEndDate(LocalDate.parse(fields[4], formatter));
            customerDto.setBillDueDate(LocalDate.parse(fields[5], formatter));

            customerDto.setEmail(fields[6]);
            customerDto.setMobileNumber(fields[7]);
            customerDto.setAddress(fields[8]);


            validateAndSave(customerDto);

        } catch (NumberFormatException | DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            failureCount.incrementAndGet();
            failedRecords.add(record); 
            System.err.println("Parsing error: " + e.getMessage()); 
        }
    }

    private synchronized void validateAndSave(CustomerDto customerDto) {
        try {
            
            validateCustomerDto(customerDto);

            Customer customer = new Customer();
            customer.setCustomerId(customerDto.getCustomerId());
            customer.setCustomerName(customerDto.getCustomerName());
            customer.setAddress(customerDto.getAddress());
            customer.setEmail(customerDto.getEmail());
            customer.setMobileNumber(customerDto.getMobileNumber());
            customer.setWallet(0.0);

            Bill bill = new Bill();
            bill.setCustomer(customer);
            bill.setUnitConsumption(customerDto.getUnitConsumption());
            bill.setBillingStartDate(customerDto.getBillingStartDate());
            bill.setBillingEndDate(customerDto.getBillingEndDate());
            bill.setBillDueDate(customerDto.getBillDueDate());
            bill.setAmountDue(calculateAmountDue(customerDto.getUnitConsumption()));
            bill.setBillStatus("Pending");
            customerRepository.save(customer);
            billRepository.save(bill);
            successCount.incrementAndGet();
        }
        catch (Exception e) {
            failureCount.incrementAndGet();
            String failedRecord = String.join(",",
                    String.valueOf(customerDto.getCustomerId()),
                    customerDto.getCustomerName(),
                    String.valueOf(customerDto.getUnitConsumption()),
                    customerDto.getBillingStartDate().toString(),
                    customerDto.getBillingEndDate().toString(),
                    customerDto.getBillDueDate().toString(),
                    customerDto.getEmail(), customerDto.getMobileNumber(), customerDto.getAddress(),
                    "Reason : " + e.getMessage()
            );
            failedRecords.add(failedRecord);

            System.err.println("An unexpected error occurred: " + e.getMessage()); // Log the error for debugging
        }
    }

    private void validateCustomerDto(CustomerDto customerDto) throws IllegalArgumentException {
       
        if (!isValidEmail(customerDto.getEmail())) {
            throw new IllegalArgumentException("Invalid email format: " + customerDto.getEmail());
        }
        if (!isValidName(customerDto.getCustomerName())) {
            throw new IllegalArgumentException("Invalid name format: " + customerDto.getCustomerName());
        }

        if (!isValidMobileNumber(customerDto.getMobileNumber())) {
            throw new IllegalArgumentException("Invalid mobile number: " + customerDto.getMobileNumber());
        }
        if (!isValidUnitConsumption(customerDto.getUnitConsumption())) {
            throw new IllegalArgumentException("Invalid unit consumption (only numbers are allowed): " + customerDto.getUnitConsumption());
        }

        if (!isValidCustomerId(customerDto.getCustomerId())) {
            throw new IllegalArgumentException("Invalid customer ID (only numbers are allowed): " + customerDto.getCustomerId());
        }
        
        if (isDuplicateBill(customerDto.getCustomerId(), customerDto.getBillingStartDate(), customerDto.getBillingEndDate())) {
            throw new IllegalArgumentException("Duplicate bill found for customer ID " + customerDto.getCustomerId() +
                    " and billing period " + customerDto.getBillingStartDate() + " to " + customerDto.getBillingEndDate());
        }
        
        
    }

    private double calculateAmountDue(int unitConsumption) {
        return unitConsumption * 41.5;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isDuplicateBill(Long customerId, LocalDate billingStartDate, LocalDate billingEndDate) {
        return billRepository.existsByCustomerCustomerIdAndBillingStartDateAndBillingEndDate(customerId, billingStartDate, billingEndDate);
    }
    
    private boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z ]+$");
    }
    
    private boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber != null && mobileNumber.matches("^[6-9]\\d{9}$");
    }
    private boolean isValidUnitConsumption(int unitConsumption) {
        return unitConsumption >= 0;  
    }

    private boolean isValidCustomerId(Long customerId) {
        return customerId >= 0; 
    }

    

}