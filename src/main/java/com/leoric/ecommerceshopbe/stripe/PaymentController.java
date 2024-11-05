package com.leoric.ecommerceshopbe.stripe;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.SellerReport;
import com.leoric.ecommerceshopbe.response.ApiResponse;
import com.leoric.ecommerceshopbe.services.interfaces.SellerReportService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import com.leoric.ecommerceshopbe.services.interfaces.TransactionService;
import com.leoric.ecommerceshopbe.stripe.model.PaymentOrder;
import com.leoric.ecommerceshopbe.stripe.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable("paymentId") String paymentId,
                                                             @RequestParam String paymentLinkId
    ) {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(paymentLinkId);
        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);


        if (paymentSuccess) {
            for (Order order : paymentOrder.getOrders()) {
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders() + 1);
                report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales() + order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Payment successful");
        return ResponseEntity.status(201).body(apiResponse);
    }
}