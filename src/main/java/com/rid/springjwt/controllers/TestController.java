package com.rid.springjwt.controllers;

import com.rid.springjwt.models.ReportFilter;
import com.rid.springjwt.models.Transaction;
import com.rid.springjwt.models.DTO.TransactionDTO;
import com.rid.springjwt.service.TransactionService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.io.FileNotFoundException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

  @Autowired
  TransactionService transactionService;

  @GetMapping("/all")
  public String allAccess(Authentication authentication) {
    return transactionService.totalPoin(authentication.getName());
  }

  @GetMapping("/history")
  @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
  public List<Transaction> gethistory(@RequestBody Transaction transaction, Authentication authentication) {
   return transactionService.history(authentication.getName());
  }

  @PostMapping("/buy")
  @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
  public String buypulsa( @RequestBody Transaction transaction,Authentication authentication) {
    transactionService.BeliPulsa(transaction,authentication.getName());
    return "Succes";
  }

  @PostMapping("/report")
  @PreAuthorize("hasRole('ADMIN')")
  public String  adminAccess(@RequestBody ReportFilter reportFilter) throws FileNotFoundException, JRException {
    return transactionService.historyReport(reportFilter);
  }

  @GetMapping("/admin2")
  @PreAuthorize("hasRole('ADMIN')")
  public String  adminAccessa() {
    return "this data";
  }
}
