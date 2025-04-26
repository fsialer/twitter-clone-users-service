package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.FollowDocument;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowPersistenceMapper {
    default Mono<Follow> toFollow(Mono<FollowDocument> followDocument) {
        return followDocument.map(this::toFollow);
    }

    Follow toFollow(FollowDocument userEntity);

    @Mapping(target = "createdAt",expression = "java(mapCreatedAt())")
    FollowDocument toFollowDocument(Follow follow);

    default LocalDateTime mapCreatedAt(){
        return LocalDateTime.now();
    }
}
