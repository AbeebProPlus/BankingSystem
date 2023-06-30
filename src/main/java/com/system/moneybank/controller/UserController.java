package com.system.moneybank.controller;

import com.system.moneybank.dtos.request.CreateAccountRequest;
import com.system.moneybank.dtos.request.CreditDebitRequest;
import com.system.moneybank.dtos.request.EnquiryRequest;
import com.system.moneybank.dtos.request.TransferRequest;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public Response createAccount(@RequestBody CreateAccountRequest request){
        return userService.createBankAccount(request);
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
    public Response checkAccountBalance(@RequestBody EnquiryRequest request){
        return userService.checkAccountBalance(request);
    }
    @GetMapping("account_name")
    public String checkAccountName(@RequestBody EnquiryRequest request){
        return userService.checkAccountName(request);
    }
    @PostMapping("credit")
    public Response creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }
    @PostMapping("debit")
    public Response debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }
    @PostMapping("transfer")
    public Response transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }
}
