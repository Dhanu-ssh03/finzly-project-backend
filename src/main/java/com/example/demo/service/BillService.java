package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.Repo.BillRepo;
import com.example.demo.Repo.CustomerRepo;
import com.example.demo.Repo.PaymentRepo;
import com.example.demo.dto.CustomerDto;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Customer;
import com.example.demo.entity.PaymentHistory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.mail.MessagingException;

@Service
public class BillService {

	@Autowired
	BillRepo billrepo;
	
	@Autowired
	PaymentRepo paymentrepo;
	
	@Autowired
	CustomerRepo customerrepo;
	
	@Autowired
	CustomerEmailService emailService;
	  
	@Autowired
	private JavaMailSender mailSender;
	
	public List<Bill> getAllPaymentData() {
		 return billrepo.findBybillStatus("pending");
	}

	 public ByteArrayInputStream generateInvoice(int billId) throws DocumentException{
		      
		    Document document = new Document();
		    ByteArrayOutputStream out = new ByteArrayOutputStream();

		    PdfWriter.getInstance(document, out);
		    document.open();

		    PaymentHistory paymentHistory = paymentrepo.findByBillBillId(billId);
		    Optional<Bill> bill = billrepo.findById(billId);
		    Bill bill1 = bill.get();
		    Customer customer = bill1.getCustomer();

		 
		    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
		    Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
		    
		    Font comapanyName = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
		    Paragraph title1=new Paragraph("Bharat Bijili Corporation ",comapanyName);
		    title1.setAlignment(Element.ALIGN_CENTER);
		    document.add(title1);
		    document.add(new Paragraph(" "));
		  
		    Paragraph title = new Paragraph("Invoice Details", headerFont);
		    title.setAlignment(Element.ALIGN_CENTER);
		    document.add(title);
		    document.add(new Paragraph(" ")); 

		   
		    PdfPTable table = new PdfPTable(2); 
		    table.setWidthPercentage(100); 
		    table.setSpacingBefore(10f); 
		    table.setSpacingAfter(10f); 

		   
		    float[] columnWidths = {1f, 2f};
		    table.setWidths(columnWidths);

		   
		    addTableHeader(table, "Attribute", "Details", headerFont);
		    addRow(table, "Customer Name:", customer.getCustomerName(), contentFont);
		
		    addRow(table, "Bill ID:", String.valueOf(bill1.getBillId()), contentFont);
		    addRow(table, "Payment ID:", String.valueOf(paymentHistory.getPaymentId()), contentFont);
		    addRow(table, "Unit Consumed:", String.valueOf(bill1.getUnitConsumption()), contentFont);
		    addRow(table, "Rate Per Unit:", "Rs. 41.5", contentFont);
		    addRow(table, "Due Date:", String.valueOf(bill1.getBillDueDate()), contentFont);
		    addRow(table, "Payment Date:", String.valueOf(paymentHistory.getPaymentDate()), contentFont);
		    addRow(table, "Total Amount Due:", String.format("Rs. %.2f", paymentHistory.getAmount()), contentFont);
		    addRow(table, "Bonus Applied:", String.format("Rs. %.2f", paymentHistory.getDiscount()), contentFont);
		    addRow(table, "Net Amount:", String.format("Rs. %.2f", paymentHistory.getNetAmount()), contentFont);

		    document.add(table);

		    document.close();
		    return new ByteArrayInputStream(out.toByteArray());
		}

		private void addTableHeader(PdfPTable table, String header1, String header2, Font font) {
		    PdfPCell headerCell1 = new PdfPCell(new Phrase(header1, font));
		    headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    headerCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		    table.addCell(headerCell1);

		    PdfPCell headerCell2 = new PdfPCell(new Phrase(header2, font));
		    headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		    headerCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		    table.addCell(headerCell2);
		}

		
		private void addRow(PdfPTable table, String attribute, String value, Font font) {
		    PdfPCell attributeCell = new PdfPCell(new Phrase(attribute, font));
		    attributeCell.setPadding(5);
		    table.addCell(attributeCell);

		    PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
		    valueCell.setPadding(5);
		    table.addCell(valueCell);
		}
	    

	   public void updatePayStatus(int billId) {
		  Optional<Bill> billUpdate=billrepo.findById(billId);
		  if(billUpdate.isPresent()) {
			 Bill bill = billUpdate.get(); 
		     bill.setBillStatus("Paid");  
		     billrepo.save(bill);
		  }else {
		     throw new IllegalArgumentException("Bill not found for ID: " + billId);
		  }
	  }


	public void invoiceMail(int billId) {
		
        PaymentHistory paymentHistory=paymentrepo.findByBillBillId(billId);
        Optional<Bill> bill=billrepo.findById(billId);
        Bill bill1=bill.get();
        Customer customer=bill1.getCustomer();
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail()); 
        
        message.setText("Dear " + customer.getCustomerName() + ",\n\n" +
                       
                       "Customer Name "+customer.getCustomerName() + "\n\n" +
                       "Customer ID: " +customer.getCustomerId() + "\n\n" +
                       "Bill Id: "+bill1.getBillId() + "\n\n" +
                       "Rate Per Unit Rs41.5 "+ "\n\n" +
                       "Due Date: "+bill1.getBillDueDate() + "\n\n" +
                       "PaymentDate: "+paymentHistory.getPaymentDate() + "\n\n" +
                       "Toal Amount Due: "+paymentHistory.getAmount() + "\n\n" +
                       "Bonus Applied: "+paymentHistory.getDiscount()+"\n\n"+
                       "Net Amount: "+paymentHistory.getNetAmount()+"\n\n"+
                        
                       
                        "Thank you for using our service!"); 

       
        mailSender.send(message);
    

}
	
	

	
}
