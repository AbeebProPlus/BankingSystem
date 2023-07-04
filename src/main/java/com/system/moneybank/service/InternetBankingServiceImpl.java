package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.CardResponse;
import com.system.moneybank.dtos.response.InternetBankingRegistrationResponse;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;
import com.system.moneybank.exceptions.CardException;
import com.system.moneybank.exceptions.CustomerNotFound;
import com.system.moneybank.exceptions.RestrictedAccountException;
import com.system.moneybank.models.*;
import com.system.moneybank.repository.InternetBankingCustomersRepo;
import com.system.moneybank.service.emailService.EmailDetails;
import com.system.moneybank.service.emailService.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import static com.system.moneybank.models.AccountStatus.RESTRICTED;
import static com.system.moneybank.models.CardStatus.DEACTIVATED;
import static com.system.moneybank.models.CardStatus.EXPIRED;
import static com.system.moneybank.models.TransactionStatus.SUCCESS;
import static com.system.moneybank.models.TransactionType.CREDIT;
import static com.system.moneybank.models.TransactionType.DEBIT;
import static com.system.moneybank.utils.AccountUtils.*;
import static com.system.moneybank.utils.AccountUtils.CARD_DEACTIVATION_FAILED;

@Service
@RequiredArgsConstructor
public class InternetBankingServiceImpl implements InternetBankingService{

    private final InternetBankingCustomersRepo internetBankingCustomersRepo;
    private final TransactionService transactionService;

    private final EmailSenderService emailSenderService;
    private final CardService cardService;
    private final UserService userService;
    @Override
    public InternetBankingRegistrationResponse signUp(RegisterForInternetBanking request) {
        try {
            InternetBankingCustomer internetBankingCustomer = internetBankingCustomersRepo.findByAccountNumber(request.getAccountNumber());
            if (internetBankingCustomer != null) throw new RuntimeException("Have you signed up recently?");
            Customer customer = userService.findByAccountNumber(request.getAccountNumber());
            if (customer == null) throw new RuntimeException("Provide correct acc details");
            Card card = cardService.findCardByCustomerId(customer.getId());
            checkIfCardIsNull(card);
            checkCardStatus(card);
            if (!card.getPin().equals(request.getCardPin())) throw new CardException("Provide correct card details");
            InternetBankingCustomer iCustomer = getiCustomer(request, customer);
            InternetBankingCustomer iCustomer2 = internetBankingCustomersRepo.save(iCustomer);
            return InternetBankingRegistrationResponse.builder().id(iCustomer2.getId()).message("Sign up successful").build();
        }catch (Exception ex){
            return InternetBankingRegistrationResponse.builder().message(ex.getMessage()).build();
        }
    }
    @Override
    public CardResponse deActivateCard(DeactivateCard request) {
        try {
            Customer customer = userService.findByAccountNumber(request.getAccountNumber());
            if (customer == null) throw new CustomerNotFound(ACCOUNT_NOT_FOUND_MESSAGE);
            Card card = cardService.findCardByNumber(request.getCardNumber());
            if (card == null) throw new RuntimeException("Card doesn't exists");
            card.setStatus(CardStatus.DEACTIVATED);
            cardService.saveCard(card);
            return CardResponse.builder().code(CARD_DEACTIVATION_SUCCESSFUL).message(CARD_DEACTIVATION_SUCCESS_MESSAGE).build();
        }catch (Exception ex){
            return CardResponse.builder().code(CARD_DEACTIVATION_FAILED).message(ex.getMessage()).build();
        }
    }

    @Override
    public Response transfer(TransferRequest request) {
        boolean isValidDestination = userService.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isValidDestination) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(INVALID_DESTINATION_MESSAGE).build();
        Customer sourceAccount = userService.findByAccountNumber(request.getSourceAccountNumber());
        if (sourceAccount.getAccountStatus().equals(RESTRICTED))
            throw new RestrictedAccountException("SOURCE ACCOUNT WAS RESTRICTED");
        if (request.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0)return Response.builder().code(ACCOUNT_DEBIT_DECLINED_CODE).message(ACCOUNT_DEBIT_DECLINED_MESSAGE).build();
        BigDecimal debitedAmount = request.getAmount();
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(debitedAmount));
        Customer destinationAccount = userService.findByAccountNumber(request.getDestinationAccountNumber());
        if (destinationAccount.getAccountStatus().equals(RESTRICTED))
            throw new RestrictedAccountException("DESTINATION ACCOUNT WAS RESTRICTED");
        BigDecimal updatedDestinationBalance = destinationAccount.getAccountBalance().add(debitedAmount);
        destinationAccount.setAccountBalance(updatedDestinationBalance);

        userService.save(destinationAccount);
        userService.save(sourceAccount);
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
    public CardResponse changeCardPin(ChangeCardPinRequest request) {
        try {
            Card card = cardService.findCardByAccountNumber(request.getAccountNumber());
            if (card == null) throw new CardException("Invalid details");
            Card foundCard = cardService.findCardByNumber(request.getCardNumber());
            checkIfCardIsNull(foundCard);
            checkCardStatus(foundCard);
            if (!Objects.equals(foundCard.getPin(), request.getOldPin())) throw new CardException("Invalid details");
            foundCard.setPin(request.getNewPin());
            cardService.saveCard(foundCard);
            return CardResponse.builder().code(CARD_PIN_CHANGED_CODE).message("Card pin changed successfully").build();
        }catch (Exception ex){
            return CardResponse.builder().code(CARD_PIN_CHANGED_FAILURE).message(ex.getMessage()).build();
        }
    }

    @Override
    public TransactionHistoryResponse getAllTransactionsDoneByCustomer(TransactionHistoryRequest request) {
        try {
            Customer customer = userService.findByAccountNumber(request.getAccountNumber());
            if (customer == null) throw new CustomerNotFound(ACCOUNT_NOT_FOUND_MESSAGE);
            return TransactionHistoryResponse.builder().code(ACCOUNT_FOUND_CODE).message(ACCOUNT_FOUND_MESSAGE).transactionList(customer.getTransactionList()).build();
        }catch (Exception ex){
            return TransactionHistoryResponse.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ex.getMessage()).transactionList(null).build();
        }
    }
    @Override
    public Response checkAccountBalance(EnquiryRequest enquiryRequest) {
        if (!isValidAccountNumber(enquiryRequest)) return Response.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ACCOUNT_NOT_FOUND_MESSAGE).build();
        Customer user = userService.findByAccountNumber(enquiryRequest.getAccountNumber());
        return Response.builder().code(ACCOUNT_FOUND_CODE).message(ACCOUNT_FOUND_MESSAGE).accountDetails(getAccountDetails(user)).build();
    }
    private boolean isValidAccountNumber(EnquiryRequest enquiryRequest) {
        return userService.existsByAccountNumber(enquiryRequest.getAccountNumber());
    }


    private AccountDetails getAccountDetails(Customer savedUser) {
        return AccountDetails.builder().accountName(savedUser.getFirstName() + " " +
                        savedUser.getLastName() + " " +
                        savedUser.getMiddleName())
                .accountBalance(savedUser.getAccountBalance())
                .accountNumber(savedUser.getAccountNumber())
                .build();
    }


    private EmailDetails mailMessage(Customer savedUser, String subject, String email, String message) {
        return EmailDetails.builder()
                .subject(subject)
                .recipientMailAddress(email)
                .message(message)
                .build();
    }
    private void checkCardStatus(Card card) {
        if (card.getStatus() == EXPIRED || card.getStatus() == DEACTIVATED) throw new CardException("Invalid card");
    }

    private void checkIfCardIsNull(Card card) {
        if (card == null) throw new CardException("Provide correct card details");
    }

    private InternetBankingCustomer getiCustomer(RegisterForInternetBanking request, Customer customer) {
        return InternetBankingCustomer.builder()
                .accountNumber(customer.getAccountNumber()).password(request.getPassword())
                .userName(request.getUserName()).transactionPin(request.getPreferredTransactionPin())
                .role(Role.CUSTOMER)
                .build();
    }

    private String hashDetails(String detail){
        return BCrypt.hashpw(detail, BCrypt.gensalt());
    }
    private boolean confirmPassword(String candidate, String detail){
        return BCrypt.checkpw(candidate, detail);
    }
}