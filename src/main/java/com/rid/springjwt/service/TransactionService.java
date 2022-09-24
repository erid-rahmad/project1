package com.rid.springjwt.service;

import com.rid.springjwt.models.ReportFilter;
import com.rid.springjwt.models.Transaction;
import com.rid.springjwt.models.DTO.TransactionDTO;
import com.rid.springjwt.models.User;
import com.rid.springjwt.repository.TransactionRepository;
import com.rid.springjwt.repository.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.exolab.castor.types.DateTime;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional

public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    public TransactionService(  ModelMapper modelMapper, EntityManager entityManager) {

        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    public Transaction BeliPulsa(Transaction transaction_, String name){
        Optional<User> user = userRepository.findByUsername(name);
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.now());
        transaction.setNominal(transaction_.getNominal());
        transaction.setUser(user.get());
        if (transaction_.getName().equals("PULSA")){
            transaction.setName("PULSA");
            if (transaction_.getNominal().intValue()>50000 && transaction_.getNominal().intValue()<=100000){
                transaction.setPoin(new BigDecimal(transaction_.getNominal().intValue()/2000*1));
            }
            else if (transaction_.getNominal().intValue()>100000 ){
                transaction.setPoin(new BigDecimal(transaction_.getNominal().intValue()/2000*2));
            }

        }else {
            transaction.setName("LISTIK");
            if ( transaction_.getNominal().intValue()>10000 && transaction_.getNominal().intValue()<=30000){
                transaction.setPoin(new BigDecimal(transaction_.getNominal().intValue()/1000*1));
            }
            else if (transaction_.getNominal().intValue()>30000 ){
                transaction.setPoin(new BigDecimal(transaction_.getNominal().intValue()/1000*2));
            }
        }
        return transactionRepository.save(transaction);
    }

    public List<Transaction> history( String name){
        Optional<User> user = userRepository.findByUsername(name);
        return transactionRepository.findbyid(user.get().getId());
    }

    public String historyReport(ReportFilter reportFilter) throws JRException, FileNotFoundException {
        StringBuilder sql = new StringBuilder().append("SELECT * from Transaction t where t.id > 0");

        if (reportFilter.getUserId() != null){
            sql.append(" and t.user_id="+reportFilter.getUserId());
        }

        if (reportFilter.getStarDate() != null){
            java.sql.Date date = new java.sql.Date(reportFilter.getStarDate().getTime());
            sql.append(" and t.date > '"+date.toString()+"'");
        }

        if (reportFilter.getEndDate() != null){
            java.sql.Date date = new java.sql.Date(reportFilter.getEndDate().getTime());
            sql.append(" and t.date < '"+date.toString()+"'");
        }
        Query query = entityManager.createNativeQuery(sql.toString(),Transaction.class);
        List<Transaction> customerList = query.getResultList();
        List<TransactionDTO> transactionDTOS =  customerList
                .stream()
                .map(transaction ->
                      mapingDTO(transaction)
                 )
                .collect(Collectors.toList());
        String path = "D:\\reserch\\spring-boot-spring-security-jwt-authentication-master\\report";
        File file = ResourceUtils.getFile("classpath:bank.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transactionDTOS);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        Random rand = new Random();
        int upperbound = 25342341;
        int int_random = rand.nextInt(upperbound);
        String name = path+"\\report"+int_random+".pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, name);

        return name;
    }

    public String totalPoin(String name){
        Optional<User> user = userRepository.findByUsername(name);
        BigDecimal bigDecimal =transactionRepository.curentPoin(user.get().getId());
        return bigDecimal.toString();
    }

    public TransactionDTO mapingDTO(Transaction transaction){
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDate(Date.valueOf(transaction.getDate()));
        transactionDTO.setNominal(transaction.getNominal().intValue());
        transactionDTO.setPoin(transaction.getPoin().intValue());
        transactionDTO.setUsername(transaction.getUser().getUsername());
        return transactionDTO;
    }

}
