package com.system.moneybank.controller;

import com.system.moneybank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction/")
@Tag(name = "Transaction Api")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(
            summary = "Gets details of a particular transaction done by a customer on internet banking"
    )
    @GetMapping("{transactionId}")
    @PreAuthorize("hasAuthority('OFFICER')")
    public ResponseEntity<?> getTransaction(@PathVariable String transactionId){
        return new ResponseEntity<>(transactionService.getATransactionDetails(transactionId), HttpStatus.OK);
    }
}
