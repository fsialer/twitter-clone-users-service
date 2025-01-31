package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper;


import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPersistenceMapper {
    default Flux<User> toUsers(Flux<UserEntity> userEntities) {
        return userEntities.map(this::toUser);
    }

    default Mono<User> toUser(Mono<UserEntity> userEntities) {
        return userEntities.map(this::toUser);
    }

    User toUser(UserEntity userEntity);

    @Mapping(target = "createdAt",expression = "java(mapCreatedAt())")
    @Mapping(target = "updatedAt",expression = "java(mapUpdatedAt())")
    UserEntity toUserEntity(User user);

    default LocalDateTime mapCreatedAt(){
        return LocalDateTime.now();
    }

    default LocalDateTime mapUpdatedAt(){
        return LocalDateTime.now();
    }

}
