package com.leoric.ecommerceshopbe.models.mapstruct;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.response.AddressDtoResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "street", source = "street")
//    @Mapping(target = "locality", source = "locality")
//    @Mapping(target = "zip", source = "zip")
//    @Mapping(target = "city", source = "city")
//    @Mapping(target = "country", source = "country")
//    @Mapping(target = "mobile", source = "mobile")
    Address toAddress(AddAddressRequestDTO addressDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDto(AddAddressRequestDTO addressDto, @MappingTarget Address address);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(source = "user.id", target = "user_id")
//    @Mapping(source = "seller.id", target = "seller_id")
    AddressDtoResponse toAddressDtoResponse(Address address);
}