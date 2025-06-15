package com.fernando.ms.users.app.utils;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ExistsUserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class TestUtilUser {

    public static User buildUserMock(){
        return User.builder()
                .id("cde8c071a420424abf28b189ae2cd6982")
                .names("Fernando")
                .lastNames("Sialer Ayala")
                .fullName("Fernando Sialer Ayala")
                .email("asialer05@hotmail.com")
                .userId("4f57f5d4f668d4ff5")
                .birth(LocalDate.of(1991, Month.JANUARY,5))
                .sex("MALE")
                .build();
    }

    public static UserEntity buildUserEntityMock(){
        return UserEntity.builder()
                .id("cde8c071a420424abf28b189ae2cd6982")
                .names("Fernando")
                .lastNames("Sialer Ayala")
                .fullName("Fernando Sialer Ayala")
                .email("asialer05@hotmail.com")
                .userId("4f57f5d4f668d4ff5")
                .birth(LocalDate.of(1991, Month.JANUARY,5))
                .sex("MALE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public  static UserResponse buildUserResponseMock(){
        return UserResponse.builder()
                .id("cde8c071a420424abf28b189ae2cd6982")
                .names("Fernando")
                .lastNames("Sialer Ayala")
                .fullName("Fernando Sialer Ayala")
                .email("asialer05@hotmail.com")
                .birth(LocalDate.of(1991, Month.JANUARY,5))
                .sex("MALE")
                .build();
    }

    public static CreateUserRequest buildCreateUserRequestMock(){
        return  CreateUserRequest.builder()
                .names("Fernando")
                .lastNames("Sialer Ayala")
                .email("asialer05@hotmail.com")
                .userId("cde8c071a420424abf28b189ae2cd69824")
                .build();
    }

    public static UpdateUserRequest buildUpdateUserRequestMock(){
        return  UpdateUserRequest.builder()
                .names("Fernando")
                .lastNames("Sialer Ayala")
                .email("asialer05@hotmail.com")
                .birth(LocalDate.of(1991, Month.JANUARY,5))
                .sex("MALE")
                .build();
    }

    public static ExistsUserResponse buildExistsUserResponseMock(){
        return  ExistsUserResponse.builder()
                .exists(true)
                .build();
    }
}
