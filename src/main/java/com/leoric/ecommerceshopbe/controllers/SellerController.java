package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.SellerReport;
import com.leoric.ecommerceshopbe.models.constants.AccountStatus;
import com.leoric.ecommerceshopbe.requests.dto.SellerEditRequestDto;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.SellerReportService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
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
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final GlobalUtil globalUtil;

    @GetMapping("/")
    public ResponseEntity<Result<List<Seller>>> getAllSellers(@RequestParam(required = false) AccountStatus status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        Result<List<Seller>> response = Result.success(sellers, "Seller's details fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<Seller>> getSellerById(@PathVariable Long id) {
        Seller seller = sellerService.getSellerById(id);
        Result<Seller> response = Result.success(seller, "Seller's details fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/report")
    public ResponseEntity<Result<SellerReport>> getSellerReportBySeller(Authentication connectedUser) {
        Seller seller = globalUtil.getPrincipalAsSeller(connectedUser);
        SellerReport sellerReport = sellerReportService.getSellerReport(seller);
        Result<SellerReport> response = Result.success(sellerReport, "Seller's details fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }

    @PatchMapping()
    public ResponseEntity<Result<Seller>> updateSeller(Authentication connectedUser,
                                                       @RequestBody SellerEditRequestDto sellerReq) {
        Seller updatedSeller = sellerService.updateSeller(connectedUser, sellerReq);
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