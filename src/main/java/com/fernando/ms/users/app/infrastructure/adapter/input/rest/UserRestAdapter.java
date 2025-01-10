package com.fernando.ms.users.app.infrastructure.adapter.input.rest;


import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.domain.exceptions.CustomValidationException;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestAdapter{
    private final UserInputPort userInputPort;
    private final UserRestMapper userRestMapper;

    @GetMapping
    public Flux<UserResponse> findAll(){
        return userRestMapper.toUsersResponse(userInputPort.findAll());
    }
    @GetMapping("/{id}")
    public Mono<UserResponse> findById(@PathVariable Long id){
        return userRestMapper.toUserResponse(userInputPort.finById(id));
    }

    @PostMapping
    public Mono<UserResponse> save(@Validated @RequestBody CreateUserRequest rq){
        // Si ocurre una excepción de validación, lanzamos nuestra propia excepción controlada
        return userRestMapper.toUserResponse(userInputPort.save(userRestMapper.toUser(rq)));

    }
}
