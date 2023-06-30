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
@RequestMapping("/api/v1/customer/")
@Tag(name = "Customer Account API's")
public class CustomerController {
    private final UserService userService;

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

    @PostMapping("transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request){
        return new ResponseEntity<>(userService.transfer(request), HttpStatus.OK);
    }
    @GetMapping("transaction_history")
    public ResponseEntity<?> getCustomerTransactions(@RequestBody TransactionHistoryRequest request){
        return new ResponseEntity<>(userService.getAllTransactionsDoneByCustomer(request), HttpStatus.OK);
    }
}