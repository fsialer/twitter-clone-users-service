package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.*;
import com.fernando.ms.users.app.domain.models.User;
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
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserPersistencePort userPersistencePort;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("When Users Information Is Correct Expect A List Users")
    void When_UsersInformationIsCorrect_Expect_AListUsers(){
        when(userPersistencePort.findAll()).thenReturn(Flux.just(TestUtilUser.buildUserMock()));

        Flux<User> users=userService.findAll();
        StepVerifier.create(users)
                .expectNext(TestUtilUser.buildUserMock())
                .verifyComplete();
        Mockito.verify(userPersistencePort,times(1)).findAll();
    }

    @Test
    @DisplayName("When User Identifier Is Correct Except User Information Correct")
    void When_UserIdentifierIsCorrect_Except_UserInformationCorrect(){
        when(userPersistencePort.findById(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserMock()));
        Mono<User> userMono=userService.findById("cde8c071a420424abf28b189ae2cd698");
        StepVerifier.create(userMono)
                .expectNext(TestUtilUser.buildUserMock())
                .verifyComplete();
        Mockito.verify(userPersistencePort,times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Identifier Is Invalid")
    void Expect_UserNotFoundException_When_UserIdentifierIsInvalid(){
        when(userPersistencePort.findById(anyString())).thenReturn(Mono.empty());
        Mono<User> userMono=userService.findById("cde8c071a420424abf28b189ae2cd698");
        StepVerifier.create(userMono)
                .expectError(UserNotFoundException.class)
                .verify();
        Mockito.verify(userPersistencePort,times(1)).findById(anyString());
    }


    @Test
    @DisplayName("When User Information Is Correct Expect User Information Saved Successfully")
    void When_UserInformationIsCorrect_Expect_UserInformationSavedSuccessfully() {
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(userPersistencePort.save(any(User.class))).thenReturn(Mono.just(TestUtilUser.buildUserMock()));

        Mono<User> savedUser = userService.save(TestUtilUser.buildUserMock());

        StepVerifier.create(savedUser)
                .expectNext(TestUtilUser.buildUserMock())
                .verifyComplete();

        Mockito.verify(userPersistencePort,times(1)).existsByEmail(anyString());
        Mockito.verify(userPersistencePort,times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Expect UserEmailAlreadyExistsException When Username Already Exists")
    void Expect_UserEmailAlreadyExistsException_When_UsernameAlreadyExists() {
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(true));

        Mono<User> savedUser = userService.save(TestUtilUser.buildUserMock());

        StepVerifier.create(savedUser)
                .expectError(UserEmailAlreadyExistsException.class)
                .verify();

        Mockito.verify(userPersistencePort,times(1)).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).save(any(User.class));
    }

    @Test
    @DisplayName("When User Information Is Correct Expect User Information Updated Successfully")
    void When_UserInformationIsCorrect_Expect_UserInformationUpdatedSuccessfully() {
        User updatedUser = TestUtilUser.buildUserMock();
        updatedUser.setEmail("newemail@example.com");

        when(userPersistencePort.findById(anyString())).thenReturn(Mono.just( TestUtilUser.buildUserMock()));
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(userPersistencePort.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        Mono<User> result = userService.update("cde8c071a420424abf28b189ae2cd698", updatedUser);

        StepVerifier.create(result)
                .expectNext(updatedUser)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).findById(anyString());
        Mockito.verify(userPersistencePort, times(1)).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("When User Information Is Correct Expect User Information Updated Successfully")
    void When_UserInformationEmailIsSame_Expect_UserInformationUpdatedSuccessfully() {
        User updatedUser = TestUtilUser.buildUserMock();

        when(userPersistencePort.findById(anyString())).thenReturn(Mono.just( TestUtilUser.buildUserMock()));
        when(userPersistencePort.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        Mono<User> result = userService.update("cde8c071a420424abf28b189ae2cd698", updatedUser);

        StepVerifier.create(result)
                .expectNext(updatedUser)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).findById(anyString());
        Mockito.verify(userPersistencePort, never()).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Identifier Does Not Exists")
    void Expect_UserNotFoundException_When_UserIdentifierDoesNotExists() {
        User updatedUser = TestUtilUser.buildUserMock();
        updatedUser.setEmail("newemail@hotmail.com");

        User existingUser = TestUtilUser.buildUserMock();
        existingUser.setEmail("existingemail@example.com");

        when(userPersistencePort.findById(anyString())).thenReturn(Mono.empty());

        Mono<User> result = userService.update("cde8c071a420424abf28b189ae2cd698", updatedUser);

        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();

        Mockito.verify(userPersistencePort, times(1)).findById(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).save(any(User.class));
    }


    @Test
    @DisplayName("Expect UserEmailAlreadyExistsException When Email Already Exists")
    void Expect_UserEmailAlreadyExistsException_When_EmailAlreadyExists() {
        User updatedUser = TestUtilUser.buildUserMock();
        updatedUser.setEmail("newemail@hotmail.com");

        User existingUser = TestUtilUser.buildUserMock();
        existingUser.setEmail("existingemail@example.com");

        when(userPersistencePort.findById(anyString())).thenReturn(Mono.just( existingUser));
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(true));

        Mono<User> result = userService.update("cde8c071a420424abf28b189ae2cd698", updatedUser);

        StepVerifier.create(result)
                .expectError(UserEmailAlreadyExistsException.class)
                .verify();

        Mockito.verify(userPersistencePort, times(1)).findById(anyString());
        Mockito.verify(userPersistencePort, times(1)).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).save(any(User.class));
    }


    @Test
    @DisplayName("When User Exists Expect User Deleted Successfully")
    void When_UserExists_Expect_UserDeletedSuccessfully() {
        when(userPersistencePort.findById(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserMock()));
        when(userPersistencePort.delete(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = userService.delete("cde8c071a420424abf28b189ae2cd698");

        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).findById(anyString());
        Mockito.verify(userPersistencePort, times(1)).delete(anyString());
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Does Not Exist")
    void Expect_UserNotFoundException_When_UserDoesNotExist() {
        when(userPersistencePort.findById(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = userService.delete("cde8c071a420424abf28b189ae2cd698");

        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();

        Mockito.verify(userPersistencePort, times(1)).findById(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).delete(anyString());
    }

    @Test
    @DisplayName("When User Verification Is Successful Expect User Verified")
    void When_UserVerificationIsSuccessful_Expect_UserVerified() {

        when(userPersistencePort.verifyUser(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = userService.verifyUser("cde8c071a420424abf28b189ae2cd698");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).verifyUser(anyString());
    }

    @Test
    @DisplayName("When User Verification Is Incorrect Expect User Do Not Verified")
    void When_UserVerificationIsIncorrect_Expect_UserDoNotVerified() {

        when(userPersistencePort.verifyUser(anyString())).thenReturn(Mono.just(false));

        Mono<Boolean> result = userService.verifyUser("cde8c071a420424abf28b189ae2cd698");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).verifyUser(anyString());
    }

    @Test
    @DisplayName("When User IDs Are Correct Expect Users Returned")
    void When_UserIDsAreCorrect_Expect_UsersReturned() {
        User user2 = TestUtilUser.buildUserMock();
        user2.setId("cde8c071a420424abf28b189ae2cd6982");

        when(userPersistencePort.findByIds(anyIterable())).thenReturn(Flux.just( TestUtilUser.buildUserMock(), user2));

        Flux<User> result = userService.findByIds(List.of("cde8c071a420424abf28b189ae2cd698","cde8c071a420424abf28b189ae2cd6982"));

        StepVerifier.create(result)
                .expectNext(TestUtilUser.buildUserMock())
                .expectNext(user2)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).findByIds(anyIterable());
    }

    @Test
    @DisplayName("When User Authenticate Is Correct Except User Information Correct")
    void When_UserAuthenticateIsCorrect_Except_UserInformationCorrect(){
        when(userPersistencePort.findByUserId(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserMock()));
        Mono<User> userMono=userService.findByUserId("cde8c071a420424abf28b189ae2cd698");
        StepVerifier.create(userMono)
                .expectNext(TestUtilUser.buildUserMock())
                .verifyComplete();
        Mockito.verify(userPersistencePort,times(1)).findByUserId(anyString());
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Authenticate Is Invalid")
    void Expect_UserNotFoundException_When_UserAuthenticateIsInvalid(){
        when(userPersistencePort.findByUserId(anyString())).thenReturn(Mono.empty());
        Mono<User> userMono=userService.findByUserId("cde8c071a420424abf28b189ae2cd698");
        StepVerifier.create(userMono)
                .expectError(UserNotFoundException.class)
                .verify();
        Mockito.verify(userPersistencePort,times(1)).findByUserId(anyString());
    }

    @Test
    @DisplayName("When User Authenticated Is Correct Expect User Information Updated Successfully")
    void When_UserAuthenticatedIsCorrect_Expect_UserInformationUpdatedSuccessfully() {
        User updatedUser = TestUtilUser.buildUserMock();
        updatedUser.setEmail("newemail@example.com");

        when(userPersistencePort.findByUserId(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserMock()));
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(userPersistencePort.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        Mono<User> result = userService.updateByUserId("cde8c071a420424abf28b189ae2cd698", updatedUser);

        StepVerifier.create(result)
                .expectNext(updatedUser)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).findByUserId(anyString());
        Mockito.verify(userPersistencePort, times(1)).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("When User Authenticated Email Same Is Correct Expect User Information Updated Successfully")
    void When_UserAuthenticatedEmailSame_Expect_UserInformationUpdatedSuccessfully() {
        User updatedUser = TestUtilUser.buildUserMock();

        when(userPersistencePort.findByUserId(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserMock()));
        when(userPersistencePort.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        Mono<User> result = userService.updateByUserId("cde8c071a420424abf28b189ae2cd698", updatedUser);

        StepVerifier.create(result)
                .expectNext(updatedUser)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).findByUserId(anyString());
        Mockito.verify(userPersistencePort,never()).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Authenticated Did Not Exists")
    void Expect_UserNotFoundException_When_UserIdentifierDidNotExists() {
        User updatedUser = TestUtilUser.buildUserMock();

        when(userPersistencePort.findByUserId(anyString())).thenReturn(Mono.empty());

        Mono<User> result = userService.updateByUserId("cde8c071a420424abf28b189ae2cd698", updatedUser);

        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();

        Mockito.verify(userPersistencePort, times(1)).findByUserId(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).save(any(User.class));
    }

    @Test
    @DisplayName("Expect UserEmailAlreadyExistsException When Email User Authenticated Already Exists")
    void Expect_UserEmailAlreadyExistsException_When_EmailUserAuthenticateAlreadyExists() {
        User updatedUser = TestUtilUser.buildUserMock();
        updatedUser.setEmail("newemail@hotmail.com");

        User existingUser = TestUtilUser.buildUserMock();
        existingUser.setEmail("existingemail@example.com");

        when(userPersistencePort.findByUserId(anyString())).thenReturn(Mono.just( existingUser));
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(true));

        Mono<User> result = userService.updateByUserId("cde8c071a420424abf28b189ae2cd698", updatedUser);

        StepVerifier.create(result)
                .expectError(UserEmailAlreadyExistsException.class)
                .verify();

        Mockito.verify(userPersistencePort, times(1)).findByUserId(anyString());
        Mockito.verify(userPersistencePort, times(1)).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).save(any(User.class));
    }
}