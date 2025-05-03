package com.fernando.ms.users.app.infraestructure.adapter.output.persistence.mapper;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserPersistenceMapperTest {

    private UserPersistenceMapper userPersistenceMapper;

    @BeforeEach
    void setUp(){
        userPersistenceMapper= Mappers.getMapper(UserPersistenceMapper.class);
    }

    @Test
    @DisplayName("When Map Flux UserEntity Expect FluxUser")
    void When_MapFluxUserEntity_Expect_FluxUser(){
        UserEntity userEntity= TestUtilUser.buildUserEntityMock();

        Flux<User> userFlux=userPersistenceMapper.toUsers(Flux.just(userEntity));
        StepVerifier.create(userFlux)
                .consumeNextWith(user->{
                    assertEquals("cde8c071a420424abf28b189ae2cd6982", user.getId());
                    assertEquals("Fernando", user.getNames());
                    assertEquals("Sialer Ayala", user.getLastNames());
                    assertEquals("asialer05@hotmail.com", user.getEmail());
                    assertEquals("4f57f5d4f668d4ff5", user.getUserId());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("When Map Mono UserEntity Expect MonoUser")
    void When_MapMonoUserEntity_Expect_MonoUser(){
        UserEntity userEntity= TestUtilUser.buildUserEntityMock();

        Mono<User> userMono=userPersistenceMapper.toUser(Mono.just(userEntity));
        StepVerifier.create(userMono)
                        .consumeNextWith(user->{
                            assertEquals("cde8c071a420424abf28b189ae2cd6982", user.getId());
                            assertEquals("Fernando", user.getNames());
                            assertEquals("Sialer Ayala", user.getLastNames());
                            assertEquals("asialer05@hotmail.com", user.getEmail());
                            assertEquals("4f57f5d4f668d4ff5", user.getUserId());
                        })
                .verifyComplete();
    }

    @Test
    @DisplayName("When Map User Entity Expect User")
    void When_MapUserEntity_Expect_User(){
        UserEntity userEntity= TestUtilUser.buildUserEntityMock();
        User user=userPersistenceMapper.toUser(userEntity);
        assertEquals("cde8c071a420424abf28b189ae2cd6982", user.getId());
        assertEquals("Fernando", user.getNames());
        assertEquals("Sialer Ayala", user.getLastNames());
        assertEquals("asialer05@hotmail.com", user.getEmail());
        assertEquals("4f57f5d4f668d4ff5", user.getUserId());
    }

    @Test
    @DisplayName("When Map User Expect UserEntity")
    void When_MapUser_Expect_UserEntity(){
        User user= TestUtilUser.buildUserMock();
        UserEntity userEntity=userPersistenceMapper.toUserEntity(user);
        assertEquals("cde8c071a420424abf28b189ae2cd6982", userEntity.getId());
        assertEquals("Fernando", userEntity.getNames());
        assertEquals("Sialer Ayala", userEntity.getLastNames());
        assertEquals("asialer05@hotmail.com", userEntity.getEmail());
        assertEquals("4f57f5d4f668d4ff5", userEntity.getUserId());
    }
}
