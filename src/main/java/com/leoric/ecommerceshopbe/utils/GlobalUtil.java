package com.leoric.ecommerceshopbe.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoric.ecommerceshopbe.handler.InvalidAccountTypeAccessException;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GlobalUtil {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    public static Account getAccountFromPrincipal(Object principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null");
        }
        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof Seller) {
            return (Seller) principal;
        }
        throw new IllegalArgumentException("Unknown principal type");
    }

    public static void duplicatedFieldsFinder(String path, String rootNodePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(Paths.get(path).toFile());
        Set<String> uniqueCategories = new HashSet<>();
        Set<String> parentCategoryIds = new HashSet<>();
        int counter = 0;
        for (JsonNode catNode : rootNode.path(rootNodePath)) {
            String categoryId = catNode.path("categoryId").asText();
            String parentCategoryId = catNode.path("parentCategoryId").asText();

            if (parentCategoryId.isEmpty()) {
                System.out.println("Invalid parentCategory in:" + categoryId);
                counter++;
            }
            parentCategoryIds.add(parentCategoryId);

            if (!uniqueCategories.add(categoryId)) {
                System.out.println("Duplicate categoryId found: " + categoryId);
                counter++;
            }
        }
        for (String catId : uniqueCategories) {
            if (!parentCategoryIds.add(catId)) {
                System.out.println("Duplicate between categoryId and parrentId found: " + catId);
                counter++;
            }
        }
        System.out.println("Check duplicates finished with " + counter + " duplicates found");
    }

    public String sanitizeCategoryId(String categoryId) {
        String processed = categoryId.trim().replaceAll("\\s+", "_");
        processed = processed.replaceAll("[^a-zA-Z0-9_]", "");
        processed = processed.replaceAll("^_+|_+$", "");
        processed = processed.replaceAll("_+", "_");
        return processed.toLowerCase();
    }

    public String sanitizeCategoryName(String categoryName) {
        String processed = categoryName.trim().replaceAll("_+", "_");
        processed = processed.replaceAll("[^a-zA-Z0-9_]", "");
        processed = processed.replaceAll("_", " ");
        processed = processed.replaceAll("\\s+", " ");
        processed = capitalizeEachWord(processed);
        return processed;
    }
    public Account getAccountFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("No authentication found");
        }
        return getAccountFromPrincipal(authentication.getPrincipal());
    }

    public User getPrincipalAsUser(Authentication authentication) {
        Account account = getAccountFromAuthentication(authentication);
        if (account instanceof User) {
            return userRepository.findById(account.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
        }
        throw new InvalidAccountTypeAccessException("Access denied: This endpoint is restricted to User accounts only.");
    }

    public Seller getPrincipalAsSeller(Authentication authentication) {
        Account account = getAccountFromAuthentication(authentication);
        if (account instanceof Seller) {
            return sellerRepository.findById(account.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        }
        throw new InvalidAccountTypeAccessException("Your account is not of type Seller");
    }

    private String capitalizeEachWord(String input) {
        String[] words = input.split(" ");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return capitalized.toString().trim();
    }
}