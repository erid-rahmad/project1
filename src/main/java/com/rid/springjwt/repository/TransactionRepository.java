package com.rid.springjwt.repository;


import com.rid.springjwt.models.Transaction;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    @Query("select a from Transaction a where a.User.id =?1")
    List<Transaction> findbyid(Long id);

    @Query("FROM Transaction t where t.User =:user")
    List<Transaction> findbyid1(
            @Param("user") Long user

    );







}
