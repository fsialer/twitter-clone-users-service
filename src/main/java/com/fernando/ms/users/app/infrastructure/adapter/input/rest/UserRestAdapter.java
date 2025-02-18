package com.fernando.ms.users.app.infrastructure.adapter.input.rest;


import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.ChangePasswordRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UserAuthRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ExistsUserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
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
        return userInputPort.save(userRestMapper.toAuthor(rq))
                .flatMap(user -> {
                    String location = "/users/".concat(user.getId().toString());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(userRestMapper.toUserResponse(user)));
                });
    }

    @PostMapping("/admin")
    public Mono<ResponseEntity<UserResponse>> saveAdmin(@Valid @RequestBody CreateUserRequest rq){
        return userInputPort.save(userRestMapper.toAdmin(rq))
                .flatMap(user -> {
                    String location = "/users/admin/".concat(user.getId().toString());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(userRestMapper.toUserResponse(user)));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable Long id,@Valid @RequestBody UpdateUserRequest rq){
        return userInputPort.update(id,userRestMapper.toUser(rq))
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@PathVariable Long id){
        return userInputPort.delete(id);
    }

    @PutMapping("/{id}/change-password")
    public Mono<ResponseEntity<UserResponse>> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest rq){
        return userInputPort.changePassword(id,userRestMapper.toUser(rq))
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @PostMapping("/auth")
    public Mono<ResponseEntity<UserResponse>> authentication(@Valid @RequestBody UserAuthRequest rq){
        return userInputPort.authentication(userRestMapper.toUser(rq))
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @GetMapping("/{id}/verify")
    public Mono<ResponseEntity<ExistsUserResponse>> verify(@PathVariable("id") Long id){
        return userInputPort.verifyUser(id)
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toExistsUserResponse(user))));
    }

    @GetMapping("/find-by-ids")
    public Flux<UserResponse> findByIds(@RequestParam("ids") List<Long> ids){
        return userRestMapper.toUsersResponse(userInputPort.findByIds(ids));
    }

    @GetMapping("/{username}/username")
    public Mono<ResponseEntity<UserResponse>> findByUsername(@PathVariable("username") String username){
        return userInputPort.findByUsername(username)
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }
}
