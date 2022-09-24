package com.rid.springjwt;

import com.rid.springjwt.models.ReportFilter;
import com.rid.springjwt.models.Transaction;
import com.rid.springjwt.models.User;
import com.rid.springjwt.repository.UserRepository;
import com.rid.springjwt.security.services.TransactionService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {

	@Autowired
	TransactionService transactionService;

	@Autowired
	UserRepository userRepository;

//	@BeforeEach



	@Test
	public void transaksi() {

		Transaction transaction = new Transaction();
		Optional<User> user = userRepository.findByUsername("rid");

		transaction.setUser(user.get());
		transaction.setNominal(new BigDecimal(30000));
		transaction.setName("PULSA");

		transactionService.BeliPulsa(transaction,user.get().getUsername());


		transaction.setName("LISTRIK TES");
		transactionService.BeliPulsa(transaction,user.get().getUsername());


	}

	@Test
	public void history() {
		transactionService.history("rid");
	}

	@Test
	public void report() {
		Optional<User> user = userRepository.findByUsername("rid");
		ReportFilter reportFilter = new ReportFilter();
		transactionService.historyReport(reportFilter);
		reportFilter.setUserId(user.get().getId().intValue());
		transactionService.historyReport(reportFilter);
	}

}
