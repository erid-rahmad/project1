package com.rid.springjwt.security.services;

import com.rid.springjwt.mapper.TransactionMapper;
import com.rid.springjwt.models.ReportFilter;
import com.rid.springjwt.models.Transaction;
import com.rid.springjwt.models.TransactionDTO;
import com.rid.springjwt.models.User;
import com.rid.springjwt.repository.TransactionRepository;
import com.rid.springjwt.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional

public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;


    TransactionMapper transactionMapper;


    private final ModelMapper modelMapper;



    private final EntityManager entityManager;

    public TransactionService(ModelMapper modelMapper, EntityManager entityManager) {
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

    public List<TransactionDTO> historyReport(ReportFilter reportFilter){
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

        return  customerList
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());



    }

}
