package com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ExistsUserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRestMapper {

    default Flux<UserResponse> toUsersResponse(Flux<User> users){
        return users.map(this::toUserResponse);
    }

    UserResponse toUserResponse(User user);

    User toUser(CreateUserRequest rq);
    User toUser(UpdateUserRequest rq);

    default ExistsUserResponse toExistsUserResponse(Boolean exists){
        return ExistsUserResponse.builder()
                .exists(exists)
                .build();
    }
}
