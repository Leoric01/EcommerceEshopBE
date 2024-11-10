package com.leoric.ecommerceshopbe.models.mapstruct;

import com.leoric.ecommerceshopbe.requests.dto.UserUpdateReqDto;
import com.leoric.ecommerceshopbe.security.auth.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserUpdateReqDto(UserUpdateReqDto newData, @MappingTarget User connectedUser);

}