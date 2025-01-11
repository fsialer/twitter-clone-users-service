package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.UserEmailAlreadyExistsException;
import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.domain.exceptions.UserUsernameAlreadyExistsException;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.utils.PasswordUtils;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserPersistencePort userPersistencePort;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordUtils passwordUtils;

    @Test
    @DisplayName("When Users Information Is Correct Expect A List Users")
    void When_UsersInformationIsCorrect_Expect_AListUsers(){
        User user= TestUtilUser.buildUserMock();
        when(userPersistencePort.findAll()).thenReturn(Flux.just(user));

        Flux<User> users=userService.findAll();
        StepVerifier.create(users)
                .expectNext(user)
                .verifyComplete();
        Mockito.verify(userPersistencePort,times(1)).findAll();
    }

    @Test
    @DisplayName("When User Identifier Is Correct Except User Information Correct")
    void When_UserIdentifierIsCorrect_Except_UserInformationCorrect(){
        User user= TestUtilUser.buildUserMock();
        when(userPersistencePort.finById(anyLong())).thenReturn(Mono.just(user));
        Mono<User> userMono=userService.finById(1L);
        StepVerifier.create(userMono)
                .expectNext(user)
                .verifyComplete();
        Mockito.verify(userPersistencePort,times(1)).finById(anyLong());
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Identifier Is Invalid")
    void Expect_UserNotFoundException_When_UserIdentifierIsInvalid(){
        User user= TestUtilUser.buildUserMock();
        when(userPersistencePort.finById(anyLong())).thenReturn(Mono.empty());
        Mono<User> userMono=userService.finById(1L);
        StepVerifier.create(userMono)
                .expectError(UserNotFoundException.class)
                .verify();
        Mockito.verify(userPersistencePort,times(1)).finById(anyLong());
    }


    @Test
    @DisplayName("When User Information Is Correct Expect User Information Saved Successfully")
    void When_UserInformationIsCorrect_Expect_UserInformationSavedSuccessfully() {
        User user=TestUtilUser.buildUserMock();
        when(userPersistencePort.existsByUsername(anyString())).thenReturn(Mono.just(false));
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(passwordUtils.generateSalt()).thenReturn("salt");
        when(passwordUtils.hashPassword(anyString(), anyString())).thenReturn("hashedPassword");
        when(userPersistencePort.save(any(User.class))).thenReturn(Mono.just(user));

        Mono<User> savedUser = userService.save(user);

        StepVerifier.create(savedUser)
                .expectNext(user)
                .verifyComplete();

        Mockito.verify(userPersistencePort,times(1)).existsByUsername(anyString());
        Mockito.verify(userPersistencePort,times(1)).existsByEmail(anyString());
        Mockito.verify(passwordUtils,times(1)).generateSalt();
        Mockito.verify(passwordUtils,times(1)).hashPassword(anyString(), anyString());
        Mockito.verify(userPersistencePort,times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Expect UserUsernameAlreadyExistsException When Username Already Exists")
    void Expect_UserUsernameAlreadyExistsException_When_UsernameAlreadyExists() {
        User user=TestUtilUser.buildUserMock();
        when(userPersistencePort.existsByUsername(anyString())).thenReturn(Mono.just(true));

        Mono<User> savedUser = userService.save(user);

        StepVerifier.create(savedUser)
                .expectError(UserUsernameAlreadyExistsException.class)
                .verify();

        Mockito.verify(userPersistencePort,times(1)).existsByUsername(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).existsByEmail(anyString());
        Mockito.verify(passwordUtils, Mockito.never()).generateSalt();
        Mockito.verify(passwordUtils, Mockito.never()).hashPassword(anyString(), anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).save(any(User.class));
    }

    @Test
    @DisplayName("Expect UserEmailAlreadyExistsException When Username Already Exists")
    void Expect_UserEmailAlreadyExistsException_When_UsernameAlreadyExists() {
        User user=TestUtilUser.buildUserMock();
        when(userPersistencePort.existsByUsername(anyString())).thenReturn(Mono.just(false));
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(true));

        Mono<User> savedUser = userService.save(user);

        StepVerifier.create(savedUser)
                .expectError(UserEmailAlreadyExistsException.class)
                .verify();

        Mockito.verify(userPersistencePort,times(1)).existsByUsername(anyString());
        Mockito.verify(userPersistencePort,times(1)).existsByEmail(anyString());
        Mockito.verify(passwordUtils, Mockito.never()).generateSalt();
        Mockito.verify(passwordUtils, Mockito.never()).hashPassword(anyString(), anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).save(any(User.class));
    }

    @Test
    @DisplayName("When User Information Is Correct Expect User Information Updated Successfully")
    void When_UserInformationIsCorrect_Expect_UserInformationUpdatedSuccessfully() {
        User existingUser = TestUtilUser.buildUserMock();
        User updatedUser = TestUtilUser.buildUserMock();
        updatedUser.setEmail("newemail@example.com");

        when(userPersistencePort.finById(anyLong())).thenReturn(Mono.just(existingUser));
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(userPersistencePort.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        Mono<User> result = userService.update(1L, updatedUser);

        StepVerifier.create(result)
                .expectNext(updatedUser)
                .verifyComplete();

        Mockito.verify(userPersistencePort, times(1)).finById(anyLong());
        Mockito.verify(userPersistencePort, times(1)).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Expect UserEmailAlreadyExistsException When Email Already Exists")
    void Expect_UserEmailAlreadyExistsException_When_EmailAlreadyExists() {
        User existingUser = TestUtilUser.buildUserMock();
        User updatedUser = TestUtilUser.buildUserMock();
        updatedUser.setEmail("newemail@example.com");

        when(userPersistencePort.finById(anyLong())).thenReturn(Mono.just(existingUser));
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Mono.just(true));

        Mono<User> result = userService.update(1L, updatedUser);

        StepVerifier.create(result)
                .expectError(UserEmailAlreadyExistsException.class)
                .verify();

        Mockito.verify(userPersistencePort, times(1)).finById(anyLong());
        Mockito.verify(userPersistencePort, times(1)).existsByEmail(anyString());
        Mockito.verify(userPersistencePort, Mockito.never()).save(any(User.class));
    }

}
