package com.fernando.ms.users.app.infraestructure.adapter.output.persistence.repository;

import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository.UserRepositoryCustomImpl;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryCustomImplTest {

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private UserRepositoryCustomImpl userRepositoryCustom;

    @Test
    @DisplayName("When Find User By FullName And Pagination Expect List Of Users")
    void When_FindUserByFullNameAndPagination_Expect_ListUsers() {
        UserEntity userEntity = TestUtilUser.buildUserEntityMock();
        UserEntity userEntity2 = TestUtilUser.buildUserEntityMock();
        userEntity2.setId("d854gorfd4");
        userEntity2.setNames("Felipe");
        userEntity2.setFullName("Felipe Sialer Ayala");

        when(reactiveMongoTemplate.find(any(Query.class), any(Class.class)))
                .thenReturn(Flux.just(userEntity, userEntity2));

        Flux<UserEntity> result = userRepositoryCustom.findAllByFullNamePagination("Fe", 1, 10);

        StepVerifier.create(result)
                .expectNext(userEntity)
                .expectNext(userEntity2)
                .verifyComplete();
    }

}
