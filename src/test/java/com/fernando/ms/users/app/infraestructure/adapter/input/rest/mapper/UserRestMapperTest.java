package com.fernando.ms.users.app.infraestructure.adapter.input.rest.mapper;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ExistsUserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRestMapperTest {
    private UserRestMapper userRestMapper;

    @BeforeEach
    void setUtp(){
        userRestMapper= Mappers.getMapper(UserRestMapper.class);
    }

    @Test
    @DisplayName("When Map Flux UserEntity Expect FluxUser")
    void When_MapFluxUser_Expect_FluxUserResponse(){
        User user= TestUtilUser.buildUserMock();

        Flux<UserResponse> userResponseFlux=userRestMapper.toUsersResponse(Flux.just(user));
        StepVerifier.create(userResponseFlux)
                .consumeNextWith(userResponse->{
                    assertEquals("cde8c071a420424abf28b189ae2cd6982", userResponse.getId());
                    assertEquals("Fernando", userResponse.getNames());
                    assertEquals("Sialer Ayala", userResponse.getLastNames());
                    assertEquals("asialer05@hotmail.com", userResponse.getEmail());
                    assertEquals("MALE", userResponse.getSex());
                    assertEquals(LocalDate.of(1991, Month.JANUARY,5), userResponse.getBirth());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("When Map Flux UserEntity Expect FluxUser")
    void When_MapUser_Expect_UserResponse(){
        User user= TestUtilUser.buildUserMock();

        UserResponse userResponse=userRestMapper.toUserResponse(user);
        assertEquals("cde8c071a420424abf28b189ae2cd6982", userResponse.getId());
        assertEquals("Fernando", userResponse.getNames());
        assertEquals("Sialer Ayala", userResponse.getLastNames());
        assertEquals("asialer05@hotmail.com", userResponse.getEmail());
        assertEquals("MALE", userResponse.getSex());
        assertEquals(LocalDate.of(1991, Month.JANUARY,5), userResponse.getBirth());
    }

    @Test
    @DisplayName("When Map CreateUserRequest Expect User")
    void When_MapCreateUserRequest_Expect_User(){
        CreateUserRequest createUserRequest= TestUtilUser.buildCreateUserRequestMock();

        User user=userRestMapper.toUser(createUserRequest);
        assertEquals("Fernando", user.getNames());
        assertEquals("Sialer Ayala", user.getLastNames());
        assertEquals("asialer05@hotmail.com", user.getEmail());
    }

    @Test
    @DisplayName("When Map UpdateUserRequest Expect User")
    void When_MapUpdateUserRequest_Expect_User(){
        UpdateUserRequest updateUserRequest= TestUtilUser.buildUpdateUserRequestMock();

        User user=userRestMapper.toUser(updateUserRequest);
        assertEquals("Fernando", user.getNames());
        assertEquals("Sialer Ayala", user.getLastNames());
        assertEquals("asialer05@hotmail.com", user.getEmail());
        assertEquals("MALE", user.getSex());
        assertEquals(LocalDate.of(1991, Month.JANUARY,5), user.getBirth());
    }

    @Test
    @DisplayName("When Map Boolean Expect ExistsUserResponse")
    void When_MapBoolean_Expect_ExistsUserResponse(){
        ExistsUserResponse existsUserResponse=userRestMapper.toExistsUserResponse(Boolean.TRUE);
        assertTrue(existsUserResponse.getExists());
    }

}
