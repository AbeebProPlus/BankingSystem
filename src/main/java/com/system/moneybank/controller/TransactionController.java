package com.system.moneybank.controller;

import com.system.moneybank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction/")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("{transactionId}")
    public ResponseEntity<?> getTransaction(@PathVariable String transactionId){
        return new ResponseEntity<>(transactionService.getATransactionDetails(transactionId), HttpStatus.OK);
    }
}
