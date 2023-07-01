package com.system.moneybank.controller;


import com.system.moneybank.dtos.request.*;
import com.system.moneybank.service.OfficerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/officer/")
@Tag(name = "Officer Account API's")

public class OfficerController {
    private final OfficerService officerService;

    @Operation(
            summary = "creates a new user account",
            description = "creates a new user, store user in the database and gives an Id to the user"
    )
    @ApiResponse(
            responseCode = "201",
            description = "CREATED"
    )
    @PostMapping("new_account")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request){
        return new ResponseEntity<>(officerService.createBankAccount(request), HttpStatus.OK);
    }

    @GetMapping("account_balance")
    public ResponseEntity<?> checkAccountBalance(@RequestBody EnquiryRequest request){
        return new ResponseEntity<>(officerService.checkAccountBalance(request), HttpStatus.OK);
    }

    @GetMapping("account_name")
    public ResponseEntity<?> checkAccountName(@RequestBody EnquiryRequest request){
        return new ResponseEntity<>(officerService.checkAccountName(request), HttpStatus.OK);
    }

    @PostMapping("credit")
    public ResponseEntity<?> creditAccount(@RequestBody CreditDebitRequest request){
        return new ResponseEntity<>(officerService.creditAccount(request), HttpStatus.OK);
    }
    @PostMapping("debit")
    public ResponseEntity<?> debitAccount(@RequestBody CreditDebitRequest request){
        return new ResponseEntity<>(officerService.debitAccount(request), HttpStatus.OK);
    }
    @GetMapping("transaction_history")
    public ResponseEntity<?> getCustomerTransactions(@RequestBody TransactionHistoryRequest request){
        return new ResponseEntity<>(officerService.getAllTransactionsDoneByCustomer(request), HttpStatus.OK);
    }
    @GetMapping("all_transactions")
    public ResponseEntity<?> viewAllBankingHallTransactions(){
        return new ResponseEntity<>(officerService.viewAllBankingHallTransactions(), HttpStatus.OK);
    }
    @PostMapping("restriction")
    public ResponseEntity<?> restrictBankAccount(@RequestBody RestrictAccountRequest request){
        return new ResponseEntity<>(officerService.restrictBankAccount(request), HttpStatus.OK);
    }

}