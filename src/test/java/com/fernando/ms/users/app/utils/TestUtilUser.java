package com.fernando.ms.users.app.utils;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ExistsUserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;

import java.time.LocalDateTime;

public class TestUtilUser {

    public static User buildUserMock(){
        return new User(1L,"Fernando","Sialer","asialer05@hotmail.com");
    }

    public static UserEntity buildUserEntityMock(){
        return UserEntity.builder()
                .id(1L)
                .names("Fernando")
                .lastNames("Sialer")
                .email("asialer05@hotmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public  static UserResponse buildUserResponseMock(){
        return UserResponse.builder()
                .id(1L)
                .username("falex")
                .names("Fernando Sialer")
                .email("asialer05@hotmail.com")
                .build();
    }

    public static CreateUserRequest buildCreateUserRequestMock(){
        return  CreateUserRequest.builder()
                .names("Fernando Sialer")
                .email("asialer05@hotmail.com")
                .build();
    }

    public static UpdateUserRequest buildUpdateUserRequestMock(){
        return  UpdateUserRequest.builder()
                .names("Fernando Sialer")
                .email("asialer05@hotmail.com")
                .build();
    }

    public static ExistsUserResponse buildExistsUserResponseMock(){
        return  ExistsUserResponse.builder()
                .exists(true)
                .build();
    }
}
