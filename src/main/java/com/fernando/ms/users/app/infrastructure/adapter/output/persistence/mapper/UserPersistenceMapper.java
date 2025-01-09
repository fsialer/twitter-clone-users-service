package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper;


import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    default Flux<User> toUsers(Flux<UserEntity> userEntities) {
        return userEntities.map(this::toUser); // Convierte cada elemento individualmente
    }

    User toUser(UserEntity userEntity);

    UserEntity toUserEntity(User user);

}
