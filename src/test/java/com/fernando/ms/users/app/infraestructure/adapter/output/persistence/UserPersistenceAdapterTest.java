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

import static org.mockito.ArgumentMatchers.*;
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

    @Test
    @DisplayName("When User Information Is Correct Expect User Information Saved Successfully")
    void When_User_Information_Is_Correct_Expect_User_Information_Saved_Successfully() {
        User user= TestUtilUser.buildUserMock();
        UserEntity userEntity= TestUtilUser.buildUserEntityMock();
        when(userReactiveRepository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));
        when(userPersistenceMapper.toUserEntity(any(User.class))).thenReturn(userEntity);
        when(userPersistenceMapper.toUser(any(Mono.class))).thenReturn(Mono.just(user));

        Mono<User> savedUser = userPersistenceAdapter.save(user);

        StepVerifier.create(savedUser)
                .expectNext(user)
                .verifyComplete();

        Mockito.verify(userReactiveRepository,times(1)).save(any(UserEntity.class));
        Mockito.verify(userPersistenceMapper,times(1)).toUserEntity(any(User.class));
        Mockito.verify(userPersistenceMapper,times(1)).toUser(any(Mono.class));
    }

    @Test
    @DisplayName("When Email User Exists Expect Return True")
    void When_EmailUserExists_Expect_Return_True() {

        when(userReactiveRepository.existsByEmailIgnoreCase(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> exists = userPersistenceAdapter.existsByEmail("test@example.com");

        StepVerifier.create(exists)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(userReactiveRepository,times(1)).existsByEmailIgnoreCase(anyString());
    }

    @Test
    @DisplayName("When Username User Exists Expect Return True")
    void When_UsernameUserExists_Expect_Return_True() {
        when(userReactiveRepository.existsByUsernameIgnoreCase(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> exists = userPersistenceAdapter.existsByUsername("testuser");

        StepVerifier.create(exists)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(userReactiveRepository,times(1)).existsByUsernameIgnoreCase(anyString());
    }

    @Test
    @DisplayName("When User Exists Expect User Deleted Successfully")
    void When_UserExists_Expect_UserDeletedSuccessfully() {
        when(userReactiveRepository.deleteById(anyLong())).thenReturn(Mono.empty());
        Mono<Void> result = userPersistenceAdapter.delete(1L);
        StepVerifier.create(result)
                .verifyComplete();
        Mockito.verify(userReactiveRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("When Username Is Correct Expect User Information Correct")
    void When_UsernameIsCorrect_Expect_UserInformationCorrect() {
        User user = TestUtilUser.buildUserMock();
        UserEntity userEntity = TestUtilUser.buildUserEntityMock();

        when(userReactiveRepository.findByUsername(anyString())).thenReturn(Mono.just(userEntity));
        when(userPersistenceMapper.toUser(any(UserEntity.class))).thenReturn(user);

        Mono<User> result = userPersistenceAdapter.findByUsername("testuser");

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        Mockito.verify(userReactiveRepository, times(1)).findByUsername(anyString());
        Mockito.verify(userPersistenceMapper, times(1)).toUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("When User Verification Is Successful Expect User Verified")
    void When_UserVerificationIsSuccessful_Expect_UserVerified() {
        when(userReactiveRepository.existsById(anyLong())).thenReturn(Mono.just(true));

        Mono<Boolean> result = userPersistenceAdapter.verifyUser(1L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(userReactiveRepository, times(1)).existsById(anyLong());
    }
}
