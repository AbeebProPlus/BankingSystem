package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;
import com.system.moneybank.exceptions.CustomerNotFound;
import com.system.moneybank.exceptions.RestrictedAccountException;
import com.system.moneybank.models.AccountDetails;
import com.system.moneybank.models.Customer;
import com.system.moneybank.models.Transaction;
import com.system.moneybank.repository.UserRepo;
import com.system.moneybank.service.emailService.EmailDetails;
import com.system.moneybank.service.emailService.EmailSenderService;
import com.system.moneybank.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.system.moneybank.models.AccountStatus.ACTIVE;
import static com.system.moneybank.models.AccountStatus.RESTRICTED;
import static com.system.moneybank.models.TransactionStatus.SUCCESS;
import static com.system.moneybank.models.TransactionType.CREDIT;
import static com.system.moneybank.models.TransactionType.DEBIT;
import static com.system.moneybank.utils.AccountUtils.*;
import static java.math.BigDecimal.ZERO;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{

    private final UserRepo userRepo;
    private final TransactionService transactionService;
    private final EmailSenderService emailSenderService;


    @Override
    public Response checkAccountBalance(EnquiryRequest enquiryRequest) {
        if (!isValidAccountNumber(enquiryRequest)) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ACCOUNT_NOT_FOUND_MESSAGE).build();
        Customer user = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return Response.builder().code(ACCOUNT_FOUND_CODE).message(ACCOUNT_FOUND_MESSAGE).accountDetails(getAccountDetails(user)).build();
    }


    @Override
    public Response transfer(TransferRequest request) {
        boolean isValidDestination = userRepo.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isValidDestination) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(INVALID_DESTINATION_MESSAGE).build();
        Customer sourceAccount = userRepo.findByAccountNumber(request.getSourceAccountNumber());
        if (sourceAccount.getAccountStatus().equals(RESTRICTED))
            throw new RestrictedAccountException("SOURCE ACCOUNT WAS RESTRICTED");
        if (request.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0)return Response.builder().code(ACCOUNT_DEBIT_DECLINED_CODE).message(ACCOUNT_DEBIT_DECLINED_MESSAGE).build();
        BigDecimal debitedAmount = request.getAmount();
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(debitedAmount));
        Customer destinationAccount = userRepo.findByAccountNumber(request.getDestinationAccountNumber());
        if (destinationAccount.getAccountStatus().equals(RESTRICTED))
            throw new RestrictedAccountException("DESTINATION ACCOUNT WAS RESTRICTED");
        BigDecimal updatedDestinationBalance = destinationAccount.getAccountBalance().add(debitedAmount);
        destinationAccount.setAccountBalance(updatedDestinationBalance);

        userRepo.save(destinationAccount);
        userRepo.save(sourceAccount);
        Transaction sourceTransaction = Transaction.builder().type(DEBIT).amount(request.getAmount()).accountNumber(destinationAccount.getAccountNumber())
                .customer(sourceAccount).status(SUCCESS).date(LocalDate.now()).time(LocalTime.now())
                .processedBy("SELF").build();
        transactionService.save(sourceTransaction);
        sourceAccount.getTransactionList().add(sourceTransaction);
        String debitMessage = "\nDear "+ sourceAccount.getFirstName() + "\nA debit transaction occurred on your account." +
                "\"Amount: " + request.getAmount() + "\nCurrent balance: "  + sourceAccount.getAccountBalance() +
                "\nDestination: " + destinationAccount.getFirstName() + " " + destinationAccount.getLastName() + "\nDate: " + LocalDate.now() +  "\nTime: " + LocalTime.now();
        Transaction destinationTransaction = Transaction.builder().type(CREDIT).amount(request.getAmount()).accountNumber(sourceAccount.getAccountNumber())
                .customer(destinationAccount).status(SUCCESS).date(LocalDate.now()).time(LocalTime.now())
                .processedBy(sourceAccount.getFirstName() + " " + sourceAccount.getLastName()).build();
        transactionService.save(destinationTransaction);
        destinationAccount.getTransactionList().add(destinationTransaction);
        EmailDetails debitDetails = mailMessage(sourceAccount, "Debit transaction notification", sourceAccount.getEmail(), debitMessage);
        emailSenderService.sendMail(debitDetails);
        String creditMessage = "\nDear "+ destinationAccount.getFirstName() + "\nA credit transaction occurred on your account." +
                "\"Amount: " + request.getAmount() + "\nCurrent balance: "  + destinationAccount.getAccountBalance() +
                "\nFrom: " + sourceAccount.getFirstName() + " " + sourceAccount.getLastName() + "\nDate: " + LocalDate.now() + "\nTime: " + LocalTime.now();

        EmailDetails creditDetails = mailMessage(sourceAccount, "Credit transaction notification", destinationAccount.getEmail(), creditMessage);
        emailSenderService.sendMail(creditDetails);
        return Response.builder().code(TRANSFER_SUCCESS_CODE).message(TRANSFER_SUCCESS_MESSAGE).build();
    }

    @Override
    public TransactionHistoryResponse getAllTransactionsDoneByCustomer(TransactionHistoryRequest request) {
        try {
            Customer customer = userRepo.findByAccountNumber(request.getAccountNumber());
            if (customer == null) throw new CustomerNotFound(ACCOUNT_NOT_FOUND_MESSAGE);
            return TransactionHistoryResponse.builder().code(ACCOUNT_FOUND_CODE).message(ACCOUNT_FOUND_MESSAGE).transactionList(customer.getTransactionList()).build();
        }catch (Exception ex){
            return TransactionHistoryResponse.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ex.getMessage()).transactionList(null).build();
        }
    }

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

    private EmailDetails mailMessage(Customer savedUser, String subject, String email, String message) {
        return EmailDetails.builder()
                .subject(subject)
                .recipientMailAddress(email)
                .message(message)
                .build();
    }

    private boolean isValidAccountNumber(EnquiryRequest enquiryRequest) {
        return userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
    }


    private AccountDetails getAccountDetails(Customer savedUser) {
        return AccountDetails.builder().accountName(savedUser.getFirstName() + " " +
                        savedUser.getLastName() + " " +
                        savedUser.getMiddleName())
                .accountBalance(savedUser.getAccountBalance())
                .accountNumber(savedUser.getAccountNumber())
                .build();
    }

}