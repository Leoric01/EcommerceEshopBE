package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/")
public class SellerController {

    private final SellerService sellerService;
    private final AuthService authService;

    @GetMapping()
    public List<Seller> getAllSellers() {
        return sellerService.findAll();
    }


}
