package com.system.moneybank.controller;


import com.system.moneybank.dtos.request.*;
import com.system.moneybank.service.InternetBankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/iCustomer/")
@Tag(name = "Internet Banking API's")
public class InternetBankingController {
    private final InternetBankingService internetBankingService;


    @Operation(
            summary = "Adds a customer to the internet banking platform",
            description = "Given the required details, adds up a customer to the internet banking platform"
    )
    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody RegisterForInternetBanking registerForInternetBanking){
        return new ResponseEntity<>(internetBankingService.signUp(registerForInternetBanking), HttpStatus.OK);
    }
    @Operation(
            summary = "Change card pin",
            description = "Given the required details, change card pin"
    )
    @PostMapping("pin")
    public ResponseEntity<?> changeCardPin(@RequestBody ChangeCardPinRequest changeCardPinRequest){
        return new ResponseEntity<>(internetBankingService.changeCardPin(changeCardPinRequest), HttpStatus.OK);
    }
    @Operation(
            summary = "Check account balance",
            description = "Given the required details, gets account balance for the user"
    )
    @GetMapping("account_balance")
    public ResponseEntity<?> checkAccountBalance(@RequestBody EnquiryRequest request){
        return new ResponseEntity<>(internetBankingService.checkAccountBalance(request), HttpStatus.OK);
    }
    @Operation(
            summary = "Transfer from one account to another",
            description = "Given the required details, transfer to another account"
    )

    @PostMapping("transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request){
        return new ResponseEntity<>(internetBankingService.transfer(request), HttpStatus.OK);
    }
    @Operation(
            summary = "Gets transaction history of a customer",
            description = "Given the required details, gets transaction history from the database"
    )

    @GetMapping("transaction_history")
    public ResponseEntity<?> getCustomerTransactions(@RequestBody TransactionHistoryRequest request){
        return new ResponseEntity<>(internetBankingService.getAllTransactionsDoneByCustomer(request), HttpStatus.OK);
    }
}
