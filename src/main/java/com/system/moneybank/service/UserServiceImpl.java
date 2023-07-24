package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;
import com.system.moneybank.exceptions.CustomerNotFound;
import com.system.moneybank.exceptions.RestrictedAccountException;
import com.system.moneybank.models.AccountDetails;
import com.system.moneybank.models.Customer;
import com.system.moneybank.models.Transaction;
import com.system.moneybank.repository.CustomerRepo;
import com.system.moneybank.service.emailService.EmailDetails;
import com.system.moneybank.service.emailService.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.system.moneybank.models.AccountStatus.RESTRICTED;
import static com.system.moneybank.models.TransactionStatus.SUCCESS;
import static com.system.moneybank.models.TransactionType.CREDIT;
import static com.system.moneybank.models.TransactionType.DEBIT;
import static com.system.moneybank.utils.AccountUtils.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{

    private final CustomerRepo userRepo;


    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return userRepo.existsByAccountNumber(accountNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public Customer findByAccountNumber(String accountNumber) {
        return userRepo.findByAccountNumber(accountNumber);
    }

    @Override
    public Customer save(Customer customer) {
        return userRepo.save(customer);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepo.existsByPhoneNumber(phoneNumber);
    }

}