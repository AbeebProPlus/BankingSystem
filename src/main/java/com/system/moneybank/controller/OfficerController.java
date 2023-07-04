package com.system.moneybank.controller;


import com.system.moneybank.dtos.request.*;
import com.system.moneybank.service.OfficerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/officer/")
@Tag(name = "Officer Account API's")

public class OfficerController {
    private final OfficerService officerService;

    @Operation(
            summary = "Authenticates a bank officer",
            description = "Given the required details, a jwt token is generated"
    )
    @PostMapping("login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest request) {
        return new ResponseEntity<>(officerService.authenticateAndGetToken(request), HttpStatus.OK);
    }
    @Operation(
            summary = "creates a new customer account",
            description = "creates a new customer, store customer in the database and gives an Id to the user"
    )

    @PostMapping("new_account")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequest request){
        return new ResponseEntity<>(officerService.createBankAccount(request), HttpStatus.OK);
    }
    @Operation(
            summary = "Checks account balance for a customer",
            description = "Given that a customer requires for it and provides acc number, it fetches account balance from the database"
    )

    @GetMapping("account_balance")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> checkAccountBalance(@RequestBody EnquiryRequest request){
        return new ResponseEntity<>(officerService.checkAccountBalance(request), HttpStatus.OK);
    }

    @Operation(
    summary = "Gets the account name for a customer account",
    description = "Given that a customer requires for it and provides acc number, it fetches customer name from the database"
            )
    @GetMapping("account_name")

    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> checkAccountName(@RequestBody EnquiryRequest request){
        return new ResponseEntity<>(officerService.checkAccountName(request), HttpStatus.OK);
    }

    @PostMapping("credit")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> creditAccount(@RequestBody CreditDebitRequest request){
        return new ResponseEntity<>(officerService.creditAccount(request), HttpStatus.OK);
    }
    @PostMapping("debit")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> debitAccount(@RequestBody CreditDebitRequest request){
        return new ResponseEntity<>(officerService.debitAccount(request), HttpStatus.OK);
    }
    @GetMapping("transaction_history")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> getCustomerTransactions(@RequestBody TransactionHistoryRequest request){
        return new ResponseEntity<>(officerService.getAllTransactionsDoneByCustomer(request), HttpStatus.OK);
    }
    @GetMapping("all_bank_transactions")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> viewAllBankingHallTransactions(){
        return new ResponseEntity<>(officerService.viewAllBankingHallTransactions(), HttpStatus.OK);
    }
    @PostMapping("account_restriction")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> restrictBankAccount(@RequestBody RestrictAccountRequest request){
        return new ResponseEntity<>(officerService.restrictBankAccount(request), HttpStatus.OK);
    }
    @PostMapping("account_reactivation")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> activateBankAccount(@RequestBody ActivateAccount request){
        return new ResponseEntity<>(officerService.activateBankAccount(request), HttpStatus.OK);
    }
    @PostMapping("card_creation")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> createCard(@RequestBody RequestForCard request){
        return new ResponseEntity<>(officerService.createCard(request), HttpStatus.OK);
    }

    @PostMapping("card_deactivation")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> deactivateCard(@RequestBody DeactivateCard request){
        return new ResponseEntity<>(officerService.deActivateCard(request), HttpStatus.OK);
    }
}