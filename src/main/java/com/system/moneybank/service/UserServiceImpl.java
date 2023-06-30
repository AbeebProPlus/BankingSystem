package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.models.AccountDetails;
import com.system.moneybank.models.TransactionType;
import com.system.moneybank.models.User;
import com.system.moneybank.repository.UserRepo;
import com.system.moneybank.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.system.moneybank.models.AccountStatus.ACTIVE;
import static com.system.moneybank.models.TransactionType.CREDIT;
import static com.system.moneybank.models.TransactionType.DEBIT;
import static com.system.moneybank.utils.AccountUtils.*;
import static java.math.BigDecimal.ZERO;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{

    private final UserRepo userRepo;
    private final TransactionService transactionService;
    @Override
    public Response createBankAccount(CreateAccountRequest request) {
        if (accountExists(request)) return Response.builder().code(ACCOUNT_EXISTS_CODE).message(ACCOUNT_EXISTS_MESSAGE).build();
        User user = createUser(request);
        User savedUser = userRepo.save(user);
        return transactionResponse(savedUser, ACCOUNT_CREATION_CODE, ACCOUNT_CREATION_MESSAGE);
    }

    @Override
    public Response checkAccountBalance(EnquiryRequest enquiryRequest) {
        if (!isValidAccountNumber(enquiryRequest)) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ACCOUNT_NOT_FOUND_MESSAGE).build();
        User user = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return Response.builder().code(ACCOUNT_FOUND_CODE).message(ACCOUNT_FOUND_MESSAGE).accountDetails(getAccountDetails(user)).build();
    }

    @Override
    public String checkAccountName(EnquiryRequest enquiryRequest) {
        boolean accountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!accountExists) return ACCOUNT_NOT_FOUND_MESSAGE;
        User user = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return user.getFirstName() + " " + user.getLastName() + " " + user.getMiddleName();
    }

    @Override
    public Response creditAccount(CreditDebitRequest request) {
        boolean accountExists = userRepo.existsByAccountNumber(request.getAccountNumber());
        if (!accountExists) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ACCOUNT_NOT_FOUND_MESSAGE).build();
        if (isInValidAmount(request)) return Response.builder().code(ACCOUNT_CREDIT_DECLINED_CODE).message(ACCOUNT_CREDIT_DECLINED_MESSAGE).build();
        User creditedUser = creditUser(request);
        TransactionRequest transactionRequest = buildTransaction(request, creditedUser, CREDIT);
        transactionService.save(transactionRequest);
        return transactionResponse(creditedUser, ACCOUNT_CREDITED_CODE, ACCOUNT_CREDITED_MESSAGE);
    }

    private TransactionRequest buildTransaction(CreditDebitRequest request, User creditedUser, TransactionType type) {
        return TransactionRequest.builder()
                .accountNumber(creditedUser.getAccountNumber())
                .type(type)
                .amount(request.getAmount())
                .build();
    }

    @Override
    public Response debitAccount(CreditDebitRequest request) {
        boolean accountExists = userRepo.existsByAccountNumber(request.getAccountNumber());
        if (!accountExists) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ACCOUNT_NOT_FOUND_MESSAGE).build();
        User user = userRepo.findByAccountNumber(request.getAccountNumber());
        BigDecimal amountInUserAccount = user.getAccountBalance();
        if (isInValidAmount(request)) return Response.builder().code(ACCOUNT_DEBIT_DECLINED_CODE).message(ACCOUNT_DEBIT_DECLINED_MESSAGE).build();
        if (request.getAmount().compareTo(amountInUserAccount) > 0) return Response.builder().code(ACCOUNT_DEBIT_DECLINED_CODE).message(ACCOUNT_DEBIT_DECLINED_MESSAGE).build();
        User debitedUser = debitUser(request);
        TransactionRequest transactionRequest = buildTransaction(request, debitedUser, DEBIT);
        transactionService.save(transactionRequest);
        return transactionResponse(debitedUser, ACCOUNT_DEBIT_CODE, ACCOUNT_DEBIT_MESSAGE);
    }

    @Override
    public Response transfer(TransferRequest request) {
        boolean isValidDestination = userRepo.existsByAccountNumber(request.getDestination());
        if (!isValidDestination) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(INVALID_DESTINATION_MESSAGE).build();
        User sourceAccount = userRepo.findByAccountNumber(request.getSource());
        if (request.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0)return Response.builder().code(ACCOUNT_DEBIT_DECLINED_CODE).message(ACCOUNT_DEBIT_DECLINED_MESSAGE).build();
        BigDecimal debitedAmount = request.getAmount();
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(debitedAmount));
        User destinationAccount = userRepo.findByAccountNumber(request.getDestination());
        BigDecimal updatedDestinationBalance = destinationAccount.getAccountBalance().add(debitedAmount);
        destinationAccount.setAccountBalance(updatedDestinationBalance);

        userRepo.save(destinationAccount);
        userRepo.save(sourceAccount);
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(destinationAccount.getAccountNumber())
                .type(CREDIT)
                .amount(request.getAmount())
                .build();
        transactionService.save(transactionRequest);
//        EmailDetails debitMessage = EmailDetails.builder().subject("DEBIT ALERT").recipient(sourceAccount.getEmail())
//                .message("Amount: " + request.getAmount() +
//                 "\nCurrent balance: "  + sourceAccount.getAccountBalance() +
//                 "\nDestination: " + destinationAccount.getFirstName() + " " + destinationAccount.getLastName() +
//                 "\nDate: " + LocalDateTime.now())
//                .build();
//       String sourceAccountName = sourceAccount.getFirstName() + " " + sourceAccount.getLastName() + " " + sourceAccount.getMiddleName();
//        EmailDetails creditMessage = EmailDetails.builder().subject("CREDIT ALERT").recipient(destinationAccount.getEmail())
//                .message("Amount: " + request.getAmount() +
//                        "\nCurrent balance: "  + destinationAccount.getAccountBalance() +
//                        "\nFrom: " + sourceAccount.getFirstName() + " " + sourceAccount.getLastName() +
//                        "\nDate: " + LocalDateTime.now())
//                .build();
        return Response.builder().code(TRANSFER_SUCCESS_CODE).message(TRANSFER_SUCCESS_MESSAGE).build();
    }

    private User creditUser(CreditDebitRequest request) {
        User userToBeCredited = userRepo.findByAccountNumber(request.getAccountNumber());
        BigDecimal userToBeCreditedBalance = userToBeCredited.getAccountBalance();
        userToBeCredited.setAccountBalance(userToBeCreditedBalance.add(request.getAmount()));
        return userRepo.save(userToBeCredited);
    }
    private User debitUser(CreditDebitRequest request){
        User userToBeDebited = userRepo.findByAccountNumber(request.getAccountNumber());
        BigDecimal userToBeDebitedBalance = userToBeDebited.getAccountBalance();
        userToBeDebited.setAccountBalance(userToBeDebitedBalance.subtract(request.getAmount()));
        return userRepo.save(userToBeDebited);
    }


    private boolean isInValidAmount(CreditDebitRequest request) {
        return request.getAmount().compareTo(ZERO) <= 0;
    }

    private boolean isValidAccountNumber(EnquiryRequest enquiryRequest) {
        return userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
    }


    private User createUser(CreateAccountRequest request) {
        return User.builder().firstName(request.getFirstName()).lastName(request.getLastName()).middleName(request.getMiddleName())
                .email(request.getEmail()).phoneNumber(request.getPhoneNumber()).secondPhoneNumber(request.getSecondPhoneNumber())
                .address(request.getAddress()).gender(request.getGender())
                .accountNumber(AccountUtils.generateAccountNumber()).accountBalance(ZERO).accountStatus(ACTIVE)
                .build();
    }

    private Response transactionResponse(User savedUser, String code, String message) {
        return Response.builder().code(code).message(message).accountDetails(getAccountDetails(savedUser)).build();
    }

    private AccountDetails getAccountDetails(User savedUser) {
        return AccountDetails.builder().accountName(savedUser.getFirstName() + " " +
                        savedUser.getLastName() + " " +
                        savedUser.getMiddleName())
                .accountBalance(savedUser.getAccountBalance())
                .accountNumber(savedUser.getAccountNumber())
                .build();
    }

    private boolean accountExists(CreateAccountRequest request) {
        return userRepo.existsByEmail(request.getEmail());
    }
}