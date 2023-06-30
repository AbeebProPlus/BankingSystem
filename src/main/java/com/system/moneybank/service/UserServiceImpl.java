package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.models.AccountDetails;
import com.system.moneybank.models.Customer;
import com.system.moneybank.models.Transaction;
import com.system.moneybank.models.TransactionType;
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
    public Response createBankAccount(CreateAccountRequest request) {
        if (accountExists(request)) return Response.builder().code(ACCOUNT_EXISTS_CODE).message(ACCOUNT_EXISTS_MESSAGE).build();
        Customer user = createUser(request);
        Customer savedUser = userRepo.save(user);
        String message = "Dear " + savedUser.getFirstName() + " you now have an account with Lemonade Bank." +
                "\nAccount name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getMiddleName() +
                "\nAccount number: " + savedUser.getAccountNumber() +
                "\nDate: " + LocalDate.now() +
                "\nTime: " + LocalTime.now() +
                "\nThank you for banking with us";
        EmailDetails details = mailMessage(savedUser, "Account creation notification", savedUser.getEmail(), message);
        emailSenderService.sendMail(details);
        return transactionResponse(savedUser, ACCOUNT_CREATION_CODE, ACCOUNT_CREATION_MESSAGE);
    }

    private EmailDetails mailMessage(Customer savedUser, String subject, String email, String message) {
        return EmailDetails.builder()
                .subject(subject)
                .recipientMailAddress(email)
                .message(message)
                .build();
    }

    @Override
    public Response checkAccountBalance(EnquiryRequest enquiryRequest) {
        if (!isValidAccountNumber(enquiryRequest)) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ACCOUNT_NOT_FOUND_MESSAGE).build();
        Customer user = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return Response.builder().code(ACCOUNT_FOUND_CODE).message(ACCOUNT_FOUND_MESSAGE).accountDetails(getAccountDetails(user)).build();
    }

    @Override
    public String checkAccountName(EnquiryRequest enquiryRequest) {
        boolean accountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!accountExists) return ACCOUNT_NOT_FOUND_MESSAGE;
        Customer user = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return user.getFirstName() + " " + user.getLastName() + " " + user.getMiddleName();
    }

    @Override
    public Response creditAccount(CreditDebitRequest request) {
        boolean accountExists = userRepo.existsByAccountNumber(request.getAccountNumber());
        if (!accountExists) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ACCOUNT_NOT_FOUND_MESSAGE).build();
        if (isInValidAmount(request)) return Response.builder().code(ACCOUNT_CREDIT_DECLINED_CODE).message(ACCOUNT_CREDIT_DECLINED_MESSAGE).build();
        Customer creditedUser = creditUser(request);
        TransactionRequest transactionRequest = buildTransaction(request, creditedUser, CREDIT);
       // transactionService.save(transactionRequest);
        String message = "Dear " + creditedUser.getFirstName() + "A credit transaction occurred on your account " +
                        "\nAmount: " + request.getAmount() +
                        "\nCurrent balance: "  + creditedUser.getAccountBalance() +
                        "\nFrom: " + request.getDepositorName() +
                        "\nDate: " + LocalDate.now() +
                        "\nTime: " + LocalTime.now();
        EmailDetails details = mailMessage(creditedUser, "Credit transaction notification", creditedUser.getEmail(), message);
        emailSenderService.sendMail(details);
        return transactionResponse(creditedUser, ACCOUNT_CREDITED_CODE, ACCOUNT_CREDITED_MESSAGE);
    }

    private TransactionRequest buildTransaction(CreditDebitRequest request, Customer creditedUser, TransactionType type) {
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
        Customer user = userRepo.findByAccountNumber(request.getAccountNumber());
        BigDecimal amountInUserAccount = user.getAccountBalance();
        if (isInValidAmount(request)) return Response.builder().code(ACCOUNT_DEBIT_DECLINED_CODE).message(ACCOUNT_DEBIT_DECLINED_MESSAGE).build();
        if (request.getAmount().compareTo(amountInUserAccount) > 0) return Response.builder().code(ACCOUNT_DEBIT_DECLINED_CODE).message(ACCOUNT_DEBIT_DECLINED_MESSAGE).build();
        Customer debitedUser = debitUser(request);
        TransactionRequest transactionRequest = buildTransaction(request, debitedUser, DEBIT);
       // transactionService.save(transactionRequest);
        String message = "Dear " + debitedUser.getFirstName() + " A debit transaction occurred on your account." +
                "\nAmount: " + request.getAmount() +
                "\nCurrent balance: "  + debitedUser.getAccountBalance() +
                "\nDate: " + LocalDateTime.now() +
                "\nTime: " + LocalTime.now();
        EmailDetails details = mailMessage(debitedUser, "Debit transaction notification", debitedUser.getEmail(), message);
        emailSenderService.sendMail(details);
        return transactionResponse(debitedUser, ACCOUNT_DEBIT_CODE, ACCOUNT_DEBIT_MESSAGE);
    }

    @Override
    public Response transfer(TransferRequest request) {
        boolean isValidDestination = userRepo.existsByAccountNumber(request.getDestination());
        if (!isValidDestination) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(INVALID_DESTINATION_MESSAGE).build();
        Customer sourceAccount = userRepo.findByAccountNumber(request.getSource());
        if (request.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0)return Response.builder().code(ACCOUNT_DEBIT_DECLINED_CODE).message(ACCOUNT_DEBIT_DECLINED_MESSAGE).build();
        BigDecimal debitedAmount = request.getAmount();
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(debitedAmount));
        Customer destinationAccount = userRepo.findByAccountNumber(request.getDestination());
        BigDecimal updatedDestinationBalance = destinationAccount.getAccountBalance().add(debitedAmount);
        destinationAccount.setAccountBalance(updatedDestinationBalance);

        userRepo.save(destinationAccount);
        userRepo.save(sourceAccount);
        Transaction sourceTransaction = Transaction.builder()
                .type(DEBIT).amount(request.getAmount()).accountNumber(destinationAccount.getAccountNumber())
                .customer(sourceAccount).status(SUCCESS).date(LocalDate.now()).time(LocalTime.now()).build();
        transactionService.save(sourceTransaction);
        sourceAccount.getTransactionList().add(sourceTransaction);
        System.out.println(sourceAccount.getTransactionList());
        String debitMessage = "Dear "+ sourceAccount.getFirstName() + " A debit transaction occurred on your account." +
                "\"Amount: " + request.getAmount() + "\nCurrent balance: "  + sourceAccount.getAccountBalance() +
                "\nDestination: " + destinationAccount.getFirstName() + " " + destinationAccount.getLastName() + "\nDate: " + LocalDate.now() +  "\nTime: " + LocalTime.now();
        Transaction destinationTransaction = Transaction.builder()
                .type(CREDIT).amount(request.getAmount()).accountNumber(sourceAccount.getAccountNumber())
                .customer(destinationAccount).status(SUCCESS).date(LocalDate.now()).time(LocalTime.now()).build();
        transactionService.save(destinationTransaction);
        destinationAccount.getTransactionList().add(destinationTransaction);
        System.out.println(destinationAccount.getTransactionList());
        EmailDetails debitDetails = mailMessage(sourceAccount, "Debit transaction notification", sourceAccount.getEmail(), debitMessage);
        emailSenderService.sendMail(debitDetails);
        String creditMessage = "Dear "+ destinationAccount.getFirstName() + " A credit transaction occurred on your account." +
                "\"Amount: " + request.getAmount() + "\nCurrent balance: "  + destinationAccount.getAccountBalance() +
                "\nFrom: " + sourceAccount.getFirstName() + " " + sourceAccount.getLastName() + "\nDate: " + LocalDate.now() + "\nTime: " + LocalTime.now();

        EmailDetails creditDetails = mailMessage(sourceAccount, "Credit transaction notification", destinationAccount.getEmail(), creditMessage);
        emailSenderService.sendMail(creditDetails);
        return Response.builder().code(TRANSFER_SUCCESS_CODE).message(TRANSFER_SUCCESS_MESSAGE).build();
    }

    private Customer creditUser(CreditDebitRequest request) {
        Customer userToBeCredited = userRepo.findByAccountNumber(request.getAccountNumber());
        BigDecimal userToBeCreditedBalance = userToBeCredited.getAccountBalance();
        userToBeCredited.setAccountBalance(userToBeCreditedBalance.add(request.getAmount()));
        return userRepo.save(userToBeCredited);
    }
    private Customer debitUser(CreditDebitRequest request){
        Customer userToBeDebited = userRepo.findByAccountNumber(request.getAccountNumber());
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


    private Customer createUser(CreateAccountRequest request) {
        return Customer.builder().firstName(request.getFirstName()).lastName(request.getLastName()).middleName(request.getMiddleName())
                .email(request.getEmail()).phoneNumber(request.getPhoneNumber()).secondPhoneNumber(request.getSecondPhoneNumber())
                .address(request.getAddress()).gender(request.getGender())
                .accountNumber(AccountUtils.generateAccountNumber()).accountBalance(ZERO).accountStatus(ACTIVE)
                .build();
    }

    private Response transactionResponse(Customer savedUser, String code, String message) {
        return Response.builder().code(code).message(message).accountDetails(getAccountDetails(savedUser)).build();
    }

    private AccountDetails getAccountDetails(Customer savedUser) {
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