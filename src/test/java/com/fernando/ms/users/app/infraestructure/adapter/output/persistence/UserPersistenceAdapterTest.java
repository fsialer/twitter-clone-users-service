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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {
    @Mock
    private UserReactiveRepository userReactiveRepository;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @InjectMocks
    private UserPersistenceAdapter userPersistenceAdapter;


    @Test
    @DisplayName("When Users Are Correct Expect A List Users Correct")
    void When_UsersAreCorrect_Expect_AListUsersCorrect() {
        when(userReactiveRepository.findAll()).thenReturn(Flux.just(TestUtilUser.buildUserEntityMock()));
        when(userPersistenceMapper.toUsers(any(Flux.class))).thenReturn(Flux.just( TestUtilUser.buildUserMock()));
        Flux<User> result = userPersistenceAdapter.findAll();
        StepVerifier.create(result)
                .expectNext( TestUtilUser.buildUserMock())
                .verifyComplete();
        Mockito.verify(userReactiveRepository,times(1)).findAll();
        Mockito.verify(userPersistenceMapper,times(1)).toUsers(any(Flux.class));
    }

    @Test
    @DisplayName("When User Identifier Is Correct Expect User Information Correct")
    void When_UserIdentifierIsCorrect_Expect_UserInformationCorrect(){
        when(userReactiveRepository.findById(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserEntityMock()));
        when(userPersistenceMapper.toUser(any(UserEntity.class))).thenReturn(TestUtilUser.buildUserMock());

        Mono<User> result=userPersistenceAdapter.findById("cde8c071a420424abf28b189ae2cd698");
        StepVerifier.create(result)
                .expectNext(TestUtilUser.buildUserMock())
                .verifyComplete();
        Mockito.verify(userReactiveRepository,times(1)).findById(anyString());
        Mockito.verify(userPersistenceMapper,times(1)).toUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("When User Information Is Correct Expect User Information Saved Successfully")
    void When_User_Information_Is_Correct_Expect_User_Information_Saved_Successfully() {
        when(userReactiveRepository.save(any(UserEntity.class))).thenReturn(Mono.just(TestUtilUser.buildUserEntityMock()));
        when(userPersistenceMapper.toUserEntity(any(User.class))).thenReturn(TestUtilUser.buildUserEntityMock());
        when(userPersistenceMapper.toUser(any(Mono.class))).thenReturn(Mono.just(TestUtilUser.buildUserMock()));

        Mono<User> savedUser = userPersistenceAdapter.save(TestUtilUser.buildUserMock());

        StepVerifier.create(savedUser)
                .expectNext(TestUtilUser.buildUserMock())
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
    @DisplayName("When User Exists Expect User Deleted Successfully")
    void When_UserExists_Expect_UserDeletedSuccessfully() {
        when(userReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());
        Mono<Void> result = userPersistenceAdapter.delete("cde8c071a420424abf28b189ae2cd698");
        StepVerifier.create(result)
                .verifyComplete();
        Mockito.verify(userReactiveRepository, times(1)).deleteById(anyString());
    }

    @Test
    @DisplayName("When User Verification Is Successful Expect User Verified")
    void When_UserVerificationIsSuccessful_Expect_UserVerified() {
        when(userReactiveRepository.existsById(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = userPersistenceAdapter.verifyUser("cde8c071a420424abf28b189ae2cd698");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(userReactiveRepository, times(1)).existsById(anyString());
    }

    @Test
    @DisplayName("When User IDs Are Correct Expect Users Returned")
    void When_UserIDsAreCorrect_Expect_UsersReturned() {
        User user2 = TestUtilUser.buildUserMock();
        user2.setId("cde8c071a420424abf28b189ae2cd6982");
        UserEntity userEntity2 = TestUtilUser.buildUserEntityMock();
        userEntity2.setId("cde8c071a420424abf28b189ae2cd6982");

        when(userReactiveRepository.findAllById(anyIterable())).thenReturn(Flux.just(TestUtilUser.buildUserEntityMock(), userEntity2));
        when(userPersistenceMapper.toUsers(any(Flux.class))).thenReturn(Flux.just(TestUtilUser.buildUserMock(), user2));

        Flux<User> result = userPersistenceAdapter.findByIds(List.of("cde8c071a420424abf28b189ae2cd698", "cde8c071a420424abf28b189ae2cd6982"));

        StepVerifier.create(result)
                .expectNext(TestUtilUser.buildUserMock())
                .expectNext(user2)
                .verifyComplete();

        Mockito.verify(userReactiveRepository, times(1)).findAllById(anyIterable());
        Mockito.verify(userPersistenceMapper, times(1)).toUsers(any(Flux.class));
    }

    @Test
    @DisplayName("When UserId Exists Expect Verify User By UserId Return True")
    void When_UserIdExists_Expect_VerifyUserByUserId_ReturnTrue() {
        when(userReactiveRepository.existsByUserIdIgnoreCase(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = userPersistenceAdapter.verifyUserByUserId("testUserId");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(userReactiveRepository, times(1)).existsByUserIdIgnoreCase(anyString());
    }

    @Test
    @DisplayName("When UserIdIsCorrect Expect Find By UserId Return User Information")
    void When_UserIdIsCorrect_Expect_FindByUserId_ReturnUserInformation() {
        when(userReactiveRepository.findByUserId(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserEntityMock()));
        when(userPersistenceMapper.toUser(any(UserEntity.class))).thenReturn(TestUtilUser.buildUserMock());

        Mono<User> result = userPersistenceAdapter.findByUserId("testUserId");

        StepVerifier.create(result)
                .expectNext(TestUtilUser.buildUserMock())
                .verifyComplete();

        Mockito.verify(userReactiveRepository, times(1)).findByUserId(anyString());
        Mockito.verify(userPersistenceMapper, times(1)).toUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("When FullName Exists Is Correct Expect A List Users")
    void When_FullNameExistsIsCorrect_Expect_AListUsers() {
        UserEntity userEntity = TestUtilUser.buildUserEntityMock();
        User user = TestUtilUser.buildUserMock();

        when(userReactiveRepository.findAllByFullNamePagination(anyString(), anyInt(), anyInt())).thenReturn(Flux.just(userEntity));
        when(userPersistenceMapper.toUsers(any(Flux.class))).thenReturn(Flux.just(user));

        Flux<User> users = userPersistenceAdapter.findUserByFullName("Fe", 1, 20);

        StepVerifier.create(users)
                .expectNext(user)
                .verifyComplete();

        Mockito.verify(userReactiveRepository, times(1)).findAllByFullNamePagination(anyString(), anyInt(), anyInt());
        Mockito.verify(userPersistenceMapper, times(1)).toUsers(any(Flux.class));
    }
}
