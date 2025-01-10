package com.fernando.ms.users.app.infrastructure.adapter.input.rest;


import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.domain.exceptions.CustomValidationException;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

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
    public Mono<ResponseEntity<UserResponse>> save(@Valid @RequestBody CreateUserRequest rq){
        return userInputPort.save(userRestMapper.toUser(rq))
                .flatMap(user -> {
                    String location = "/users/".concat(user.getId().toString());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(userRestMapper.toUserResponse(user)));
                });
    }
}
