package com.leoric.ecommerceshopbe.models.mapstruct;

import com.leoric.ecommerceshopbe.models.Seller;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SellerMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSellerFromDto(Seller newDataSeller, @MappingTarget Seller patchedSeller);

    @Mapping(target = "password", ignore = true)
    Seller mapWithoutPassword(Seller seller);
}

