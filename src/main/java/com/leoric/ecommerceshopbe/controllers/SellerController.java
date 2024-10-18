package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.SellerReportService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/seller/")
public class SellerController {

    private final SellerService sellerService;
    private final SellerReportService sellerReportService;

    @GetMapping()
    public List<Seller> getAllSellers() {
        return sellerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<Seller>> getSellerById(@PathVariable Long id) {
        Seller seller = sellerService.getSellerById(id);
        Result<Seller> response = Result.success(seller, "Seller's details fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }

    //    @GetMapping("/report")
//    public ResponseEntity<Result<SellerReport>> getSellerByReport(Authentication connectedUser) {
//        Seller seller = (Seller) connectedUser.getPrincipal();
//        SellerReport sellerReport = sellerReportService.getSellerReport(seller);
//        Result<SellerReport> response = Result.success(sellerReport, "Seller's details fetched succesfully", OK.value());
//        return ResponseEntity.status(OK).body(response);
//    }
    @PatchMapping()
    public ResponseEntity<Result<Seller>> updateSeller(Authentication connectedUser,
                                                       @RequestBody Seller seller) {
        Seller updatedSeller = sellerService.updateSeller(connectedUser, seller);
        Result<Seller> response = Result.success(updatedSeller, "Seller's details updated succesfully", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Result<Seller>> updateSellerById(@PathVariable Long id,
                                                           @RequestBody Seller seller) {
        Seller updatedSeller = sellerService.updateSellerById(id, seller);
        Result<Seller> response = Result.success(updatedSeller, "Seller's details updated succesfully", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<String>> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        Result<String> response = Result.success("Seller's details updated succesfully", ACCEPTED.value());
        return ResponseEntity.status(ACCEPTED).body(response);
    }
}
