package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper;


import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    default Flux<User> toUsers(Flux<UserEntity> userEntities) {
        return userEntities.map(this::toUser); // Convierte cada elemento individualmente
    }

    default Mono<User> toUser(Mono<UserEntity> userEntities) {
        return userEntities.map(this::toUser); // Convierte cada elemento individualmente
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
