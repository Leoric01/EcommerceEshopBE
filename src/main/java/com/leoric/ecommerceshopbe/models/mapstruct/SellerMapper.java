package com.leoric.ecommerceshopbe.models.mapstruct;

import com.leoric.ecommerceshopbe.models.Seller;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SellerMapper {

//    @Mappings({
//            // Direct mappings for Seller fields
//            @Mapping(target = "sellerName", source = "sellerEditDto.sellerName"),
//            @Mapping(target = "email", source = "sellerEditDto.email"),
//            @Mapping(target = "password", source = "sellerEditDto.password"),
//            @Mapping(target = "vat", source = "sellerEditDto.vat"),
//
//            // Mapping for pickupAddress fields
//            @Mapping(target = "pickupAddress.name", source = "sellerEditDto.pickupAddress.name"),
//            @Mapping(target = "pickupAddress.street", source = "sellerEditDto.pickupAddress.street"),
//            @Mapping(target = "pickupAddress.locality", source = "sellerEditDto.pickupAddress.locality"),
//            @Mapping(target = "pickupAddress.city", source = "sellerEditDto.pickupAddress.city"),
//            @Mapping(target = "pickupAddress.country", source = "sellerEditDto.pickupAddress.country"),
//            @Mapping(target = "pickupAddress.zip", source = "sellerEditDto.pickupAddress.zip"),
//            @Mapping(target = "pickupAddress.mobile", source = "sellerEditDto.pickupAddress.mobile"),
//
//            // Mapping for businessDetails fields
//            @Mapping(target = "businessDetails.businessName", source = "sellerEditDto.businessDetails.businessName"),
//            @Mapping(target = "businessDetails.businessAddress", source = "sellerEditDto.businessDetails.businessAddress"),
//            @Mapping(target = "businessDetails.businessEmail", source = "sellerEditDto.businessDetails.businessEmail"),
//            @Mapping(target = "businessDetails.businessMobile", source = "sellerEditDto.businessDetails.businessMobile"),
//            @Mapping(target = "businessDetails.logo", source = "sellerEditDto.businessDetails.logo"),
//            @Mapping(target = "businessDetails.banner", source = "sellerEditDto.businessDetails.banner"),
//
//            // Mapping for bankDetails fields
//            @Mapping(target = "bankDetails.accountNumber", source = "sellerEditDto.bankDetails.accountNumber"),
//            @Mapping(target = "bankDetails.accountHolderName", source = "sellerEditDto.bankDetails.accountHolderName"),
//            @Mapping(target = "bankDetails.iban", source = "sellerEditDto.bankDetails.iban")
//    })
//    void updateSellerFromSellerEditDto(SellerEditRequestDto sellerEditDto, @MappingTarget Seller seller);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "pickupAddress.id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateSellerFromSeller(Seller newDataSeller, @MappingTarget Seller connectedSeller);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    Seller mapWithoutPassword(Seller seller);

}