package com.fernando.ms.users.app.infraestructure.adapter.output.persistence;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.UserPersistenceAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository.UserReactiveRepository;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPersistenceAdapterTest {
    @Mock
    private UserReactiveRepository userReactiveRepository;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @InjectMocks
    private UserPersistenceAdapter userPersistenceAdapter;


    @Test
    @DisplayName("When Users Are Correct Expect A List Users Correct")
    void When_UsersAreCorrect_Expect_AListUsersCorrect() {
        User user= TestUtilUser.buildUserMock();
        UserEntity userEntity= TestUtilUser.buildUserEntityMock();
        when(userReactiveRepository.findAll()).thenReturn(Flux.just(userEntity));
        when(userPersistenceMapper.toUsers(any(Flux.class))).thenReturn(Flux.just(user));
        Flux<User> result = userPersistenceAdapter.findAll();
        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
        Mockito.verify(userReactiveRepository,times(1)).findAll();
        Mockito.verify(userPersistenceMapper,times(1)).toUsers(any(Flux.class));
    }

    @Test
    @DisplayName("When User Identifier Is Correct Expect User Information Correct")
    void When_UserIdentifierIsCorrect_Expect_UserInformationCorrect(){
        User user= TestUtilUser.buildUserMock();
        UserEntity userEntity= TestUtilUser.buildUserEntityMock();
        when(userReactiveRepository.findById(anyLong())).thenReturn(Mono.just(userEntity));
        when(userPersistenceMapper.toUser(any(UserEntity.class))).thenReturn(user);

        Mono<User> result=userPersistenceAdapter.finById(1L);
        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
        Mockito.verify(userReactiveRepository,times(1)).findById(anyLong());
        Mockito.verify(userPersistenceMapper,times(1)).toUser(any(UserEntity.class));
    }
}
