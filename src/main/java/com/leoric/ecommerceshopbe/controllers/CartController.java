package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.handler.OperationNotPermittedException;
import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.CartItem;
import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.requests.AddItemReq;
import com.leoric.ecommerceshopbe.requests.CartItemQuantityUpdateReq;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.services.interfaces.CartItemService;
import com.leoric.ecommerceshopbe.services.interfaces.CartService;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final GlobalUtil globalUtil;

    @GetMapping("")
    public ResponseEntity<Result<Cart>> findUserCartHandler(Authentication connectedUser) {
        User user = globalUtil.getPrincipalAsUser(connectedUser);
        Cart cart = cartService.findUserCart(user);
        Result<Cart> response = Result.success(cart, "Cart found succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }

    @PutMapping("/add")
    public ResponseEntity<Result<CartItem>> addItemToCart(Authentication connectedUser,
                                                          @RequestBody AddItemReq itemReq) {
        User user = globalUtil.getPrincipalAsUser(connectedUser);
        Product product = productService.findProductById(itemReq.getProductId());
        CartItem cartItem = cartService.addCartItem(
                user,
                product,
                itemReq.getSize(),
                itemReq.getQuantity());
        Result<CartItem> response = Result.success(cartItem, "Item added to cart successfully", ACCEPTED.value());
        return ResponseEntity.status(ACCEPTED).body(response);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Result<Void>> deleteCartItem(Authentication connectedUser,
                                                       @PathVariable Long cartItemId) {
        User user = globalUtil.getPrincipalAsUser(connectedUser);
        cartItemService.removeCartItem(user.getId(), cartItemId);
        Result<Void> response = Result.success("Item successfully deleted from cart", ACCEPTED.value());
        return ResponseEntity.status(ACCEPTED).body(response);
    }

// TODO  fix business logic, you cant alter the product, this way you can make cart item watever you want

    @PutMapping("/item")
    public ResponseEntity<Result<CartItem>> updateCartItemHandler(Authentication connectedUser,
                                                                  @RequestBody CartItemQuantityUpdateReq req) {

        if (req.getQuantity() >= 0) {
            CartItem cartItemUpdated = cartItemService.updateCartItem(connectedUser, req);
            Result<CartItem> response = Result.success(cartItemUpdated, "Item updated  successfully", ACCEPTED.value());
            return ResponseEntity.status(ACCEPTED).body(response);
        }
        throw new OperationNotPermittedException("couldn't update cart item");
    }
}