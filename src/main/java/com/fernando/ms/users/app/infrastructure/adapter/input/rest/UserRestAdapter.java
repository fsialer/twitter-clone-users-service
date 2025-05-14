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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Operations related to user")
public class UserRestAdapter{
    private final UserInputPort userInputPort;
    private final UserRestMapper userRestMapper;
    private final FollowInputPort followInputPort;
    private final FollowRestMapper followRestMapper;

    @GetMapping
    @Operation(summary = "Find all users")
    @ApiResponse(responseCode = "200", description = "Found all users")
    public Flux<UserResponse> findAll(){
        return userRestMapper.toUsersResponse(userInputPort.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find user by id")
    @ApiResponse(responseCode = "200", description = "Found user by id")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id){
        return userInputPort.findById(id)
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @PostMapping
    @Operation(summary = "Save user")
    @ApiResponse(responseCode = "201", description = "Saved user")
    public Mono<ResponseEntity<UserResponse>> save(@Valid @RequestBody CreateUserRequest rq){
        return userInputPort.save(userRestMapper.toUser(rq))
                .flatMap(user -> {
                    String location = "/users/".concat(user.getId());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(userRestMapper.toUserResponse(user)));
                });
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by id")
    @ApiResponse(responseCode = "200", description = "Updated user by id")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id,@Valid @RequestBody UpdateUserRequest rq){
        return userInputPort.update(id,userRestMapper.toUser(rq))
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by id")
    @ApiResponse(responseCode ="204", description = "Deleted user by id")
    public Mono<Void> delete(@PathVariable String id){
        return userInputPort.delete(id);
    }

    @GetMapping("/{id}/verify")
    @Operation(summary = "Verify user by id")
    @ApiResponse(responseCode ="200", description = "Exists user by id")
    public Mono<ResponseEntity<ExistsUserResponse>> verify(@PathVariable("id") String id){
        return userInputPort.verifyUser(id)
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toExistsUserResponse(user))));
    }

    @GetMapping("/find-by-ids")
    @Operation(summary = "find all users by ids")
    @ApiResponse(responseCode ="200", description = "Found all users by ids")
    public Flux<UserResponse> findByIds(@RequestParam("ids") List<String> ids){
        return userRestMapper.toUsersResponse(userInputPort.findByIds(ids));
    }

    @PostMapping("/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Follow user")
    @ApiResponse(responseCode ="204", description = "User followed")
    public Mono<Void> followUser( @RequestHeader("X-User-Id") String userId,
                                  @Valid @RequestBody CreateFollowRequest rq){
        return followInputPort.followUser(followRestMapper.toFollow(userId,rq));
    }

    @DeleteMapping("/unfollow/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Unfollow user")
    @ApiResponse(responseCode ="204", description = "User unfollowed")
    public Mono<Void> unfollowUser( @RequestHeader("X-User-Id") String userId,
                                    @PathVariable("id") String id){
        return followInputPort.unFollowUser(id,userId);
    }

    @GetMapping("/me")
    @Operation(summary = "Find user authenticated")
    @ApiResponse(responseCode ="200", description = "Found user authenticated")
    public Mono<ResponseEntity<UserResponse>> findByUserId(@RequestHeader("X-User-Id") String userId){
        return userInputPort.findByUserId(userId)
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @PutMapping("/me")
    @Operation(summary = "Update profile user authenticated")
    @ApiResponse(responseCode = "200", description = "Updated profile user authenticated")
    public Mono<ResponseEntity<UserResponse>> updateByUserId(@RequestHeader("X-User-Id") String userId,
                                                                 @Valid @RequestBody UpdateUserRequest rq){
        return userInputPort.updateByUserId(userId,userRestMapper.toUser(rq))
                .flatMap(user-> Mono.just(ResponseEntity.ok(userRestMapper.toUserResponse(user))));
    }

    @GetMapping("/{userId}/followed")
    @Operation(summary = "Find all Followed by user authenticated")
    @ApiResponse(responseCode = "200", description = "List user followed")
    public Flux<UserResponse> findAllFollowedByUserId(@PathVariable String userId){
        return userRestMapper.toUsersResponse(userInputPort.findUserFollowed(userId));
    }
}
