package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Deal;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;
    @PostMapping
    public ResponseEntity<Result<Deal>> createDeal(@RequestBody Deal deal) {
        Deal createdDeal = dealService.createDeal(deal);
        Result<Deal> result = Result.success(createdDeal, "Deal successfully created", ACCEPTED.value());
        return ResponseEntity.ok(result);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Result<Deal>> createDeal(@PathVariable Long id ,@RequestBody Deal deal) {
        Deal updatedDeal = dealService.updateDealById(id, deal);
        Result<Deal> result = Result.success(updatedDeal, "Deal successfully updated", ACCEPTED.value());
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deleteDeal(@PathVariable Long id ) {
        dealService.deleteDealById(id);
        Result<Void> result = Result.success("Deal successfully deleted", OK.value());
        return ResponseEntity.ok(result);
    }
}