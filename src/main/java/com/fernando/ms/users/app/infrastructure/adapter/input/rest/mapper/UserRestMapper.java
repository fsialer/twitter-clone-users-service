package com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.ChangePasswordRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UserAuthRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    default Flux<UserResponse> toUsersResponse(Flux<User> users){
        return users.map(this::toUserResponse);
    }

    UserResponse toUserResponse(User user);

    //Mono<UserResponse> toUserResponse(Mono<User> user);

    default  Mono<UserResponse> toUserResponse(Mono<User> user){
        return user.map(this::toUserResponse);
    }

    User toUser(CreateUserRequest rq);
    User toUser(UpdateUserRequest rq);

    User toUser(ChangePasswordRequest rq);

    User toUser(UserAuthRequest rq);

    //UserResponse toUserResponse(User user);
}
