package com.fernando.ms.users.app.infrastructure.adapter.input.rest;


import com.fernando.ms.users.app.application.ports.input.FollowInputPort;
import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.FollowRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateFollowRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
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
    private final FollowInputPort followInputPort;
    private final FollowRestMapper followRestMapper;

    @GetMapping
    public Flux<UserResponse> findAll(){
        return userRestMapper.toUsersResponse(userInputPort.findAll());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id){
        return userInputPort.findById(id)
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> save(@Valid @RequestBody CreateUserRequest rq){
        return userInputPort.save(userRestMapper.toUser(rq))
                .flatMap(user -> {
                    String location = "/users/".concat(user.getId());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(userRestMapper.toUserResponse(user)));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id,@Valid @RequestBody UpdateUserRequest rq){
        return userInputPort.update(id,userRestMapper.toUser(rq))
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id){
        return userInputPort.delete(id);
    }

    @GetMapping("/{id}/verify")
    public Mono<ResponseEntity<ExistsUserResponse>> verify(@PathVariable("id") String id){
        return userInputPort.verifyUser(id)
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toExistsUserResponse(user))));
    }

    @GetMapping("/find-by-ids")
    public Flux<UserResponse> findByIds(@RequestParam("ids") List<String> ids){
        return userRestMapper.toUsersResponse(userInputPort.findByIds(ids));
    }

    @PostMapping("/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> followUser( @RequestHeader("X-User-Id") String userId,
                                  @Valid @RequestBody CreateFollowRequest rq){
        return followInputPort.followUser(followRestMapper.toFollow(userId,rq));
    }
}
