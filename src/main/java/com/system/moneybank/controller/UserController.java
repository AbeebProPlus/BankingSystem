package com.system.moneybank.controller;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/")
@Tag(name = "Customer Account API's")
public class UserController{
    private final UserService userService;

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
        return new ResponseEntity<>(userService.createBankAccount(request), HttpStatus.OK);
    }

    @Operation(
            summary = "Gets account balance",
            description = "Given an account number, get the account balance for the user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK"
    )

    @GetMapping("account_balance")
    public ResponseEntity<?> checkAccountBalance(@RequestBody EnquiryRequest request){
        return new ResponseEntity<>(userService.checkAccountBalance(request), HttpStatus.OK);
    }
    @GetMapping("account_name")
    public ResponseEntity<?> checkAccountName(@RequestBody EnquiryRequest request){
        return new ResponseEntity<>(userService.checkAccountName(request), HttpStatus.OK);
    }
    @PostMapping("credit")
    public ResponseEntity<?> creditAccount(@RequestBody CreditDebitRequest request){
        return new ResponseEntity<>(userService.creditAccount(request), HttpStatus.OK);
    }
    @PostMapping("debit")
    public ResponseEntity<?> debitAccount(@RequestBody CreditDebitRequest request){
        return new ResponseEntity<>(userService.debitAccount(request), HttpStatus.OK);
    }
    @PostMapping("transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request){
        return new ResponseEntity<>(userService.transfer(request), HttpStatus.OK);
    }
    @GetMapping("transaction_history")
    public ResponseEntity<?> getCustomerTransactions(@RequestBody TransactionHistoryRequest request){
        return new ResponseEntity<>(userService.getAllTransactionsDoneByCustomer(request), HttpStatus.OK);
    }
}
