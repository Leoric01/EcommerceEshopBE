package com.leoric.ecommerceshopbe.models.mapstruct;

import com.leoric.ecommerceshopbe.models.Seller;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SellerMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pickupAddress.id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateSellerFromSeller(Seller newDataSeller, @MappingTarget Seller connectedSeller);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    Seller mapWithoutPassword(Seller seller);
}

