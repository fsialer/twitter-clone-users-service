package com.fernando.ms.users.app.infrastructure.adapter.input.rest;


import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
